package com.infravo.bhaktmilan.ui.screens.premium

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.Verified
import androidx.compose.material3.Card
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.infravo.bhaktmilan.ui.theme.AppBackground
import com.infravo.bhaktmilan.ui.theme.BhaktMaroon
import com.infravo.bhaktmilan.ui.theme.BhaktMaroonDark
import com.infravo.bhaktmilan.ui.theme.BhaktMaroonLight
import com.infravo.bhaktmilan.ui.theme.BorderColor
import com.infravo.bhaktmilan.ui.theme.DividerColor
import com.infravo.bhaktmilan.ui.theme.PremiumGold
import com.infravo.bhaktmilan.ui.theme.PremiumGoldDark
import com.infravo.bhaktmilan.ui.theme.PremiumGoldLight
import com.infravo.bhaktmilan.ui.theme.SurfaceBackground
import com.infravo.bhaktmilan.ui.theme.TextMuted
import com.infravo.bhaktmilan.ui.theme.TextOnGold
import com.infravo.bhaktmilan.ui.theme.TextPrimary
import com.infravo.bhaktmilan.ui.theme.TextSecondary
import com.infravo.bhaktmilan.ui.theme.SuccessGreen
import com.infravo.bhaktmilan.ui.theme.SuccessGreenLight
import com.infravo.bhaktmilan.ui.viewmodel.PremiumViewModel
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun PremiumScreen(
    viewModel: PremiumViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    var showPaymentDialog by remember {
        mutableStateOf(false)
    }

    // =========================================================
    // SUBSCRIBE SUCCESS
    // =========================================================

    LaunchedEffect(
        uiState.subscribeSuccess
    ) {
        if (uiState.subscribeSuccess) {
            showPaymentDialog = false
            viewModel.clearSubscribeSuccess()
        }
    }

    // =========================================================
    // SUBSCRIBE RESPONSE
    // =========================================================
    // Keep the response dialog state-driven. Do not mutate
    // showPaymentDialog from inside the composition/render block.
    LaunchedEffect(
        uiState.subscribeResponseCode,
        uiState.subscribeResponseMessage
    ) {
        if (
            uiState.subscribeResponseCode != null ||
            !uiState.subscribeResponseMessage.isNullOrBlank()
        ) {
            showPaymentDialog = false
        }
    }

    // =========================================================
    // SCREEN
    // =========================================================

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
    ) {

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 10.dp,
                bottom = 30.dp
            ),
            verticalArrangement =
                Arrangement.spacedBy(14.dp)
        ) {

            // =====================================================
            // HEADER
            // =====================================================

            item {
                PremiumHeader()
            }

            // =====================================================
            // PREMIUM HERO
            // =====================================================

            item {
                PremiumHeroCard()
            }

            // =====================================================
            // ERROR
            // =====================================================

            uiState.error?.let { message ->

                item {
                    ErrorCard(
                        message = message
                    )
                }
            }

            // =====================================================
            // LOADING
            // =====================================================

            if (uiState.isLoading) {

                item {
                    LoadingCard()
                }
            }

            // =====================================================
            // CURRENT SUBSCRIPTION
            // =====================================================

            uiState.subscription?.let { subscription ->

                item {
                    SubscriptionStatusCard(
                        isPremium = subscription.is_premium,
                        status = subscription.status,
                        plan = subscription.plan,
                        daysLeft = subscription.days_left,
                        startsAt = subscription.starts_at,
                        expiresAt = subscription.expires_at
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
            // AVAILABLE PLANS
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

                val isPending =
                    uiState.subscription?.status.equals(
                        "PENDING",
                        ignoreCase = true
                    )

                PremiumPlanCard(
                    name = plan.name,
                    price = plan.price,
                    durationDays = plan.duration_days,
                    selected =
                        uiState.selectedPlanId == plan.id,
                    isPremium = isPremium,
                    isPending = isPending,
                    isSubmitting = uiState.isSubscribing,
                    onClick = {

                        when {

                            // -----------------------------------------
                            // ALREADY PREMIUM
                            // -----------------------------------------

                            isPremium -> {
                                // No action
                            }

                            // -----------------------------------------
                            // OPEN REQUEST FLOW
                            //
                            // Even when subscription status is PENDING,
                            // allow the user to reach the request button.
                            // The POST API decides whether the request is
                            // new (2xx) or already exists (409).
                            // -----------------------------------------

                            else -> {

                                viewModel.selectPlan(
                                    plan.id
                                )

                                showPaymentDialog = true
                            }
                        }
                    }
                )
            }

            // =====================================================
            // TRUST FOOTER
            // =====================================================

            item {
                PremiumTrustFooter()
            }
        }

        // =========================================================
        // REQUEST / RESPONSE DIALOGS
        // =========================================================
        // Only one dialog is rendered at a time. A backend response
        // always takes priority over the payment dialog.

        when {
            uiState.subscribeResponseCode == 409 -> {

                PremiumPendingDialog(
                    onDismiss = {
                        viewModel.clearSubscribeResponse()
                    }
                )
            }

            uiState.subscribeResponseCode != null &&
                    !uiState.subscribeResponseMessage.isNullOrBlank() -> {

                PremiumSubscribeResponseDialog(
                    message = uiState.subscribeResponseMessage,
                    isConflict = false,
                    onDismiss = {
                        viewModel.clearSubscribeResponse()
                    }
                )
            }

            showPaymentDialog -> {
                PremiumPaymentDialog(
                    plans = uiState.plans,
                    selectedPlanId = uiState.selectedPlanId,
                    isSubmitting = uiState.isSubscribing,
                    onPlanSelected = { planId ->
                        viewModel.selectPlan(planId)
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
}

// =================================================================
// SUBSCRIBE RESPONSE DIALOG
// =================================================================

@Composable
private fun PremiumSubscribeResponseDialog(
    message: String?,
    isConflict: Boolean,
    onDismiss: () -> Unit
) {
    if (message.isNullOrBlank()) {
        return
    }

    val containerColor =
        if (isConflict) {
            BhaktMaroonLight
        } else {
            SuccessGreenLight
        }

    val iconTint =
        if (isConflict) {
            BhaktMaroon
        } else {
            SuccessGreen
        }

    val title =
        if (isConflict) {
            "Request Already Exists"
        } else {
            "Request Submitted"
        }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 22.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor =
                    SurfaceBackground
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp
            )
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 22.dp,
                        vertical = 24.dp
                    ),
                horizontalAlignment =
                    Alignment.CenterHorizontally
            ) {

                // =====================================================
                // ICON
                // =====================================================

                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(
                            RoundedCornerShape(18.dp)
                        )
                        .background(
                            containerColor
                        ),
                    contentAlignment =
                        Alignment.Center
                ) {

                    Icon(
                        imageVector =
                            if (isConflict) {
                                Icons.Outlined.Lock
                            } else {
                                Icons.Outlined.CheckCircle
                            },
                        contentDescription = null,
                        tint = iconTint,
                        modifier =
                            Modifier.size(28.dp)
                    )
                }

                Spacer(
                    modifier =
                        Modifier.height(14.dp)
                )

                // =====================================================
                // TITLE
                // =====================================================

                Text(
                    text = title,
                    color = TextPrimary,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily.Serif,
                    textAlign = TextAlign.Center
                )

                Spacer(
                    modifier =
                        Modifier.height(8.dp)
                )

                // =====================================================
                // API MESSAGE
                // =====================================================

                Text(
                    text = message,
                    color = TextSecondary,
                    fontSize = 12.sp,
                    lineHeight = 18.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(
                    modifier =
                        Modifier.height(12.dp)
                )

                Text(
                    text =
                        if (isConflict) {
                            "Your premium request is already awaiting approval."
                        } else {
                            "Your premium request has been created successfully."
                        },
                    color = TextMuted,
                    fontSize = 10.sp,
                    lineHeight = 16.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(20.dp))

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    onClick = onDismiss,
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        text = "OK",
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

// =================================================================
// HEADER
// =================================================================

@Composable
private fun PremiumHeader() {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 4.dp,
                bottom = 4.dp
            ),
        verticalAlignment =
            Alignment.CenterVertically
    ) {

        Column(
            modifier =
                Modifier.weight(1f)
        ) {

            Text(
                text = "Premium",
                color = TextPrimary,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif
            )

            Spacer(
                modifier =
                    Modifier.height(3.dp)
            )

            Text(
                text =
                    "Make meaningful connections with more confidence.",
                color = TextSecondary,
                fontSize = 11.sp,
                lineHeight = 17.sp
            )
        }
    }
}

// =================================================================
// DATE FORMAT
// =================================================================

private fun formatPremiumDate(
    value: String?
): String {

    if (value.isNullOrBlank()) {
        return "—"
    }

    return try {

        val dateTime =
            OffsetDateTime.parse(value)

        dateTime
            .toLocalDate()
            .format(
                DateTimeFormatter.ofPattern(
                    "dd MMM yyyy",
                    Locale.getDefault()
                )
            )

    } catch (e: Exception) {
        value
    }
}

// =================================================================
// PREMIUM HERO
// =================================================================

@Composable
private fun PremiumHeroCard() {

    Card(
        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(24.dp),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    PremiumGoldLight
            ),

        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 0.dp
            )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 20.dp,
                    vertical = 22.dp
                ),

            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            Spacer(
                modifier =
                    Modifier.height(15.dp)
            )

            Text(
                text = "Premium Membership",
                color = TextOnGold,
                fontSize = 23.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.Serif,
                textAlign = TextAlign.Center
            )

            Spacer(
                modifier =
                    Modifier.height(7.dp)
            )

            Text(
                text =
                    "Go beyond profiles. Connect with intention.",
                color = TextSecondary,
                fontSize = 12.sp,
                lineHeight = 19.sp,
                textAlign = TextAlign.Center
            )

            Spacer(
                modifier =
                    Modifier.height(14.dp)
            )

            Row(
                modifier =
                    Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.Center
            ) {

                PremiumMiniBadge(
                    icon =
                        Icons.Outlined.FavoriteBorder,
                    text = "Connect"
                )

                Spacer(
                    modifier =
                        Modifier.width(8.dp)
                )

                PremiumMiniBadge(
                    icon =
                        Icons.Outlined.Verified,
                    text = "Premium"
                )

                Spacer(
                    modifier =
                        Modifier.width(8.dp)
                )

                PremiumMiniBadge(
                    icon =
                        Icons.Outlined.Security,
                    text = "Trusted"
                )
            }
        }
    }
}

