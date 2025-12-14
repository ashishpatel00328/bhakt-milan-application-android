package com.example.bhaktmilan.ui.screens.onboarding

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.navigation.NavHostController
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight

import android.content.Context



@Composable
fun OnboardingScreen(
    navController: NavHostController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Bhakt Milan",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                val prefs = navController.context
                    .getSharedPreferences("bhakt_milan_prefs", Context.MODE_PRIVATE)

                prefs.edit().putBoolean("onboarding_done", true).apply()

                navController.navigate("home") {
                    popUpTo("onboarding") { inclusive = true }
                }
            }
        ) {
            Text("Get Started")
        }
    }
}

