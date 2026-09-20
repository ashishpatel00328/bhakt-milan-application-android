package com.infravo.bhaktmilan.data.remote.repository

import android.util.Log
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

    companion object {
        private const val TAG = "PremiumRepository"
    }

    // ==========================================
    // Premium Plans
    // ==========================================

    suspend fun getPlans():
            NetworkResult<PremiumPlansResponse> {

        Log.d(TAG, "getPlans() -> START")

        val result = safeApiCall {
            Log.d(
                TAG,
                "getPlans() -> Calling ApiService.getPremiumPlans()"
            )

            apiService.getPremiumPlans()
        }

        Log.d(
            TAG,
            "getPlans() -> RESULT: ${result.javaClass.simpleName}"
        )

        when (result) {

            is NetworkResult.Success -> {
                Log.d(
                    TAG,
                    "getPlans() -> SUCCESS: ${result.data}"
                )
            }

            is NetworkResult.Error -> {
                Log.e(
                    TAG,
                    "getPlans() -> ERROR: code=${result.code}, message=${result.message}"
                )
            }

            is NetworkResult.Loading -> {
                Log.d(
                    TAG,
                    "getPlans() -> LOADING"
                )
            }
        }

        Log.d(TAG, "getPlans() -> END")

        return result
    }

    // ==========================================
    // Current Subscription
    // ==========================================

    suspend fun getMySubscription():
            NetworkResult<MySubscriptionResponse> {

        Log.d(TAG, "getMySubscription() -> START")

        val result = safeApiCall {
            Log.d(
                TAG,
                "getMySubscription() -> Calling ApiService.getMySubscription()"
            )

            apiService.getMySubscription()
        }

        Log.d(
            TAG,
            "getMySubscription() -> RESULT: ${result.javaClass.simpleName}"
        )

        when (result) {

            is NetworkResult.Success -> {
                Log.d(
                    TAG,
                    "getMySubscription() -> SUCCESS: ${result.data}"
                )
            }

            is NetworkResult.Error -> {
                Log.e(
                    TAG,
                    "getMySubscription() -> ERROR: code=${result.code}, message=${result.message}"
                )
            }

            is NetworkResult.Loading -> {
                Log.d(
                    TAG,
                    "getMySubscription() -> LOADING"
                )
            }
        }

        Log.d(TAG, "getMySubscription() -> END")

        return result
    }

    // ==========================================
    // Premium Request
    // ==========================================

    suspend fun subscribe(
        planId: Int
    ): NetworkResult<SubscribePremiumResponse> {

        Log.d(TAG, "==========================================")
        Log.d(TAG, "subscribe() -> START")
        Log.d(TAG, "subscribe() -> planId=$planId")

        val request = SubscribePremiumRequest(
            plan = planId
        )

        Log.d(
            TAG,
            "subscribe() -> Request created: plan=${request.plan}"
        )

        Log.d(
            TAG,
            "subscribe() -> Calling ApiService.subscribePremium()"
        )

        val result = safeApiCall {
            apiService.subscribePremium(request)
        }

        Log.d(
            TAG,
            "subscribe() -> safeApiCall RESULT: ${result.javaClass.simpleName}"
        )

        when (result) {

            is NetworkResult.Success -> {

                Log.d(
                    TAG,
                    "subscribe() -> SUCCESS"
                )

                Log.d(
                    TAG,
                    "subscribe() -> Response: ${result.data}"
                )
            }

            is NetworkResult.Error -> {

                Log.e(
                    TAG,
                    "subscribe() -> ERROR"
                )

                Log.e(
                    TAG,
                    "subscribe() -> HTTP CODE: ${result.code}"
                )

                Log.e(
                    TAG,
                    "subscribe() -> MESSAGE: ${result.message}"
                )

                if (result.code == 409) {

                    Log.w(
                        TAG,
                        "subscribe() -> 409 CONFLICT RECEIVED"
                    )

                    Log.w(
                        TAG,
                        "subscribe() -> Pending premium request already exists"
                    )
                }
            }

            is NetworkResult.Loading -> {

                Log.d(
                    TAG,
                    "subscribe() -> LOADING"
                )
            }
        }

        Log.d(TAG, "subscribe() -> END")
        Log.d(TAG, "==========================================")

        return result
    }
}