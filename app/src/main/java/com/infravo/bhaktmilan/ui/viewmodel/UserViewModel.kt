package com.infravo.bhaktmilan.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infravo.bhaktmilan.data.network.NetworkResult
import com.infravo.bhaktmilan.data.remote.repository.AuthRepository
import com.infravo.bhaktmilan.data.remote.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        MyProfileUiState()
    )

    val uiState: StateFlow<MyProfileUiState> =
        _uiState.asStateFlow()

    // =========================================================
    // LOAD MY PROFILE
    // =========================================================

    fun loadMyProfile() {

        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )

            when (
                val result = profileRepository.getMyProfile()
            ) {

                is NetworkResult.Success -> {

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        profile = result.data.data,
                        error = null
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

    // =========================================================
    // LOGOUT
    // =========================================================

    fun logout() {

        viewModelScope.launch {

            /*
             * AuthRepository already handles TokenManager.clearTokens().
             * No API call is required for the current logout flow.
             */
            authRepository.logout()

            /*
             * Clear profile data from ViewModel so that
             * old profile information is not retained.
             */
            _uiState.value = MyProfileUiState()
        }
    }

    // =========================================================
    // CLEAR ERROR
    // =========================================================

    fun clearError() {

        _uiState.value = _uiState.value.copy(
            error = null
        )
    }

    // =========================================================
    // CLEAR STATE
    // =========================================================

    fun clear() {

        _uiState.value = MyProfileUiState()
    }
}