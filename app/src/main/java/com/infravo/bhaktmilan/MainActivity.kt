
package com.infravo.bhaktmilan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.infravo.bhaktmilan.data.storage.TokenManager
import com.infravo.bhaktmilan.data.websocket.PresenceWebSocketManager
import com.infravo.bhaktmilan.ui.navigation.AppNavHost
import com.infravo.bhaktmilan.ui.navigation.AppRoutes
import com.infravo.bhaktmilan.ui.navigation.BottomBar
import com.infravo.bhaktmilan.ui.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val userViewModel: UserViewModel by viewModels()

    @Inject
    lateinit var presenceWebSocketManager: PresenceWebSocketManager

    @Inject
    lateinit var tokenManager: TokenManager


    override fun onStart() {
        super.onStart()

        lifecycleScope.launch {
            presenceWebSocketManager.setForeground(true)

            val token = tokenManager.getAccessToken()
            if (!token.isNullOrBlank()) {
                presenceWebSocketManager.connect(
                    serverBaseUrl = WS_SERVER_BASE_URL,
                    accessToken = token
                )
            }
        }
    }

    override fun onStop() {
        presenceWebSocketManager.setForeground(false)
        super.onStop()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()

            val navBackStackEntry by
            navController.currentBackStackEntryAsState()

            val currentRoute =
                navBackStackEntry?.destination?.route

            val showBottomBar = currentRoute in listOf(
                AppRoutes.HOME,
                AppRoutes.REQUESTS,
                AppRoutes.SHORTLIST,
                AppRoutes.MY_PROFILE
            )

            // Connect only after leaving public authentication screens.

            val isPublicRoute =
                currentRoute == null ||
                        currentRoute == AppRoutes.SPLASH ||
                        currentRoute == AppRoutes.LOGIN ||
                        currentRoute.startsWith("${AppRoutes.OTP}/")

            LaunchedEffect(isPublicRoute) {
                if (isPublicRoute) {
                    presenceWebSocketManager.disconnect()
                } else {
                    val accessToken = tokenManager.getAccessToken()

                    if (!accessToken.isNullOrBlank()) {
                        presenceWebSocketManager.connect(
                            serverBaseUrl = WS_SERVER_BASE_URL,
                            accessToken = accessToken
                        )
                    } else {
                        presenceWebSocketManager.disconnect()
                    }
                }
            }


            Scaffold(
                bottomBar = {
                    if (showBottomBar) {
                        BottomBar(navController)
                    }
                }
            ) { paddingValues ->
                AppNavHost(
                    navController = navController,
                    startDestination = AppRoutes.SPLASH,
                    userViewModel = userViewModel,
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    }

    companion object {
        // Development server address; use the reachable host for your device.
        private const val WS_SERVER_BASE_URL =
            "http://192.168.29.166:8000"
    }
}
