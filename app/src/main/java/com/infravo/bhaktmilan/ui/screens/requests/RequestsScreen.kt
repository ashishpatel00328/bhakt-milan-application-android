package com.infravo.bhaktmilan.ui.screens.requests

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.infravo.bhaktmilan.data.remote.response.Interest
import com.infravo.bhaktmilan.ui.screens.premium.PremiumGate
import com.infravo.bhaktmilan.ui.theme.AppBackground
import com.infravo.bhaktmilan.ui.theme.BhaktMaroon
import com.infravo.bhaktmilan.ui.theme.BhaktMaroonLight
import com.infravo.bhaktmilan.ui.theme.BorderColor
import com.infravo.bhaktmilan.ui.theme.PremiumGold
import com.infravo.bhaktmilan.ui.theme.SuccessGreen
import com.infravo.bhaktmilan.ui.theme.SuccessGreenLight
import com.infravo.bhaktmilan.ui.theme.SurfaceBackground
import com.infravo.bhaktmilan.ui.theme.TextMuted
import com.infravo.bhaktmilan.ui.theme.TextPrimary
import com.infravo.bhaktmilan.ui.theme.TextSecondary
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

    LaunchedEffect(Unit) {
        interactionViewModel.loadInteractions()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
    ) {

        // =========================================================
        // HEADER
        // =========================================================

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 20.dp,
                    end = 12.dp,
                    top = 18.dp,
                    bottom = 12.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "Requests",
                color = TextPrimary,
                fontSize = 27.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.weight(1f)
            )

            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(
                        RoundedCornerShape(12.dp)
                    )
                    .clickable { },
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector =
                        Icons.Outlined.NotificationsNone,
                    contentDescription = "Notifications",
                    tint = TextPrimary,
                    modifier = Modifier.size(26.dp)
                )
            }
        }

        // =========================================================
        // RECEIVED / SENT SEGMENT
        // =========================================================

        RequestTabs(
            selectedTab = selectedTab,
            onTabSelected = {
                selectedTab = it
            }
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        // =========================================================
        // LOADING
        // =========================================================

        if (interactionState.isLoading) {

            LoadingRequests()

            return@Column
        }

        // =========================================================
        // ERROR
        // =========================================================

        interactionState.error?.let { error ->

            ErrorRequests(
                message = error
            )

            return@Column
        }

        // =========================================================
        // CURRENT LIST
        // =========================================================

        val requests =
            when (selectedTab) {

                RequestTab.RECEIVED ->
                    interactionState.receivedInterests

                RequestTab.SENT ->
                    interactionState.sentInterests
            }

        if (requests.isEmpty()) {

            EmptyRequests(
                isReceived =
                    selectedTab ==
                            RequestTab.RECEIVED
            )

        } else {

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 12.dp,
                    end = 12.dp,
                    top = 2.dp,
                    bottom = 24.dp
                ),
                verticalArrangement =
                    Arrangement.spacedBy(14.dp)
            ) {

                items(
                    items = requests,
                    key = { it.id }
                ) { interest ->

                    InterestRequestCard(
                        interest = interest,
                        isReceived =
                            selectedTab ==
                                    RequestTab.RECEIVED,
                        isActionLoading =
                            interactionState.isActionLoading,
                        premiumState = premiumState,
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


// ===============================================================
// TAB
// ===============================================================

private enum class RequestTab {
    RECEIVED,
    SENT
}


@Composable
private fun RequestTabs(
    selectedTab: RequestTab,
    onTabSelected: (RequestTab) -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clip(
                RoundedCornerShape(12.dp)
            )
            .background(
                SurfaceBackground
            )
            .border(
                width = 1.dp,
                color = BorderColor,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(3.dp)
    ) {

        RequestTabItem(
            text = "Received",
            selected =
                selectedTab ==
                        RequestTab.RECEIVED,
            modifier = Modifier.weight(1f),
            onClick = {
                onTabSelected(
                    RequestTab.RECEIVED
                )
            }
        )

        RequestTabItem(
            text = "Sent",
            selected =
                selectedTab ==
                        RequestTab.SENT,
            modifier = Modifier.weight(1f),
            onClick = {
                onTabSelected(
                    RequestTab.SENT
                )
            }
        )
    }
}


@Composable
private fun RequestTabItem(
    text: String,
    selected: Boolean,
    modifier: Modifier,
    onClick: () -> Unit
) {

    Box(
        modifier = modifier
            .clip(
                RoundedCornerShape(9.dp)
            )
            .background(
                if (selected) {
                    BhaktMaroon
                } else {
                    Color.Transparent
                }
            )
            .clickable {
                onClick()
            }
            .padding(
                vertical = 10.dp
            ),
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = text,
            color =
                if (selected) {
                    SurfaceBackground
                } else {
                    TextPrimary
                },
            fontSize = 14.sp,
            fontWeight =
                if (selected) {
                    FontWeight.Bold
                } else {
                    FontWeight.Medium
                }
        )
    }
}


// ===============================================================
// REQUEST CARD
// ===============================================================

@Composable
private fun InterestRequestCard(
    interest: Interest,
    isReceived: Boolean,
    isActionLoading: Boolean,
    premiumState:
    com.infravo.bhaktmilan.ui.viewmodel.PremiumUiState,
    premiumViewModel: PremiumViewModel,
    interactionViewModel: InteractionViewModel
) {

    val name =
        if (isReceived) {
            interest.sender_name
        } else {
            interest.receiver_name
        }

    val profileId =
        if (isReceived) {
            interest.sender_profile_id
        } else {
            interest.receiver_profile_id
        }

    val profilePk =
        if (isReceived) {
            interest.sender_profile_pk
        } else {
            interest.receiver_profile_pk
        }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                // Existing profile navigation can be
                // connected here later without changing
                // request functionality.
            },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = SurfaceBackground
        ),
        border = BorderStroke(
            width = 1.dp,
            color = BorderColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(12.dp)
        ) {

            // =====================================================
            // PROFILE AREA
            // =====================================================

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {

                ProfileAvatar(
                    name = name
                )

                Spacer(
                    modifier = Modifier.width(12.dp)
                )

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    // -------------------------------------------------
                    // NAME + STAR
                    // -------------------------------------------------

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        Text(
                            text = name,
                            color = TextPrimary,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow =
                                TextOverflow.Ellipsis,
                            modifier =
                                Modifier.weight(1f)
                        )

                        Spacer(
                            modifier = Modifier.width(6.dp)
                        )

                        Icon(
                            imageVector =
                                androidx.compose.material.icons.Icons.Filled.Star,
                            contentDescription =
                                "Shortlist",
                            tint = PremiumGold,
                            modifier =
                                Modifier.size(21.dp)
                        )
                    }

                    Spacer(
                        modifier = Modifier.height(5.dp)
                    )

                    // -------------------------------------------------
                    // PROFILE ID
                    // -------------------------------------------------

                    Text(
                        text = profileId,
                        color = TextSecondary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(
                        modifier = Modifier.height(9.dp)
                    )

                    // -------------------------------------------------
                    // STATUS BADGE
                    // -------------------------------------------------

                    RequestStatusBadge(
                        status = interest.status
                    )

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    // -------------------------------------------------
                    // STATUS
                    // -------------------------------------------------

                    Text(
                        text =
                            "Status: ${interest.status}",
                        color = TextPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(
                        modifier = Modifier.height(4.dp)
                    )

                    // -------------------------------------------------
                    // MESSAGE
                    // -------------------------------------------------

                    Text(
                        text =
                            if (interest.message.isBlank()) {
                                "Message: —"
                            } else {
                                "Message: ${interest.message}"
                            },
                        color = TextSecondary,
                        fontSize = 12.sp,
                        maxLines = 2,
                        overflow =
                            TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            // =====================================================
            // ACTIONS
            // =====================================================

            if (isReceived) {

                ReceivedActions(
                    interest = interest,
                    isActionLoading =
                        isActionLoading,
                    premiumState =
                        premiumState,
                    premiumViewModel =
                        premiumViewModel,
                    interactionViewModel =
                        interactionViewModel
                )

            } else {

                SentAction(
                    interest = interest,
                    isActionLoading =
                        isActionLoading,
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


// ===============================================================
// PROFILE AVATAR
// ===============================================================

@Composable
private fun ProfileAvatar(
    name: String
) {

    val initial =
        name
            .trim()
            .firstOrNull()
            ?.uppercase()
            ?: "?"

    Box(
        modifier = Modifier
            .size(
                width = 112.dp,
                height = 118.dp
            )
            .clip(
                RoundedCornerShape(14.dp)
            )
            .background(
                BhaktMaroonLight
            ),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            Icon(
                imageVector =
                    Icons.Outlined.Person,
                contentDescription = null,
                tint = BhaktMaroon,
                modifier = Modifier.size(38.dp)
            )

            Spacer(
                modifier = Modifier.height(2.dp)
            )

            Text(
                text = initial,
                color = BhaktMaroon,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


// ===============================================================
// STATUS BADGE
// ===============================================================

@Composable
private fun RequestStatusBadge(
    status: String
) {

    val normalized =
        status.trim().lowercase()

    val isPending =
        normalized == "pending"

    val background =
        if (isPending) {
            SuccessGreenLight
        } else {
            BhaktMaroonLight
        }

    val foreground =
        if (isPending) {
            SuccessGreen
        } else {
            BhaktMaroon
        }

    Row(
        modifier = Modifier
            .clip(
                RoundedCornerShape(50.dp)
            )
            .background(background)
            .padding(
                horizontal = 9.dp,
                vertical = 5.dp
            ),
        verticalAlignment =
            Alignment.CenterVertically
    ) {

        if (isPending) {

            Icon(
                imageVector =
                    Icons.Outlined.Schedule,
                contentDescription = null,
                tint = foreground,
                modifier = Modifier.size(14.dp)
            )

            Spacer(
                modifier = Modifier.width(4.dp)
            )
        }

        Text(
            text =
                status
                    .trim()
                    .replaceFirstChar {
                        it.uppercase()
                    },
            color = foreground,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}


// ===============================================================
// RECEIVED ACTIONS
// ===============================================================

@Composable
private fun ReceivedActions(
    interest: Interest,
    isActionLoading: Boolean,
    premiumState:
    com.infravo.bhaktmilan.ui.viewmodel.PremiumUiState,
    premiumViewModel: PremiumViewModel,
    interactionViewModel: InteractionViewModel
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement =
            Arrangement.spacedBy(12.dp)
    ) {

        // =========================================================
        // ACCEPT
        // =========================================================

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

            subscribeResponseCode =
                premiumState.subscribeResponseCode,

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
                    onClick = onClick,
                    enabled =
                        !isActionLoading,
                    modifier =
                        Modifier.weight(1f),
                    shape =
                        RoundedCornerShape(10.dp),
                    contentPadding =
                        PaddingValues(
                            vertical = 10.dp
                        ),
                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor =
                                BhaktMaroon,
                            contentColor =
                                SurfaceBackground
                        )
                ) {

                    Icon(
                        imageVector =
                            Icons.Outlined.Check,
                        contentDescription = null,
                        modifier =
                            Modifier.size(17.dp)
                    )

                    Spacer(
                        modifier =
                            Modifier.width(5.dp)
                    )

                    Text(
                        text = "Accept",
                        fontSize = 13.sp,
                        fontWeight =
                            FontWeight.SemiBold
                    )
                }
            }
        )

        // =========================================================
        // REJECT
        // =========================================================

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

            subscribeResponseCode =
                premiumState.subscribeResponseCode,

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
                    onClick = onClick,
                    enabled =
                        !isActionLoading,
                    modifier =
                        Modifier.weight(1f),
                    shape =
                        RoundedCornerShape(10.dp),
                    border =
                        BorderStroke(
                            width = 1.5.dp,
                            color = BhaktMaroon
                        ),
                    contentPadding =
                        PaddingValues(
                            vertical = 10.dp
                        )
                ) {

                    Icon(
                        imageVector =
                            Icons.Outlined.Close,
                        contentDescription = null,
                        tint = BhaktMaroon,
                        modifier =
                            Modifier.size(17.dp)
                    )

                    Spacer(
                        modifier =
                            Modifier.width(5.dp)
                    )

                    Text(
                        text = "Reject",
                        color = BhaktMaroon,
                        fontSize = 13.sp,
                        fontWeight =
                            FontWeight.SemiBold
                    )
                }
            }
        )
    }
}


// ===============================================================
// SENT ACTION
// ===============================================================

@Composable
private fun SentAction(
    interest: Interest,
    isActionLoading: Boolean,
    premiumState:
    com.infravo.bhaktmilan.ui.viewmodel.PremiumUiState,
    premiumViewModel: PremiumViewModel,
    interactionViewModel: InteractionViewModel
) {

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

        subscribeResponseCode =
            premiumState.subscribeResponseCode,

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
                onClick = onClick,
                enabled =
                    !isActionLoading,
                modifier =
                    Modifier.fillMaxWidth(),
                shape =
                    RoundedCornerShape(10.dp),
                border =
                    BorderStroke(
                        width = 1.5.dp,
                        color = BhaktMaroon
                    ),
                contentPadding =
                    PaddingValues(
                        vertical = 10.dp
                    )
            ) {

                Icon(
                    imageVector =
                        Icons.Outlined.Close,
                    contentDescription = null,
                    tint = BhaktMaroon,
                    modifier =
                        Modifier.size(17.dp)
                )

                Spacer(
                    modifier =
                        Modifier.width(5.dp)
                )

                Text(
                    text = "Cancel Request",
                    color = BhaktMaroon,
                    fontSize = 13.sp,
                    fontWeight =
                        FontWeight.SemiBold
                )
            }
        }
    )
}


// ===============================================================
// LOADING
// ===============================================================

@Composable
private fun LoadingRequests() {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        CircularProgressIndicator(
            color = BhaktMaroon
        )
    }
}


