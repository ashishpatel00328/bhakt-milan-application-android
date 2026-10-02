package com.infravo.bhaktmilan.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infravo.bhaktmilan.data.network.NetworkResult
import com.infravo.bhaktmilan.data.remote.repository.InteractionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class InteractionViewModel @Inject constructor(
    private val repository: InteractionRepository
) : ViewModel() {

    private val _uiState =
        kotlinx.coroutines.flow.MutableStateFlow(
            InteractionUiState()
        )

    val uiState =
        _uiState.asStateFlow()


    // =========================================================
    // LOAD INTERACTIONS
    // =========================================================

    fun loadInteractions() {

        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )

            when (val result = repository.getSentInterests()) {

                is NetworkResult.Success -> {

                    _uiState.value =
                        _uiState.value.copy(
                            sentInterests = result.data
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

            when (
                val result =
                    repository.getReceivedInterests()
            ) {

                is NetworkResult.Success -> {

                    _uiState.value =
                        _uiState.value.copy(
                            receivedInterests = result.data,
                            isLoading = false
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


    // =========================================================
    // SEND INTEREST
    // =========================================================

    fun sendInterest(
        receiverProfileId: Int,
        message: String
    ) {

        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(
                isActionLoading = true,
                error = null,
                lastActionSuccess = false
            )

            when (
                val result = repository.sendInterest(
                    receiverProfile = receiverProfileId.toInt(),
                    message = message
                )
            ) {

                is NetworkResult.Success -> {

                    _uiState.value =
                        _uiState.value.copy(
                            isActionLoading = false,
                            lastActionSuccess = true,
                            message = result.data.message,
                            error = null
                        )

                    loadInteractions()
                }

                is NetworkResult.Error -> {

                    _uiState.value =
                        _uiState.value.copy(
                            isActionLoading = false,
                            lastActionSuccess = false,
                            error = result.message
                        )
                }

                is NetworkResult.Loading -> Unit
            }
        }
    }


    // =========================================================
    // ACCEPT INTEREST
    // =========================================================

    fun acceptInterest(
        interestId: Int
    ) {

        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(
                isActionLoading = true,
                error = null,
                lastActionSuccess = false
            )

            when (
                val result =
                    repository.acceptInterest(interestId)
            ) {

                is NetworkResult.Success -> {

                    _uiState.value =
                        _uiState.value.copy(
                            isActionLoading = false,
                            lastActionSuccess = true,
                            message = result.data.message,
                            error = null
                        )

                    loadInteractions()
                }

                is NetworkResult.Error -> {

                    _uiState.value =
                        _uiState.value.copy(
                            isActionLoading = false,
                            lastActionSuccess = false,
                            error = result.message
                        )
                }

                is NetworkResult.Loading -> Unit
            }
        }
    }


    // =========================================================
    // REJECT INTEREST
    // =========================================================

    fun rejectInterest(
        interestId: Int
    ) {

        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(
                isActionLoading = true,
                error = null,
                lastActionSuccess = false
            )

            when (
                val result =
                    repository.rejectInterest(interestId)
            ) {

                is NetworkResult.Success -> {

                    _uiState.value =
                        _uiState.value.copy(
                            isActionLoading = false,
                            lastActionSuccess = true,
                            message = result.data.message,
                            error = null
                        )

                    loadInteractions()
                }

                is NetworkResult.Error -> {

                    _uiState.value =
                        _uiState.value.copy(
                            isActionLoading = false,
                            lastActionSuccess = false,
                            error = result.message
                        )
                }

                is NetworkResult.Loading -> Unit
            }
        }
    }


    // =========================================================
    // CANCEL INTEREST
    // =========================================================

    fun cancelInterest(
        interestId: Int
    ) {

        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(
                isActionLoading = true,
                error = null,
                lastActionSuccess = false
            )

            when (
                val result =
                    repository.cancelInterest(interestId)
            ) {

                is NetworkResult.Success -> {

                    _uiState.value =
                        _uiState.value.copy(
                            isActionLoading = false,
                            lastActionSuccess = true,
                            message = result.data.message,
                            error = null
                        )

                    loadInteractions()
                }

                is NetworkResult.Error -> {

                    _uiState.value =
                        _uiState.value.copy(
                            isActionLoading = false,
                            lastActionSuccess = false,
                            error = result.message
                        )
                }

                is NetworkResult.Loading -> Unit
            }
        }
    }


    // =========================================================
    // LOAD SHORTLISTED PROFILES
    // =========================================================

    fun loadShortlistedProfiles() {

        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(
                isShortlistLoading = true,
                shortlistError = null
            )

            when (
                val result =
                    repository.getShortlistedProfiles()
            ) {

                is NetworkResult.Success -> {

                    _uiState.value =
                        _uiState.value.copy(
                            isShortlistLoading = false,
                            shortlistedProfiles = result.data,
                            shortlistError = null
                        )
                }

                is NetworkResult.Error -> {

                    _uiState.value =
                        _uiState.value.copy(
                            isShortlistLoading = false,
                            shortlistError = result.message
                        )
                }

                is NetworkResult.Loading -> Unit
            }
        }
    }


    // =========================================================
    // ADD TO SHORTLIST
    // =========================================================

    fun addToShortlist(
        profileId: String
    ) {

        viewModelScope.launch {

            /*
             * IMPORTANT:
             *
             * Only THIS profile is marked as loading.
             * Other profile cards will not refresh.
             */
            _uiState.value = _uiState.value.copy(
                shortlistActionProfileId = profileId,
                shortlistError = null,
                message = null
            )

            when (
                val result =
                    repository.addToShortlist(profileId)
            ) {

                is NetworkResult.Success -> {

                    _uiState.value =
                        _uiState.value.copy(
                            shortlistActionProfileId = null,
                            message = result.data.message,
                            shortlistError = null
                        )

                    loadShortlistedProfiles()
                }

                is NetworkResult.Error -> {

                    _uiState.value =
                        _uiState.value.copy(
                            shortlistActionProfileId = null,
                            shortlistError = result.message
                        )
                }

                is NetworkResult.Loading -> Unit
            }
        }
    }


    // =========================================================
    // REMOVE FROM SHORTLIST
    // =========================================================

    fun removeFromShortlist(
        profileId: String
    ) {

        viewModelScope.launch {

            /*
             * Only the selected profile gets the loading state.
             */
            _uiState.value = _uiState.value.copy(
                shortlistActionProfileId = profileId,
                shortlistError = null,
                message = null
            )

            when (
                val result =
                    repository.removeFromShortlist(profileId)
            ) {

                is NetworkResult.Success -> {

                    _uiState.value =
                        _uiState.value.copy(
                            shortlistActionProfileId = null,
                            message = result.data.message,
                            shortlistError = null
                        )

                    loadShortlistedProfiles()
                }

                is NetworkResult.Error -> {

                    _uiState.value =
                        _uiState.value.copy(
                            shortlistActionProfileId = null,
                            shortlistError = result.message
                        )
                }

                is NetworkResult.Loading -> Unit
            }
        }
    }


    // =========================================================
    // ACTION LOADING
    // =========================================================

    fun setActionLoading(
        loading: Boolean
    ) {

        _uiState.value =
            _uiState.value.copy(
                isActionLoading = loading
            )
    }


    // =========================================================
    // CLEAR ACTION STATE
    // =========================================================

    fun clearActionState() {

        _uiState.value =
            _uiState.value.copy(
                lastActionSuccess = false,
                message = null
            )
    }


    // =========================================================
    // CLEAR ERROR
    // =========================================================

    fun clearError() {

        _uiState.value =
            _uiState.value.copy(
                error = null,
                shortlistError = null
            )
    }
}