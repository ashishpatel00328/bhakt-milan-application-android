package com.infravo.bhaktmilan.ui.screens.premium

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun PremiumPendingDialog(
    onDismiss: () -> Unit
) {

    AlertDialog(

        onDismissRequest = {
            onDismiss()
        },

        title = {
            Text(
                text = "Premium Request Pending",
                style = MaterialTheme.typography.titleLarge
            )
        },

        text = {
            Text(
                text =
                    "Your premium request has already been submitted " +
                            "and is waiting for admin approval.\n\n" +
                            "Please wait for approval. You do not need to " +
                            "submit another premium request."
            )
        },

        confirmButton = {

            TextButton(
                onClick = {
                    onDismiss()
                }
            ) {
                Text("OK")
            }
        }
    )
}