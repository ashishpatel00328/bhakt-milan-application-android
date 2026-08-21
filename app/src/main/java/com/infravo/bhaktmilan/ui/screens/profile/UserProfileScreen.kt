package com.infravo.bhaktmilan.ui.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import com.infravo.bhaktmilan.ui.model.BhaktProfile

@Composable
fun UserProfileScreen(profile: BhaktProfile) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        AsyncImage(
            model = profile.profileImageUrl,
            contentDescription = profile.name,
            modifier = Modifier
                .size(140.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(profile.name, style = MaterialTheme.typography.headlineSmall)
        Text("${profile.age} yrs • ${profile.city}")

        Spacer(modifier = Modifier.height(8.dp))
        Text("Gender: ${profile.gender}")
        Text("Sampraday: ${profile.sampraday}")

        Spacer(modifier = Modifier.height(16.dp))
        Text(profile.sampraday)
    }
}