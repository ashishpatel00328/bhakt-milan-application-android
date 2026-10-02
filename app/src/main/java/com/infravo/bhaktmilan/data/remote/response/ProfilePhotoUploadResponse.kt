package com.infravo.bhaktmilan.data.remote.response

data class ProfilePhotoUploadResponse(
    val success: Boolean,
    val message: String,
    val data: ProfilePhotoData?
)

data class ProfilePhotoData(
    val photo: String?
)