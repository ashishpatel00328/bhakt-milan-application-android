package com.infravo.bhaktmilan.data.model

data class SendOtpRequest(
    val mobile: String
)

data class VerifyOtpRequest(
    val mobile: String,
    val otp: String
)

data class LoginResponse(
    val access: String,
    val refresh: String
)

data class ApiResponse(
    val message: String
)


