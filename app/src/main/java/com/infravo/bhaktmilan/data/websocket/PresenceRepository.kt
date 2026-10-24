
package com.infravo.bhaktmilan.data.websocket

import android.util.Log
import com.infravo.bhaktmilan.data.remote.response.OnlineStatusResponse
import dagger.Lazy
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject

@Singleton
class PresenceRepository @Inject constructor(
    private val socketManager: Lazy<PresenceWebSocketManager>
) {
    companion object {
        private const val TAG = "PresenceRepository"
        private const val TYPE_SNAPSHOT = "presence_snapshot"
        private const val TYPE_PRESENCE = "presence"
    }

    private val scope =
        CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val _statuses =
        MutableStateFlow<Map<Int, OnlineStatusResponse>>(emptyMap())

    val statuses: StateFlow<Map<Int, OnlineStatusResponse>> =
        _statuses.asStateFlow()

    init {
        scope.launch {
            socketManager.get().messages.collect { rawMessage ->
                handleMessage(rawMessage)
            }
        }
    }

    private fun handleMessage(raw: String) {
        try {
            val json = JSONObject(raw)

            when (json.optString("type")) {
                TYPE_SNAPSHOT -> handleSnapshot(json)
                TYPE_PRESENCE -> handlePresence(json)
                // Connection acknowledgements and other messages are
                // handled by the WebSocket manager or safely ignored here.
            }
        } catch (exception: Exception) {
            // Never log the raw payload; it could contain sensitive data.
            Log.w(TAG, "Ignoring malformed presence message")
        }
    }

    private fun handleSnapshot(json: JSONObject) {
        val users = json.optJSONArray("users") ?: return

        Log.d(
            "PRESENCE_DEBUG",
            "Snapshot received: users=${users?.length() ?: 0}"
        )

        val updates = buildMap {
            for (index in 0 until users.length()) {
                val user = users.optJSONObject(index) ?: continue
                val status = parseStatus(user) ?: continue
                put(status.userId, status)
            }
        }

        Log.d(
            "PRESENCE_DEBUG",
            "Parsed statuses=${updates.size}, userIds=${updates.keys}"
        )

        if (updates.isEmpty()) return

        _statuses.value = _statuses.value + updates

        Log.d(
            "PRESENCE_DEBUG",
            "Total statuses in state=${_statuses.value.size}"
        )
    }


    private fun handlePresence(json: JSONObject) {
        val status = parseStatus(json) ?: return
        Log.d(
            "PRESENCE_DEBUG",
            "Presence event parsed: userId=${status?.userId}, online=${status?.isOnline}"
        )
        _statuses.value = _statuses.value +
                (status.userId to status)
    }

    private fun parseStatus(json: JSONObject): OnlineStatusResponse? {
        val userId = json.optInt("user_id", -1)
        if (userId <= 0) return null

        if (!json.has("is_online") ||
            json.isNull("is_online")
        ) return null

        val onlineValue = json.opt("is_online")
        if (onlineValue !is Boolean) return null

        val lastSeen = if (
            !json.has("last_seen") || json.isNull("last_seen")
        ) {
            null
        } else {
            json.optString("last_seen").takeIf { it.isNotBlank() }
        }

        return OnlineStatusResponse(
            userId = userId,
            isOnline = onlineValue,
            lastSeen = lastSeen,
            minutesAgo = null
        )
    }

    private val trackingLock = Any()
    private val consumers = mutableMapOf<String, Set<Int>>()

    fun setTrackedUsers(
        owner: String,
        userIds: Collection<Int>
    ) {
        val newIds = userIds.filter { it > 0 }.toSet()

        val (toSubscribe, toUnsubscribe) = synchronized(trackingLock) {
            val oldIds = consumers.values.flatten().toSet()

            if (newIds.isEmpty()) {
                consumers.remove(owner)
            } else {
                consumers[owner] = newIds
            }

            val newDesiredIds = consumers.values.flatten().toSet()

            (newDesiredIds - oldIds) to (oldIds - newDesiredIds)
        }

        if (toSubscribe.isNotEmpty()) {
            socketManager.get().subscribeToUsers(toSubscribe)
        }

        if (toUnsubscribe.isNotEmpty()) {
            socketManager.get().unsubscribeFromUsers(toUnsubscribe)

            _statuses.value = _statuses.value - toUnsubscribe
        }
    }
}