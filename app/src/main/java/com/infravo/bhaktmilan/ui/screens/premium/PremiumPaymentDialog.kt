package com.infravo.bhaktmilan.ui.screens.premium

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.infravo.bhaktmilan.data.remote.response.PremiumPlan
import com.infravo.bhaktmilan.ui.theme.AppBackground
import com.infravo.bhaktmilan.ui.theme.BhaktMaroon
import com.infravo.bhaktmilan.ui.theme.BhaktMaroonLight
import com.infravo.bhaktmilan.ui.theme.BorderColor
import com.infravo.bhaktmilan.ui.theme.DividerColor
import com.infravo.bhaktmilan.ui.theme.PremiumGold
import com.infravo.bhaktmilan.ui.theme.PremiumGoldLight
import com.infravo.bhaktmilan.ui.theme.SurfaceBackground
import com.infravo.bhaktmilan.ui.theme.TextOnPrimary
import com.infravo.bhaktmilan.ui.theme.TextPrimary
import com.infravo.bhaktmilan.ui.theme.TextSecondary

@Composable
fun PremiumPaymentDialog(
    plans: List<PremiumPlan>,
    selectedPlanId: Int?,
    isSubmitting: Boolean,
    onPlanSelected: (Int) -> Unit,
    onPremiumRequest: () -> Unit,
    onDismiss: () -> Unit
) {

    val selectedPlan =
        plans.firstOrNull {
            it.id == selectedPlanId
        }

    AlertDialog(
        onDismissRequest = {
            if (!isSubmitting) {
                onDismiss()
            }
        },
        containerColor = SurfaceBackground,
        shape = RoundedCornerShape(28.dp),

        title = {

            Column(
                verticalArrangement =
                    Arrangement.spacedBy(6.dp)
            ) {

                Text(
                    text = "Premium Membership",
                    style =
                        MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                    color = TextPrimary
                )

                Text(
                    text =
                        "Choose a plan and complete your premium membership.",
                    style =
                        MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }
        },

        text = {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(
                        rememberScrollState()
                    ),
                verticalArrangement =
                    Arrangement.spacedBy(14.dp)
            ) {

                // ==========================================
                // PLAN SELECTION
                // ==========================================

                DialogSectionHeader(
                    number = "01",
                    title = "Choose Your Plan",
                    subtitle =
                        "Select the membership plan you prefer."
                )

                Column(
                    verticalArrangement =
                        Arrangement.spacedBy(10.dp)
                ) {

                    plans.forEach { plan ->

                        PlanOption(
                            plan = plan,
                            selected =
                                plan.id == selectedPlanId,
                            enabled =
                                !isSubmitting,
                            onClick = {
                                onPlanSelected(plan.id)
                            }
                        )
                    }
                }

                // ==========================================
                // SELECTED PLAN
                // ==========================================

                selectedPlan?.let { plan ->

                    SelectedPlanCard(
                        plan = plan
                    )

                    // ======================================
                    // PAYMENT
                    // ======================================

                    DialogSectionHeader(
                        number = "02",
                        title = "Make Payment",
                        subtitle =
                            "Scan the QR code and pay the selected amount."
                    )

                    PaymentCard(
                        plan = plan
                    )

                    // ======================================
                    // AFTER PAYMENT
                    // ======================================

                    DialogSectionHeader(
                        number = "03",
                        title = "After Payment",
                        subtitle =
                            "Complete these steps after making the payment."
                    )

                    AfterPaymentCard()

                    // ======================================
                    // NOTE
                    // ======================================

                    NoteCard()
                }
            }
        },

        confirmButton = {

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                enabled =
                    selectedPlanId != null &&
                            !isSubmitting,
                onClick = {
                    onPremiumRequest()
                },
                shape = RoundedCornerShape(15.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BhaktMaroon,
                    contentColor = TextOnPrimary,
                    disabledContainerColor =
                        BhaktMaroon.copy(alpha = 0.45f),
                    disabledContentColor =
                        TextOnPrimary.copy(alpha = 0.8f)
                )
            ) {

                if (isSubmitting) {

                    CircularProgressIndicator(
                        modifier =
                            Modifier.size(20.dp),
                        color = TextOnPrimary,
                        strokeWidth = 2.dp
                    )

                } else {

                    Text(
                        text = "Premium Request",
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        },

        dismissButton = {

            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = !isSubmitting,
                onClick = {
                    onDismiss()
                },
                shape = RoundedCornerShape(15.dp),
                border = BorderStroke(
                    1.dp,
                    BorderColor
                )
            ) {

                Text(
                    text = "Cancel",
                    color = BhaktMaroon,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    )
}


// =========================================================
// PLAN OPTION
// =========================================================

@Composable
private fun PlanOption(
    plan: PremiumPlan,
    selected: Boolean,
    enabled: Boolean,
    onClick: () -> Unit
) {

    val backgroundColor =
        if (selected) {
            BhaktMaroonLight
        } else {
            AppBackground
        }

    val borderColor =
        if (selected) {
            BhaktMaroon
        } else {
            DividerColor
        }

    OutlinedButton(
        modifier = Modifier
            .fillMaxWidth(),
        enabled = enabled,
        onClick = onClick,
        shape = RoundedCornerShape(17.dp),
        border = BorderStroke(
            if (selected) 1.5.dp else 1.dp,
            borderColor
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = backgroundColor,
            contentColor = TextPrimary
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = 3.dp
                ),
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Column(
                modifier =
                    Modifier.weight(1f),
                verticalArrangement =
                    Arrangement.spacedBy(3.dp),
                horizontalAlignment =
                    Alignment.Start
            ) {

                Text(
                    text = plan.name,
                    style =
                        MaterialTheme.typography.bodyLarge,
                    color =
                        if (selected) {
                            BhaktMaroon
                        } else {
                            TextPrimary
                        },
                    fontWeight =
                        FontWeight.SemiBold
                )

                Text(
                    text =
                        "${plan.duration_days} days",
                    style =
                        MaterialTheme.typography.labelSmall,
                    color = TextSecondary
                )
            }

            Text(
                text =
                    "₹${plan.price}",
                style =
                    MaterialTheme.typography.titleMedium.copy(
                        fontWeight =
                            FontWeight.Bold
                    ),
                color =
                    if (selected) {
                        BhaktMaroon
                    } else {
                        PremiumGold
                    }
            )

            if (selected) {

                Spacer(
                    modifier =
                        Modifier.width(10.dp)
                )

                Box(
                    modifier = Modifier
                        .size(25.dp)
                        .clip(CircleShape)
                        .background(BhaktMaroon),
                    contentAlignment =
                        Alignment.Center
                ) {

                    Text(
                        text = "✓",
                        color = TextOnPrimary,
                        style =
                            MaterialTheme.typography.labelSmall,
                        fontWeight =
                            FontWeight.Bold
                    )
                }
            }
        }
    }
}


// =========================================================
// SELECTED PLAN
// =========================================================

@Composable
private fun SelectedPlanCard(
    plan: PremiumPlan
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(PremiumGoldLight)
            .padding(15.dp)
    ) {

        Row(
            modifier =
                Modifier.fillMaxWidth(),
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Column(
                modifier =
                    Modifier.weight(1f),
                verticalArrangement =
                    Arrangement.spacedBy(3.dp)
            ) {

                Text(
                    text = "Selected Plan",
                    style =
                        MaterialTheme.typography.labelSmall,
                    color = TextSecondary
                )

                Text(
                    text = plan.name,
                    style =
                        MaterialTheme.typography.titleMedium,
                    color = TextPrimary,
                    fontWeight =
                        FontWeight.SemiBold
                )

                Text(
                    text =
                        "${plan.duration_days} days membership",
                    style =
                        MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }

            Text(
                text =
                    "₹${plan.price}",
                style =
                    MaterialTheme.typography.titleLarge.copy(
                        fontWeight =
                            FontWeight.Bold
                    ),
                color = BhaktMaroon
            )
        }
    }
}


// =========================================================
// PAYMENT CARD
// =========================================================

@Composable
private fun PaymentCard(
    plan: PremiumPlan
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(AppBackground)
            .padding(15.dp)
    ) {

        Column(
            modifier =
                Modifier.fillMaxWidth(),
            horizontalAlignment =
                Alignment.CenterHorizontally,
            verticalArrangement =
                Arrangement.spacedBy(10.dp)
        ) {

            Text(
                text = "Payment Amount",
                style =
                    MaterialTheme.typography.labelMedium,
                color = TextSecondary
            )

            Text(
                text = "₹${plan.price}",
                style =
                    MaterialTheme.typography.headlineMedium.copy(
                        fontWeight =
                            FontWeight.Bold
                    ),
                color = BhaktMaroon
            )

            Box(
                modifier = Modifier
                    .size(220.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(SurfaceBackground)
                    .padding(8.dp)
            ) {

                Image(
                    painter = painterResource(
                        id =
                            PremiumPaymentConfig
                                .QR_IMAGE_RES
                    ),
                    contentDescription =
                        "Premium payment QR",
                    modifier =
                        Modifier.fillMaxWidth(),
                    contentScale =
                        ContentScale.Fit
                )
            }

            Text(
                text =
                    "Scan the QR code and pay ₹${plan.price}.",
                style =
                    MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
        }
    }
}


// =========================================================
// AFTER PAYMENT
// =========================================================

@Composable
private fun AfterPaymentCard() {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(SurfaceBackground)
            .padding(15.dp)
    ) {

        Column(
            verticalArrangement =
                Arrangement.spacedBy(14.dp)
        ) {

            PaymentStep(
                number = "1",
                title = "Complete the payment",
                description =
                    "Pay the amount shown for your selected plan."
            )

            HorizontalDivider(
                color = DividerColor
            )

            PaymentStep(
                number = "2",
                title = "Send payment screenshot",
                description =
                    "Send your payment screenshot on WhatsApp to " +
                            PremiumPaymentConfig
                                .WHATSAPP_NUMBER
            )

            HorizontalDivider(
                color = DividerColor
            )

            PaymentStep(
                number = "3",
                title = "Wait for verification",
                description =
                    "After manual verification, the admin will approve your premium request."
            )
        }
    }
}


// =========================================================
// PAYMENT STEP
// =========================================================

@Composable
private fun PaymentStep(
    number: String,
    title: String,
    description: String
) {

    Row(
        modifier =
            Modifier.fillMaxWidth(),
        horizontalArrangement =
            Arrangement.spacedBy(10.dp),
        verticalAlignment =
            Alignment.Top
    ) {

        Box(
            modifier = Modifier
                .size(30.dp)
                .clip(CircleShape)
                .background(BhaktMaroon),
            contentAlignment =
                Alignment.Center
        ) {

            Text(
                text = number,
                color = TextOnPrimary,
                style =
                    MaterialTheme.typography.labelSmall,
                fontWeight =
                    FontWeight.Bold
            )
        }

        Column(
            modifier =
                Modifier.weight(1f),
            verticalArrangement =
                Arrangement.spacedBy(3.dp)
        ) {

            Text(
                text = title,
                style =
                    MaterialTheme.typography.bodyMedium,
                color = TextPrimary,
                fontWeight =
                    FontWeight.SemiBold
            )

            Text(
                text = description,
                style =
                    MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
        }
    }
}


// =========================================================
// NOTE CARD
// =========================================================

@Composable
private fun NoteCard() {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(PremiumGoldLight)
            .padding(14.dp)
    ) {

        Row(
            horizontalArrangement =
                Arrangement.spacedBy(10.dp),
            verticalAlignment =
                Alignment.Top
        ) {

            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(PremiumGold),
                contentAlignment =
                    Alignment.Center
            ) {

                Text(
                    text = "!",
                    color = SurfaceBackground,
                    style =
                        MaterialTheme.typography.labelMedium,
                    fontWeight =
                        FontWeight.Bold
                )
            }

            Column(
                modifier =
                    Modifier.weight(1f),
                verticalArrangement =
                    Arrangement.spacedBy(3.dp)
            ) {

                Text(
                    text = "Important",
                    style =
                        MaterialTheme.typography.labelMedium,
                    color = TextPrimary,
                    fontWeight =
                        FontWeight.SemiBold
                )

                Text(
                    text =
                        "Please make the payment only for the selected plan and send the payment screenshot after payment.",
                    style =
                        MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }
        }
    }
}


// =========================================================
// SECTION HEADER
// =========================================================

@Composable
private fun DialogSectionHeader(
    number: String,
    title: String,
    subtitle: String
) {

    Row(
        modifier =
            Modifier.fillMaxWidth(),
        horizontalArrangement =
            Arrangement.spacedBy(10.dp),
        verticalAlignment =
            Alignment.Top
    ) {

        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(BhaktMaroonLight),
            contentAlignment =
                Alignment.Center
        ) {

            Text(
                text = number,
                style =
                    MaterialTheme.typography.labelSmall,
                color = BhaktMaroon,
                fontWeight =
                    FontWeight.Bold
            )
        }

        Column(
            modifier =
                Modifier.weight(1f),
            verticalArrangement =
                Arrangement.spacedBy(2.dp)
        ) {

            Text(
                text = title,
                style =
                    MaterialTheme.typography.titleSmall,
                color = TextPrimary,
                fontWeight =
                    FontWeight.SemiBold
            )

            Text(
                text = subtitle,
                style =
                    MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
        }
    }
}