// =================================================================
// MINI BADGE
// =================================================================

@Composable
private fun PremiumMiniBadge(
    icon: ImageVector,
    text: String
) {

    Row(
        modifier = Modifier
            .clip(
                RoundedCornerShape(10.dp)
            )
            .background(
                SurfaceBackground.copy(
                    alpha = 0.72f
                )
            )
            .padding(
                horizontal = 9.dp,
                vertical = 6.dp
            ),

        verticalAlignment =
            Alignment.CenterVertically
    ) {

        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = PremiumGoldDark,
            modifier =
                Modifier.size(14.dp)
        )

        Spacer(
            modifier =
                Modifier.width(4.dp)
        )

        Text(
            text = text,
            color = TextOnGold,
            fontSize = 9.sp,
            fontWeight =
                FontWeight.SemiBold
        )
    }
}

// =================================================================
// ERROR CARD
// =================================================================

@Composable
private fun ErrorCard(
    message: String
) {

    Card(
        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(16.dp),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    BhaktMaroonLight
            ),

        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 0.dp
            )
    ) {

        Text(
            text = message,
            color = BhaktMaroonDark,
            fontSize = 12.sp,
            lineHeight = 18.sp,
            modifier =
                Modifier.padding(14.dp)
        )
    }
}

// =================================================================
// LOADING CARD
// =================================================================

