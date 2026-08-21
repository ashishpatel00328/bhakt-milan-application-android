package com.infravo.bhaktmilan.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infravo.bhaktmilan.data.network.NetworkResult
import com.infravo.bhaktmilan.data.remote.cache.LocationCache
import com.infravo.bhaktmilan.data.remote.repository.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(

    private val repository: LocationRepository

) : ViewModel() {

    private val _uiState =
        MutableStateFlow(LocationUiState())

    val uiState: StateFlow<LocationUiState> =
        _uiState.asStateFlow()
    fun loadCountries() {

        if (LocationCache.isCountriesLoaded()) {

            _uiState.value = _uiState.value.copy(

                countries = LocationCache.getCountries()

            )

            return

        }

        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(
                isLoading = true
            )

            when (val result = repository.getCountries()) {

                is NetworkResult.Success -> {

                    LocationCache.saveCountries(
                        result.data.data
                    )

                    _uiState.value = _uiState.value.copy(

                        isLoading = false,

                        countries = LocationCache.getCountries()

                    )

                }

                is NetworkResult.Error -> {

                    _uiState.value = _uiState.value.copy(

                        isLoading = false,

                        error = result.message

                    )

                }

                else -> Unit

            }

        }

    }
    // ==========================================
    // Load States
    // ==========================================

    fun loadStates(
        countryId: Int
    ) {

        if (LocationCache.hasStates(countryId)) {

            _uiState.value = _uiState.value.copy(

                states = LocationCache.getStates(countryId)

            )

            return

        }

        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(
                isLoading = true
            )

            when (

                val result =
                    repository.getStates(countryId)

            ) {

                is NetworkResult.Success -> {

                    LocationCache.saveStates(

                        countryId,

                        result.data.data

                    )

                    _uiState.value = _uiState.value.copy(

                        isLoading = false,

                        states = LocationCache.getStates(countryId)

                    )

                }

                is NetworkResult.Error -> {

                    _uiState.value = _uiState.value.copy(

                        isLoading = false,

                        error = result.message

                    )

                }

                is NetworkResult.Loading -> Unit

            }

        }

    }
    // ==========================================
    // Load Cities
    // ==========================================

    fun loadCities(
        stateId: Int
    ) {

        if (LocationCache.hasCities(stateId)) {

            _uiState.value = _uiState.value.copy(

                cities = LocationCache.getCities(stateId)

            )

            return

        }

        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(
                isLoading = true
            )

            when (

                val result =
                    repository.getCities(stateId)

            ) {

                is NetworkResult.Success -> {

                    LocationCache.saveCities(

                        stateId,

                        result.data.data

                    )

                    _uiState.value = _uiState.value.copy(

                        isLoading = false,

                        cities = LocationCache.getCities(stateId)

                    )

                }

                is NetworkResult.Error -> {

                    _uiState.value = _uiState.value.copy(

                        isLoading = false,

                        error = result.message

                    )

                }

                is NetworkResult.Loading -> Unit

            }

        }

    }
    // ==========================================
    // Helpers
    // ==========================================

    fun clearError() {

        _uiState.value = _uiState.value.copy(

            error = null

        )

    }

    fun clearStates() {

        _uiState.value = _uiState.value.copy(

            states = emptyMap(),

            cities = emptyMap()

        )

    }

    fun clearCities() {

        _uiState.value = _uiState.value.copy(

            cities = emptyMap()

        )

    }

}