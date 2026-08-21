package com.infravo.bhaktmilan.data.remote.repository

import com.infravo.bhaktmilan.data.network.NetworkResult
import com.infravo.bhaktmilan.data.remote.cache.LocationCache
import com.infravo.bhaktmilan.data.remote.cache.MastersCache
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BootstrapRepository @Inject constructor(

    private val masterRepository: MasterRepository,

    private val locationRepository: LocationRepository

) {

    // ==========================================
    // Bootstrap
    // ==========================================

    suspend fun ensureBootstrap(): NetworkResult<Unit> {

        val mastersResult =
            ensureMastersLoaded()

        if (mastersResult is NetworkResult.Error)
            return mastersResult

        val countriesResult =
            ensureCountriesLoaded()

        if (countriesResult is NetworkResult.Error)
            return countriesResult





        return NetworkResult.Success(Unit)

    }

    // ==========================================
    // Masters
    // ==========================================

    suspend fun ensureMastersLoaded(): NetworkResult<Unit> {

        if (MastersCache.isLoaded()) {
            return NetworkResult.Success(Unit)
        }

        return when (val result = masterRepository.getMasters()) {

            is NetworkResult.Success -> {

                MastersCache.save(
                    result.data.data
                )

                NetworkResult.Success(Unit)

            }

            is NetworkResult.Error -> {

                NetworkResult.Error(
                    code = result.code,
                    message = result.message
                )

            }

            is NetworkResult.Loading -> {

                NetworkResult.Loading

            }

        }

    }

    // ==========================================
    // Countries
    // ==========================================

    suspend fun ensureCountriesLoaded(): NetworkResult<Unit> {

        if (LocationCache.isCountriesLoaded()) {

            return NetworkResult.Success(Unit)

        }

        return when (

            val result =
                locationRepository.getCountries()

        ) {

            is NetworkResult.Success -> {

                LocationCache.saveCountries(
                    result.data.data
                )

                NetworkResult.Success(Unit)

            }

            is NetworkResult.Error -> {

                NetworkResult.Error(

                    code = result.code,

                    message = result.message

                )

            }

            is NetworkResult.Loading -> {

                NetworkResult.Loading

            }

        }

    }



    suspend fun ensureStatesLoaded(
        countryId: Int?
    ): NetworkResult<Unit> {

        if (countryId == null)
            return NetworkResult.Success(Unit)

        if (LocationCache.hasStates(countryId))
            return NetworkResult.Success(Unit)

        return when (

            val result =
                locationRepository.getStates(countryId)

        ) {

            is NetworkResult.Success -> {

                LocationCache.saveStates(

                    countryId,

                    result.data.data

                )

                NetworkResult.Success(Unit)

            }

            is NetworkResult.Error -> {

                NetworkResult.Error(

                    code = result.code,

                    message = result.message

                )

            }

            is NetworkResult.Loading -> {

                NetworkResult.Loading

            }

        }

    }



    suspend fun ensureCitiesLoaded(
        stateId: Int?
    ): NetworkResult<Unit> {

        if (stateId == null)
            return NetworkResult.Success(Unit)

        if (LocationCache.hasCities(stateId))
            return NetworkResult.Success(Unit)

        return when (

            val result =
                locationRepository.getCities(stateId)

        ) {

            is NetworkResult.Success -> {

                LocationCache.saveCities(

                    stateId,

                    result.data.data

                )

                NetworkResult.Success(Unit)

            }

            is NetworkResult.Error -> {

                NetworkResult.Error(

                    code = result.code,

                    message = result.message

                )

            }

            is NetworkResult.Loading -> {

                NetworkResult.Loading

            }

        }

    }




}