package com.infravo.bhaktmilan.ui.screens.interactions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SendInterestDialog(
    message: String,
    isLoading: Boolean,
    error: String?,
    onMessageChange: (String) -> Unit,
    onSend: () -> Unit,
    onDismiss: () -> Unit
) {

    AlertDialog(
        onDismissRequest = {
            if (!isLoading) {
                onDismiss()
            }
        },

        title = {
            Text(
                text = "Send Interest",
                style = MaterialTheme.typography.titleLarge
            )
        },

        text = {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),

                verticalArrangement =
                    Arrangement.spacedBy(12.dp)
            ) {

                Text(
                    text =
                        "Write a message to send with your interest."
                )

                OutlinedTextField(
                    value = message,

                    onValueChange = {
                        onMessageChange(it)
                    },

                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(
                            min = 120.dp
                        ),

                    label = {
                        Text("Message")
                    },

                    placeholder = {
                        Text(
                            "Write your message..."
                        )
                    },

                    enabled = !isLoading,

                    singleLine = false,

                    maxLines = 5
                )

                error?.let { errorMessage ->

                    Text(
                        text = errorMessage,
                        color =
                            MaterialTheme.colorScheme.error,
                        style =
                            MaterialTheme.typography.bodySmall
                    )
                }
            }
        },

        confirmButton = {

            Button(

                enabled =
                    message.isNotBlank() &&
                            !isLoading,

                onClick = {
                    onSend()
                }
            ) {

                if (isLoading) {

                    CircularProgressIndicator()

                } else {

                    Text(
                        text = "Send Interest"
                    )
                }
            }
        },

        dismissButton = {

            OutlinedButton(
                enabled = !isLoading,

                onClick = {
                    onDismiss()
                }
            ) {

                Text("Cancel")
            }
        }
    )
}