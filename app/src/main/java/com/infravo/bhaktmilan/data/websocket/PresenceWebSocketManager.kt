
package com.infravo.bhaktmilan.data.websocket

import android.os.Handler
import android.os.Looper
import android.util.Log
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONObject
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PresenceWebSocketManager @Inject constructor() {

    private val client = OkHttpClient.Builder()
        .pingInterval(30, TimeUnit.SECONDS)
        .build()

    private val handler = Handler(Looper.getMainLooper())

    @Volatile private var webSocket: WebSocket? = null
    @Volatile private var foreground = false
    @Volatile private var shouldReconnect = false
    @Volatile private var serverBaseUrl: String? = null
    @Volatile private var accessToken: String? = null

    private var retryCount = 0

    private val _messages = MutableSharedFlow<String>(
        extraBufferCapacity = 32
    )
    val messages = _messages.asSharedFlow()

    private val _connectionState = MutableSharedFlow<String>(
        extraBufferCapacity = 8,
        replay = 1
    )
    val connectionState = _connectionState.asSharedFlow()

    private val reconnectRunnable = Runnable {
        val url = serverBaseUrl
        val token = accessToken

        if (foreground && shouldReconnect &&
            !url.isNullOrBlank() && !token.isNullOrBlank()
        ) {
            connect(url, token)
        }
    }

    @Synchronized
    fun setForeground(isForeground: Boolean) {
        foreground = isForeground

        if (!isForeground) {
            // Background: stop socket and retries.
            shouldReconnect = false
            handler.removeCallbacks(reconnectRunnable)
            webSocket?.close(1000, "App backgrounded")
            webSocket = null
            _connectionState.tryEmit("DISCONNECTED")
        } else {
            // Reconnect only if a valid session was already requested.
            val url = serverBaseUrl
            val token = accessToken

            if (!url.isNullOrBlank() && !token.isNullOrBlank()) {
                shouldReconnect = true
                connect(url, token)
            }
        }
    }

    @Synchronized
    fun connect(serverBaseUrl: String, accessToken: String) {
        if (!foreground) {
            Log.d(TAG, "App is backgrounded; connection skipped")
            return
        }

        if (this.serverBaseUrl != serverBaseUrl ||
            this.accessToken != accessToken
        ) {
            webSocket?.cancel()
            webSocket = null
            retryCount = 0
        }

        this.serverBaseUrl = serverBaseUrl
        this.accessToken = accessToken
        shouldReconnect = true

        if (webSocket != null) return

        handler.removeCallbacks(reconnectRunnable)

        try {
            val baseUrl = serverBaseUrl.toHttpUrl()
            val wsUrl = baseUrl.newBuilder()
                .encodedPath("/ws/presence/")
                .query(null)
                .addQueryParameter("token", accessToken)
                .build()
                .toString()
                .replaceFirst("https://", "wss://")
                .replaceFirst("http://", "ws://")

            val request = Request.Builder()
                .url(wsUrl)
                .build()

            _connectionState.tryEmit("CONNECTING")

            val socket = client.newWebSocket(
                request,
                object : WebSocketListener() {

                    override fun onOpen(
                        webSocket: WebSocket,
                        response: Response
                    ) {
                        if (this@PresenceWebSocketManager.webSocket !== webSocket) {
                            webSocket.close(1000, "Stale connection")
                            return
                        }

                        retryCount = 0
                        Log.d(TAG, "WebSocket connected")
                        _connectionState.tryEmit("CONNECTED")
                    }

                    override fun onMessage(
                        webSocket: WebSocket,
                        text: String
                    ) {
                        try {
                            val message = JSONObject(text)

                            if (message.optString("type") == "ping") {
                                webSocket.send(
                                    JSONObject()
                                        .put("type", "pong")
                                        .toString()
                                )
                                Log.d(TAG, "Pong sent")
                            } else {
                                _messages.tryEmit(text)
                            }
                        } catch (e: Exception) {
                            Log.e(TAG, "Invalid WebSocket message", e)
                            _messages.tryEmit(text)
                        }
                    }

                    override fun onClosing(
                        webSocket: WebSocket,
                        code: Int,
                        reason: String
                    ) {
                        webSocket.close(code, reason)
                    }

                    override fun onClosed(
                        webSocket: WebSocket,
                        code: Int,
                        reason: String
                    ) {
                        clearSocketIfCurrent(webSocket)
                        Log.d(TAG, "WebSocket closed: $code")
                        scheduleReconnectIfNeeded()
                    }

                    override fun onFailure(
                        webSocket: WebSocket,
                        t: Throwable,
                        response: Response?
                    ) {
                        clearSocketIfCurrent(webSocket)
                        Log.e(TAG, "WebSocket connection failed", t)
                        _connectionState.tryEmit("ERROR")
                        scheduleReconnectIfNeeded()
                    }
                }
            )

            webSocket = socket
        } catch (e: Exception) {
            webSocket = null
            Log.e(TAG, "Unable to create WebSocket request", e)
            _connectionState.tryEmit("ERROR")
            scheduleReconnectIfNeeded()
        }
    }

    @Synchronized
    private fun clearSocketIfCurrent(socket: WebSocket) {
        if (webSocket === socket) {
            webSocket = null
        }
    }

    @Synchronized
    private fun scheduleReconnectIfNeeded() {
        if (!foreground || !shouldReconnect) {
            _connectionState.tryEmit("DISCONNECTED")
            return
        }

        // Exponential backoff: 2, 4, 8 ... up to 60 seconds.
        val delaySeconds =
            (2L shl retryCount.coerceAtMost(5)).coerceAtMost(60L)
        retryCount++

        _connectionState.tryEmit("RECONNECTING")
        handler.removeCallbacks(reconnectRunnable)
        handler.postDelayed(
            reconnectRunnable,
            delaySeconds * 1000
        )
        Log.d(TAG, "Reconnect scheduled in $delaySeconds seconds")
    }

    @Synchronized
    fun disconnect() {
        shouldReconnect = false
        handler.removeCallbacks(reconnectRunnable)
        webSocket?.close(1000, "Client logout")
        webSocket = null
        accessToken = null
        retryCount = 0
        _connectionState.tryEmit("DISCONNECTED")
    }

    companion object {
        private const val TAG = "PresenceWebSocket"
    }
}