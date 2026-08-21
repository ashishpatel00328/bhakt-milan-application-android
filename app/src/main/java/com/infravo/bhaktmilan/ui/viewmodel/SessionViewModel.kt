package com.infravo.bhaktmilan.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infravo.bhaktmilan.data.network.NetworkResult
import com.infravo.bhaktmilan.data.remote.repository.ProfileRepository
import com.infravo.bhaktmilan.data.storage.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val tokenManager: TokenManager,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(SessionUiState())

    val uiState: StateFlow<SessionUiState> =
        _uiState.asStateFlow()

    init {
        checkSession()
    }

    fun checkSession() {

        viewModelScope.launch {

            val token =
                tokenManager.getAccessToken()

            // No saved token
            if (token.isNullOrBlank()) {

                _uiState.value = SessionUiState(
                    isChecking = false,
                    isLoggedIn = false
                )

                return@launch
            }

            // Validate token against backend
            when (
                val result =
                    profileRepository.getMyProfile()
            ) {

                is NetworkResult.Success -> {

                    _uiState.value = SessionUiState(
                        isChecking = false,
                        isLoggedIn = true
                    )
                }

                is NetworkResult.Error -> {

                    tokenManager.clearTokens()

                    _uiState.value = SessionUiState(
                        isChecking = false,
                        isLoggedIn = false
                    )
                }

                is NetworkResult.Loading -> Unit
            }
        }
    }
}