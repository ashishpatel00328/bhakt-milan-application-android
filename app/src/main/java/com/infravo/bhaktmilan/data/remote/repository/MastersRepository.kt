package com.infravo.bhaktmilan.data.remote.repository

import com.infravo.bhaktmilan.data.network.NetworkResult
import com.infravo.bhaktmilan.data.remote.api.ApiService
import com.infravo.bhaktmilan.data.remote.response.MastersResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MasterRepository @Inject constructor(

    private val apiService: ApiService

) : BaseRepository() {

    // ==========================================
    // Bootstrap Masters
    // ==========================================

    suspend fun getMasters(): NetworkResult<MastersResponse> {

        return safeApiCall {

            apiService.getMasters()

        }

    }

}