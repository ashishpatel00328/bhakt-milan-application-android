package com.infravo.bhaktmilan.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
    onProfileClick: (ProfileListUiModel) -> Unit,
    onSendInterest: (ProfileListUiModel) -> Unit
) {

    var searchQuery by remember {
        mutableStateOf("")
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

        } else {

            LazyColumn(
                modifier = Modifier.padding(
                    start = 12.dp,
                    end = 12.dp,
                    bottom = 12.dp
                ),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {

                items(
                    items = filteredProfiles,
                    key = { profile -> profile.id }
                ) { profile ->

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

                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {

                            Row(
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

                                    if (profile.education.isNotBlank()) {
                                        Text(
                                            text = profile.education,
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }

                                    Spacer(
                                        modifier = Modifier.height(2.dp)
                                    )

                                    if (profile.occupation.isNotBlank()) {
                                        Text(
                                            text = profile.occupation,
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                }
                            }

                            Spacer(
                                modifier = Modifier.height(16.dp)
                            )

                            Button(
                                onClick = {
                                    onSendInterest(profile)
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            ) {

                                Text(
                                    text = "Send Interest"
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}