package com.infravo.bhaktmilan.ui.navigation

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.infravo.bhaktmilan.data.mapper.toOnboardingFormData
import com.infravo.bhaktmilan.ui.screens.auth.LoginScreen
import com.infravo.bhaktmilan.ui.screens.auth.OtpScreen
import com.infravo.bhaktmilan.ui.screens.home.HomeScreen
import com.infravo.bhaktmilan.ui.screens.myprofile.HelpSupportScreen
import com.infravo.bhaktmilan.ui.screens.myprofile.MyProfileScreen
import com.infravo.bhaktmilan.ui.screens.onboarding.OnboardingScreen
import com.infravo.bhaktmilan.ui.screens.premium.PremiumScreen
import com.infravo.bhaktmilan.ui.screens.profile.ProfileDetailScreen
import com.infravo.bhaktmilan.ui.screens.requests.RequestsScreen
import com.infravo.bhaktmilan.ui.screens.shortlist.ShortlistScreen
import com.infravo.bhaktmilan.ui.theme.AppBackground
import com.infravo.bhaktmilan.ui.theme.BhaktMaroon
import com.infravo.bhaktmilan.ui.theme.BhaktMaroonLight
import com.infravo.bhaktmilan.ui.theme.BorderColor
import com.infravo.bhaktmilan.ui.theme.SurfaceBackground
import com.infravo.bhaktmilan.ui.theme.TextMuted
import com.infravo.bhaktmilan.ui.theme.TextPrimary
import com.infravo.bhaktmilan.ui.theme.TextSecondary
import com.infravo.bhaktmilan.ui.viewmodel.ProfileViewModel
import com.infravo.bhaktmilan.ui.viewmodel.SessionViewModel
import com.infravo.bhaktmilan.ui.viewmodel.UserViewModel
import com.infravo.bhaktmilan.ui.screens.splash.SplashScreen
import com.infravo.bhaktmilan.ui.screens.myprofile.PersonalDetailsScreen

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

        // =========================================================
        // LOGIN
        // =========================================================
        composable(AppRoutes.LOGIN) {

            LoginScreen(
                onSendOtp = { mobile ->

                    navController.navigate(
                        "${AppRoutes.OTP}/$mobile"
                    )
                }
            )
        }


        // =========================================================
        // OTP
        // =========================================================
        composable(
            route = "${AppRoutes.OTP}/{mobile}",
            arguments = listOf(
                navArgument("mobile") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->

            val mobile =
                backStackEntry.arguments
                    ?.getString("mobile")
                    ?: ""

            OtpScreen(
                mobile = mobile,
                navController = navController
            )
        }


        // =========================================================
        // HOME
        // =========================================================
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
                },

                onSendInterest = { profile ->

                    navController.navigate(
                        "${AppRoutes.USER_PROFILE}/${profile.profileId}"
                    )
                }
            )
        }


        // =========================================================
        // USER PROFILE DETAIL
        // =========================================================
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


        // =========================================================
        // MY PROFILE
        // =========================================================
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
                    myProfileViewModel::clearError,

                // -------------------------------------------------
                // View Profile
                // -------------------------------------------------

                onViewProfile = { profile ->

                    profile.id?.let { profileId ->

                        navController.navigate(
                            "${AppRoutes.USER_PROFILE}/$profileId"
                        )
                    }
                },

                // -------------------------------------------------
                // Edit Profile
                // -------------------------------------------------

                onEditProfile = {

                    navController.navigate(
                        AppRoutes.EDIT_PROFILE
                    )
                },

                // -------------------------------------------------
                // Personal Details
                // -------------------------------------------------

                onPersonalDetailsClick = {

                    navController.navigate(
                        AppRoutes.PERSONAL_DETAILS
                    )
                },

                // -------------------------------------------------
                // Account Settings
                // -------------------------------------------------

