package com.example.bhaktmilan.ui.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.bhaktmilan.ui.model.BhaktProfile


@Composable
fun ProfileDetailScreen(profile: BhaktProfile) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        item {
            Text(profile.name, style = MaterialTheme.typography.headlineSmall)
            Text("${profile.age} yrs • ${profile.city}")
        }

        item {
            Divider()
            InfoRow("Sampraday", profile.sampraday)
            InfoRow("Profession", profile.profession)
            InfoRow("Education", profile.education)
            InfoRow("Marital Status", profile.maritalStatus)
            InfoRow("Height", profile.height)
            InfoRow("Diet", profile.diet)
        }

        item {
            Divider()
            Text("About", style = MaterialTheme.typography.titleMedium)
            Text(profile.about)
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontWeight = FontWeight.SemiBold)
        Text(value)
    }
}
