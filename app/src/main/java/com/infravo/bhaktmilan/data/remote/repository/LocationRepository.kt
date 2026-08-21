package com.infravo.bhaktmilan.data.remote.repository

import com.infravo.bhaktmilan.data.network.NetworkResult
import com.infravo.bhaktmilan.data.remote.api.ApiService
import com.infravo.bhaktmilan.data.remote.cache.LocationCache
import com.infravo.bhaktmilan.data.remote.response.MasterListResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationRepository @Inject constructor(
    private val apiService: ApiService
) : BaseRepository() {

    /**
     * Countries
     */
    suspend fun getCountries(): NetworkResult<MasterListResponse> {

        val result = safeApiCall {
            apiService.getCountries()
        }

        if (result is NetworkResult.Success) {
            LocationCache.saveCountries(result.data.data)
        }

        return result
    }

    /**
     * States
     */
    suspend fun getStates(
        countryId: Int
    ): NetworkResult<MasterListResponse> {

        val result = safeApiCall {
            apiService.getStates(countryId)
        }

        if (result is NetworkResult.Success) {
            LocationCache.saveStates(
                countryId,
                result.data.data
            )
        }

        return result
    }

    /**
     * Cities
     */
    suspend fun getCities(
        stateId: Int
    ): NetworkResult<MasterListResponse> {

        val result = safeApiCall {
            apiService.getCities(stateId)
        }

        if (result is NetworkResult.Success) {
            LocationCache.saveCities(
                stateId,
                result.data.data
            )
        }

        return result
    }

    fun clearCache() {
        LocationCache.clear()
    }
}