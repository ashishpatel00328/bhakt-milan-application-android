package com.infravo.bhaktmilan.ui.screens.requests

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*

@Composable
fun RequestsScreen() {

    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Received", "Sent")

    Column {

        TabRow(selectedTabIndex = selectedTab) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title) }
                )
            }
        }

        when (selectedTab) {
            0 -> ReceivedRequests()
            1 -> SentRequests()
        }
    }
}
