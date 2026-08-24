package com.infravo.bhaktmilan.ui.screens.requests

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
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
import com.infravo.bhaktmilan.data.remote.response.Interest
import com.infravo.bhaktmilan.ui.screens.premium.PremiumGate
import com.infravo.bhaktmilan.ui.viewmodel.InteractionViewModel
import com.infravo.bhaktmilan.ui.viewmodel.PremiumViewModel

@Composable
fun RequestsScreen(
    interactionViewModel: InteractionViewModel = hiltViewModel(),
    premiumViewModel: PremiumViewModel = hiltViewModel()
) {

    val interactionState by
    interactionViewModel.uiState.collectAsState()

    val premiumState by
    premiumViewModel.uiState.collectAsState()

    var selectedTab by remember {
        mutableStateOf(RequestTab.RECEIVED)
    }

    // ==========================================
    // Load Requests
    // ==========================================

    LaunchedEffect(Unit) {
        interactionViewModel.loadInteractions()
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
            text = "Requests",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        // ==========================================
        // Tabs
        // ==========================================

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {

            FilterChip(
                selected =
                    selectedTab == RequestTab.RECEIVED,

                onClick = {
                    selectedTab =
                        RequestTab.RECEIVED
                },

                label = {
                    Text("Received")
                }
            )

            Spacer(
                modifier = Modifier.width(8.dp)
            )

            FilterChip(
                selected =
                    selectedTab == RequestTab.SENT,

                onClick = {
                    selectedTab =
                        RequestTab.SENT
                },

                label = {
                    Text("Sent")
                }
            )
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        // ==========================================
        // Loading
        // ==========================================

        if (interactionState.isLoading) {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                CircularProgressIndicator()
            }

            return@Column
        }

        // ==========================================
        // Error
        // ==========================================

        interactionState.error?.let { error ->

            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(
                    bottom = 12.dp
                )
            )
        }

        // ==========================================
        // Current Requests
        // ==========================================

        val requests =
            when (selectedTab) {

                RequestTab.RECEIVED ->
                    interactionState.receivedInterests

                RequestTab.SENT ->
                    interactionState.sentInterests
            }

        if (requests.isEmpty()) {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text =
                        if (
                            selectedTab ==
                            RequestTab.RECEIVED
                        ) {
                            "No received requests."
                        } else {
                            "No sent requests."
                        }
                )
            }

        } else {

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement =
                    Arrangement.spacedBy(12.dp)
            ) {

                items(
                    items = requests,
                    key = { it.id }
                ) { interest ->

                    InterestCard(
                        interest = interest,
                        isReceived =
                            selectedTab ==
                                    RequestTab.RECEIVED,
                        isActionLoading =
                            interactionState
                                .isActionLoading,
                        premiumState =
                            premiumState,
                        premiumViewModel =
                            premiumViewModel,
                        interactionViewModel =
                            interactionViewModel
                    )
                }
            }
        }
    }
}

// ==========================================
// Request Tab
// ==========================================

private enum class RequestTab {
    RECEIVED,
    SENT
}

// ==========================================
// Interest Card
// ==========================================

