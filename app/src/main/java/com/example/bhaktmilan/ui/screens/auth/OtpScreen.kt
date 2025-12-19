package com.example.bhaktmilan.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun OtpScreen(
    mobile: String,
    isNewAccount: Boolean,
    onOtpVerified: (Boolean) -> Unit
) {
    var otp by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Verify OTP", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(8.dp))
        Text("Sent to +91 $mobile")

        Spacer(Modifier.height(24.dp))

        OutlinedTextField(
            value = otp,
            onValueChange = {
                if (it.length <= 6 && it.all { ch -> ch.isDigit() }) {
                    otp = it
                }
            },
            label = { Text("Enter OTP") },
            placeholder = { Text("Enter OTP") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(Modifier.height(16.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                // 🔴 demo OTP always success
                onOtpVerified(isNewAccount)
            }
        ) {
            Text("Verify")
        }
    }
}
