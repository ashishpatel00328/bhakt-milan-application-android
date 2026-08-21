package com.infravo.bhaktmilan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.infravo.bhaktmilan.ui.navigation.AppRoutes
import com.infravo.bhaktmilan.ui.navigation.AppNavHost
import com.infravo.bhaktmilan.ui.navigation.BottomBar
import com.infravo.bhaktmilan.ui.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val navController = rememberNavController()

            val navBackStackEntry by navController.currentBackStackEntryAsState()

            val currentRoute = navBackStackEntry?.destination?.route

            val showBottomBar = currentRoute in listOf(
                AppRoutes.HOME,
                AppRoutes.REQUESTS,
                AppRoutes.SHORTLIST,
                AppRoutes.MY_PROFILE
            )

            // V1 Authentication Flow
            // Splash screen & Token check will be added later.
            val startDestination = AppRoutes.SPLASH

            Scaffold(

                bottomBar = {

                    if (showBottomBar) {
                        BottomBar(navController)
                    }

                }

            ) { paddingValues ->

                AppNavHost(
                    navController = navController,
                    startDestination = startDestination,
                    userViewModel = userViewModel,
                    modifier = Modifier.padding(paddingValues)
                )

            }
        }
    }
}