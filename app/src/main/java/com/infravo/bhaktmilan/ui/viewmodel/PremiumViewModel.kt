package com.infravo.bhaktmilan.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infravo.bhaktmilan.data.network.NetworkResult
import com.infravo.bhaktmilan.data.remote.repository.PremiumRepository
import com.infravo.bhaktmilan.data.remote.response.MySubscription
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import javax.inject.Inject

@HiltViewModel
class PremiumViewModel @Inject constructor(
    private val repository: PremiumRepository
) : ViewModel() {

    companion object {
        private const val TAG = "PremiumVM"
    }

    private val _uiState = MutableStateFlow(PremiumUiState())
    val uiState: StateFlow<PremiumUiState> = _uiState.asStateFlow()

    /**
     * Prevents multiple subscribe requests from being started
     * by rapid repeated clicks.
     */
    private val subscribeMutex = Mutex()

    init {
        Log.d(TAG, "==========================================")
        Log.d(TAG, "PremiumViewModel -> INIT")
        Log.d(TAG, "PremiumViewModel -> loadPremiumData()")
        Log.d(TAG, "==========================================")

        loadPremiumData()
    }

    // =========================================================
    // DEBUG STATE LOGGER
    // =========================================================

    private fun logUiState(source: String) {
        val state = _uiState.value

        Log.d(
            TAG,
            """
            UI STATE [$source]
            isLoading=${state.isLoading}
            isSubscribing=${state.isSubscribing}
            selectedPlanId=${state.selectedPlanId}
            subscribeSuccess=${state.subscribeSuccess}
            subscribeResponseCode=${state.subscribeResponseCode}
            subscribeResponseMessage=${state.subscribeResponseMessage}
            error=${state.error}
            subscription.is_premium=${state.subscription?.is_premium}
            subscription.status=${state.subscription?.status}
            plansCount=${state.plans.size}
            """.trimIndent()
        )
    }

    // =========================================================
    // LOAD PREMIUM DATA
    // =========================================================

    fun loadPremiumData() {

        Log.d(TAG, "loadPremiumData() -> START")

        viewModelScope.launch {

            Log.d(TAG, "loadPremiumData() -> Coroutine START")

            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )

            logUiState("loadPremiumData -> loading state")

            Log.d(TAG, "loadPremiumData() -> Starting getPlans()")

            val plansDeferred = async {
                repository.getPlans()
            }

            Log.d(TAG, "loadPremiumData() -> Starting getMySubscription()")

            val subscriptionDeferred = async {
                repository.getMySubscription()
            }

            Log.d(TAG, "loadPremiumData() -> Waiting for plans result")

            val plansResult = plansDeferred.await()

            Log.d(
                TAG,
                "loadPremiumData() -> Plans result received: ${plansResult.javaClass.simpleName}"
            )

            Log.d(TAG, "loadPremiumData() -> Waiting for subscription result")

            val subscriptionResult = subscriptionDeferred.await()

            Log.d(
                TAG,
                "loadPremiumData() -> Subscription result received: ${subscriptionResult.javaClass.simpleName}"
            )

            var plans = _uiState.value.plans
            var subscription = _uiState.value.subscription
            var error: String? = null

            when (plansResult) {

                is NetworkResult.Success -> {

                    Log.d(
                        TAG,
                        "loadPremiumData() -> Plans SUCCESS"
                    )

                    plans = plansResult.data.data

                    Log.d(
                        TAG,
                        "loadPremiumData() -> Plans count=${plans.size}"
                    )
                }

                is NetworkResult.Error -> {

                    Log.e(
                        TAG,
                        "loadPremiumData() -> Plans ERROR: code=${plansResult.code}, message=${plansResult.message}"
                    )

                    error = plansResult.message
                }

                is NetworkResult.Loading -> {

                    Log.d(
                        TAG,
                        "loadPremiumData() -> Plans LOADING"
                    )
                }
            }

            when (subscriptionResult) {

                is NetworkResult.Success -> {

                    Log.d(
                        TAG,
                        "loadPremiumData() -> Subscription SUCCESS"
                    )

                    subscription = subscriptionResult.data.data

                    Log.d(
                        TAG,
                        "loadPremiumData() -> isPremium=${subscription?.is_premium}, status=${subscription?.status}"
                    )
                }

                is NetworkResult.Error -> {

                    Log.e(
                        TAG,
                        "loadPremiumData() -> Subscription ERROR: code=${subscriptionResult.code}, message=${subscriptionResult.message}"
                    )

                    if (error == null) {
                        error = subscriptionResult.message
                    }
                }

                is NetworkResult.Loading -> {

                    Log.d(
                        TAG,
                        "loadPremiumData() -> Subscription LOADING"
                    )
                }
            }

            _uiState.value = _uiState.value.copy(
                isLoading = false,
                plans = plans,
                subscription = subscription,
                error = error
            )

            logUiState("loadPremiumData -> FINAL")

            Log.d(TAG, "loadPremiumData() -> END")
        }
    }

    // =========================================================
    // GET LATEST SUBSCRIPTION
    // =========================================================

    suspend fun getLatestSubscription(): MySubscription? {

        Log.d(TAG, "getLatestSubscription() -> START")

        Log.d(
            TAG,
            "getLatestSubscription() -> Calling repository.getMySubscription()"
        )

        val result = repository.getMySubscription()

        Log.d(
            TAG,
            "getLatestSubscription() -> RESULT: ${result.javaClass.simpleName}"
        )

        return when (result) {

            is NetworkResult.Success -> {

                val latestSubscription = result.data.data

                Log.d(
                    TAG,
                    "getLatestSubscription() -> SUCCESS"
                )

                Log.d(
                    TAG,
                    "getLatestSubscription() -> isPremium=${latestSubscription?.is_premium}, status=${latestSubscription?.status}"
                )

                _uiState.value = _uiState.value.copy(
                    subscription = latestSubscription,
                    error = null
                )

                logUiState("getLatestSubscription -> SUCCESS state")

                Log.d(TAG, "getLatestSubscription() -> END")

                latestSubscription
            }

            is NetworkResult.Error -> {

                Log.e(
                    TAG,
                    "getLatestSubscription() -> ERROR: code=${result.code}, message=${result.message}"
                )

                _uiState.value = _uiState.value.copy(
                    error = result.message
                )

                logUiState("getLatestSubscription -> ERROR state")

                Log.d(TAG, "getLatestSubscription() -> RETURN null")

                null
            }

            is NetworkResult.Loading -> {

                Log.d(
                    TAG,
                    "getLatestSubscription() -> LOADING"
                )

                Log.d(TAG, "getLatestSubscription() -> RETURN null")

                null
            }
        }
    }

    // =========================================================
    // SELECT PLAN
    // =========================================================

    fun selectPlan(planId: Int) {

        Log.d(TAG, "selectPlan() -> START")
        Log.d(TAG, "selectPlan() -> planId=$planId")

        val currentSubscription = _uiState.value.subscription

        Log.d(
            TAG,
            "selectPlan() -> current isPremium=${currentSubscription?.is_premium}, status=${currentSubscription?.status}"
        )

        if (
            currentSubscription?.is_premium == true ||
            currentSubscription?.status.equals(
                "PENDING",
                ignoreCase = true
            ) ||
            _uiState.value.isSubscribing
        ) {

            Log.w(
                TAG,
                "selectPlan() -> BLOCKED"
            )

            logUiState("selectPlan -> BLOCKED")

            return
        }

        _uiState.value = _uiState.value.copy(
            selectedPlanId = planId,
            error = null
        )

        logUiState("selectPlan -> PLAN SELECTED")

        Log.d(TAG, "selectPlan() -> END")
    }

    // =========================================================
    // SUBSCRIBE
    // =========================================================

    fun subscribe() {

        Log.d(TAG, "==========================================")
        Log.d(TAG, "subscribe() -> CALLED")
        Log.d(TAG, "==========================================")

        viewModelScope.launch {

            Log.d(TAG, "subscribe() -> Coroutine START")

            if (!subscribeMutex.tryLock()) {

                Log.w(
                    TAG,
                    "subscribe() -> MUTEX LOCK FAILED - duplicate request blocked"
                )

                return@launch
            }

            Log.d(TAG, "subscribe() -> MUTEX LOCK ACQUIRED")

            try {

                // =====================================================
                // INITIAL SUBMITTING STATE
                // =====================================================

                _uiState.value = _uiState.value.copy(
                    isSubscribing = true,
                    subscribeSuccess = false,
                    subscribeResponseMessage = null,
                    subscribeResponseCode = null,
                    error = null
                )

                logUiState("subscribe -> INITIAL STATE")

                // =====================================================
                // SELECTED PLAN
                // =====================================================

                val planId = _uiState.value.selectedPlanId

                Log.d(
                    TAG,
                    "subscribe() -> selectedPlanId=$planId"
                )

                if (planId == null) {

                    Log.e(
                        TAG,
                        "subscribe() -> STOPPED: selectedPlanId is NULL"
                    )

                    _uiState.value = _uiState.value.copy(
                        isSubscribing = false,
                        subscribeSuccess = false,
                        error = "Please select a premium plan."
                    )

                    logUiState("subscribe -> NO PLAN")

                    return@launch
                }

                Log.d(
                    TAG,
                    "subscribe() -> Valid planId=$planId"
                )

                // =====================================================
                // POST PREMIUM REQUEST
                // =====================================================

                Log.d(
                    TAG,
                    "subscribe() -> BEFORE repository.subscribe($planId)"
                )

                val result = repository.subscribe(planId)

                Log.d(
                    TAG,
                    "subscribe() -> AFTER repository.subscribe()"
                )

                Log.d(
                    TAG,
                    "subscribe() -> RESULT TYPE=${result.javaClass.simpleName}"
                )

                // =====================================================
                // HANDLE RESULT
                // =====================================================

                when (result) {

                    // =================================================
                    // SUCCESS
                    // =================================================

                    is NetworkResult.Success -> {

                        Log.d(
                            TAG,
                            "subscribe() -> SUCCESS BRANCH"
                        )

                        val responseMessage = result.data.message

                        Log.d(
                            TAG,
                            "subscribe() -> SUCCESS message=$responseMessage"
                        )

                        Log.d(
                            TAG,
                            "subscribe() -> BEFORE refreshSubscriptionNow()"
                        )

                        refreshSubscriptionNow()

                        Log.d(
                            TAG,
                            "subscribe() -> AFTER refreshSubscriptionNow()"
                        )

                        _uiState.value = _uiState.value.copy(
                            isSubscribing = false,
                            subscribeSuccess = true,
                            subscribeResponseMessage = responseMessage,
                            subscribeResponseCode = null,
                            error = null
                        )

                        logUiState("subscribe -> SUCCESS FINAL STATE")
                    }

                    // =================================================
                    // ERROR
                    // =================================================

                    is NetworkResult.Error -> {

                        Log.e(
                            TAG,
                            "subscribe() -> ERROR BRANCH"
                        )

                        Log.e(
                            TAG,
                            "subscribe() -> HTTP CODE=${result.code}"
                        )

                        Log.e(
                            TAG,
                            "subscribe() -> MESSAGE=${result.message}"
                        )

                        // =============================================
                        // HTTP 409
                        // =============================================

                        if (result.code == 409) {

                            Log.w(
                                TAG,
                                "subscribe() -> 409 CONFLICT BRANCH ENTERED"
                            )

                            val responseMessage = result.message.ifBlank {
                                "Your premium request already exists."
                            }

                            Log.w(
                                TAG,
                                "subscribe() -> 409 responseMessage=$responseMessage"
                            )

                            Log.w(
                                TAG,
                                "subscribe() -> BEFORE setting 409 UI STATE"
                            )

                            _uiState.value = _uiState.value.copy(
                                isSubscribing = false,
                                subscribeSuccess = false,
                                subscribeResponseMessage = responseMessage,
                                subscribeResponseCode = 409,
                                error = null
                            )

                            Log.w(
                                TAG,
                                "subscribe() -> AFTER setting 409 UI STATE"
                            )

                            logUiState(
                                "subscribe -> 409 STATE PUBLISHED"
                            )

                            Log.w(
                                TAG,
                                "subscribe() -> BEFORE refreshSubscriptionNow() AFTER 409"
                            )

                            refreshSubscriptionNow()

                            Log.w(
                                TAG,
                                "subscribe() -> AFTER refreshSubscriptionNow() AFTER 409"
                            )

                            logUiState(
                                "subscribe -> 409 AFTER REFRESH"
                            )

                        } else {

                            // =========================================
                            // OTHER API ERRORS
                            // =========================================

                            Log.e(
                                TAG,
                                "subscribe() -> OTHER ERROR BRANCH"
                            )

                            _uiState.value = _uiState.value.copy(
                                isSubscribing = false,
                                subscribeSuccess = false,
                                subscribeResponseMessage = null,
                                subscribeResponseCode = null,
                                error = result.message
                            )

                            logUiState(
                                "subscribe -> OTHER ERROR STATE"
                            )
                        }
                    }

                    // =================================================
                    // LOADING
                    // =================================================

                    is NetworkResult.Loading -> {

                        Log.w(
                            TAG,
                            "subscribe() -> LOADING BRANCH"
                        )

                        _uiState.value = _uiState.value.copy(
                            isSubscribing = false,
                            subscribeSuccess = false,
                            subscribeResponseMessage = null,
                            subscribeResponseCode = null,
                            error = "Unable to complete the premium request. Please try again."
                        )

                        logUiState(
                            "subscribe -> LOADING STATE"
                        )
                    }
                }

            } catch (e: Exception) {

                Log.e(
                    TAG,
                    "subscribe() -> EXCEPTION",
                    e
                )

                throw e

            } finally {

                Log.d(
                    TAG,
                    "subscribe() -> FINALLY - unlocking mutex"
                )

                subscribeMutex.unlock()

                Log.d(
                    TAG,
                    "subscribe() -> MUTEX UNLOCKED"
                )

                logUiState(
                    "subscribe -> FINALLY"
                )
            }

            Log.d(TAG, "subscribe() -> Coroutine END")
        }
    }

    // =========================================================
    // REFRESH SUBSCRIPTION
    // =========================================================

    private suspend fun refreshSubscriptionNow() {

        Log.d(
            TAG,
            "refreshSubscriptionNow() -> START"
        )

        Log.d(
            TAG,
            "refreshSubscriptionNow() -> Calling repository.getMySubscription()"
        )

        val result = repository.getMySubscription()

        Log.d(
            TAG,
            "refreshSubscriptionNow() -> RESULT: ${result.javaClass.simpleName}"
        )

        when (result) {

            is NetworkResult.Success -> {

                Log.d(
                    TAG,
                    "refreshSubscriptionNow() -> SUCCESS"
                )

                val subscription = result.data.data

                Log.d(
                    TAG,
                    "refreshSubscriptionNow() -> isPremium=${subscription?.is_premium}, status=${subscription?.status}"
                )

                _uiState.value = _uiState.value.copy(
                    subscription = subscription,
                    error = null
                )

                logUiState(
                    "refreshSubscriptionNow -> SUCCESS STATE"
                )
            }

            is NetworkResult.Error -> {

                Log.e(
                    TAG,
                    "refreshSubscriptionNow() -> ERROR: code=${result.code}, message=${result.message}"
                )

                _uiState.value = _uiState.value.copy(
                    error = result.message
                )

                logUiState(
                    "refreshSubscriptionNow -> ERROR STATE"
                )
            }

            is NetworkResult.Loading -> {

                Log.d(
                    TAG,
                    "refreshSubscriptionNow() -> LOADING"
                )
            }
        }

        Log.d(
            TAG,
            "refreshSubscriptionNow() -> END"
        )
    }

    // =========================================================
    // PUBLIC REFRESH
    // =========================================================

    fun refreshSubscription() {

        Log.d(
            TAG,
            "refreshSubscription() -> CALLED"
        )

        viewModelScope.launch {

            Log.d(
                TAG,
                "refreshSubscription() -> Coroutine START"
            )

            refreshSubscriptionNow()

            Log.d(
                TAG,
                "refreshSubscription() -> Coroutine END"
            )
        }
    }

    // =========================================================
    // PREMIUM REQUEST DISABLED
    // =========================================================

    fun isPremiumRequestDisabled(): Boolean {

        val state = _uiState.value

        val disabled =
            state.isSubscribing ||
                    state.subscription?.is_premium == true ||
                    state.subscription?.status.equals(
                        "PENDING",
                        ignoreCase = true
                    )

        Log.d(
            TAG,
            "isPremiumRequestDisabled() -> $disabled | isSubscribing=${state.isSubscribing}, isPremium=${state.subscription?.is_premium}, status=${state.subscription?.status}"
        )

        return disabled
    }

    // =========================================================
    // CLEAR SUCCESS
    // =========================================================

    fun clearSubscribeSuccess() {

        Log.d(
            TAG,
            "clearSubscribeSuccess() -> CALLED"
        )

        _uiState.value = _uiState.value.copy(
            subscribeSuccess = false
        )

        logUiState(
            "clearSubscribeSuccess -> AFTER"
        )
    }

    // =========================================================
    // CLEAR RESPONSE
    // =========================================================

    fun clearSubscribeResponse() {

        Log.d(
            TAG,
            "clearSubscribeResponse() -> CALLED"
        )

        _uiState.value = _uiState.value.copy(
            subscribeResponseMessage = null,
            subscribeResponseCode = null
        )

        logUiState(
            "clearSubscribeResponse -> AFTER"
        )
    }

    // =========================================================
    // CLEAR ERROR
    // =========================================================

    fun clearError() {

        Log.d(
            TAG,
            "clearError() -> CALLED"
        )

        _uiState.value = _uiState.value.copy(
            error = null
        )

        logUiState(
            "clearError -> AFTER"
        )
    }

    // =========================================================
    // VIEWMODEL CLEARED
    // =========================================================

    override fun onCleared() {

        Log.w(
            TAG,
            "PremiumViewModel -> onCleared()"
        )

        super.onCleared()
    }
}