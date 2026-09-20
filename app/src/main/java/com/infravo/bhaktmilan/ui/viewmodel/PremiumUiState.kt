package com.infravo.bhaktmilan.ui.viewmodel

import com.infravo.bhaktmilan.data.remote.response.MySubscription
import com.infravo.bhaktmilan.data.remote.response.PremiumPlan

data class PremiumUiState(

    // ==========================================
    // Loading
    // ==========================================

    val isLoading: Boolean = false,

    val isSubscribing: Boolean = false,

    // ==========================================
    // Premium Plans
    // ==========================================

    val plans: List<PremiumPlan> = emptyList(),

    val selectedPlanId: Int? = null,

    // ==========================================
    // Current Subscription
    // ==========================================

    val subscription: MySubscription? = null,

    // ==========================================
    // Request State
    // ==========================================

    val subscribeSuccess: Boolean = false,

    /**
     * Actual message returned by the
     * premium subscribe API.
     *
     * Example:
     * "Premium request created successfully."
     *
     * or:
     * "You already have a pending premium request."
     */
    val subscribeResponseMessage: String? = null,

    /**
     * HTTP status code returned by the
     * premium subscribe API.
     *
     * Used by the UI to distinguish
     * successful request from HTTP 409.
     */
    val subscribeResponseCode: Int? = null,

    // ==========================================
    // Error
    // ==========================================

    val error: String? = null
)