@Composable
private fun LoadingCard() {

    Card(
        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(18.dp),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    SurfaceBackground
            ),

        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 0.dp
            )
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(22.dp),

            contentAlignment =
                Alignment.Center
        ) {

            CircularProgressIndicator(
                color = BhaktMaroon,
                modifier =
                    Modifier.size(28.dp)
            )
        }
    }
}

// =================================================================
// SUBSCRIPTION STATUS
// =================================================================

@Composable
private fun SubscriptionStatusCard(
    isPremium: Boolean,
    status: String?,
    plan: String?,
    daysLeft: Int,
    startsAt: String?,
    expiresAt: String?
) {

    val isPending =
        status.equals(
            "PENDING",
            ignoreCase = true
        )

    val containerColor =
        when {

            isPremium ->
                PremiumGoldLight

            isPending ->
                BhaktMaroonLight

            else ->
                SurfaceBackground
        }

    val iconTint =
        when {

            isPremium ->
                PremiumGoldDark

            isPending ->
                BhaktMaroon

            else ->
                TextSecondary
        }

    Card(
        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(20.dp),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    containerColor
            ),

        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 0.dp
            )
    ) {

        Column(
            modifier =
                Modifier.padding(18.dp)
        ) {

            Row(
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(
                            RoundedCornerShape(12.dp)
                        )
                        .background(
                            SurfaceBackground
                        ),

                    contentAlignment =
                        Alignment.Center
                ) {

                    Icon(
                        imageVector =
                            when {

                                isPremium ->
                                    Icons.Outlined.Verified

                                isPending ->
                                    Icons.Outlined.Lock

                                else ->
                                    Icons.Outlined.Star
                            },

                        contentDescription =
                            null,

                        tint = iconTint,

                        modifier =
                            Modifier.size(21.dp)
                    )
                }

                Spacer(
                    modifier =
                        Modifier.width(11.dp)
                )

                Column(
                    modifier =
                        Modifier.weight(1f)
                ) {

                    Text(
                        text =
                            when {

                                isPremium ->
                                    "Premium Active"

                                isPending ->
                                    "Premium Request Pending"

                                else ->
                                    "Premium Not Active"
                            },

                        color =
                            TextPrimary,

                        fontSize = 15.sp,

                        fontWeight =
                            FontWeight.SemiBold
                    )

                    Spacer(
                        modifier =
                            Modifier.height(3.dp)
                    )

                    Text(
                        text =
                            when {

                                isPremium ->
                                    "Your premium membership is active."

                                isPending ->
                                    "Your request is waiting for admin approval."

                                else ->
                                    "Choose a plan to unlock premium benefits."
                            },

                        color =
                            TextSecondary,

                        fontSize = 10.sp,

                        lineHeight =
                            16.sp
                    )
                }
            }

            // =====================================================
            // ACTIVE SUBSCRIPTION DETAILS
            // =====================================================

            if (isPremium) {

                Spacer(
                    modifier =
                        Modifier.height(16.dp)
                )

                androidx.compose.material3.HorizontalDivider(
                    color =
                        DividerColor
                )

                Spacer(
                    modifier =
                        Modifier.height(14.dp)
                )

                Row(
                    modifier =
                        Modifier.fillMaxWidth(),

                    horizontalArrangement =
                        Arrangement.spacedBy(8.dp)
                ) {

                    SubscriptionStat(
                        title = "Plan",
                        value =
                            plan ?: "Premium",
                        modifier =
                            Modifier.weight(1f)
                    )

                    SubscriptionStat(
                        title = "Days Left",
                        value =
                            daysLeft.toString(),
                        modifier =
                            Modifier.weight(1f)
                    )
                }

                if (
                    !startsAt.isNullOrBlank() ||
                    !expiresAt.isNullOrBlank()
                ) {

                    Spacer(
                        modifier =
                            Modifier.height(10.dp)
                    )

                    Row(
                        modifier =
                            Modifier.fillMaxWidth(),

                        horizontalArrangement =
                            Arrangement.spacedBy(8.dp)
                    ) {

                        if (
                            !startsAt.isNullOrBlank()
                        ) {

                            SubscriptionStat(
                                title = "Started",
                                value =
                                    formatPremiumDate(
                                        startsAt
                                    ),
                                modifier =
                                    Modifier.weight(1f)
                            )
                        }

                        if (
                            !expiresAt.isNullOrBlank()
                        ) {

                            SubscriptionStat(
                                title = "Expires",
                                value =
                                    formatPremiumDate(
                                        expiresAt
                                    ),
                                modifier =
                                    Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            // =====================================================
            // PENDING MESSAGE
            // =====================================================

            if (isPending) {

                Spacer(
                    modifier =
                        Modifier.height(13.dp)
                )

                Text(
                    text =
                        "Please do not submit another premium request while this one is pending.",

                    color =
                        BhaktMaroonDark,

                    fontSize = 10.sp,

                    lineHeight =
                        16.sp
                )
            }
        }
    }
}

// =================================================================
// SUBSCRIPTION STAT
// =================================================================

@Composable
private fun SubscriptionStat(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .clip(
                RoundedCornerShape(12.dp)
            )
            .background(
                SurfaceBackground.copy(
                    alpha = 0.65f
                )
            )
            .padding(
                horizontal = 11.dp,
                vertical = 9.dp
            )
    ) {

        Text(
            text = title,
            color = TextMuted,
            fontSize = 9.sp
        )

        Spacer(
            modifier =
                Modifier.height(3.dp)
        )

        Text(
            text = value,
            color = TextPrimary,
            fontSize = 11.sp,
            fontWeight =
                FontWeight.SemiBold,
            maxLines = 2,
            overflow =
                TextOverflow.Ellipsis
        )
    }
}

// =================================================================
// BENEFITS
// =================================================================

@Composable
private fun PremiumBenefitsCard() {

    Card(
        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(20.dp),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    SurfaceBackground
            ),

        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 0.dp
            )
    ) {

        Column(
            modifier =
                Modifier.padding(18.dp)
        ) {

            Text(
                text =
                    "Why choose Premium?",

                color =
                    TextPrimary,

                fontSize =
                    16.sp,

                fontWeight =
                    FontWeight.SemiBold
            )

            Spacer(
                modifier =
                    Modifier.height(4.dp)
            )

            Text(
                text =
                    "Premium is designed to make meaningful connections easier.",

                color =
                    TextMuted,

                fontSize =
                    10.sp,

                lineHeight =
                    16.sp
            )

            Spacer(
                modifier =
                    Modifier.height(15.dp)
            )

            PremiumBenefitRow(
                icon =
                    Icons.Outlined.FavoriteBorder,

                title =
                    "Send Interest Requests",

                description =
                    "Express genuine interest and start a meaningful connection."
            )

            PremiumBenefitDivider()

            PremiumBenefitRow(
                icon =
                    Icons.Outlined.AutoAwesome,

                title =
                    "Premium Experience",

                description =
                    "Enjoy a more focused and elevated matchmaking experience."
            )

            PremiumBenefitDivider()

            PremiumBenefitRow(
                icon =
                    Icons.Outlined.Security,

                title =
                    "Connect with Confidence",

                description =
                    "Take your next step thoughtfully and respectfully."
            )
        }
    }
}

