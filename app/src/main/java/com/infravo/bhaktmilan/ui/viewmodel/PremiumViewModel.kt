package com.infravo.bhaktmilan.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infravo.bhaktmilan.data.network.NetworkResult
import com.infravo.bhaktmilan.data.remote.repository.PremiumRepository
import com.infravo.bhaktmilan.data.remote.response.MySubscription
import com.infravo.bhaktmilan.data.remote.response.PremiumPlan
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PremiumViewModel @Inject constructor(
    private val repository: PremiumRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(PremiumUiState())

    val uiState: StateFlow<PremiumUiState> =
        _uiState.asStateFlow()

    init {
        loadPremiumData()
    }

    // ==========================================
    // Load Plans + Current Subscription
    // ==========================================

    fun loadPremiumData() {

        viewModelScope.launch {

            _uiState.value =
                _uiState.value.copy(
                    isLoading = true,
                    error = null
                )

            val plansDeferred = async {
                repository.getPlans()
            }

            val subscriptionDeferred = async {
                repository.getMySubscription()
            }

            val plansResult =
                plansDeferred.await()

            val subscriptionResult =
                subscriptionDeferred.await()

            var plans =
                _uiState.value.plans

            var subscription =
                _uiState.value.subscription

            var error: String? = null

            // ==========================================
            // Plans
            // ==========================================

            when (plansResult) {

                is NetworkResult.Success -> {

                    plans =
                        plansResult.data.data
                }

                is NetworkResult.Error -> {

                    error =
                        plansResult.message
                }

                is NetworkResult.Loading -> Unit
            }

            // ==========================================
            // Subscription
            // ==========================================

            when (subscriptionResult) {

                is NetworkResult.Success -> {

                    subscription =
                        subscriptionResult.data.data
                }

                is NetworkResult.Error -> {

                    if (error == null) {

                        error =
                            subscriptionResult.message
                    }
                }

                is NetworkResult.Loading -> Unit
            }

            _uiState.value =
                _uiState.value.copy(
                    isLoading = false,
                    plans = plans,
                    subscription = subscription,
                    error = error
                )
        }
    }

    // ==========================================
    // Fresh Subscription Check
    // ==========================================
    //
    // Used BEFORE every protected action.
    //
    // Returns the latest backend subscription
    // and updates PremiumUiState.
    // ==========================================

    suspend fun getLatestSubscription():
            MySubscription? {

        return when (
            val result =
                repository.getMySubscription()
        ) {

            is NetworkResult.Success -> {

                val latestSubscription =
                    result.data.data

                _uiState.value =
                    _uiState.value.copy(
                        subscription =
                            latestSubscription,
                        error = null
                    )

                latestSubscription
            }

            is NetworkResult.Error -> {

                _uiState.value =
                    _uiState.value.copy(
                        error = result.message
                    )

                null
            }

            is NetworkResult.Loading -> {

                null
            }
        }
    }

    // ==========================================
    // Select Plan
    // ==========================================

    fun selectPlan(
        planId: Int
    ) {

        val currentSubscription =
            _uiState.value.subscription

        // ------------------------------------------
        // Do not allow plan selection when already
        // premium or request is pending.
        // ------------------------------------------

        if (
            currentSubscription?.is_premium == true ||
            currentSubscription?.status.equals(
                "PENDING",
                ignoreCase = true
            )
        ) {
            return
        }

        _uiState.value =
            _uiState.value.copy(
                selectedPlanId = planId,
                error = null
            )
    }

    // ==========================================
    // Subscribe
    // ==========================================

    fun subscribe() {

        viewModelScope.launch {

            // ------------------------------------------
            // Prevent duplicate submit
            // ------------------------------------------

            if (
                _uiState.value.isSubscribing
            ) {
                return@launch
            }

            // ------------------------------------------
            // Fresh backend verification
            // ------------------------------------------

            val latestSubscription =
                getLatestSubscription()

            // ------------------------------------------
            // Already Premium
            // ------------------------------------------

            if (
                latestSubscription?.is_premium == true
            ) {

                _uiState.value =
                    _uiState.value.copy(
                        isSubscribing = false,
                        subscribeSuccess = false,
                        error =
                            "You already have an active premium subscription."
                    )

                return@launch
            }

            // ------------------------------------------
            // Request Already Pending
            // ------------------------------------------

            if (
                latestSubscription?.status.equals(
                    "PENDING",
                    ignoreCase = true
                )
            ) {

                _uiState.value =
                    _uiState.value.copy(
                        isSubscribing = false,
                        subscribeSuccess = false,
                        error =
                            "Your premium request is already pending approval."
                    )

                return@launch
            }

            // ------------------------------------------
            // Plan Required
            // ------------------------------------------

            val planId =
                _uiState.value.selectedPlanId

            if (planId == null) {

                _uiState.value =
                    _uiState.value.copy(
                        isSubscribing = false,
                        subscribeSuccess = false,
                        error =
                            "Please select a premium plan."
                    )

                return@launch
            }

            // ==========================================
            // Start Request
            // ==========================================

            _uiState.value =
                _uiState.value.copy(
                    isSubscribing = true,
                    subscribeSuccess = false,
                    error = null
                )

            when (
                val result =
                    repository.subscribe(planId)
            ) {

                is NetworkResult.Success -> {

                    _uiState.value =
                        _uiState.value.copy(
                            isSubscribing = false,
                            subscribeSuccess = true,
                            error = null
                        )

                    // --------------------------------------
                    // Backend request is now created.
                    // Refresh current subscription.
                    // --------------------------------------

                    refreshSubscription()
                }

                is NetworkResult.Error -> {

                    _uiState.value =
                        _uiState.value.copy(
                            isSubscribing = false,
                            subscribeSuccess = false,
                            error =
                                result.message
                        )
                }

                is NetworkResult.Loading -> Unit
            }
        }
    }

    // ==========================================
    // Refresh Subscription
    // ==========================================

    fun refreshSubscription() {

        viewModelScope.launch {

            when (
                val result =
                    repository.getMySubscription()
            ) {

                is NetworkResult.Success -> {

                    _uiState.value =
                        _uiState.value.copy(
                            subscription =
                                result.data.data,
                            error = null
                        )
                }

                is NetworkResult.Error -> {

                    _uiState.value =
                        _uiState.value.copy(
                            error =
                                result.message
                        )
                }

                is NetworkResult.Loading -> Unit
            }
        }
    }

    // ==========================================
    // Helper - Request Disabled
    // ==========================================

    fun isPremiumRequestDisabled(): Boolean {

        val state =
            _uiState.value

        return state.isSubscribing ||
                state.subscription?.is_premium == true ||
                state.subscription?.status.equals(
                    "PENDING",
                    ignoreCase = true
                )
    }

    // ==========================================
    // Clear Subscribe Success
    // ==========================================

    fun clearSubscribeSuccess() {

        _uiState.value =
            _uiState.value.copy(
                subscribeSuccess = false
            )
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