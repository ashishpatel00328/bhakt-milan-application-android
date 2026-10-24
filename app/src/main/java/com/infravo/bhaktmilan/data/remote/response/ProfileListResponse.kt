
package com.infravo.bhaktmilan.data.remote.response

import com.google.gson.annotations.SerializedName

data class ProfileListResponse(
    val success: Boolean,
    val message: String,
    val data: ProfilePageData
)

data class ProfilePageData(
    val results: List<ProfileListItem>
)

data class ProfileListItem(
    val id: Int,
    @SerializedName("userId")
    val userId: Int,
    val profile_id: String,
    val full_name: String,
    val age: Int,
    val gender: Int?,
    val sampraday: String?,
    val city: Int?,
    val state: Int?,
    val height_cm: Int?,
    val education: String?,
    val occupation: String?,
    val profile_photo: String?,

    // Presence fields returned by the API
    val is_online: Boolean? = false,
    val last_seen: String? = null,
    val minutes_ago: Int? = null
)