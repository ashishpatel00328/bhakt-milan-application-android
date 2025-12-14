package com.example.bhaktmilan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.bhaktmilan.ui.navigation.AppNavHost
import com.example.bhaktmilan.ui.theme.BhaktMilanTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = getSharedPreferences("bhakt_milan_prefs", MODE_PRIVATE)
        val isOnboardingDone = prefs.getBoolean("onboarding_done", false)

        setContent {
            BhaktMilanTheme {
                val navController = rememberNavController()

                AppNavHost(
                    navController = navController,
                    startDestination = if (isOnboardingDone) "home" else "onboarding"
                )
            }
        }
    }
}
