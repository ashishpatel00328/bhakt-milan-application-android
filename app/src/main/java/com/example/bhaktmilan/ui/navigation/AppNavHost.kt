package com.example.bhaktmilan.ui.navigation

import HomeScreen
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bhaktmilan.ui.screens.onboarding.OnboardingScreen
import com.example.bhaktmilan.navigation.AppRoutes
import androidx.navigation.NavHostController
import com.example.bhaktmilan.ui.data.fakeProfiles
import com.example.bhaktmilan.ui.model.Bhakt
import com.example.bhaktmilan.ui.model.BhaktProfile
import com.example.bhaktmilan.ui.screens.profile.ProfileDetailScreen



@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        composable("home") {
            HomeScreen(
                profiles = fakeProfiles,
                onProfileClick = { profile ->
                    navController.currentBackStackEntry
                        ?.savedStateHandle
                        ?.set("profile", profile)

                    navController.navigate("profile")
                }
            )
        }

        composable("profile") {
            val profile =
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.get<BhaktProfile>("profile")

            profile?.let {
                ProfileDetailScreen(profile = it)
            }
        }




    }
}