// =================================================================
// BENEFIT ROW
// =================================================================

@Composable
private fun PremiumBenefitRow(
    icon: ImageVector,
    title: String,
    description: String
) {

    Row(
        modifier =
            Modifier.fillMaxWidth(),

        verticalAlignment =
            Alignment.Top
    ) {

        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(
                    RoundedCornerShape(11.dp)
                )
                .background(
                    PremiumGoldLight
                ),

            contentAlignment =
                Alignment.Center
        ) {

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = PremiumGoldDark,
                modifier =
                    Modifier.size(19.dp)
            )
        }

        Spacer(
            modifier =
                Modifier.width(11.dp)
        )

        Column(
            modifier =
                Modifier.weight(1f)
        ) {

            Text(
                text = title,
                color = TextPrimary,
                fontSize = 13.sp,
                fontWeight =
                    FontWeight.SemiBold
            )

            Spacer(
                modifier =
                    Modifier.height(3.dp)
            )

            Text(
                text = description,
                color = TextSecondary,
                fontSize = 10.sp,
                lineHeight =
                    16.sp
            )
        }
    }
}

// =================================================================
// BENEFIT DIVIDER
// =================================================================

@Composable
private fun PremiumBenefitDivider() {

    Spacer(
        modifier =
            Modifier.height(12.dp)
    )

    androidx.compose.material3.HorizontalDivider(
        color =
            DividerColor
    )

    Spacer(
        modifier =
            Modifier.height(12.dp)
    )
}

