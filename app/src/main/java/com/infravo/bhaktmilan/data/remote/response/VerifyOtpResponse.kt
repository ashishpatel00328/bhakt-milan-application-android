package com.infravo.bhaktmilan.data.remote.response

data class VerifyOtpResponse(
    val success: Boolean,
    val message: String,
    val data: VerifyOtpData
)

data class VerifyOtpData(
    val access: String,
    val refresh: String,
    val is_new_user: Boolean,
    val is_profile_completed: Boolean
)