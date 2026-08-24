package com.infravo.bhaktmilan.data.remote.repository

import com.infravo.bhaktmilan.data.network.NetworkResult
import com.infravo.bhaktmilan.data.remote.api.ApiService
import com.infravo.bhaktmilan.data.remote.request.SubscribePremiumRequest
import com.infravo.bhaktmilan.data.remote.response.MySubscriptionResponse
import com.infravo.bhaktmilan.data.remote.response.PremiumPlansResponse
import com.infravo.bhaktmilan.data.remote.response.SubscribePremiumResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PremiumRepository @Inject constructor(
    private val apiService: ApiService
) : BaseRepository() {

    // ==========================================
    // Premium Plans
    // ==========================================

    suspend fun getPlans():
            NetworkResult<PremiumPlansResponse> {

        return safeApiCall {
            apiService.getPremiumPlans()
        }
    }

    // ==========================================
    // Current Subscription
    // ==========================================

    suspend fun getMySubscription():
            NetworkResult<MySubscriptionResponse> {

        return safeApiCall {
            apiService.getMySubscription()
        }
    }

    // ==========================================
    // Premium Request
    // ==========================================

    suspend fun subscribe(
        planId: Int
    ): NetworkResult<SubscribePremiumResponse> {

        return safeApiCall {

            apiService.subscribePremium(
                SubscribePremiumRequest(
                    plan = planId
                )
            )
        }
    }
}