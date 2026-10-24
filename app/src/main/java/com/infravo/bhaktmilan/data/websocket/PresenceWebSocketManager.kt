package com.infravo.bhaktmilan.data.websocket

import android.os.Handler
import android.os.Looper
import android.util.Log
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Single WebSocket manager for real-time presence.
 *
 * Expected backend endpoint: /ws/presence/?token=<JWT access token>
 * Keep the existing JSON ping -> pong heartbeat. Do not log the access token.
 */
@Singleton
class PresenceWebSocketManager @Inject constructor() {

    companion object {
        private const val TAG = "PresenceWebSocket"
        private const val PRESENCE_PATH = "/ws/presence/"
        private const val MAX_BATCH_SIZE = 200
        private const val INITIAL_RECONNECT_DELAY_MS = 2_000L
        private const val MAX_RECONNECT_DELAY_MS = 60_000L
    }

    private val client = OkHttpClient.Builder()
        // OkHttp WebSocket control-frame ping; the app-level JSON ping/pong
        // from the backend is also handled below.
        .pingInterval(30, TimeUnit.SECONDS)
        .build()

    private val mainHandler = Handler(Looper.getMainLooper())

    private val _messages = MutableSharedFlow<String>(extraBufferCapacity = 64)
    val messages = _messages.asSharedFlow()

    private val _connectionState = MutableStateFlow("DISCONNECTED")
    val connectionState = _connectionState.asStateFlow()

    private val lock = Any()

    @Volatile
    private var webSocket: WebSocket? = null

    @Volatile
    private var socketOpen = false

    @Volatile
    private var foreground = true

    @Volatile
    private var shouldReconnect = false

    @Volatile
    private var serverBaseUrl: String? = null

    @Volatile
    private var accessToken: String? = null

    private var retryCount = 0
    private var reconnectRunnable: Runnable? = null

    // The desired subscription set survives reconnects.
    private val subscribedUserIds = linkedSetOf<Int>()

    /**
     * Call this with the existing API base URL and the current JWT access token.
     * Accepted base URL schemes: http, https, ws, wss.
     */
    fun connect(serverBaseUrl: String, accessToken: String) {
        val base = serverBaseUrl.trim()
        val token = accessToken.trim()

        if (base.isBlank() || token.isBlank()) {
            Log.e(TAG, "connect() skipped: base URL or access token is blank")
            _connectionState.value = "ERROR"
            return
        }

        this.serverBaseUrl = base
        this.accessToken = token
        shouldReconnect = true

        Log.d(TAG, "connect() requested; foreground=$foreground, base=${safeBaseForLog(base)}")

        if (foreground) {
            mainHandler.post { connectInternal() }
        } else {
            Log.d(TAG, "Connection deferred because app is in background")
        }
    }

    /**
     * Call from the app's existing foreground/background lifecycle handling.
     * The socket is intentionally closed while the app is in background.
     */
    fun setForeground(isForeground: Boolean) {
        foreground = isForeground
        Log.d(TAG, "setForeground($isForeground)")

        if (!isForeground) {
            shouldReconnect = false
            cancelReconnect()
            socketOpen = false

            val oldSocket = webSocket
            webSocket = null
            oldSocket?.close(1000, "App moved to background")
            _connectionState.value = "DISCONNECTED"
            return
        }

        val base = serverBaseUrl
        val token = accessToken
        if (!base.isNullOrBlank() && !token.isNullOrBlank()) {
            shouldReconnect = true
            mainHandler.post { connectInternal() }
        } else {
            Log.d(TAG, "Foreground resumed, but connect() has not supplied URL/token yet")
        }
    }

    /**
     * Stops reconnect attempts and clears credentials/subscriptions.
     * Use on logout or when the app intentionally disconnects presence.
     */
    fun disconnect() {
        Log.d(TAG, "disconnect() requested")
        shouldReconnect = false
        cancelReconnect()
        socketOpen = false

        val oldSocket = webSocket
        webSocket = null
        oldSocket?.close(1000, "Client disconnect")

        synchronized(lock) {
            subscribedUserIds.clear()
        }

        serverBaseUrl = null
        accessToken = null
        retryCount = 0
        _connectionState.value = "DISCONNECTED"
    }

    /**
     * Adds users to the desired subscription set and sends subscribe messages
     * immediately when the WebSocket is open. IDs must be backend User.id values.
     */
    fun subscribeToUsers(userIds: Collection<Int>) {
        val validIds = userIds.filter { it > 0 }.toSet()
        if (validIds.isEmpty()) {
            Log.d(TAG, "subscribeToUsers(): no valid user IDs")
            return
        }

        val newlyAdded = synchronized(lock) {
            val added = validIds - subscribedUserIds
            subscribedUserIds.addAll(validIds)
            added
        }

        if (newlyAdded.isEmpty()) {
            Log.d(TAG, "subscribeToUsers(): all ${validIds.size} IDs already tracked")
            return
        }

        Log.d(TAG, "Subscribe requested for ${newlyAdded.size} new user IDs")
        sendUserIdBatches(type = "subscribe", userIds = newlyAdded)
    }

