package com.infravo.bhaktmilan.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infravo.bhaktmilan.data.network.NetworkResult
import com.infravo.bhaktmilan.data.remote.cache.LocationCache
import com.infravo.bhaktmilan.data.remote.cache.MastersCache
import com.infravo.bhaktmilan.data.remote.repository.AuthRepository
import com.infravo.bhaktmilan.data.remote.repository.BootstrapRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(

    private val repository: AuthRepository,

    private val bootstrapRepository: BootstrapRepository

) : ViewModel() {

    private val _uiState =
        MutableStateFlow(AuthUiState())

    val uiState: StateFlow<AuthUiState> =
        _uiState.asStateFlow()

    // ==========================================
    // SEND OTP
    // ==========================================

    fun sendOtp(
        mobile: String
    ) {

        if (mobile.length != 10) {

            _uiState.value = _uiState.value.copy(
                error = "Enter a valid mobile number."
            )

            return

        }

        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(

                isLoading = true,

                error = null,

                otpSent = false

            )

            when (

                val result =
                    repository.sendOtp(mobile)

            ) {

                is NetworkResult.Success -> {

                    _uiState.value =
                        _uiState.value.copy(

                            isLoading = false,

                            otpSent = true

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
    // VERIFY OTP
    // ==========================================

    fun verifyOtp(
        mobile: String,
        otp: String
    ) {

        if (otp.length != 6) {

            _uiState.value = _uiState.value.copy(
                error = "Enter valid OTP."
            )

            return

        }

        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(

                isLoading = true,

                error = null

            )

            when (

                val result =
                    repository.verifyOtp(
                        mobile,
                        otp
                    )

            ) {

                is NetworkResult.Success -> {

                    val auth = result.data.data

                    when (

                        val bootstrap =
                            bootstrapRepository.ensureBootstrap()

                    ) {

                        is NetworkResult.Success -> {

                            _uiState.value =
                                _uiState.value.copy(

                                    isLoading = false,

                                    loginSuccess = true,

                                    isNewUser =
                                        auth.is_new_user,

                                    isProfileCompleted =
                                        auth.is_profile_completed

                                )

                        }

                        is NetworkResult.Error -> {

                            _uiState.value =
                                _uiState.value.copy(

                                    isLoading = false,

                                    error = bootstrap.message

                                )

                        }

                        is NetworkResult.Loading -> Unit

                    }

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
    // LOGOUT
    // ==========================================

    fun logout() {

        viewModelScope.launch {

            repository.logout()

            MastersCache.clear()

            LocationCache.clear()

            _uiState.value = AuthUiState()

        }

    }

    // ==========================================
    // CLEAR UI STATE
    // ==========================================

    fun clearState() {

        _uiState.value = _uiState.value.copy(

            isLoading = false,

            error = null,

            otpSent = false,

            loginSuccess = false

        )

    }

}