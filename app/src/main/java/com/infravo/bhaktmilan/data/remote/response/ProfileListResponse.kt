package com.infravo.bhaktmilan.data.remote.response

data class ProfileListResponse(
    val success: Boolean,
    val message: String,
    val data: List<ProfileListItem>
)

data class ProfileListItem(

    val id: Int,

    val profile_id: String,

    val full_name: String,

    val age: Int,

    val profile_photo: String?,

    val country: Int?,

    val state: Int?,

    val city: Int?,

    val sampraday: Int?,

    val caste: Int?,

    val height_cm: Int?,
    val education: String?,

    val occupation: String?,
)