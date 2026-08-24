package com.infravo.bhaktmilan.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infravo.bhaktmilan.data.network.NetworkResult
import com.infravo.bhaktmilan.data.remote.repository.InteractionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InteractionViewModel @Inject constructor(
    private val repository: InteractionRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(InteractionUiState())

    val uiState: StateFlow<InteractionUiState> =
        _uiState.asStateFlow()

    // ==========================================
    // Load Sent + Received
    // ==========================================

    fun loadInteractions() {

        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )

            val sentDeferred = async {
                repository.getSentInterests()
            }

            val receivedDeferred = async {
                repository.getReceivedInterests()
            }

            // Do NOT use awaitAll() here.
            // awaitAll() loses the concrete generic type.

            val sentResult =
                sentDeferred.await()

            val receivedResult =
                receivedDeferred.await()

            var error: String? = null

            // ==========================================
            // Sent Interests
            // ==========================================

            when (sentResult) {

                is NetworkResult.Success -> {

                    _uiState.value =
                        _uiState.value.copy(
                            sentInterests =
                                sentResult.data
                        )
                }

                is NetworkResult.Error -> {

                    error =
                        sentResult.message
                }

                is NetworkResult.Loading -> Unit
            }

            // ==========================================
            // Received Interests
            // ==========================================

            when (receivedResult) {

                is NetworkResult.Success -> {

                    _uiState.value =
                        _uiState.value.copy(
                            receivedInterests =
                                receivedResult.data
                        )
                }

                is NetworkResult.Error -> {

                    if (error == null) {

                        error =
                            receivedResult.message
                    }
                }

                is NetworkResult.Loading -> Unit
            }

            // ==========================================
            // Final State
            // ==========================================

            _uiState.value =
                _uiState.value.copy(
                    isLoading = false,
                    error = error
                )
        }
    }

    // ==========================================
    // Send Interest
    // ==========================================

    fun sendInterest(
        receiverProfileId: Int,
        message: String = ""
    ) {

        viewModelScope.launch {

            setActionLoading()

            when (
                val result =
                    repository.sendInterest(
                        receiverProfile =
                            receiverProfileId,
                        message = message
                    )
            ) {

                is NetworkResult.Success -> {

                    _uiState.value =
                        _uiState.value.copy(
                            isActionLoading = false,
                            lastActionSuccess = true,
                            message =
                                result.data.message,
                            error = null
                        )

                    loadInteractions()
                }

                is NetworkResult.Error -> {

                    _uiState.value =
                        _uiState.value.copy(
                            isActionLoading = false,
                            error = result.message
                        )
                }

                is NetworkResult.Loading -> Unit
            }
        }
    }

    // ==========================================
    // Accept Interest
    // ==========================================

    fun acceptInterest(
        interestId: Int
    ) {

        viewModelScope.launch {

            setActionLoading()

            when (
                val result =
                    repository.acceptInterest(
                        interestId
                    )
            ) {

                is NetworkResult.Success -> {

                    _uiState.value =
                        _uiState.value.copy(
                            isActionLoading = false,
                            lastActionSuccess = true,
                            message =
                                result.data.message,
                            error = null
                        )

                    loadInteractions()
                }

                is NetworkResult.Error -> {

                    _uiState.value =
                        _uiState.value.copy(
                            isActionLoading = false,
                            error = result.message
                        )
                }

                is NetworkResult.Loading -> Unit
            }
        }
    }

    // ==========================================
    // Reject Interest
    // ==========================================

    fun rejectInterest(
        interestId: Int
    ) {

        viewModelScope.launch {

            setActionLoading()

            when (
                val result =
                    repository.rejectInterest(
                        interestId
                    )
            ) {

                is NetworkResult.Success -> {

                    _uiState.value =
                        _uiState.value.copy(
                            isActionLoading = false,
                            lastActionSuccess = true,
                            message =
                                result.data.message,
                            error = null
                        )

                    loadInteractions()
                }

                is NetworkResult.Error -> {

                    _uiState.value =
                        _uiState.value.copy(
                            isActionLoading = false,
                            error = result.message
                        )
                }

                is NetworkResult.Loading -> Unit
            }
        }
    }

    // ==========================================
    // Cancel Interest
    // ==========================================

    fun cancelInterest(
        interestId: Int
    ) {

        viewModelScope.launch {

            setActionLoading()

            when (
                val result =
                    repository.cancelInterest(
                        interestId
                    )
            ) {

                is NetworkResult.Success -> {

                    _uiState.value =
                        _uiState.value.copy(
                            isActionLoading = false,
                            lastActionSuccess = true,
                            message =
                                result.data.message,
                            error = null
                        )

                    loadInteractions()
                }

                is NetworkResult.Error -> {

                    _uiState.value =
                        _uiState.value.copy(
                            isActionLoading = false,
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

    private fun setActionLoading() {

        _uiState.value =
            _uiState.value.copy(
                isActionLoading = true,
                lastActionSuccess = false,
                message = null,
                error = null
            )
    }

    fun clearActionState() {

        _uiState.value =
            _uiState.value.copy(
                lastActionSuccess = false,
                message = null
            )
    }

    fun clearError() {

        _uiState.value =
            _uiState.value.copy(
                error = null
            )
    }
}