    /**
     * Removes users from the desired subscription set and sends unsubscribe
     * messages when connected.
     */
    fun unsubscribeFromUsers(userIds: Collection<Int>) {
        val validIds = userIds.filter { it > 0 }.toSet()
        if (validIds.isEmpty()) return

        val actuallyRemoved = synchronized(lock) {
            val removed = validIds intersect subscribedUserIds
            subscribedUserIds.removeAll(validIds)
            removed
        }

        if (actuallyRemoved.isEmpty()) {
            Log.d(TAG, "unsubscribeFromUsers(): no tracked IDs matched")
            return
        }

        Log.d(TAG, "Unsubscribe requested for ${actuallyRemoved.size} user IDs")
        sendUserIdBatches(type = "unsubscribe", userIds = actuallyRemoved)
    }

    private fun connectInternal() {
        if (!foreground || !shouldReconnect) {
            Log.d(TAG, "connectInternal() skipped: foreground=$foreground, shouldReconnect=$shouldReconnect")
            return
        }

        if (webSocket != null) {
            Log.d(TAG, "connectInternal() skipped: a socket is already connecting/open")
            return
        }

        val base = serverBaseUrl
        val token = accessToken
        if (base.isNullOrBlank() || token.isNullOrBlank()) {
            Log.e(TAG, "connectInternal() failed: URL/token missing")
            _connectionState.value = "ERROR"
            return
        }

        val wsUrl = buildWebSocketUrl(base, token)
        if (wsUrl == null) {
            Log.e(TAG, "Invalid WebSocket base URL. Expected http(s)://host or ws(s)://host")
            _connectionState.value = "ERROR"
            return
        }

        // Do not log wsUrl because it contains the JWT query parameter.
        val request = try {
            Request.Builder()
                .url(wsUrl)
                .build()
        } catch (exception: Exception) {
            Log.e(TAG, "Failed to build WebSocket request: ${exception.message}", exception)
            _connectionState.value = "ERROR"
            return
        }

        Log.d(TAG, "Opening WebSocket: ${safeBaseForLog(base)}$PRESENCE_PATH")
        _connectionState.value = "CONNECTING"

        try {
            webSocket = client.newWebSocket(request, object : WebSocketListener() {

                override fun onOpen(webSocket: WebSocket, response: Response) {
                    this@PresenceWebSocketManager.webSocket = webSocket
                    socketOpen = true
                    retryCount = 0
                    Log.i(
                        TAG,
                        "WebSocket connected; HTTP=${response.code}, " +
                                "trackedUsers=${trackedUserCount()}"
                    )
                    _connectionState.value = "CONNECTED"
                    restoreSubscriptions()
                }

                override fun onMessage(webSocket: WebSocket, text: String) {
                    Log.v(TAG, "Incoming WebSocket message (${text.length} chars)")

                    try {
                        val json = JSONObject(text)
                        when (json.optString("type")) {
                            "ping" -> {
                                val sent = webSocket.send(JSONObject().put("type", "pong").toString())
                                Log.d(TAG, "Backend JSON ping received; pongSent=$sent")
                                return
                            }

                            "connection" -> {
                                Log.i(
                                    TAG,
                                    "Backend connection acknowledgement: ${json.optString("status", "unknown")}"
                                )
                            }

                            "error" -> {
                                Log.w(TAG, "Backend sent a WebSocket error message")
                            }
                        }
                    } catch (exception: Exception) {
                        // Unknown/non-JSON messages are passed to the repository;
                        // it will ignore malformed presence payloads safely.
                        Log.w(TAG, "Incoming message was not valid JSON: ${exception.message}")
                    }

                    val emitted = _messages.tryEmit(text)
                    if (!emitted) {
                        Log.w(TAG, "Message flow buffer full; message could not be emitted")
                    }
                }

                override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                    Log.w(TAG, "WebSocket closing: code=$code, reason=$reason")
                    webSocket.close(code, reason)
                }

                override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                    if (this@PresenceWebSocketManager.webSocket === webSocket) {
                        this@PresenceWebSocketManager.webSocket = null
                    }
                    socketOpen = false
                    Log.w(TAG, "WebSocket closed: code=$code, reason=$reason")
                    _connectionState.value = "DISCONNECTED"
                    scheduleReconnect()
                }

                override fun onFailure(
                    webSocket: WebSocket,
                    t: Throwable,
                    response: Response?
                ) {
                    if (this@PresenceWebSocketManager.webSocket === webSocket) {
                        this@PresenceWebSocketManager.webSocket = null
                    }
                    socketOpen = false
                    Log.e(
                        TAG,
                        "WebSocket failure: ${t.javaClass.simpleName}: ${t.message}; " +
                                "HTTP=${response?.code ?: "no response"}"
                    )
                    _connectionState.value = "DISCONNECTED"
                    scheduleReconnect()
                }
            })
        } catch (exception: Exception) {
            webSocket = null
            socketOpen = false
            Log.e(TAG, "newWebSocket() failed: ${exception.message}", exception)
            _connectionState.value = "DISCONNECTED"
            scheduleReconnect()
        }
    }

    /**
     * Converts the base URL to an HTTP(S) URL first, builds the endpoint using
     * HttpUrl.Builder (which only accepts http/https), then changes the final
     * serialized URL scheme to ws/wss. Never call Builder.scheme("ws"/"wss").
     */
    private fun buildWebSocketUrl(baseUrl: String, token: String): String? {
        val normalized = when {
            baseUrl.startsWith("wss://", ignoreCase = true) ->
                "https://" + baseUrl.substringAfter("://")
            baseUrl.startsWith("ws://", ignoreCase = true) ->
                "http://" + baseUrl.substringAfter("://")
            else -> baseUrl
        }

        val httpUrl: HttpUrl = normalized.toHttpUrlOrNull() ?: return null
        val secure = httpUrl.isHttps

        val endpoint = httpUrl.newBuilder()
            .encodedPath(PRESENCE_PATH)
            .query(null)
            .addQueryParameter("token", token)
            .build()
            .toString()

        return if (secure) {
            endpoint.replaceFirst("https://", "wss://")
        } else {
            endpoint.replaceFirst("http://", "ws://")
        }
    }

    private fun sendUserIdBatches(type: String, userIds: Collection<Int>) {
        if (!socketOpen) {
            Log.d(TAG, "$type queued: WebSocket is not open; IDs will be restored on connect")
            return
        }

        userIds.toList().distinct().chunked(MAX_BATCH_SIZE).forEachIndexed { index, batch ->
            val payload = JSONObject()
                .put("type", type)
                .put("user_ids", JSONArray(batch))
                .toString()

            val sent = webSocket?.send(payload) == true
            Log.d(
                TAG,
                "$type batch ${index + 1}: count=${batch.size}, sent=$sent"
            )
        }
    }

    private fun restoreSubscriptions() {
        val ids = synchronized(lock) { subscribedUserIds.toList() }
        if (ids.isEmpty()) {
            Log.d(TAG, "Reconnect completed; no subscriptions to restore")
            return
        }

        Log.i(TAG, "Restoring subscriptions for ${ids.size} users")
        sendUserIdBatches(type = "subscribe", userIds = ids)
    }

    private fun trackedUserCount(): Int =
        synchronized(lock) { subscribedUserIds.size }

    private fun scheduleReconnect() {
        if (!shouldReconnect || !foreground) {
            Log.d(TAG, "Reconnect not scheduled: shouldReconnect=$shouldReconnect, foreground=$foreground")
            return
        }

        cancelReconnect()

        val exponent = retryCount.coerceAtMost(5)
        val delay = (INITIAL_RECONNECT_DELAY_MS * (1L shl exponent))
            .coerceAtMost(MAX_RECONNECT_DELAY_MS)
        retryCount += 1

        Log.w(TAG, "Reconnecting in ${delay}ms (attempt=$retryCount)")
        _connectionState.value = "RECONNECTING"

        val runnable = Runnable {
            reconnectRunnable = null
            if (shouldReconnect && foreground && webSocket == null) {
                connectInternal()
            } else {
                Log.d(TAG, "Reconnect runnable skipped: state changed before retry")
            }
        }
        reconnectRunnable = runnable
        mainHandler.postDelayed(runnable, delay)
    }

    private fun cancelReconnect() {
        reconnectRunnable?.let(mainHandler::removeCallbacks)
        reconnectRunnable = null
    }

    private fun safeBaseForLog(baseUrl: String): String {
        val normalized = when {
            baseUrl.startsWith("wss://", ignoreCase = true) ->
                "https://" + baseUrl.substringAfter("://")
            baseUrl.startsWith("ws://", ignoreCase = true) ->
                "http://" + baseUrl.substringAfter("://")
            else -> baseUrl
        }
        val url = normalized.toHttpUrlOrNull() ?: return "<invalid-base-url>"
        val defaultPort = if (url.isHttps) 443 else 80
        val portSuffix = if (url.port != defaultPort) ":${url.port}" else ""
        val scheme = if (url.isHttps) "https" else "http"
        return "$scheme://${url.host}$portSuffix"
    }
}
