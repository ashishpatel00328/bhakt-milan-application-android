package com.infravo.bhaktmilan.ui.screens.premium

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.infravo.bhaktmilan.data.remote.response.MySubscription
import com.infravo.bhaktmilan.data.remote.response.PremiumPlan
import kotlinx.coroutines.launch

@Composable
fun PremiumGate(
    subscription: MySubscription?,
    plans: List<PremiumPlan>,
    selectedPlanId: Int?,
    isSubmitting: Boolean,
    subscribeSuccess: Boolean,

    // ==========================================
    // Fresh Premium Check
    // ==========================================

    onCheckPremium: suspend () -> MySubscription?,

    // ==========================================
    // Premium Actions
    // ==========================================

    onPlanSelected: (Int) -> Unit,

    onPremiumRequest: () -> Unit,

    onClearSubscribeSuccess: () -> Unit,

    // ==========================================
    // Actual Protected Action
    // ==========================================

    onProtectedAction: () -> Unit,

    // ==========================================
    // Caller UI
    // ==========================================

    content: @Composable (
        onClick: () -> Unit
    ) -> Unit
) {

    val coroutineScope =
        rememberCoroutineScope()

    var showPaymentDialog by remember {
        mutableStateOf(false)
    }

    var showPendingDialog by remember {
        mutableStateOf(false)
    }

    var isCheckingPremium by remember {
        mutableStateOf(false)
    }

    // ==========================================
    // Subscribe Success
    // ==========================================

    LaunchedEffect(subscribeSuccess) {

        if (subscribeSuccess) {

            showPaymentDialog = false
            showPendingDialog = false

            onClearSubscribeSuccess()
        }
    }

    // ==========================================
    // Protected Action Handler
    // ==========================================

    val handleProtectedAction: () -> Unit = handler@{

        if (isCheckingPremium) {
            return@handler
        }

        coroutineScope.launch {

            isCheckingPremium = true

            showPaymentDialog = false
            showPendingDialog = false

            val latestSubscription =
                onCheckPremium()

            isCheckingPremium = false

            when {

                // ==========================================
                // Premium Active
                // ==========================================

                latestSubscription?.is_premium == true -> {

                    onProtectedAction()
                }

                // ==========================================
                // Premium Request Pending
                // ==========================================

                latestSubscription?.status.equals(
                    "PENDING",
                    ignoreCase = true
                ) -> {

                    showPendingDialog = true
                }

                // ==========================================
                // Not Premium / Expired
                // ==========================================

                else -> {

                    showPaymentDialog = true
                }
            }
        }
    }
 // ==========================================
    // Caller UI
    // ==========================================

    content(
        handleProtectedAction
    )

    // ==========================================
    // Payment Dialog
    // ==========================================

    if (showPaymentDialog) {

        PremiumPaymentDialog(

            plans = plans,

            selectedPlanId =
                selectedPlanId,

            isSubmitting =
                isSubmitting,

            onPlanSelected = { planId ->

                onPlanSelected(
                    planId
                )
            },

            onPremiumRequest = {

                onPremiumRequest()
            },

            onDismiss = {

                if (!isSubmitting) {

                    showPaymentDialog = false
                }
            }
        )
    }

    // ==========================================
    // Pending Dialog
    // ==========================================

    if (showPendingDialog) {

        PremiumPendingDialog(

            onDismiss = {

                showPendingDialog = false
            }
        )
    }
}