package com.infravo.bhaktmilan.data.remote.response

data class ShortlistListResponse(
    val success: Boolean,
    val message: String,
    val data: List<ShortlistedProfile>
)

data class ShortlistedProfile(
    val id: Int,
    val profile_id: String,
    val full_name: String,
    val age: Int,
    val gender: Int,
    val sampraday: String?,
    val city: Int,
    val state: Int,
    val height_cm: Int,
    val education: String,
    val occupation: String,
    val profile_photo: String?,
    val is_online: Boolean,
    val is_shortlisted: Boolean
)