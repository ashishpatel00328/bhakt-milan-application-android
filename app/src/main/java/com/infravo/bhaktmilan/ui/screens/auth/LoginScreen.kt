package com.infravo.bhaktmilan.ui.screens.auth

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.infravo.bhaktmilan.ui.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    onSendOtp: (String) -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {

    var mobile by remember {
        mutableStateOf("")
    }

    val uiState by viewModel.uiState.collectAsState()

    val context = LocalContext.current

    LaunchedEffect(uiState.otpSent) {

        if (uiState.otpSent) {

            onSendOtp(mobile)

            viewModel.clearState()
        }
    }

    LaunchedEffect(uiState.error) {

        uiState.error?.let {

            Toast.makeText(
                context,
                it,
                Toast.LENGTH_SHORT
            ).show()

            viewModel.clearState()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Welcome to BhaktMilan",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Login with your mobile number",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = mobile,
            onValueChange = {

                if (it.length <= 10 && it.all(Char::isDigit)) {
                    mobile = it
                }

            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            label = {
                Text("Mobile Number")
            },
            placeholder = {
                Text("Enter 10 digit mobile number")
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            enabled = mobile.length == 10 && !uiState.isLoading,
            onClick = {
                viewModel.sendOtp(mobile)
            }
        ) {

            if (uiState.isLoading) {

                CircularProgressIndicator()

            } else {

                Text("Send OTP")

            }
        }
    }
}