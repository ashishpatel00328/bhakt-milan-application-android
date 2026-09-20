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
import androidx.compose.material.icons.outlined.Phone
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
import com.infravo.bhaktmilan.R
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
        // CENTER LOGIN AREA
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
                modifier = Modifier
                    .fillMaxWidth(),
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
                        .padding(24.dp)
                ) {

                    // =================================================
                    // TITLE
                    // =================================================

                    Text(
                        text = "Welcome back",
                        color = TextPrimary,
                        fontSize = 23.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(
                        modifier = Modifier.height(7.dp)
                    )

                    Text(
                        text = "Continue your journey towards a meaningful connection.",
                        color = TextSecondary,
                        fontSize = 13.sp,
                        lineHeight = 19.sp
                    )

                    Spacer(
                        modifier = Modifier.height(24.dp)
                    )

                    // =================================================
                    // MOBILE NUMBER
                    // =================================================

                    Text(
                        text = "Mobile Number",
                        color = TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    OutlinedTextField(
                        value = mobile,
                        onValueChange = { value ->

                            if (
                                value.length <= 10 &&
                                value.all(Char::isDigit)
                            ) {
                                mobile = value
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Phone
                        ),
                        leadingIcon = {

                            Icon(
                                imageVector = Icons.Outlined.Phone,
                                contentDescription = null,
                                tint = BhaktMaroon
                            )
                        },
                        prefix = {

                            Text(
                                text = "+91 ",
                                color = TextPrimary,
                                fontWeight = FontWeight.SemiBold
                            )
                        },
                        label = {
                            Text("Mobile Number")
                        },
                        placeholder = {
                            Text("Mobile Number")
                        }
                    )

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Text(
                        text = "We'll send a one-time verification code to this number.",
                        color = TextMuted,
                        fontSize = 11.sp,
                        lineHeight = 16.sp
                    )

                    Spacer(
                        modifier = Modifier.height(24.dp)
                    )

                    // =================================================
                    // CONTINUE BUTTON
                    // =================================================

                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        enabled =
                            mobile.length == 10 &&
                                    !uiState.isLoading,
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BhaktMaroon,
                            disabledContainerColor = BhaktMaroonLight
                        ),
                        onClick = {
                            viewModel.sendOtp(mobile)
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
                                text = "Continue with OTP",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(
                        modifier = Modifier.height(20.dp)
                    )

                    // =================================================
                    // SECURE DIVIDER
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
                            text = "  Secure & simple  ",
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
                text = "Your journey to a meaningful relationship begins here.",
                color = TextSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text = "By continuing, you agree to our Terms & Privacy Policy.",
                color = TextMuted,
                fontSize = 10.sp
            )
        }
    }
}