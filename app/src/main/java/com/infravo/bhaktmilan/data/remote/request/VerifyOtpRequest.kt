package com.infravo.bhaktmilan.data.remote.request

data class VerifyOtpRequest(
    val mobile: String,
    val otp: String
)