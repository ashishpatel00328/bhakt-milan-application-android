package com.infravo.bhaktmilan.ui.screens.premium

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

private const val TAG = "PremiumDialogs"

/**
 * ------------------------------------------------------------
 * PREMIUM RESPONSE DIALOG
 * ------------------------------------------------------------
 *
 * Used for backend responses such as:
 *
 * 409:
 * "You already have a pending premium request."
 *
 * The dialog is completely controlled by PremiumScreen state.
 */
@Composable
fun PremiumResponseDialog(
    responseCode: Int?,
    message: String,
    onDismiss: () -> Unit
) {
    Log.d(
        TAG,
        """
        PremiumResponseDialog COMPOSED
        responseCode = $responseCode
        message      = $message
        """.trimIndent()
    )

    AlertDialog(
        onDismissRequest = {
            Log.d(
                TAG,
                "Response dialog -> onDismissRequest"
            )

            onDismiss()
        },

        title = {
            Text(
                text = when (responseCode) {
                    409 -> "Request Already Sent"
                    200, 201 -> "Premium Request Sent"
                    else -> "Premium Request"
                }
            )
        },

        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                if (responseCode != null) {
                    Log.d(
                        TAG,
                        "Displaying response code = $responseCode"
                    )

                    Text(
                        text = "Response code: $responseCode",
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyLarge
                )

                if (responseCode == 409) {

                    Log.d(
                        TAG,
                        "409 message detected -> displaying pending explanation"
                    )

                    Text(
                        text = "Your previous premium request is already waiting for admin approval. Please wait until it is reviewed.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        },

        confirmButton = {
            TextButton(
                onClick = {
                    Log.d(
                        TAG,
                        "Response dialog -> OK clicked"
                    )

                    onDismiss()
                }
            ) {
                Text("OK")
            }
        }
    )
}


/**
 * ------------------------------------------------------------
 * PREMIUM PAYMENT DIALOG
 * ------------------------------------------------------------
 *
 * This dialog does NOT directly call the repository.
 *
 * It only calls:
 *
 * onSubscribe()
 *
 * which is connected by PremiumScreen to:
 *
 * viewModel.subscribe()
 */
@Composable
fun PremiumPaymentDialog(
    selectedPlanId: Int?,
    isSubmitting: Boolean,
    onSubscribe: () -> Unit,
    onDismiss: () -> Unit
) {
    Log.d(
        TAG,
        """
        PremiumPaymentDialog COMPOSED
        selectedPlanId = $selectedPlanId
        isSubmitting   = $isSubmitting
        """.trimIndent()
    )

    AlertDialog(
        onDismissRequest = {

            Log.d(
                TAG,
                "Payment dialog -> onDismissRequest"
            )

            if (!isSubmitting) {
                onDismiss()
            } else {
                Log.d(
                    TAG,
                    "Dismiss ignored because subscription request is running"
                )
            }
        },

        title = {
            Text(
                text = "Premium Membership"
            )
        },

        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                Text(
                    text = "You are requesting premium membership."
                )

                if (selectedPlanId != null) {

                    Log.d(
                        TAG,
                        "Payment dialog displaying planId=$selectedPlanId"
                    )

                    Text(
                        text = "Selected plan: $selectedPlanId",
                        style = MaterialTheme.typography.bodyMedium
                    )
                } else {

                    Log.d(
                        TAG,
                        "WARNING: Payment dialog opened without selected plan"
                    )

                    Text(
                        text = "No premium plan is selected.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Text(
                    text = "Your request will be sent to the admin for approval.",
                    style = MaterialTheme.typography.bodyMedium
                )

                if (isSubmitting) {

                    Log.d(
                        TAG,
                        "Payment dialog -> showing submitting indicator"
                    )

                    CircularProgressIndicator(
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        },

        dismissButton = {

            TextButton(
                enabled = !isSubmitting,
                onClick = {

                    Log.d(
                        TAG,
                        "Payment dialog -> Cancel clicked"
                    )

                    onDismiss()
                }
            ) {
                Text("Cancel")
            }
        },

        confirmButton = {

            Button(
                enabled = selectedPlanId != null && !isSubmitting,
                onClick = {

                    Log.d(
                        TAG,
                        """
                        Payment dialog -> SUBSCRIBE CLICKED
                        selectedPlanId=$selectedPlanId
                        isSubmitting=$isSubmitting
                        """.trimIndent()
                    )

                    if (selectedPlanId == null) {

                        Log.d(
                            TAG,
                            "SUBSCRIBE BLOCKED -> selectedPlanId is null"
                        )

                        return@Button
                    }

                    if (isSubmitting) {

                        Log.d(
                            TAG,
                            "SUBSCRIBE BLOCKED -> already submitting"
                        )

                        return@Button
                    }

                    Log.d(
                        TAG,
                        "Calling onSubscribe() -> PremiumViewModel.subscribe()"
                    )

                    onSubscribe()
                }
            ) {
                Text(
                    text = if (isSubmitting) {
                        "Sending..."
                    } else {
                        "Send Request"
                    }
                )
            }
        }
    )
}