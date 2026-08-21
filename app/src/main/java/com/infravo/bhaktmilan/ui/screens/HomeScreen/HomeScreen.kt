package com.infravo.bhaktmilan.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.infravo.bhaktmilan.ui.model.ProfileListUiModel

@Composable
fun HomeScreen(
    profiles: List<ProfileListUiModel>,
    onProfileClick: (ProfileListUiModel) -> Unit
) {

    var searchQuery by remember {
        mutableStateOf("")
    }

    val likedMap = remember {
        mutableStateMapOf<Int, Boolean>()
    }

    val shortlistedMap = remember {
        mutableStateMapOf<Int, Boolean>()
    }

    val filteredProfiles = profiles.filter {

        it.fullName.contains(
            searchQuery,
            ignoreCase = true
        ) ||

                it.location.contains(
                    searchQuery,
                    ignoreCase = true
                )
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        OutlinedTextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            placeholder = {
                Text("Search by name or location")
            },
            singleLine = true
        )

        if (filteredProfiles.isEmpty()) {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = "No profiles found."
                )
            }

            return
        }

        LazyColumn(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            items(filteredProfiles) { profile ->

                val isLiked =
                    likedMap[profile.id] == true

                val isShortlisted =
                    shortlistedMap[profile.id] == true

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onProfileClick(profile)
                        },
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 6.dp
                    )
                ) {

                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        AsyncImage(
                            model = profile.profilePhoto,
                            contentDescription = profile.fullName,
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                        )

                        Spacer(
                            modifier = Modifier.width(12.dp)
                        )

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {

                            Text(
                                text = profile.fullName,
                                style = MaterialTheme.typography.titleMedium
                            )

                            Spacer(
                                modifier = Modifier.height(4.dp)
                            )

                            Text(
                                text = "${profile.age} • ${profile.location}",
                                style = MaterialTheme.typography.bodyMedium
                            )

                            Spacer(
                                modifier = Modifier.height(4.dp)
                            )

                            Text(
                                text = profile.height,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )

                            Spacer(
                                modifier = Modifier.height(2.dp)
                            )

                            Text(
                                text = profile.education,
                                style = MaterialTheme.typography.bodySmall
                            )

                            Spacer(
                                modifier = Modifier.height(2.dp)
                            )

                            Text(
                                text = profile.occupation,
                                style = MaterialTheme.typography.bodySmall
                            )
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                IconButton(
                                    onClick = {
                                        likedMap[profile.id] = !isLiked
                                    }
                                ) {

                                    Icon(
                                        imageVector = if (isLiked)
                                            Icons.Default.Favorite
                                        else
                                            Icons.Default.FavoriteBorder,
                                        contentDescription = "Like",
                                        tint = if (isLiked)
                                            Color.Red
                                        else
                                            Color.Gray
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        shortlistedMap[profile.id] = !isShortlisted
                                    }
                                ) {

                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = "Shortlist",
                                        tint = if (isShortlisted)
                                            Color(0xFFFFC107)
                                        else
                                            Color.Gray
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}