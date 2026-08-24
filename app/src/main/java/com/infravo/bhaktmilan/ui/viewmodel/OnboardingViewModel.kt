package com.infravo.bhaktmilan.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infravo.bhaktmilan.data.mapper.toCreateProfileRequest
import com.infravo.bhaktmilan.data.mapper.toUpdateProfileRequest
import com.infravo.bhaktmilan.data.network.NetworkResult
import com.infravo.bhaktmilan.data.remote.cache.LocationCache
import com.infravo.bhaktmilan.data.remote.cache.MastersCache
import com.infravo.bhaktmilan.data.remote.repository.BootstrapRepository
import com.infravo.bhaktmilan.data.remote.repository.ProfileRepository
import com.infravo.bhaktmilan.data.remote.response.MasterItem
import com.infravo.bhaktmilan.ui.model.OnboardingFormData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class OnboardingUiState(
    val isLoading: Boolean = false,
    val isSubmitting: Boolean = false,
    val isCreated: Boolean = false,
    val error: String? = null,

    val genders: List<MasterItem> = emptyList(),
    val maritalStatuses: List<MasterItem> = emptyList(),
    val bloodGroups: List<MasterItem> = emptyList(),
    val disabilities: List<MasterItem> = emptyList(),
    val dietPreferences: List<MasterItem> = emptyList(),
    val profileManagedBy: List<MasterItem> = emptyList(),

    val countries: List<MasterItem> = emptyList(),
    val states: List<MasterItem> = emptyList(),
    val cities: List<MasterItem> = emptyList()
)

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val bootstrapRepository: BootstrapRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(OnboardingUiState())

    val uiState: StateFlow<OnboardingUiState> =
        _uiState.asStateFlow()

    init {
        loadInitialData()
    }

    // ==========================================
    // Initial Bootstrap Data
    // ==========================================

    private fun loadInitialData() {

        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )

            when (
                val result =
                    bootstrapRepository.ensureBootstrap()
            ) {

                is NetworkResult.Success -> {

                    _uiState.value =
                        _uiState.value.copy(
                            isLoading = false,

                            genders =
                                MastersCache.getGenders(),

                            maritalStatuses =
                                MastersCache.getMaritalStatuses(),

                            bloodGroups =
                                MastersCache.getBloodGroups(),

                            disabilities =
                                MastersCache.getDisabilities(),

                            dietPreferences =
                                MastersCache.getDietPreferences(),

                            profileManagedBy =
                                MastersCache.getProfileManagedBy(),

                            countries =
                                LocationCache.getCountryItems()
                        )
                }

                is NetworkResult.Error -> {

                    _uiState.value =
                        _uiState.value.copy(
                            isLoading = false,
                            error = result.message
                        )
                }

                is NetworkResult.Loading -> Unit
            }
        }
    }

    // ==========================================
    // Load States
    // ==========================================

    fun loadStates(
        countryId: Int
    ) {

        viewModelScope.launch {

            when (
                val result =
                    bootstrapRepository.ensureStatesLoaded(
                        countryId
                    )
            ) {

                is NetworkResult.Success -> {

                    _uiState.value =
                        _uiState.value.copy(
                            states =
                                LocationCache.getStateItems(
                                    countryId
                                ),
                            cities = emptyList(),
                            error = null
                        )
                }

                is NetworkResult.Error -> {

                    _uiState.value =
                        _uiState.value.copy(
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

        viewModelScope.launch {

            when (
                val result =
                    bootstrapRepository.ensureCitiesLoaded(
                        stateId
                    )
            ) {

                is NetworkResult.Success -> {

                    _uiState.value =
                        _uiState.value.copy(
                            cities =
                                LocationCache.getCityItems(
                                    stateId
                                ),
                            error = null
                        )
                }

                is NetworkResult.Error -> {

                    _uiState.value =
                        _uiState.value.copy(
                            error = result.message
                        )
                }

                is NetworkResult.Loading -> Unit
            }
        }
    }

    // ==========================================
    // Create Profile
    // ==========================================

    fun createProfile(
        form: OnboardingFormData
    ) {

        viewModelScope.launch {

            _uiState.value =
                _uiState.value.copy(
                    isSubmitting = true,
                    error = null,
                    isCreated = false
                )

            try {

                val request =
                    form.toCreateProfileRequest()

                when (
                    val result =
                        profileRepository.createProfile(
                            request
                        )
                ) {

                    is NetworkResult.Success -> {

                        _uiState.value =
                            _uiState.value.copy(
                                isSubmitting = false,
                                isCreated = true
                            )
                    }

                    is NetworkResult.Error -> {

                        _uiState.value =
                            _uiState.value.copy(
                                isSubmitting = false,
                                error = result.message
                            )
                    }

                    is NetworkResult.Loading -> Unit
                }

            } catch (e: IllegalStateException) {

                _uiState.value =
                    _uiState.value.copy(
                        isSubmitting = false,
                        error = e.message
                            ?: "Please complete all required fields."
                    )
            }
        }
    }

    // ==========================================
    // Clear Error
    // ==========================================

    fun clearError() {

        _uiState.value =
            _uiState.value.copy(
                error = null
            )
    }

    // ==========================================
    // Clear Created State
    // ==========================================

    fun clearCreatedState() {

        _uiState.value =
            _uiState.value.copy(
                isCreated = false
            )
    }

    fun updateProfile(
        form: OnboardingFormData
    ) {

        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(
                isSubmitting = true,
                error = null,
                isCreated = false
            )

            try {

                val request =
                    form.toUpdateProfileRequest()

                when (
                    val result =
                        profileRepository.updateProfile(request)
                ) {

                    is NetworkResult.Success -> {

                        _uiState.value =
                            _uiState.value.copy(
                                isSubmitting = false,
                                isCreated = true
                            )
                    }

                    is NetworkResult.Error -> {

                        _uiState.value =
                            _uiState.value.copy(
                                isSubmitting = false,
                                error = result.message
                            )
                    }

                    is NetworkResult.Loading -> Unit
                }

            } catch (e: Exception) {

                _uiState.value =
                    _uiState.value.copy(
                        isSubmitting = false,
                        error = e.message
                            ?: "Unable to update profile."
                    )
            }
        }
    }
}