//                onAccountSettingsClick = {
//
//                    navController.navigate(
//                        AppRoutes.ACCOUNT_SETTINGS
//                    )
//                },

                // -------------------------------------------------
                // Privacy & Security
                // -------------------------------------------------

                onPrivacySecurityClick = {

                    navController.navigate(
                        AppRoutes.PRIVACY_SECURITY
                    )
                },

                // -------------------------------------------------
                // Help & Support
                // -------------------------------------------------

                onHelpSupportClick = {

                    navController.navigate(
                        AppRoutes.HELP_SUPPORT
                    )
                },

                // -------------------------------------------------
                // Premium
                // -------------------------------------------------

                onPremiumClick = {

                    navController.navigate(
                        AppRoutes.PREMIUM
                    )
                },

                // -------------------------------------------------
                // Logout
                // -------------------------------------------------

                onLogout = {

                    myProfileViewModel.logout()

                    navController.navigate(
                        AppRoutes.LOGIN
                    ) {

                        popUpTo(AppRoutes.HOME) {
                            inclusive = true
                        }

                        launchSingleTop = true
                    }
                }
            )
        }


        // =========================================================
        // EDIT PROFILE
        // =========================================================
        composable(AppRoutes.EDIT_PROFILE) {

            val myProfileViewModel: UserViewModel =
                hiltViewModel()

            val uiState by
            myProfileViewModel.uiState.collectAsState()

            LaunchedEffect(Unit) {
                myProfileViewModel.loadMyProfile()
            }

            val profile = uiState.profile

            when {

                uiState.isLoading -> {

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {

                        CircularProgressIndicator(
                            color = BhaktMaroon
                        )
                    }
                }

                profile != null -> {

                    OnboardingScreen(
                        initialForm =
                            profile.toOnboardingFormData(),

                        isEditMode = true,

                        onSuccess = {
                            navController.popBackStack()
                        }
                    )
                }

                uiState.error != null -> {

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {

                        Text(
                            text =
                                uiState.error
                                    ?: "Unable to load profile",
                            color = TextSecondary
                        )
                    }
                }

                else -> {

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {

                        Text(
                            text = "Profile not found",
                            color = TextSecondary
                        )
                    }
                }
            }
        }


        // =========================================================
        // PERSONAL DETAILS
        // =========================================================
        // =========================================================
