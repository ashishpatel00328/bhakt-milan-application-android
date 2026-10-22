package com.infravo.bhaktmilan.data.remote.response

import com.google.gson.annotations.SerializedName

data class OnlineStatusResponse(
    @SerializedName("user_id")
    val userId: Int,

    @SerializedName("is_online")
    val isOnline: Boolean,

    @SerializedName("last_seen")
    val lastSeen: String? = null,

    @SerializedName("minutes_ago")
    val minutesAgo: Int? = null
)