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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.infravo.bhaktmilan.ui.navigation.AppRoutes
import com.infravo.bhaktmilan.ui.viewmodel.AuthViewModel

@Composable
fun OtpScreen(
    mobile: String,
    navController: NavHostController,
    viewModel: AuthViewModel = hiltViewModel()
) {

    var otp by remember {
        mutableStateOf("")
    }

    val uiState by viewModel.uiState.collectAsState()

    val context = LocalContext.current

    LaunchedEffect(uiState.loginSuccess) {

        if (!uiState.loginSuccess) return@LaunchedEffect

        when {

            uiState.isNewUser -> {

                navController.navigate(AppRoutes.ONBOARDING) {
                    popUpTo(AppRoutes.LOGIN) {
                        inclusive = true
                    }
                }
            }

            !uiState.isProfileCompleted -> {

                navController.navigate(AppRoutes.ONBOARDING) {
                    popUpTo(AppRoutes.LOGIN) {
                        inclusive = true
                    }
                }
            }

            else -> {

                navController.navigate(AppRoutes.HOME) {
                    popUpTo(AppRoutes.LOGIN) {
                        inclusive = true
                    }
                }
            }
        }

        viewModel.clearState()
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
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Verify OTP",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "OTP sent to +91 $mobile",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = otp,
            onValueChange = {

                if (it.length <= 6 && it.all(Char::isDigit)) {
                    otp = it
                }

            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            label = {
                Text("Enter OTP")
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            enabled = otp.length == 6 && !uiState.isLoading,
            onClick = {

                viewModel.verifyOtp(
                    mobile = mobile,
                    otp = otp
                )
            }
        ) {

            if (uiState.isLoading) {

                CircularProgressIndicator()

            } else {

                Text("Verify OTP")
            }
        }
    }
}