// PERSONAL DETAILS
// =========================================================

        composable(AppRoutes.PERSONAL_DETAILS) {

            val personalDetailsViewModel: UserViewModel =
                hiltViewModel()

            val uiState by
            personalDetailsViewModel.uiState.collectAsState()

            LaunchedEffect(Unit) {
                personalDetailsViewModel.loadMyProfile()
            }

            when {

                uiState.isLoading -> {

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(AppBackground),
                        contentAlignment = Alignment.Center
                    ) {

                        CircularProgressIndicator(
                            color = BhaktMaroon
                        )
                    }
                }

                uiState.profile != null -> {

                    PersonalDetailsScreen(
                        profile = uiState.profile!!,
                        onBack = {
                            navController.popBackStack()
                        }
                    )
                }

                uiState.error != null -> {

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(AppBackground),
                        contentAlignment = Alignment.Center
                    ) {

                        Text(
                            text = uiState.error
                                ?: "Unable to load profile",
                            color = TextSecondary
                        )
                    }
                }

                else -> {

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(AppBackground),
                        contentAlignment = Alignment.Center
                    ) {

                        Text(
                            text = "Profile not found",
                            color = TextSecondary
                        )
                    }
                }
            }
        }


        // =========================================================
        // ACCOUNT SETTINGS
        // =========================================================
        composable(AppRoutes.ACCOUNT_SETTINGS) {

            AccountSettingsPlaceholderScreen(
                title = "Account Settings",
                subtitle = "Manage your account preferences",
                icon = Icons.Outlined.Settings,
                onBack = {
                    navController.popBackStack()
                }
            )
        }


        // =========================================================
        // PRIVACY & SECURITY
        // =========================================================
        composable(AppRoutes.PRIVACY_SECURITY) {

            AccountSettingsPlaceholderScreen(
                title = "Privacy & Security",
                subtitle = "Manage your privacy and security",
                icon = Icons.Outlined.Lock,
                onBack = {
                    navController.popBackStack()
                }
            )
        }


        // =========================================================
        // HELP & SUPPORT
        // =========================================================
        composable(AppRoutes.HELP_SUPPORT) {
            HelpSupportScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }


        // =========================================================
        // REQUESTS
        // =========================================================
        composable(AppRoutes.REQUESTS) {

            RequestsScreen()
        }


        // =========================================================
        // SHORTLIST
        // =========================================================
        composable(AppRoutes.SHORTLIST) {

            ShortlistScreen()
        }


        // =========================================================
        // ONBOARDING
        // =========================================================
        composable(AppRoutes.ONBOARDING) {

            OnboardingScreen(

                onSuccess = {

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


        // =========================================================
        // SPLASH / SESSION CHECK
        // =========================================================
        composable(AppRoutes.SPLASH) {

            val sessionViewModel: SessionViewModel =
                hiltViewModel()

            val sessionState by
            sessionViewModel.uiState.collectAsState()

            var splashFinished by remember {
                mutableStateOf(false)
            }

            /*
             * Custom BhaktMilan splash screen.
             *
             * SplashScreen itself keeps the artwork visible
             * for 1.8 seconds.
             */
            SplashScreen(
                onFinished = {
                    splashFinished = true
                }
            )

            /*
             * Keep the existing SessionViewModel logic.
             *
             * Navigation starts only after:
             * 1. Splash animation/time is finished
             * 2. Session check is completed
             */
            LaunchedEffect(
                splashFinished,
                sessionState.isChecking,
                sessionState.isLoggedIn
            ) {

                if (!splashFinished || sessionState.isChecking) {
                    return@LaunchedEffect
                }

                if (sessionState.isLoggedIn) {

                    navController.navigate(
                        AppRoutes.HOME
                    ) {

                        popUpTo(AppRoutes.SPLASH) {
                            inclusive = true
                        }

                        launchSingleTop = true
                    }

                } else {

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


        // =========================================================
        // PREMIUM
        // =========================================================
        composable(AppRoutes.PREMIUM) {

            PremiumScreen()
        }


    }
}


// =================================================================
// ACCOUNT / SETTINGS PLACEHOLDER
// =================================================================

@Composable
private fun AccountSettingsPlaceholderScreen(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onBack: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
    ) {

        // =========================================================
        // TOP BAR
        // =========================================================

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 12.dp,
                    vertical = 10.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(
                onClick = onBack
            ) {

                Icon(
                    imageVector =
                        Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = "Back",
                    tint = TextPrimary
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(
                        start = 4.dp
                    )
            ) {

                Text(
                    text = title,
                    color = TextPrimary,
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = subtitle,
                    color = TextSecondary,
                    fontSize = 12.sp
                )
            }
        }

        Spacer(
            modifier = Modifier.height(14.dp)
        )

        // =========================================================
        // CONTENT CARD
        // =========================================================

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp
                ),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = SurfaceBackground
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 0.dp
            )
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment =
                    Alignment.CenterHorizontally
            ) {

                Box(
                    modifier = Modifier
                        .size(68.dp)
                        .clip(CircleShape)
                        .background(BhaktMaroonLight),
                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = BhaktMaroon,
                        modifier = Modifier.size(30.dp)
                    )
                }

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                Text(
                    text = title,
                    color = TextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(6.dp)
                )

                Text(
                    text = subtitle,
                    color = TextSecondary,
                    fontSize = 13.sp
                )

                Spacer(
                    modifier = Modifier.height(18.dp)
                )

                Text(
                    text = "More options will be available here.",
                    color = TextMuted,
                    fontSize = 12.sp
                )
            }
        }
    }
}