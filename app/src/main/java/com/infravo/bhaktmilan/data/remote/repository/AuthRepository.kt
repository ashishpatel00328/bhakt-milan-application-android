package com.infravo.bhaktmilan.data.remote.repository

import com.infravo.bhaktmilan.data.network.NetworkResult
import com.infravo.bhaktmilan.data.remote.api.ApiService
import com.infravo.bhaktmilan.data.remote.request.SendOtpRequest
import com.infravo.bhaktmilan.data.remote.request.VerifyOtpRequest
import com.infravo.bhaktmilan.data.remote.response.SendOtpResponse
import com.infravo.bhaktmilan.data.remote.response.VerifyOtpResponse
import com.infravo.bhaktmilan.data.storage.TokenManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val apiService: ApiService,
    private val tokenManager: TokenManager
) : BaseRepository() {

    suspend fun sendOtp(
        mobile: String
    ): NetworkResult<SendOtpResponse> {

        return safeApiCall {

            apiService.sendOtp(
                SendOtpRequest(
                    mobile = mobile
                )
            )
        }
    }

    suspend fun verifyOtp(
        mobile: String,
        otp: String
    ): NetworkResult<VerifyOtpResponse> {

        val result = safeApiCall {

            apiService.verifyOtp(
                VerifyOtpRequest(
                    mobile = mobile,
                    otp = otp
                )
            )
        }

        if (result is NetworkResult.Success) {

            tokenManager.saveTokens(
                accessToken = result.data.data.access,
                refreshToken = result.data.data.refresh
            )
        }

        return result
    }

    suspend fun logout() {
        tokenManager.clearTokens()
    }

    suspend fun isLoggedIn(): Boolean {
        return tokenManager.isLoggedIn()
    }
}