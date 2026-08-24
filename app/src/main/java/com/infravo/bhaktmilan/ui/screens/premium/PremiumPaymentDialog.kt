package com.infravo.bhaktmilan.ui.screens.premium

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.infravo.bhaktmilan.data.remote.response.PremiumPlan

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

        title = {
            Text(
                text = "Premium Membership"
            )
        },

        text = {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(
                        rememberScrollState()
                    ),
                verticalArrangement =
                    Arrangement.spacedBy(12.dp)
            ) {

                // ==========================================
                // Information
                // ==========================================

                Text(
                    text =
                        "Select a premium plan to continue."
                )

                // ==========================================
                // Plans
                // ==========================================

                plans.forEach { plan ->

                    OutlinedButton(
                        modifier =
                            Modifier.fillMaxWidth(),

                        enabled =
                            !isSubmitting,

                        onClick = {

                            onPlanSelected(
                                plan.id
                            )
                        }
                    ) {

                        Text(
                            text =
                                if (
                                    plan.id ==
                                    selectedPlanId
                                ) {
                                    "✓ ${plan.name} - ₹${plan.price}"
                                } else {
                                    "${plan.name} - ₹${plan.price}"
                                }
                        )
                    }
                }

                // ==========================================
                // Selected Plan
                // ==========================================

                selectedPlan?.let { plan ->

                    Spacer(
                        modifier = Modifier.height(4.dp)
                    )

                    Text(
                        text =
                            "Selected Plan",
                        style =
                            MaterialTheme.typography.titleMedium
                    )

                    Text(
                        text =
                            plan.name
                    )

                    Text(
                        text =
                            "Amount: ₹${plan.price}"
                    )

                    Text(
                        text =
                            "Duration: ${plan.duration_days} days"
                    )

                    Spacer(
                        modifier = Modifier.height(4.dp)
                    )

                    // ======================================
                    // Payment
                    // ======================================

                    Text(
                        text =
                            "Payment",
                        style =
                            MaterialTheme.typography.titleMedium
                    )

                    Image(
                        painter =
                            painterResource(
                                id =
                                    PremiumPaymentConfig
                                        .QR_IMAGE_RES
                            ),

                        contentDescription =
                            "Premium payment QR",

                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp),

                        contentScale =
                            ContentScale.Fit
                    )

                    Text(
                        text =
                            "Scan the QR code and pay " +
                                    "₹${plan.price}."
                    )

                    // ======================================
                    // WhatsApp
                    // ======================================

                    Text(
                        text =
                            "After payment",
                        style =
                            MaterialTheme.typography.titleMedium
                    )

                    Text(
                        text =
                            "Send the payment screenshot " +
                                    "on WhatsApp to " +
                                    PremiumPaymentConfig
                                        .WHATSAPP_NUMBER
                    )

                    Text(
                        text =
                            "After manual verification, " +
                                    "the admin will approve your " +
                                    "premium request."
                    )
                }
            }
        },

        // ==========================================
        // Request Button
        // ==========================================

        confirmButton = {

            Button(

                enabled =
                    selectedPlanId != null &&
                            !isSubmitting,

                onClick = {

                    onPremiumRequest()
                }
            ) {

                if (isSubmitting) {

                    CircularProgressIndicator(
                        modifier =
                            Modifier.size(20.dp)
                    )

                } else {

                    Text(
                        text =
                            "Premium Request"
                    )
                }
            }
        },

        // ==========================================
        // Cancel
        // ==========================================

        dismissButton = {

            OutlinedButton(
                enabled = !isSubmitting,

                onClick = {
                    onDismiss()
                }
            ) {
                Text("Cancel")
            }
        }
    )
}