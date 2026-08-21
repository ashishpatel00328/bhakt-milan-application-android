package com.infravo.bhaktmilan.data.remote.api

import com.infravo.bhaktmilan.data.remote.request.CreateProfileRequest
import com.infravo.bhaktmilan.data.remote.request.SendOtpRequest
import com.infravo.bhaktmilan.data.remote.request.VerifyOtpRequest
import com.infravo.bhaktmilan.data.remote.response.MasterItem
import com.infravo.bhaktmilan.data.remote.response.MastersResponse
import com.infravo.bhaktmilan.data.remote.response.ProfileDetailResponse
import com.infravo.bhaktmilan.data.remote.response.ProfileListResponse
import com.infravo.bhaktmilan.data.remote.response.SendOtpResponse
import com.infravo.bhaktmilan.data.remote.response.VerifyOtpResponse
import com.infravo.bhaktmilan.data.remote.response.MasterListResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    // =========================
    // Authentication
    // =========================

    @POST("auth/send-otp/")
    suspend fun sendOtp(
        @Body request: SendOtpRequest
    ): Response<SendOtpResponse>

    @POST("auth/verify-otp/")
    suspend fun verifyOtp(
        @Body request: VerifyOtpRequest
    ): Response<VerifyOtpResponse>

    // =========================
    // Masters
    // =========================

    @GET("masters/")
    suspend fun getMasters(): Response<MastersResponse>

    // =========================
    // Profiles
    // =========================

    @POST("profile/")
    suspend fun createProfile(
        @Body request: CreateProfileRequest
    ): Response<ProfileDetailResponse>

    @GET("profile/profiles/")
    suspend fun getProfiles(
        @Query("page") page: Int
    ): Response<ProfileListResponse>

    @GET("profile/profiles/{profileId}/")
    suspend fun getProfileDetail(
        @Path("profileId") profileId: String
    ): Response<ProfileDetailResponse>

    // =========================
    // My Profile
    // =========================



    @GET("profile/my-profile/")
    suspend fun getMyProfile(): Response<ProfileDetailResponse>

    // =========================
    // Location Masters
    // =========================

    @GET("masters/countries/")
    suspend fun getCountries(): Response<MasterListResponse>

    @GET("masters/states/")
    suspend fun getStates(
        @Query("country") countryId: Int
    ): Response<MasterListResponse>

    @GET("masters/cities/")
    suspend fun getCities(
        @Query("state") stateId: Int
    ): Response<MasterListResponse>

// =========================
// Guru
// =========================

//    @GET("masters/gurus/")
//    suspend fun getGurus(): Response<MasterListResponse>
//
//    @GET("masters/sub-castes/")
//    suspend fun getSubCastes(
//        @Query("caste") casteId: Int
//    ): Response<MasterListResponse>
//
//    @GET("masters/gotras/")
//    suspend fun getGotras(): Response<MasterListResponse>
//
//    @GET("masters/nakshatras/")
//    suspend fun getNakshatras(): Response<MasterListResponse>
//
//    @GET("masters/zodiacs/")
//    suspend fun getZodiacs(): Response<MasterListResponse>
}