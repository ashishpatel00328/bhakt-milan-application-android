package com.infravo.bhaktmilan.data.remote.response

// ==========================================
// Premium Plans
// ==========================================

data class PremiumPlansResponse(
    val success: Boolean,
    val message: String,
    val data: List<PremiumPlan>
)

data class PremiumPlan(
    val id: Int,
    val code: String,
    val name: String,
    val price: String,
    val duration_days: Int
)

// ==========================================
// My Subscription
// ==========================================

data class MySubscriptionResponse(
    val success: Boolean,
    val message: String,
    val data: MySubscription
)

data class MySubscription(
    val plan: String?,
    val status: String?,
    val starts_at: String?,
    val expires_at: String?,
    val is_premium: Boolean,
    val days_left: Int
)

// ==========================================
// Subscribe
// ==========================================

data class SubscribePremiumResponse(
    val success: Boolean,
    val message: String,
    val data: PremiumSubscriptionRequest
)

data class PremiumSubscriptionRequest(
    val id: Int,
    val status: String
)