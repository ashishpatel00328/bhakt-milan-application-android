package com.infravo.bhaktmilan.data.remote.repository

import com.infravo.bhaktmilan.data.network.NetworkResult
import com.infravo.bhaktmilan.data.remote.api.ApiService
import com.infravo.bhaktmilan.data.remote.request.InterestRequest
import com.infravo.bhaktmilan.data.remote.response.Interest
import com.infravo.bhaktmilan.data.remote.response.InterestListResponse
import com.infravo.bhaktmilan.data.remote.response.InterestResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InteractionRepository @Inject constructor(
    private val apiService: ApiService
) : BaseRepository() {

    // ==========================================
    // Send Interest
    // ==========================================

    suspend fun sendInterest(
        receiverProfile: Int,
        message: String
    ): NetworkResult<InterestResponse> {

        return safeApiCall {
            apiService.sendInterest(
                InterestRequest(
                    receiver_profile = receiverProfile,
                    message = message
                )
            )
        }
    }

    // ==========================================
    // Accept
    // ==========================================

    suspend fun acceptInterest(
        interestId: Int
    ): NetworkResult<InterestResponse> {

        return safeApiCall {
            apiService.acceptInterest(interestId)
        }
    }

    // ==========================================
    // Reject
    // ==========================================

    suspend fun rejectInterest(
        interestId: Int
    ): NetworkResult<InterestResponse> {

        return safeApiCall {
            apiService.rejectInterest(interestId)
        }
    }

    // ==========================================
    // Cancel
    // ==========================================

    suspend fun cancelInterest(
        interestId: Int
    ): NetworkResult<InterestResponse> {

        return safeApiCall {
            apiService.cancelInterest(interestId)
        }
    }

    // ==========================================
    // Sent
    // ==========================================

    // ==========================================
// Sent
// ==========================================

    suspend fun getSentInterests():
            NetworkResult<List<Interest>> {

        return when (
            val result =
                safeApiCall {
                    apiService.getSentInterests()
                }
        ) {

            is NetworkResult.Success -> {

                NetworkResult.Success(
                    result.data.data
                )
            }

            is NetworkResult.Error -> {

                result
            }

            is NetworkResult.Loading -> {

                NetworkResult.Loading
            }
        }
    }
    // ==========================================
    // Received
    // ==========================================

    // ==========================================
// Received
// ==========================================

    // ==========================================
// Received
// ==========================================

    suspend fun getReceivedInterests():
            NetworkResult<List<Interest>> {

        return when (
            val result =
                safeApiCall {
                    apiService.getReceivedInterests()
                }
        ) {

            is NetworkResult.Success -> {

                NetworkResult.Success(
                    result.data.data
                )
            }

            is NetworkResult.Error -> {

                result
            }

            is NetworkResult.Loading -> {

                NetworkResult.Loading
            }
        }
    }
}