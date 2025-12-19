package com.example.bhaktmilan.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen(
    onSendOtp: (String) -> Unit,
    onCreateAccountClick: () -> Unit
) {
    var mobile by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Login",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Login using your mobile number",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = mobile,
            onValueChange = {
                if (it.length <= 10 && it.all { ch -> ch.isDigit() }) {
                    mobile = it
                }
            },
            label = { Text("Mobile Number") },
            placeholder = { Text("Enter 10 digit number") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { onSendOtp(mobile) },
            modifier = Modifier.fillMaxWidth(),
            enabled = mobile.length == 10
        ) {
            Text("Login")
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = onCreateAccountClick,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Create New Account")
        }
    }
}
