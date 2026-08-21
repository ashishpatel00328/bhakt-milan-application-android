package com.infravo.bhaktmilan.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.ui.graphics.vector.ImageVector
import com.infravo.bhaktmilan.ui.navigation.AppRoutes

sealed class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
) {

    object Home : BottomNavItem(
        AppRoutes.HOME,
        Icons.Default.Home,
        "Home"
    )

    object Requests : BottomNavItem(
        AppRoutes.REQUESTS,
        Icons.Default.Inbox,
        "Requests"
    )

    object Shortlist : BottomNavItem(
        AppRoutes.SHORTLIST,
        Icons.Default.StarBorder,
        "Shortlist"
    )

//    object MyProfile : BottomNavItem(
//        AppRoutes.MYPROFILE,
//        Icons.Default.Person,
//        "MyProfile"
//    )

    object MyProfile : BottomNavItem(
        AppRoutes.MY_PROFILE,
        Icons.Default.Person,
        "My Profile"
    )
}

