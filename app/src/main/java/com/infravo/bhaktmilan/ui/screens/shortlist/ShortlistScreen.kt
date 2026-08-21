package com.infravo.bhaktmilan.ui.screens.shortlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.infravo.bhaktmilan.ui.model.BhaktProfile

@Composable
fun ShortlistScreen(
    shortlistedProfiles: List<BhaktProfile> = emptyList()
) {

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        // 🔹 Top Title
        Text(
            text = "Shortlisted Profiles",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(16.dp)
        )

        if (shortlistedProfiles.isEmpty()) {
            EmptyShortlist()
        } else {
            LazyColumn(
                modifier = Modifier.padding(horizontal = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(shortlistedProfiles.size) { index ->
                    ShortlistCard(profile = shortlistedProfiles[index])
                }
            }
        }
    }
}

@Composable
private fun EmptyShortlist() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "You haven’t shortlisted anyone yet",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun ShortlistCard(profile: BhaktProfile) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // 🔹 Profile Image
            AsyncImage(
                model = profile.profileImageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            // 🔹 Profile Info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    profile.name,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    "${profile.age} yrs • ${profile.city}",
                    style = MaterialTheme.typography.bodySmall
                )

                Text(
                    profile.sampraday,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // 🔹 Star Icon (Shortlisted)
            Icon(
                imageVector = Icons.Outlined.Star,
                contentDescription = "Shortlisted",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}
