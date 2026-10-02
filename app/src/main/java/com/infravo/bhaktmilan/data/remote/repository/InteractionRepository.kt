package com.infravo.bhaktmilan.data.remote.repository

import com.infravo.bhaktmilan.data.network.NetworkResult
import com.infravo.bhaktmilan.data.remote.api.ApiService
import com.infravo.bhaktmilan.data.remote.request.InterestRequest
import com.infravo.bhaktmilan.data.remote.request.ShortlistRequest
import com.infravo.bhaktmilan.data.remote.response.Interest
import com.infravo.bhaktmilan.data.remote.response.InterestResponse
import com.infravo.bhaktmilan.data.remote.response.ShortlistResponse
import com.infravo.bhaktmilan.data.remote.response.ShortlistedProfile
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
    // Accept Interest
    // ==========================================

    suspend fun acceptInterest(
        interestId: Int
    ): NetworkResult<InterestResponse> {

        return safeApiCall {
            apiService.acceptInterest(interestId)
        }
    }

    // ==========================================
    // Reject Interest
    // ==========================================

    suspend fun rejectInterest(
        interestId: Int
    ): NetworkResult<InterestResponse> {

        return safeApiCall {
            apiService.rejectInterest(interestId)
        }
    }

    // ==========================================
    // Cancel Interest
    // ==========================================

    suspend fun cancelInterest(
        interestId: Int
    ): NetworkResult<InterestResponse> {

        return safeApiCall {
            apiService.cancelInterest(interestId)
        }
    }

    // ==========================================
    // Sent Interests
    // ==========================================

    suspend fun getSentInterests(): NetworkResult<List<Interest>> {

        return when (
            val result = safeApiCall {
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
    // Received Interests
    // ==========================================

    suspend fun getReceivedInterests(): NetworkResult<List<Interest>> {

        return when (
            val result = safeApiCall {
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

    // ==========================================
    // Add Profile to Shortlist
    // ==========================================

    suspend fun addToShortlist(
        profileId: String
    ): NetworkResult<ShortlistResponse> {

        return safeApiCall {
            apiService.addToShortlist(
                ShortlistRequest(
                    profile_id = profileId
                )
            )
        }
    }

    // ==========================================
    // Remove Profile from Shortlist
    // ==========================================

    suspend fun removeFromShortlist(
        profileId: String
    ): NetworkResult<ShortlistResponse> {

        return safeApiCall {
            apiService.removeFromShortlist(profileId)
        }
    }

    // ==========================================
    // Get Shortlisted Profiles
    // ==========================================

    suspend fun getShortlistedProfiles():
            NetworkResult<List<ShortlistedProfile>> {

        return when (
            val result = safeApiCall {
                apiService.getShortlistedProfiles()
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