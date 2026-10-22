package com.infravo.bhaktmilan.data.remote.repository

import com.infravo.bhaktmilan.data.remote.api.ApiService
import com.infravo.bhaktmilan.data.remote.response.OnlineStatusResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OnlineStatusRepository @Inject constructor(
    private val ApiService: ApiService
) {
    suspend fun getOnlineStatus(
        userId: Int
    ): Result<OnlineStatusResponse> {
        return try {
            val response = ApiService.getOnlineStatus(userId)

            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) }
                    ?: Result.failure(
                        Exception("Online status response empty hai")
                    )
            } else {
                Result.failure(
                    Exception("API error: ${response.code()}")
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}