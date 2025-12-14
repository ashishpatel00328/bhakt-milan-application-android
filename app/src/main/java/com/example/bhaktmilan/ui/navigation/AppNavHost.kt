package com.example.bhaktmilan.ui.navigation

import HomeScreen
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.bhaktmilan.navigation.AppRoutes
import androidx.navigation.NavHostController
import com.example.bhaktmilan.ui.data.fakeProfiles
import com.example.bhaktmilan.ui.screens.requests.RequestsScreen
import com.example.bhaktmilan.ui.screens.shortlist.ShortlistScreen
import com.example.bhaktmilan.ui.screens.myprofile.MyProfileScreen



@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String,
    modifier: Modifier = Modifier

) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {

        composable(AppRoutes.HOME) {
            HomeScreen(
                profiles = fakeProfiles,
                onProfileClick = { profile ->
                    navController.currentBackStackEntry
                        ?.savedStateHandle
                        ?.set("profile", profile)

                    navController.navigate(AppRoutes.PROFILE)
                }
            )
        }

        composable(AppRoutes.PROFILE) {
            MyProfileScreen()
        }

        composable(AppRoutes.REQUESTS) {
            RequestsScreen()
        }
        composable("shortlist") {
            ShortlistScreen()
        }


    }
}
