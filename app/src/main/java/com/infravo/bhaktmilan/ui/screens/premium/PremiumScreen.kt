package com.infravo.bhaktmilan.ui.screens.premium

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.infravo.bhaktmilan.ui.viewmodel.PremiumViewModel

@Composable
fun PremiumScreen(
    viewModel: PremiumViewModel = hiltViewModel()
) {

    val uiState by
    viewModel.uiState.collectAsState()

    var showPaymentDialog by remember {
        mutableStateOf(false)
    }

    // ==========================================
    // Subscribe Success
    // ==========================================

    LaunchedEffect(uiState.subscribeSuccess) {

        if (uiState.subscribeSuccess) {

            showPaymentDialog = false

            viewModel.clearSubscribeSuccess()

            viewModel.refreshSubscription()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // ==========================================
        // Header
        // ==========================================

        Text(
            text = "Premium",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(
            modifier = Modifier.padding(
                top = 6.dp
            )
        )

        Text(
            text =
                "Unlock premium features and send interest requests.",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        // ==========================================
        // Loading
        // ==========================================

        if (uiState.isLoading) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {

                CircularProgressIndicator()
            }
        }

        // ==========================================
        // Error
        // ==========================================

        uiState.error?.let { message ->

            Text(
                text = message,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(
                    bottom = 12.dp
                )
            )
        }

        // ==========================================
        // Current Subscription
        // ==========================================

        uiState.subscription?.let { subscription ->

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {

                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement =
                        Arrangement.spacedBy(6.dp)
                ) {

                    when {

                        // ==================================
                        // ACTIVE
                        // ==================================

                        subscription.is_premium -> {

                            Text(
                                text = "Premium Active",
                                style =
                                    MaterialTheme.typography.titleMedium
                            )

                            subscription.plan?.let { plan ->

                                Text(
                                    text = "Plan: $plan"
                                )
                            }

                            Text(
                                text =
                                    "Days left: ${subscription.days_left}"
                            )

                            subscription.starts_at?.let {

                                Text(
                                    text =
                                        "Started: $it"
                                )
                            }

                            subscription.expires_at?.let {

                                Text(
                                    text =
                                        "Expires: $it"
                                )
                            }
                        }

                        // ==================================
                        // PENDING
                        // ==================================

                        subscription.status.equals(
                            "PENDING",
                            ignoreCase = true
                        ) -> {

                            Text(
                                text =
                                    "Premium Request Pending",
                                style =
                                    MaterialTheme.typography.titleMedium
                            )

                            Text(
                                text =
                                    "Your premium request is waiting " +
                                            "for admin approval."
                            )

                            Text(
                                text =
                                    "Please do not submit another request."
                            )
                        }

                        // ==================================
                        // OTHER / EXPIRED
                        // ==================================

                        else -> {

                            Text(
                                text =
                                    "Premium Not Active",
                                style =
                                    MaterialTheme.typography.titleMedium
                            )

                            subscription.status?.let {

                                Text(
                                    text =
                                        "Status: $it"
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        // ==========================================
        // Plans
        // ==========================================

        Text(
            text = "Available Plans",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement =
                Arrangement.spacedBy(12.dp)
        ) {

            items(
                items = uiState.plans,
                key = { plan ->
                    plan.id
                }
            ) { plan ->

                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement =
                            Arrangement.spacedBy(6.dp)
                    ) {

                        Text(
                            text = plan.name,
                            style =
                                MaterialTheme.typography.titleMedium
                        )

                        Text(
                            text = "₹${plan.price}",
                            style =
                                MaterialTheme.typography.headlineSmall
                        )

                        Text(
                            text =
                                "${plan.duration_days} days"
                        )

                        OutlinedButton(
                            modifier =
                                Modifier.fillMaxWidth(),

                            enabled =
                                !viewModel
                                    .isPremiumRequestDisabled(),

                            onClick = {

                                viewModel.selectPlan(
                                    plan.id
                                )

                                showPaymentDialog = true
                            }
                        ) {

                            when {

                                uiState.subscription
                                    ?.is_premium == true -> {

                                    Text("Premium Active")
                                }

                                uiState.subscription
                                    ?.status.equals(
                                        "PENDING",
                                        ignoreCase = true
                                    ) -> {

                                    Text("Request Pending")
                                }

                                uiState.isSubscribing -> {

                                    Text("Submitting...")
                                }

                                uiState.selectedPlanId ==
                                        plan.id -> {

                                    Text("Selected")
                                }

                                else -> {

                                    Text("Select Plan")
                                }
                            }
                        }
                    }
                }
            }
        }

        // ==========================================
        // Payment Dialog
        // ==========================================

        if (showPaymentDialog) {

            PremiumPaymentDialog(

                plans = uiState.plans,

                selectedPlanId =
                    uiState.selectedPlanId,

                isSubmitting =
                    uiState.isSubscribing,

                onPlanSelected = { planId ->

                    viewModel.selectPlan(
                        planId
                    )
                },

                onPremiumRequest = {

                    viewModel.subscribe()
                },

                onDismiss = {

                    if (!uiState.isSubscribing) {

                        showPaymentDialog = false
                    }
                }
            )
        }
    }
}