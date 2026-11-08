package com.infravo.bhaktmilan.ui.navigation

import androidx.compose.animation.animateColorAsState
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

private val NavDeepMaroon = Color(0xFF3F0A1A)
private val NavRoyalMaroon = Color(0xFF7A1030)
private val NavBlush = Color(0xFFF6DDD7)

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
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(
                start = 12.dp,
                end = 12.dp,
                top = 6.dp,
                bottom = 8.dp
            ),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 14.dp,
                    shape = RoundedCornerShape(40.dp),
                    clip = false
                )
                .clip(RoundedCornerShape(40.dp))
                .background(NavDeepMaroon)
                .padding(
                    horizontal = 6.dp,
                    vertical = 6.dp
                ),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->

                val selected = currentRoute == item.route

                val itemBackground = animateColorAsState(
                    targetValue = if (selected) {
                        NavBlush
                    } else {
                        Color.Transparent
                    },
                    label = "bottom_nav_item_background"
                )

                val itemForeground = animateColorAsState(
                    targetValue = if (selected) {
                        NavRoyalMaroon
                    } else {
                        NavBlush
                    },
                    label = "bottom_nav_item_foreground"
                )

                val interactionSource =
                    remember { MutableInteractionSource() }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(30.dp))
                        .background(itemBackground.value)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            navController.navigate(item.route) {
                                popUpTo(
                                    navController.graph.startDestinationId
                                ) {
                                    saveState = true
                                }

                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                        .padding(
                            horizontal = 2.dp,
                            vertical = 7.dp
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        tint = itemForeground.value,
                        modifier = Modifier.size(20.dp)
                    )

                    Spacer(
                        modifier = Modifier.height(2.dp)
                    )

                    Text(
                        text = item.label,
                        maxLines = 1,
                        fontSize = 10.sp,
                        lineHeight = 12.sp,
                        fontWeight = if (selected) {
                            FontWeight.SemiBold
                        } else {
                            FontWeight.Medium
                        },
                        color = itemForeground.value
                    )
                }
            }
        }
    }
}