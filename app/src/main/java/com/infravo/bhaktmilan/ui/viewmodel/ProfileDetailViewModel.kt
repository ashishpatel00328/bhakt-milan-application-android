package com.infravo.bhaktmilan.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infravo.bhaktmilan.data.network.NetworkResult
import com.infravo.bhaktmilan.data.remote.repository.BootstrapRepository
import com.infravo.bhaktmilan.data.remote.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileDetailViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val bootstrapRepository: BootstrapRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(ProfileDetailUiState())

    val uiState: StateFlow<ProfileDetailUiState> =
        _uiState.asStateFlow()

    // ==========================================
    // Load Profile
    // ==========================================

    fun loadProfile(
        profileCode: String
    ) {

        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )

            when (
                val result =
                    profileRepository.getProfileDetail(profileCode)
            ) {

                is NetworkResult.Success -> {

                    val profile = result.data.data

                    // ----------------------------------
                    // Ensure Bootstrap
                    // Masters + Countries
                    // ----------------------------------

                    when (
                        val bootstrap =
                            bootstrapRepository.ensureBootstrap()
                    ) {

                        is NetworkResult.Error -> {

                            _uiState.value =
                                _uiState.value.copy(
                                    isLoading = false,
                                    error = bootstrap.message
                                )

                            return@launch
                        }

                        else -> Unit
                    }

                    // ----------------------------------
                    // Lazy Load States
                    // ----------------------------------

                    when (
                        val stateResult =
                            bootstrapRepository.ensureStatesLoaded(
                                profile.country
                            )
                    ) {

                        is NetworkResult.Error -> {

                            _uiState.value =
                                _uiState.value.copy(
                                    isLoading = false,
                                    error = stateResult.message
                                )

                            return@launch
                        }

                        else -> Unit
                    }

                    // ----------------------------------
                    // Lazy Load Cities
                    // ----------------------------------

                    when (
                        val cityResult =
                            bootstrapRepository.ensureCitiesLoaded(
                                profile.state
                            )
                    ) {

                        is NetworkResult.Error -> {

                            _uiState.value =
                                _uiState.value.copy(
                                    isLoading = false,
                                    error = cityResult.message
                                )

                            return@launch
                        }

                        else -> Unit
                    }

                    // ----------------------------------
                    // Success
                    // ----------------------------------

                    _uiState.value =
                        _uiState.value.copy(
                            isLoading = false,
                            profile = profile
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
    // Clear Error
    // ==========================================

    fun clearError() {

        _uiState.value =
            _uiState.value.copy(
                error = null
            )
    }
}