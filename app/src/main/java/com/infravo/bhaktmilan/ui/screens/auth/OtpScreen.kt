package com.infravo.bhaktmilan.ui.screens.auth

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.infravo.bhaktmilan.R
import com.infravo.bhaktmilan.ui.navigation.AppRoutes
import com.infravo.bhaktmilan.ui.theme.AppBackground
import com.infravo.bhaktmilan.ui.theme.BhaktMaroon
import com.infravo.bhaktmilan.ui.theme.BhaktMaroonLight
import com.infravo.bhaktmilan.ui.theme.BorderColor
import com.infravo.bhaktmilan.ui.theme.SurfaceBackground
import com.infravo.bhaktmilan.ui.theme.TextMuted
import com.infravo.bhaktmilan.ui.theme.TextPrimary
import com.infravo.bhaktmilan.ui.theme.TextSecondary
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

        if (!uiState.loginSuccess) {
            return@LaunchedEffect
        }

        when {

            uiState.isNewUser -> {

                navController.navigate(
                    AppRoutes.ONBOARDING
                ) {
                    popUpTo(AppRoutes.LOGIN) {
                        inclusive = true
                    }
                }
            }

            !uiState.isProfileCompleted -> {

                navController.navigate(
                    AppRoutes.ONBOARDING
                ) {
                    popUpTo(AppRoutes.LOGIN) {
                        inclusive = true
                    }
                }
            }

            else -> {

                navController.navigate(
                    AppRoutes.HOME
                ) {
                    popUpTo(AppRoutes.LOGIN) {
                        inclusive = true
                    }
                }
            }
        }

        viewModel.clearState()
    }

    LaunchedEffect(uiState.error) {

        uiState.error?.let { message ->

            Toast.makeText(
                context,
                message,
                Toast.LENGTH_SHORT
            ).show()

            viewModel.clearState()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
    ) {

        // =====================================================
        // BACKGROUND ARTWORK
        // =====================================================

        Image(
            painter = painterResource(
                id = R.drawable.bhaktmilan_splash
            ),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.18f),
            contentScale = ContentScale.Crop
        )

        // =====================================================
        // CENTER CONTENT
        // =====================================================

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = 20.dp,
                    vertical = 24.dp
                ),
            contentAlignment = Alignment.Center
        ) {

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(26.dp),
                colors = CardDefaults.cardColors(
                    containerColor = SurfaceBackground.copy(
                        alpha = 0.96f
                    )
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 4.dp
                )
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    // =================================================
                    // OTP ICON
                    // =================================================

                    Box(
                        modifier = Modifier
                            .size(58.dp)
                            .background(
                                color = BhaktMaroonLight,
                                shape = RoundedCornerShape(18.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {

                        Icon(
                            imageVector = Icons.Outlined.Lock,
                            contentDescription = null,
                            tint = BhaktMaroon,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(
                        modifier = Modifier.height(18.dp)
                    )

                    // =================================================
                    // TITLE
                    // =================================================

                    Text(
                        text = "Verify your number",
                        color = TextPrimary,
                        fontSize = 23.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(
                        modifier = Modifier.height(7.dp)
                    )

                    Text(
                        text = "Enter the 6-digit verification code sent to",
                        color = TextSecondary,
                        fontSize = 13.sp
                    )

                    Spacer(
                        modifier = Modifier.height(5.dp)
                    )

                    Text(
                        text = "+91 $mobile",
                        color = BhaktMaroon,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(
                        modifier = Modifier.height(24.dp)
                    )

                    // =================================================
                    // OTP FIELD
                    // =================================================

                    OutlinedTextField(
                        value = otp,
                        onValueChange = { value ->

                            if (
                                value.length <= 6 &&
                                value.all(Char::isDigit)
                            ) {
                                otp = value
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        leadingIcon = {

                            Icon(
                                imageVector = Icons.Outlined.Lock,
                                contentDescription = null,
                                tint = BhaktMaroon
                            )
                        },
                        label = {
                            Text("Verification Code")
                        },
                        placeholder = {
                            Text("Enter 6 digit OTP")
                        }
                    )

                    Spacer(
                        modifier = Modifier.height(22.dp)
                    )

                    // =================================================
                    // VERIFY BUTTON
                    // =================================================

                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        enabled =
                            otp.length == 6 &&
                                    !uiState.isLoading,
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BhaktMaroon,
                            disabledContainerColor = BhaktMaroonLight
                        ),
                        onClick = {

                            viewModel.verifyOtp(
                                mobile = mobile,
                                otp = otp
                            )
                        }
                    ) {

                        if (uiState.isLoading) {

                            CircularProgressIndicator(
                                modifier = Modifier.size(21.dp),
                                color = SurfaceBackground,
                                strokeWidth = 2.dp
                            )

                        } else {

                            Text(
                                text = "Verify & Continue",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(
                        modifier = Modifier.height(20.dp)
                    )

                    // =================================================
                    // SECURITY DIVIDER
                    // =================================================

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        HorizontalDivider(
                            modifier = Modifier.weight(1f),
                            color = BorderColor
                        )

                        Text(
                            text = "  Secure verification  ",
                            color = TextMuted,
                            fontSize = 10.sp
                        )

                        HorizontalDivider(
                            modifier = Modifier.weight(1f),
                            color = BorderColor
                        )
                    }
                }
            }
        }

        // =====================================================
        // FOOTER
        // =====================================================

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(
                    horizontal = 20.dp,
                    vertical = 14.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Your information is protected and secure.",
                color = TextSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text = "Bhakt Milan • Secure Login",
                color = TextMuted,
                fontSize = 10.sp
            )
        }
    }
}