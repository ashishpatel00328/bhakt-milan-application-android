package com.example.bhaktmilan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Scaffold
import androidx.navigation.compose.rememberNavController
import com.example.bhaktmilan.navigation.AppRoutes
import com.example.bhaktmilan.ui.navigation.AppNavHost
import com.example.bhaktmilan.ui.navigation.BottomBar
import com.example.bhaktmilan.ui.theme.BhaktMilanTheme
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = getSharedPreferences("bhakt_milan_prefs", MODE_PRIVATE)
        val isOnboardingDone = prefs.getBoolean("onboarding_done", false)

        setContent {
            val navController = rememberNavController()

            Scaffold(
                bottomBar = {
                    BottomBar(navController)
                }
            ) { paddingValues ->

                AppNavHost(
                    navController = navController,
                    startDestination = AppRoutes.HOME,
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }


    }
}
