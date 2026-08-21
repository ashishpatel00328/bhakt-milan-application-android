package com.infravo.bhaktmilan.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.infravo.bhaktmilan.ui.navigation.AppRoutes
import com.infravo.bhaktmilan.ui.screens.auth.LoginScreen
import com.infravo.bhaktmilan.ui.screens.auth.OtpScreen
import com.infravo.bhaktmilan.ui.screens.home.HomeScreen
import com.infravo.bhaktmilan.ui.screens.myprofile.MyProfileScreen
import com.infravo.bhaktmilan.ui.screens.onboarding.OnboardingScreen
import com.infravo.bhaktmilan.ui.screens.profile.ProfileDetailScreen
import com.infravo.bhaktmilan.ui.screens.requests.RequestsScreen
import com.infravo.bhaktmilan.ui.screens.shortlist.ShortlistScreen
import com.infravo.bhaktmilan.ui.viewmodel.ProfileViewModel
import com.infravo.bhaktmilan.ui.viewmodel.SessionViewModel
import com.infravo.bhaktmilan.ui.viewmodel.UserViewModel

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String,
    userViewModel: UserViewModel,
    modifier: Modifier = Modifier
) {

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {

        // ==========================================
        // Login
        // ==========================================

        composable(AppRoutes.LOGIN) {

            LoginScreen(
                onSendOtp = { mobile ->
                    navController.navigate(
                        "${AppRoutes.OTP}/$mobile"
                    )
                }
            )
        }

        // ==========================================
        // OTP
        // ==========================================

        composable(
            route = "${AppRoutes.OTP}/{mobile}",
            arguments = listOf(
                navArgument("mobile") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->

            val mobile =
                backStackEntry.arguments?.getString("mobile")
                    ?: ""

            OtpScreen(
                mobile = mobile,
                navController = navController
            )
        }

        // ==========================================
        // Home
        // ==========================================

        composable(AppRoutes.HOME) {

            val profileViewModel: ProfileViewModel =
                hiltViewModel()

            val uiState by
            profileViewModel.uiState.collectAsState()

            HomeScreen(
                profiles = uiState.profiles,
                onProfileClick = { profile ->

                    navController.navigate(
                        "${AppRoutes.USER_PROFILE}/${profile.profileId}"
                    )
                }
            )
        }

        // ==========================================
        // User Profile
        // ==========================================

        composable(
            route = "${AppRoutes.USER_PROFILE}/{profileCode}",
            arguments = listOf(
                navArgument("profileCode") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->

            val profileCode =
                backStackEntry.arguments
                    ?.getString("profileCode")
                    ?: ""

            ProfileDetailScreen(
                profileCode = profileCode
            )
        }

        // ==========================================
        // My Profile
        // ==========================================

        composable(AppRoutes.MY_PROFILE) {

            val myProfileViewModel: UserViewModel =
                hiltViewModel()

            val uiState by
            myProfileViewModel.uiState.collectAsState()

            LaunchedEffect(Unit) {
                myProfileViewModel.loadMyProfile()
            }

            MyProfileScreen(
                uiState = uiState,
                onClearError =
                    myProfileViewModel::clearError
            )
        }

        // ==========================================
        // Requests
        // ==========================================

        composable(AppRoutes.REQUESTS) {
            RequestsScreen()
        }

        // ==========================================
        // Shortlist
        // ==========================================

        composable(AppRoutes.SHORTLIST) {
            ShortlistScreen()
        }

        // ==========================================
        // Onboarding
        // ==========================================

        composable(AppRoutes.ONBOARDING) {

            OnboardingScreen(
                onProfileCreated = {

                    navController.navigate(
                        AppRoutes.HOME
                    ) {

                        popUpTo(AppRoutes.LOGIN) {
                            inclusive = true
                        }

                        launchSingleTop = true
                    }
                }
            )
        }

        // ==========================================
        // Splash / Session Check
        // ==========================================

        composable(AppRoutes.SPLASH) {

            val sessionViewModel: SessionViewModel =
                hiltViewModel()

            val sessionState by
            sessionViewModel.uiState.collectAsState()

            when {

                sessionState.isChecking -> {

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {

                        CircularProgressIndicator()
                    }
                }

                sessionState.isLoggedIn -> {

                    LaunchedEffect(Unit) {

                        navController.navigate(
                            AppRoutes.HOME
                        ) {

                            popUpTo(AppRoutes.SPLASH) {
                                inclusive = true
                            }

                            launchSingleTop = true
                        }
                    }
                }

                else -> {

                    LaunchedEffect(Unit) {

                        navController.navigate(
                            AppRoutes.LOGIN
                        ) {

                            popUpTo(AppRoutes.SPLASH) {
                                inclusive = true
                            }

                            launchSingleTop = true
                        }
                    }
                }
            }
        }
    }
}