// =================================================================
// PLANS HEADER
// =================================================================

@Composable
private fun PlansHeader() {

    Column(
        modifier =
            Modifier.padding(
                horizontal = 2.dp
            )
    ) {

        Text(
            text =
                "Choose your plan",

            color =
                TextPrimary,

            fontSize =
                17.sp,

            fontWeight =
                FontWeight.SemiBold
        )

        Spacer(
            modifier =
                Modifier.height(3.dp)
        )

        Text(
            text =
                "Select the membership that suits your journey.",

            color =
                TextMuted,

            fontSize =
                10.sp
        )
    }
}

// =================================================================
// PLAN CARD
// =================================================================

@Composable
private fun PremiumPlanCard(
    name: String,
    price: Any,
    durationDays: Int,
    selected: Boolean,
    isPremium: Boolean,
    isPending: Boolean,
    isSubmitting: Boolean,
    onClick: () -> Unit
) {

    val cardColor =
        when {

            isPending ->
                BhaktMaroonLight

            selected ->
                PremiumGoldLight

            else ->
                SurfaceBackground
        }

    val borderColor =
        when {

            isPending ->
                BhaktMaroon

            selected ->
                PremiumGold

            else ->
                BorderColor
        }

    val clickable =
        !isPremium &&
                !isSubmitting

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width =
                    if (
                        selected ||
                        isPending
                    ) {
                        1.5.dp
                    } else {
                        1.dp
                    },

                color =
                    borderColor,

                shape =
                    RoundedCornerShape(20.dp)
            )
            .clickable(
                enabled =
                    clickable,

                onClick =
                    onClick
            ),

        shape =
            RoundedCornerShape(20.dp),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    cardColor
            ),

        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 0.dp
            )
    ) {

        Column(
            modifier =
                Modifier.padding(18.dp)
        ) {

            Row(
                modifier =
                    Modifier.fillMaxWidth(),

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Column(
                    modifier =
                        Modifier.weight(1f)
                ) {

                    Row(
                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        Text(
                            text = name,
                            color = TextPrimary,
                            fontSize = 16.sp,
                            fontWeight =
                                FontWeight.SemiBold
                        )

                        if (selected) {

                            Spacer(
                                modifier =
                                    Modifier.width(7.dp)
                            )

                            Box(
                                modifier = Modifier
                                    .clip(
                                        RoundedCornerShape(8.dp)
                                    )
                                    .background(
                                        PremiumGold
                                    )
                                    .padding(
                                        horizontal = 7.dp,
                                        vertical = 4.dp
                                    )
                            ) {

                                Text(
                                    text = "Selected",
                                    color = TextOnGold,
                                    fontSize = 8.sp,
                                    fontWeight =
                                        FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(
                        modifier =
                            Modifier.height(5.dp)
                    )

                    Text(
                        text =
                            "$durationDays days",

                        color =
                            TextSecondary,

                        fontSize =
                            11.sp
                    )
                }

                Column(
                    horizontalAlignment =
                        Alignment.End
                ) {

                    Text(
                        text =
                            "₹$price",

                        color =
                            if (isPending) {
                                BhaktMaroon
                            } else if (selected) {
                                PremiumGoldDark
                            } else {
                                BhaktMaroon
                            },

                        fontSize =
                            21.sp,

                        fontWeight =
                            FontWeight.Bold
                    )

                    Text(
                        text = "membership",
                        color = TextMuted,
                        fontSize = 8.sp
                    )
                }
            }

            Spacer(
                modifier =
                    Modifier.height(14.dp)
            )

            androidx.compose.material3.HorizontalDivider(
                color =
                    DividerColor
            )

            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )

            Row(
                modifier =
                    Modifier.fillMaxWidth(),

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Icon(
                    imageVector =
                        when {

                            isPremium ->
                                Icons.Outlined.Verified

                            isPending ->
                                Icons.Outlined.Lock

                            selected ->
                                Icons.Outlined.CheckCircle

                            else ->
                                Icons.Outlined.Star
                        },

                    contentDescription =
                        null,

                    tint =
                        when {

                            isPremium ->
                                PremiumGoldDark

                            isPending ->
                                BhaktMaroon

                            else ->
                                PremiumGoldDark
                        },

                    modifier =
                        Modifier.size(17.dp)
                )

                Spacer(
                    modifier =
                        Modifier.width(7.dp)
                )

                Text(
                    text =
                        when {

                            isPremium ->
                                "Premium Active"

                            isPending ->
                                "Request Pending"

                            isSubmitting ->
                                "Submitting..."

                            selected ->
                                "Continue with this plan"

                            else ->
                                "Select this plan"
                        },

                    color =
                        if (isPending) {
                            BhaktMaroon
                        } else {
                            TextPrimary
                        },

                    fontSize =
                        11.sp,

                    fontWeight =
                        FontWeight.SemiBold,

                    modifier =
                        Modifier.weight(1f)
                )

                if (
                    !isPremium &&
                    !isSubmitting
                ) {

                    Text(
                        text = "›",
                        color =
                            BhaktMaroon,
                        fontSize = 21.sp,
                        fontWeight =
                            FontWeight.Light
                    )
                }
            }
        }
    }
}

// =================================================================
// TRUST FOOTER
// =================================================================

@Composable
private fun PremiumTrustFooter() {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 10.dp,
                vertical = 8.dp
            ),

        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(
                    BhaktMaroonLight
                ),

            contentAlignment =
                Alignment.Center
        ) {

            Icon(
                imageVector =
                    Icons.Outlined.Security,

                contentDescription =
                    null,

                tint =
                    BhaktMaroon,

                modifier =
                    Modifier.size(20.dp)
            )
        }

        Spacer(
            modifier =
                Modifier.height(9.dp)
        )

        Text(
            text =
                "A meaningful connection starts with trust.",

            color =
                BhaktMaroonDark,

            fontSize =
                12.sp,

            fontWeight =
                FontWeight.SemiBold,

            fontFamily =
                FontFamily.Serif,

            textAlign =
                TextAlign.Center
        )

        Spacer(
            modifier =
                Modifier.height(4.dp)
        )

        Text(
            text =
                "Premium membership • Bhakt Milan",

            color =
                TextMuted,

            fontSize =
                9.sp,

            textAlign =
                TextAlign.Center
        )
    }
}
