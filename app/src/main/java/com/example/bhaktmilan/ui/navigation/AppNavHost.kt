package com.example.bhaktmilan.ui.navigation

import HomeScreen
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.bhaktmilan.navigation.AppRoutes
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.bhaktmilan.data.AuthRepository.checkUserExists
import com.example.bhaktmilan.ui.data.fakeProfiles
import com.example.bhaktmilan.ui.screens.requests.RequestsScreen
import com.example.bhaktmilan.ui.screens.shortlist.ShortlistScreen
import com.example.bhaktmilan.ui.screens.myprofile.MyProfileScreen
import com.example.bhaktmilan.ui.screens.auth.LoginScreen
import com.example.bhaktmilan.ui.screens.auth.OtpScreen
import com.example.bhaktmilan.ui.screens.onboarding.OnboardingScreen


@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String,
    modifier: Modifier = Modifier

) {
    NavHost(
        navController = navController,
        startDestination = "onboarding",
        modifier = modifier
    ) {
//        composable("login") {
//            LoginScreen(
//                onSendOtp = { mobile ->
//                    navController.currentBackStackEntry
//                        ?.savedStateHandle
//                        ?.set("mobile", mobile)
//
//                    navController.navigate("otp")
//                },
//                onCreateAccountClick = {
//                    navController.navigate("onboarding")
//                }
//            )
//        }

        composable("login") {
            LoginScreen(
                onSendOtp = { mobile ->

                    // demo backend response
                    val isNewAccount = checkUserExists(mobile)

                    navController.navigate("otp/$mobile/$isNewAccount")
                },
                onCreateAccountClick = {
                    navController.navigate("onboarding")
                }
            )
        }

        composable(
            route = "otp/{mobile}/{isNew}",
            arguments = listOf(
                navArgument("mobile") { type = NavType.StringType },
                navArgument("isNew") { type = NavType.BoolType }
            )
        ) { backStackEntry ->

            val mobile =
                backStackEntry.arguments?.getString("mobile") ?: ""
            val isNew =
                backStackEntry.arguments?.getBoolean("isNew") ?: false

            OtpScreen(
                mobile = mobile,
                isNewAccount = isNew,
                onOtpVerified = {
                    if (isNew) {
                        navController.navigate("onboarding") {
                            popUpTo("auth") { inclusive = true }
                        }
                    } else {
                        navController.navigate("home") {
                            popUpTo("auth") { inclusive = true }
                        }
                    }
                }
            )
        }


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
        composable(AppRoutes.SHORTLIST) {
            ShortlistScreen()
        }

        composable("onboarding") {
            OnboardingScreen { formData ->
                // later: save to ViewModel / backend
                navController.navigate("home") {
                    popUpTo("login") { inclusive = true }
                }
            }
        }



    }
}
