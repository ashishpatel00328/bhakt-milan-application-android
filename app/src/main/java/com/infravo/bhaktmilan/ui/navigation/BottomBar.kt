package com.infravo.bhaktmilan.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.infravo.bhaktmilan.ui.theme.BhaktMaroonLight
import com.infravo.bhaktmilan.ui.theme.BottomNavBackground
import com.infravo.bhaktmilan.ui.theme.BottomNavSelected
import com.infravo.bhaktmilan.ui.theme.BottomNavUnselected

@Composable
fun BottomBar(
    navController: NavController
) {

    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Requests,
        BottomNavItem.Shortlist,
        BottomNavItem.MyProfile
    )

    val navBackStackEntry =
        navController.currentBackStackEntryAsState()

    val currentRoute =
        navBackStackEntry.value?.destination?.route

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(BottomNavBackground)
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(
                start = 12.dp,
                end = 12.dp,
                top = 6.dp,
                bottom = 8.dp
            )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(
                    RoundedCornerShape(24.dp)
                )
                .background(BottomNavBackground)
                .padding(
                    horizontal = 8.dp,
                    vertical = 8.dp
                ),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {

            items.forEach { item ->

                val selected =
                    currentRoute == item.route

                val interactionSource =
                    remember {
                        MutableInteractionSource()
                    }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(
                            RoundedCornerShape(18.dp)
                        )
                        .background(
                            if (selected) {
                                BhaktMaroonLight
                            } else {
                                BottomNavBackground
                            }
                        )
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {

                            navController.navigate(
                                item.route
                            ) {
                                popUpTo(
                                    navController
                                        .graph
                                        .startDestinationId
                                ) {
                                    saveState = true
                                }

                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                        .padding(
                            vertical = 7.dp
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        tint = if (selected) {
                            BottomNavSelected
                        } else {
                            BottomNavUnselected
                        }
                    )

                    Spacer(
                        modifier = Modifier.height(3.dp)
                    )

                    Text(
                        text = item.label,
                        maxLines = 1,
                        fontSize = 11.sp,
                        fontWeight = if (selected) {
                            FontWeight.SemiBold
                        } else {
                            FontWeight.Medium
                        },
                        color = if (selected) {
                            BottomNavSelected
                        } else {
                            BottomNavUnselected
                        }
                    )
                }
            }
        }
    }
}