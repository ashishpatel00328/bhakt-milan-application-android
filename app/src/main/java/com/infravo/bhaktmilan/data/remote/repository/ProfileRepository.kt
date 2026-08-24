package com.infravo.bhaktmilan.data.remote.repository

import com.infravo.bhaktmilan.data.network.NetworkResult
import com.infravo.bhaktmilan.data.remote.api.ApiService
import com.infravo.bhaktmilan.data.remote.request.CreateProfileRequest
import com.infravo.bhaktmilan.data.remote.request.UpdateProfileRequest
import com.infravo.bhaktmilan.data.remote.response.ProfileDetailResponse
import com.infravo.bhaktmilan.data.remote.response.ProfileListResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepository @Inject constructor(
    private val apiService: ApiService
) : BaseRepository() {

    // ==========================================
    // Profile List
    // ==========================================

    suspend fun getProfiles(
        page: Int = 1
    ): NetworkResult<ProfileListResponse> {

        return safeApiCall {
            apiService.getProfiles(page)
        }
    }

    // ==========================================
    // Profile Detail
    // ==========================================

    suspend fun getProfileDetail(
        profileId: String
    ): NetworkResult<ProfileDetailResponse> {

        return safeApiCall {
            apiService.getProfileDetail(profileId)
        }
    }

    // ==========================================
    // Create Profile
    // ==========================================

    suspend fun createProfile(
        request: CreateProfileRequest
    ): NetworkResult<ProfileDetailResponse> {

        return safeApiCall {
            apiService.createProfile(request)
        }
    }
    // ==========================================
    // My Profile
    // ==========================================

    suspend fun getMyProfile(): NetworkResult<ProfileDetailResponse> {

        return safeApiCall {
            apiService.getMyProfile()
        }
    }

    suspend fun updateProfile(
        request: UpdateProfileRequest
    ): NetworkResult<ProfileDetailResponse> {

        return safeApiCall {
            apiService.updateProfile(request)
        }
    }
}