@Composable
private fun InterestCard(
    interest: Interest,
    isReceived: Boolean,
    isActionLoading: Boolean,
    premiumState:
    com.infravo.bhaktmilan.ui.viewmodel.PremiumUiState,
    premiumViewModel: PremiumViewModel,
    interactionViewModel: InteractionViewModel
) {

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement =
                Arrangement.spacedBy(6.dp)
        ) {

            // ==========================================
            // Person
            // ==========================================

            Text(
                text =
                    if (isReceived) {
                        interest.sender_name
                    } else {
                        interest.receiver_name
                    },

                style =
                    MaterialTheme.typography.titleMedium
            )

            Text(
                text =
                    if (isReceived) {
                        interest.sender_profile_id
                    } else {
                        interest.receiver_profile_id
                    },

                style =
                    MaterialTheme.typography.bodySmall
            )

            // ==========================================
            // Status
            // ==========================================

            Text(
                text =
                    "Status: ${interest.status}"
            )

            // ==========================================
            // Message
            // ==========================================

            if (interest.message.isNotBlank()) {

                Text(
                    text =
                        "Message: ${interest.message}"
                )
            }

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            // ==========================================
            // RECEIVED
            // ==========================================

            if (isReceived) {

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    // ======================================
                    // ACCEPT
                    // ======================================

                    PremiumGate(

                        subscription =
                            premiumState.subscription,

                        plans =
                            premiumState.plans,

                        selectedPlanId =
                            premiumState.selectedPlanId,

                        isSubmitting =
                            premiumState.isSubscribing,

                        subscribeSuccess =
                            premiumState.subscribeSuccess,

                        onCheckPremium = {

                            premiumViewModel
                                .getLatestSubscription()
                        },

                        onPlanSelected = { planId ->

                            premiumViewModel
                                .selectPlan(planId)
                        },

                        onPremiumRequest = {

                            premiumViewModel
                                .subscribe()
                        },

                        onClearSubscribeSuccess = {

                            premiumViewModel
                                .clearSubscribeSuccess()
                        },

                        onProtectedAction = {

                            interactionViewModel
                                .acceptInterest(
                                    interest.id
                                )
                        },

                        content = { onClick ->

                            Button(
                                modifier =
                                    Modifier.weight(1f),

                                onClick = onClick,

                                enabled =
                                    !isActionLoading
                            ) {

                                Text("Accept")
                            }
                        }
                    )

                    Spacer(
                        modifier = Modifier.width(8.dp)
                    )

                    // ======================================
                    // REJECT
                    // ======================================

                    PremiumGate(

                        subscription =
                            premiumState.subscription,

                        plans =
                            premiumState.plans,

                        selectedPlanId =
                            premiumState.selectedPlanId,

                        isSubmitting =
                            premiumState.isSubscribing,

                        subscribeSuccess =
                            premiumState.subscribeSuccess,

                        onCheckPremium = {

                            premiumViewModel
                                .getLatestSubscription()
                        },

                        onPlanSelected = { planId ->

                            premiumViewModel
                                .selectPlan(planId)
                        },

                        onPremiumRequest = {

                            premiumViewModel
                                .subscribe()
                        },

                        onClearSubscribeSuccess = {

                            premiumViewModel
                                .clearSubscribeSuccess()
                        },

                        onProtectedAction = {

                            interactionViewModel
                                .rejectInterest(
                                    interest.id
                                )
                        },

                        content = { onClick ->

                            OutlinedButton(
                                modifier =
                                    Modifier.weight(1f),

                                onClick = onClick,

                                enabled =
                                    !isActionLoading
                            ) {

                                Text("Reject")
                            }
                        }
                    )
                }

            } else {

                // ==========================================
                // SENT - CANCEL
                // ==========================================

                PremiumGate(

                    subscription =
                        premiumState.subscription,

                    plans =
                        premiumState.plans,

                    selectedPlanId =
                        premiumState.selectedPlanId,

                    isSubmitting =
                        premiumState.isSubscribing,

                    subscribeSuccess =
                        premiumState.subscribeSuccess,

                    onCheckPremium = {

                        premiumViewModel
                            .getLatestSubscription()
                    },

                    onPlanSelected = { planId ->

                        premiumViewModel
                            .selectPlan(planId)
                    },

                    onPremiumRequest = {

                        premiumViewModel
                            .subscribe()
                    },

                    onClearSubscribeSuccess = {

                        premiumViewModel
                            .clearSubscribeSuccess()
                    },

                    onProtectedAction = {

                        interactionViewModel
                            .cancelInterest(
                                interest.id
                            )
                    },

                    content = { onClick ->

                        OutlinedButton(
                            modifier =
                                Modifier.fillMaxWidth(),

                            onClick = onClick,

                            enabled =
                                !isActionLoading
                        ) {

                            Text("Cancel")
                        }
                    }
                )
            }
        }
    }
}