// ===============================================================
// ERROR
// ===============================================================

@Composable
private fun ErrorRequests(
    message: String
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(28.dp),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .size(70.dp)
                    .clip(
                        RoundedCornerShape(22.dp)
                    )
                    .background(
                        BhaktMaroonLight
                    ),
                contentAlignment =
                    Alignment.Center
            ) {

                Icon(
                    imageVector =
                        Icons.Outlined.FavoriteBorder,
                    contentDescription = null,
                    tint = BhaktMaroon,
                    modifier =
                        Modifier.size(34.dp)
                )
            }

            Spacer(
                modifier =
                    Modifier.height(16.dp)
            )

            Text(
                text = "Unable to load requests",
                color = TextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier =
                    Modifier.height(7.dp)
            )

            Text(
                text = message,
                color = TextSecondary,
                fontSize = 13.sp
            )
        }
    }
}


// ===============================================================
// EMPTY
// ===============================================================

@Composable
private fun EmptyRequests(
    isReceived: Boolean
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .size(84.dp)
                    .clip(
                        RoundedCornerShape(26.dp)
                    )
                    .background(
                        BhaktMaroonLight
                    ),
                contentAlignment =
                    Alignment.Center
            ) {

                Icon(
                    imageVector =
                        Icons.Outlined.FavoriteBorder,
                    contentDescription = null,
                    tint = BhaktMaroon,
                    modifier =
                        Modifier.size(40.dp)
                )
            }

            Spacer(
                modifier =
                    Modifier.height(18.dp)
            )

            Text(
                text =
                    if (isReceived) {
                        "No Requests Yet"
                    } else {
                        "No Sent Requests"
                    },
                color = TextPrimary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier =
                    Modifier.height(7.dp)
            )

            Text(
                text =
                    if (isReceived) {
                        "When someone sends you an interest,\nit will appear here."
                    } else {
                        "Your sent interests will appear here."
                    },
                color = TextSecondary,
                fontSize = 13.sp,
                lineHeight = 19.sp
            )
        }
    }
}