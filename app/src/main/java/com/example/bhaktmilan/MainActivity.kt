package com.example.bhaktmilan

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.material3.Scaffold



class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = getSharedPreferences("bhakt_milan_prefs", MODE_PRIVATE)
        val isOnboardingDone = prefs.getBoolean("onboarding_done", false)


        setContent {
            val navController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            val showBottomBar = currentRoute in listOf(
                "home",
                "requests",
                "shortlist",
                "myprofile"
            )


            Scaffold(
                bottomBar = {
                    //BottomBar(navController)

                        if (showBottomBar) {
                            BottomBar(navController)
                        }

                }
            ) { paddingValues ->

                AppNavHost(
                    navController = navController,
                    startDestination = AppRoutes.LOGIN,
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }


    }
}
