package com.infravo.bhaktmilan.ui.screens.shortlist

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.infravo.bhaktmilan.ui.model.BhaktProfile
import com.infravo.bhaktmilan.ui.theme.AppBackground
import com.infravo.bhaktmilan.ui.theme.BhaktMaroon
import com.infravo.bhaktmilan.ui.theme.BhaktMaroonLight
import com.infravo.bhaktmilan.ui.theme.BorderColor
import com.infravo.bhaktmilan.ui.theme.PremiumGold
import com.infravo.bhaktmilan.ui.theme.SurfaceBackground
import com.infravo.bhaktmilan.ui.theme.TextMuted
import com.infravo.bhaktmilan.ui.theme.TextPrimary
import com.infravo.bhaktmilan.ui.theme.TextSecondary


@Composable
fun ShortlistScreen(
    shortlistedProfiles: List<BhaktProfile> = emptyList()
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
    ) {

        // =========================================================
        // HEADER
        // =========================================================

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(76.dp)
                .padding(
                    horizontal = 20.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "Shortlist",
                color = TextPrimary,
                fontSize = 27.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // =========================================================
        // LIST
        // =========================================================

        if (shortlistedProfiles.isEmpty()) {

            EmptyShortlist()

        } else {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 14.dp,
                    end = 14.dp,
                    top = 8.dp,
                    bottom = 24.dp
                ),
                verticalArrangement =
                    Arrangement.spacedBy(14.dp)
            ) {

                items(
                    items = shortlistedProfiles,
                    key = { it.id }
                ) { profile ->

                    ShortlistCard(
                        profile = profile
                    )
                }
            }
        }
    }
}


// ===============================================================
// SHORTLIST CARD
// ===============================================================

@Composable
private fun ShortlistCard(
    profile: BhaktProfile
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                // Existing profile click/navigation
                // can be connected here without
                // changing shortlist data flow.
            },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = SurfaceBackground
        ),
        border = BorderStroke(
            width = 1.dp,
            color = BorderColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 12.dp,
                    vertical = 12.dp
                ),
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            // =====================================================
            // PROFILE PHOTO
            // =====================================================

            ProfileImage(
                imageUrl =
                    profile.profileImageUrl,
                name =
                    profile.name
            )

            Spacer(
                modifier = Modifier.width(14.dp)
            )

            // =====================================================
            // PROFILE INFORMATION
            // =====================================================

            Column(
                modifier = Modifier
                    .weight(1f)
            ) {

                Text(
                    text = profile.name,
                    color = TextPrimary,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(
                    modifier = Modifier.height(5.dp)
                )

                // Age + City

                val locationText =
                    buildString {

                        if (profile.age > 0) {
                            append(profile.age)
                        }

                        if (profile.city.isNotBlank()) {

                            if (isNotEmpty()) {
                                append(" • ")
                            }

                            append(profile.city)
                        }
                    }

                if (locationText.isNotBlank()) {

                    Text(
                        text = locationText,
                        color = TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow =
                            TextOverflow.Ellipsis
                    )
                }

                // =================================================
                // PROFESSION
                // =================================================

                if (profile.profession.isNotBlank()) {

                    Spacer(
                        modifier = Modifier.height(3.dp)
                    )

                    Text(
                        text = profile.profession,
                        color = TextSecondary,
                        fontSize = 13.sp,
                        maxLines = 1,
                        overflow =
                            TextOverflow.Ellipsis
                    )
                }

                // =================================================
                // SAMPPRADAYA
                // =================================================

                if (profile.sampraday.isNotBlank()) {

                    Spacer(
                        modifier = Modifier.height(3.dp)
                    )

                    Text(
                        text = profile.sampraday,
                        color = TextMuted,
                        fontSize = 11.sp,
                        maxLines = 1,
                        overflow =
                            TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(
                modifier = Modifier.width(8.dp)
            )

            // =====================================================
            // SHORTLIST STAR
            // =====================================================

            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(
                        BhaktMaroonLight
                    ),
                contentAlignment =
                    Alignment.Center
            ) {

                Icon(
                    imageVector =
                        Icons.Outlined.Star,
                    contentDescription =
                        "Shortlisted",
                    tint = PremiumGold,
                    modifier =
                        Modifier.size(25.dp)
                )
            }
        }
    }
}


// ===============================================================
// PROFILE IMAGE
// ===============================================================

@Composable
private fun ProfileImage(
    imageUrl: String,
    name: String
) {

    val hasImage =
        imageUrl.isNotBlank()

    Box(
        modifier = Modifier
            .size(82.dp)
            .clip(CircleShape)
            .background(
                BhaktMaroonLight
            ),
        contentAlignment =
            Alignment.Center
    ) {

        if (hasImage) {

            AsyncImage(
                model = imageUrl,
                contentDescription =
                    "$name profile photo",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape),
                contentScale =
                    ContentScale.Crop
            )

        } else {

            Icon(
                imageVector =
                    Icons.Outlined.Person,
                contentDescription = null,
                tint = BhaktMaroon,
                modifier =
                    Modifier.size(38.dp)
            )
        }
    }
}


// ===============================================================
// EMPTY STATE
// ===============================================================

@Composable
private fun EmptyShortlist() {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = 32.dp
            ),
        contentAlignment =
            Alignment.Center
    ) {

        Column(
            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .size(88.dp)
                    .clip(
                        RoundedCornerShape(28.dp)
                    )
                    .background(
                        BhaktMaroonLight
                    ),
                contentAlignment =
                    Alignment.Center
            ) {

                Icon(
                    imageVector =
                        Icons.Outlined.StarOutline,
                    contentDescription = null,
                    tint = BhaktMaroon,
                    modifier =
                        Modifier.size(44.dp)
                )
            }

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            Text(
                text = "No Shortlisted Profiles",
                color = TextPrimary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(7.dp)
            )

            Text(
                text =
                    "Profiles you shortlist will appear here.",
                color = TextSecondary,
                fontSize = 13.sp
            )
        }
    }
}