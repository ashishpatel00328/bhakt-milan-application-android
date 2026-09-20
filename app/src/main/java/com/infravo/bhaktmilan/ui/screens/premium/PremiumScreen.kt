package com.infravo.bhaktmilan.ui.screens.premium

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.infravo.bhaktmilan.ui.viewmodel.PremiumViewModel

private const val TAG = "PremiumScreen"

@Composable
fun PremiumScreen(
    viewModel: PremiumViewModel = hiltViewModel()
) {

    // =========================================================
    // UI STATE
    //
    // Using collectAsState() intentionally here.
    // This guarantees the active composition observes the
    // StateFlow without lifecycle gating.
    // =========================================================

    val uiState by viewModel.uiState.collectAsState()

    // =========================================================
    // PAYMENT DIALOG
    // =========================================================

    var showPaymentDialog by remember {
        mutableStateOf(false)
    }

    // =========================================================
    // SCREEN ENTER / EXIT DEBUG
    // =========================================================

    DisposableEffect(Unit) {

        Log.d(
            TAG,
            "################################################"
        )

        Log.d(
            TAG,
            "### PremiumScreen ENTERED / COMPOSED ###"
        )

        Log.d(
            TAG,
            "### ViewModel instance = ${viewModel.hashCode()} ###"
        )

        Log.d(
            TAG,
            "################################################"
        )

        onDispose {

            Log.w(
                TAG,
                "################################################"
            )

            Log.w(
                TAG,
                "### PremiumScreen DISPOSED ###"
            )

            Log.w(
                TAG,
                "### ViewModel instance = ${viewModel.hashCode()} ###"
            )

            Log.w(
                TAG,
                "################################################"
            )
        }
    }

    // =========================================================
    // EVERY UI STATE CHANGE
    // =========================================================

    LaunchedEffect(
        uiState.isLoading,
        uiState.isSubscribing,
        uiState.selectedPlanId,
        uiState.subscribeSuccess,
        uiState.subscribeResponseCode,
        uiState.subscribeResponseMessage,
        uiState.error,
        uiState.subscription
    ) {

        Log.d(
            TAG,
            """
            =================================================
            PREMIUM SCREEN -> UI STATE RECEIVED
            =================================================
            ViewModel       = ${viewModel.hashCode()}
            isLoading       = ${uiState.isLoading}
            isSubscribing   = ${uiState.isSubscribing}
            selectedPlanId  = ${uiState.selectedPlanId}
            success         = ${uiState.subscribeSuccess}
            responseCode    = ${uiState.subscribeResponseCode}
            responseMessage = ${uiState.subscribeResponseMessage}
            error           = ${uiState.error}
            subscription    = ${uiState.subscription}
            paymentDialog   = $showPaymentDialog
            =================================================
            """.trimIndent()
        )
    }

    // =========================================================
    // SPECIFIC 409 WATCHER
    // =========================================================

    LaunchedEffect(
        uiState.subscribeResponseCode
    ) {

        Log.d(
            TAG,
            "409 WATCHER -> responseCode=${uiState.subscribeResponseCode}"
        )

        if (
            uiState.subscribeResponseCode == 409
        ) {

            Log.w(
                TAG,
                "################################################"
            )

            Log.w(
                TAG,
                "### 409 REACHED PREMIUM SCREEN ###"
            )

            Log.w(
                TAG,
                "### message=${uiState.subscribeResponseMessage} ###"
            )

            Log.w(
                TAG,
                "### closing payment dialog ###"
            )

            Log.w(
                TAG,
                "################################################"
            )

            showPaymentDialog = false
        }
    }

    // =========================================================
    // SUCCESS WATCHER
    // =========================================================

    LaunchedEffect(
        uiState.subscribeSuccess
    ) {

        Log.d(
            TAG,
            "SUCCESS WATCHER -> success=${uiState.subscribeSuccess}"
        )

        if (
            uiState.subscribeSuccess
        ) {

            Log.d(
                TAG,
                "SUCCESS -> closing payment dialog"
            )

            showPaymentDialog = false
        }
    }

    // =========================================================
    // MAIN UI
    // =========================================================

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {

        // =====================================================
        // HEADER
        // =====================================================

        item {

            PremiumHeader()
        }

        // =====================================================
        // HERO
        // =====================================================

        item {

            PremiumHeroCard()
        }

        // =====================================================
        // ERROR
        // =====================================================

        uiState.error
            ?.takeIf {
                it.isNotBlank()
            }
            ?.let { errorMessage ->

                item {

                    Log.d(
                        TAG,
                        "Rendering ErrorCard"
                    )

                    ErrorCard(
                        message = errorMessage
                    )
                }
            }

        // =====================================================
        // LOADING
        // =====================================================

        if (
            uiState.isLoading
        ) {

            item {

                Log.d(
                    TAG,
                    "Rendering LoadingCard"
                )

                LoadingCard()
            }
        }

        // =====================================================
        // CURRENT SUBSCRIPTION
        // =====================================================

        uiState.subscription?.let { subscription ->

            item {

                Log.d(
                    TAG,
                    """
                    Rendering SubscriptionStatusCard
                    isPremium=${subscription.is_premium}
                    status=${subscription.status}
                    plan=${subscription.plan}
                    daysLeft=${subscription.days_left}
                    startsAt=${subscription.starts_at}
                    expiresAt=${subscription.expires_at}
                    """.trimIndent()
                )

                SubscriptionStatusCard(
                    subscription = subscription
                )
            }
        }

        // =====================================================
        // BENEFITS
        // =====================================================

        item {

            PremiumBenefitsCard()
        }

        // =====================================================
        // PLAN HEADER
        // =====================================================

        item {

            PlansHeader()
        }

        // =====================================================
        // PLANS
        // =====================================================

        items(
            items = uiState.plans,
            key = { plan ->
                plan.id
            }
        ) { plan ->

            val isPremium =
                uiState.subscription?.is_premium == true

            val isSubmitting =
                uiState.isSubscribing

            Log.d(
                TAG,
                """
                Rendering PremiumPlanCard
                planId=${plan.id}
                name=${plan.name}
                price=${plan.price}
                duration=${plan.duration_days}
                isPremium=$isPremium
                isSubmitting=$isSubmitting
                selectedPlanId=${uiState.selectedPlanId}
                """.trimIndent()
            )

            PremiumPlanCard(

                plan = plan,

                isSelected =
                    uiState.selectedPlanId == plan.id,

                isPremium =
                    isPremium,

                isSubmitting =
                    isSubmitting,

                onClick = {

                    Log.d(
                        TAG,
                        """
                        =================================================
                        PLAN CLICKED
                        =================================================
                        planId=${plan.id}
                        name=${plan.name}
                        isPremium=$isPremium
                        isSubmitting=$isSubmitting
                        =================================================
                        """.trimIndent()
                    )

                    // ---------------------------------------------
                    // PREMIUM USER
                    // ---------------------------------------------

                    if (
                        isPremium
                    ) {

                        Log.d(
                            TAG,
                            "PLAN CLICK IGNORED -> already premium"
                        )

                        return@PremiumPlanCard
                    }

                    // ---------------------------------------------
                    // REQUEST ALREADY RUNNING
                    // ---------------------------------------------

                    if (
                        isSubmitting
                    ) {

                        Log.d(
                            TAG,
                            "PLAN CLICK IGNORED -> request running"
                        )

                        return@PremiumPlanCard
                    }

                    // ---------------------------------------------
                    // SELECT PLAN
                    // ---------------------------------------------

                    Log.d(
                        TAG,
                        "Calling viewModel.selectPlan(${plan.id})"
                    )

                    viewModel.selectPlan(
                        plan.id
                    )

                    // ---------------------------------------------
                    // OPEN PAYMENT DIALOG
                    // ---------------------------------------------

                    showPaymentDialog = true

                    Log.d(
                        TAG,
                        "showPaymentDialog=true"
                    )
                }
            )
        }
    }

    // =========================================================
    // RESPONSE FLAGS
    // =========================================================

    val is409 =
        uiState.subscribeResponseCode == 409

    val hasOtherResponse =
        uiState.subscribeResponseCode != null &&
                uiState.subscribeResponseCode != 409

    // =========================================================
    // FINAL DIALOG DEBUG
    // =========================================================

    Log.d(
        TAG,
        """
        =================================================
        DIALOG RENDER CHECK
        =================================================
        ViewModel        = ${viewModel.hashCode()}
        responseCode     = ${uiState.subscribeResponseCode}
        responseMessage  = ${uiState.subscribeResponseMessage}
        is409            = $is409
        hasOtherResponse = $hasOtherResponse
        paymentDialog    = $showPaymentDialog
        isSubscribing    = ${uiState.isSubscribing}
        =================================================
        """.trimIndent()
    )

    // =========================================================
    // 409 POPUP
    // =========================================================

    if (
        is409
    ) {

        Log.w(
            TAG,
            "################################################"
        )

        Log.w(
            TAG,
            "### ABOUT TO RENDER PremiumPendingDialog ###"
        )

        Log.w(
            TAG,
            "### CODE = 409 ###"
        )

        Log.w(
            TAG,
            "### MESSAGE = ${uiState.subscribeResponseMessage} ###"
        )

        Log.w(
            TAG,
            "################################################"
        )

        PremiumPendingDialog(

            onDismiss = {

                Log.d(
                    TAG,
                    "409 POPUP -> DISMISS CLICKED"
                )

                Log.d(
                    TAG,
                    "409 POPUP -> calling clearSubscribeResponse()"
                )

                viewModel.clearSubscribeResponse()
            }
        )
    }

    // =========================================================
    // OTHER RESPONSE
    // =========================================================

    if (
        hasOtherResponse
    ) {

        Log.e(
            TAG,
            """
            =================================================
            OTHER PREMIUM RESPONSE
            =================================================
            code=${uiState.subscribeResponseCode}
            message=${uiState.subscribeResponseMessage}
            =================================================
            """.trimIndent()
        )
    }

    // =========================================================
    // PAYMENT DIALOG
    //
    // 409 always has priority.
    // =========================================================

    if (
        showPaymentDialog &&
        !is409
    ) {

        Log.d(
            TAG,
            ">>> RENDERING PremiumPaymentDialog <<<"
        )

        PremiumPaymentDialog(

            plans =
                uiState.plans,

            selectedPlanId =
                uiState.selectedPlanId,

            isSubmitting =
                uiState.isSubscribing,

            // -------------------------------------------------
            // PLAN SELECTED
            // -------------------------------------------------

            onPlanSelected = { planId ->

                Log.d(
                    TAG,
                    "PAYMENT DIALOG -> selected plan=$planId"
                )

                viewModel.selectPlan(
                    planId
                )
            },

            // -------------------------------------------------
            // PREMIUM REQUEST
            // -------------------------------------------------

            onPremiumRequest = {

                Log.d(
                    TAG,
                    """
                    =================================================
                    PREMIUM REQUEST CLICK
                    =================================================
                    selectedPlanId=${uiState.selectedPlanId}
                    isSubscribing=${uiState.isSubscribing}
                    responseCode=${uiState.subscribeResponseCode}
                    =================================================
                    """.trimIndent()
                )

                Log.d(
                    TAG,
                    "Calling viewModel.subscribe()"
                )

                viewModel.subscribe()
            },

            // -------------------------------------------------
            // DISMISS
            // -------------------------------------------------

            onDismiss = {

                Log.d(
                    TAG,
                    "PAYMENT DIALOG -> dismiss requested"
                )

                if (
                    !uiState.isSubscribing
                ) {

                    showPaymentDialog = false

                    Log.d(
                        TAG,
                        "PAYMENT DIALOG -> closed"
                    )

                } else {

                    Log.d(
                        TAG,
                        "PAYMENT DIALOG -> dismiss blocked, request running"
                    )
                }
            }
        )

    } else {

        if (
            is409
        ) {

            Log.d(
                TAG,
                "PAYMENT DIALOG NOT RENDERED -> 409 HAS PRIORITY"
            )
        }
    }
}