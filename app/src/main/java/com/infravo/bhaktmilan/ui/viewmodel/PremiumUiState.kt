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

    // ==========================================
    // Error
    // ==========================================

    val error: String? = null
)