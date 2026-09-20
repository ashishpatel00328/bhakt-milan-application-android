package com.infravo.bhaktmilan.ui.screens.myprofile

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.infravo.bhaktmilan.data.remote.response.ProfileDetail
import com.infravo.bhaktmilan.ui.theme.AppBackground
import com.infravo.bhaktmilan.ui.theme.BhaktMaroon
import com.infravo.bhaktmilan.ui.theme.BhaktMaroonDark
import com.infravo.bhaktmilan.ui.theme.BhaktMaroonLight
import com.infravo.bhaktmilan.ui.theme.BorderColor
import com.infravo.bhaktmilan.ui.theme.DividerColor
import com.infravo.bhaktmilan.ui.theme.PremiumGold
import com.infravo.bhaktmilan.ui.theme.PremiumGoldLight
import com.infravo.bhaktmilan.ui.theme.SurfaceBackground
import com.infravo.bhaktmilan.ui.theme.TextMuted
import com.infravo.bhaktmilan.ui.theme.TextPrimary
import com.infravo.bhaktmilan.ui.theme.TextSecondary
import com.infravo.bhaktmilan.ui.viewmodel.MyProfileUiState

@Composable
fun MyProfileScreen(
    uiState: MyProfileUiState,
    onClearError: () -> Unit,
    onViewProfile: (ProfileDetail) -> Unit,
    onEditProfile: () -> Unit,
    onPersonalDetailsClick: () -> Unit,
    onPrivacySecurityClick: () -> Unit,
    onHelpSupportClick: () -> Unit,
    onPremiumClick: () -> Unit,
    onLogout: () -> Unit
) {
    val context = LocalContext.current

    // =============================================================
    // ERROR HANDLING
    // =============================================================

    LaunchedEffect(uiState.error) {
        uiState.error?.let { message ->

            Toast.makeText(
                context,
                message,
                Toast.LENGTH_SHORT
            ).show()

            onClearError()
        }
    }

    // =============================================================
    // SCREEN STATE
    // =============================================================

    when {

        // ---------------------------------------------------------
        // LOADING
        // ---------------------------------------------------------

        uiState.isLoading -> {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(AppBackground),
                contentAlignment = Alignment.Center
            ) {

                CircularProgressIndicator(
                    color = BhaktMaroon
                )
            }
        }

        // ---------------------------------------------------------
        // PROFILE
        // ---------------------------------------------------------

        uiState.profile != null -> {

            val profile = uiState.profile

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(AppBackground)
                    .verticalScroll(
                        rememberScrollState()
                    )
                    .padding(
                        horizontal = 16.dp,
                        vertical = 8.dp
                    )
            ) {

                // =================================================
                // HEADER
                // =================================================

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = 4.dp,
                            bottom = 14.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {

                        Text(
                            text = "My Profile",
                            color = TextPrimary,
                            fontSize = 27.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif
                        )

                        Spacer(
                            modifier = Modifier.height(3.dp)
                        )

                        Text(
                            text = "Your journey, your identity",
                            color = TextMuted,
                            fontSize = 11.sp
                        )
                    }

                    IconButton(
                        onClick = onEditProfile,
                        modifier = Modifier.size(42.dp)
                    ) {

                        Icon(
                            imageVector = Icons.Outlined.Edit,
                            contentDescription = "Edit Profile",
                            tint = BhaktMaroon,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }

                // =================================================
                // PROFILE HERO
                // =================================================

                ProfileHeroCard(
                    profile = profile,
                    onViewProfile = {
                        onViewProfile(profile)
                    },
                    onEditProfile = onEditProfile
                )

                Spacer(
                    modifier = Modifier.height(18.dp)
                )

                // =================================================
                // PROFILE COMPLETION
                // =================================================

                ProfileCompletionCard()

                Spacer(
                    modifier = Modifier.height(18.dp)
                )

                // =================================================
                // PROFILE MENU
                // =================================================

                ProfileMenuCard {

                    ProfileMenuRow(
                        icon = Icons.Outlined.Person,
                        title = "Personal Details",
                        subtitle = "View your personal information",
                        onClick = onPersonalDetailsClick
                    )

                    ProfileMenuDivider()

                    ProfileMenuRow(
                        icon = Icons.Outlined.Star,
                        title = "Premium Membership",
                        subtitle = "Explore premium benefits",
                        iconTint = PremiumGold,
                        onClick = onPremiumClick
                    )

                    ProfileMenuDivider()

//                    ProfileMenuRow(
//                        icon = Icons.Outlined.Lock,
//                        title = "Privacy & Security",
//                        subtitle = "Your privacy matters to us",
//                        onClick = onPrivacySecurityClick
//                    )
//
//                    ProfileMenuDivider()

                    ProfileMenuRow(
                        icon = Icons.Outlined.HelpOutline,
                        title = "Help & Support",
                        subtitle = "We're here to help",
                        onClick = onHelpSupportClick
                    )

                    ProfileMenuDivider()

                    ProfileMenuRow(
                        icon = Icons.Outlined.ExitToApp,
                        title = "Logout",
                        subtitle = "Sign out from this device",
                        titleColor = BhaktMaroon,
                        iconTint = BhaktMaroon,
                        onClick = onLogout
                    )
                }

                Spacer(
                    modifier = Modifier.height(18.dp)
                )

                // =================================================
                // TRUST & PRIVACY
                // =================================================

                TrustPrivacyCard()

                Spacer(
                    modifier = Modifier.height(24.dp)
                )

                // =================================================
                // FOOTER
                // =================================================

                Text(
                    text = "Bhakt Milan",
                    color = BhaktMaroonDark,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily.Serif,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Text(
                    text = "Devotion meets destiny.",
                    color = TextMuted,
                    fontSize = 10.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )
            }
        }

        // ---------------------------------------------------------
        // PROFILE NOT FOUND
        // ---------------------------------------------------------

        else -> {

            ProfileNotFoundState()
        }
    }
}

// =================================================================
// PROFILE HERO CARD
// =================================================================

@Composable
private fun ProfileHeroCard(
    profile: ProfileDetail,
    onViewProfile: () -> Unit,
    onEditProfile: () -> Unit
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = SurfaceBackground
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(18.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                // =================================================
                // PROFILE PHOTO
                // =================================================

                Box(
                    modifier = Modifier
                        .size(96.dp)
                        .clip(CircleShape)
                        .background(BhaktMaroonLight)
                        .border(
                            width = 3.dp,
                            color = PremiumGold,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {

                    if (!profile.profile_photo.isNullOrBlank()) {

                        AsyncImage(
                            model = profile.profile_photo,
                            contentDescription = "Profile photo",
                            modifier = Modifier
                                .size(90.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )

                    } else {

                        Icon(
                            imageVector = Icons.Outlined.Person,
                            contentDescription = null,
                            tint = BhaktMaroon,
                            modifier = Modifier.size(42.dp)
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.width(15.dp)
                )

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = profile.full_name
                            .orEmpty()
                            .ifBlank {
                                "Your Profile"
                            },
                        color = TextPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = FontFamily.Serif,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(
                        modifier = Modifier.height(5.dp)
                    )

                    ProfileMetaText(
                        profile = profile
                    )

                    Spacer(
                        modifier = Modifier.height(10.dp)
                    )

                    Box(
                        modifier = Modifier
                            .clip(
                                RoundedCornerShape(9.dp)
                            )
                            .background(
                                BhaktMaroonLight
                            )
                            .clickable(
                                onClick = onViewProfile
                            )
                            .padding(
                                horizontal = 11.dp,
                                vertical = 7.dp
                            )
                    ) {

                        Text(
                            text = "View Profile  ›",
                            color = BhaktMaroon,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

//            Spacer(
//                modifier = Modifier.height(15.dp)
//            )



            // =================================================
            // PROFILE ACTION STRIP
            // =================================================


        }
    }
}

// =================================================================
// PROFILE META
// =================================================================

@Composable
private fun ProfileMetaText(
    profile: ProfileDetail
) {

    val parts = mutableListOf<String>()

    profile.age?.let {
        parts.add("$it years")
    }

    if (!profile.occupation.isNullOrBlank()) {
        parts.add(profile.occupation)
    }

    if (!profile.sampraday.isNullOrBlank()) {
        parts.add(profile.sampraday)
    }

    Text(
        text = if (parts.isEmpty()) {
            "Complete your profile"
        } else {
            parts.joinToString(" • ")
        },
        color = TextSecondary,
        fontSize = 11.sp,
        lineHeight = 17.sp,
        maxLines = 3,
        overflow = TextOverflow.Ellipsis
    )
}

// =================================================================
// HERO ACTION CHIP
// =================================================================

@Composable
private fun HeroActionChip(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Row(
        modifier = modifier
            .clip(
                RoundedCornerShape(12.dp)
            )
            .background(
                AppBackground
            )
            .border(
                width = 1.dp,
                color = BorderColor,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(
                onClick = onClick
            )
            .padding(
                horizontal = 11.dp,
                vertical = 9.dp
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {

        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = BhaktMaroon,
            modifier = Modifier.size(16.dp)
        )

        Spacer(
            modifier = Modifier.width(6.dp)
        )

        Text(
            text = text,
            color = TextPrimary,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

// =================================================================
// PROFILE COMPLETION
// =================================================================

@Composable
private fun ProfileCompletionCard() {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = PremiumGoldLight
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {

        Row(
            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 14.dp
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(
                        RoundedCornerShape(11.dp)
                    )
                    .background(
                        SurfaceBackground
                    ),
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = Icons.Outlined.FavoriteBorder,
                    contentDescription = null,
                    tint = PremiumGold,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(
                modifier = Modifier.width(11.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = "Make your profile stand out",
                    color = TextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(
                    modifier = Modifier.height(3.dp)
                )

                Text(
                    text = "Keep your profile complete and meaningful.",
                    color = TextSecondary,
                    fontSize = 10.sp,
                    lineHeight = 15.sp
                )
            }
        }
    }
}

// =================================================================
// MENU CARD
// =================================================================

@Composable
private fun ProfileMenuCard(
    content: @Composable ColumnScope.() -> Unit
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = SurfaceBackground
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {

        Column(
            modifier = Modifier.fillMaxWidth(),
            content = content
        )
    }
}

// =================================================================
// MENU ROW
// =================================================================

@Composable
private fun ProfileMenuRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    titleColor: androidx.compose.ui.graphics.Color = TextPrimary,
    iconTint: androidx.compose.ui.graphics.Color = TextSecondary,
    onClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = onClick
            )
            .padding(
                horizontal = 16.dp,
                vertical = 13.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        // =================================================
        // ICON
        // =================================================

        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(
                    RoundedCornerShape(11.dp)
                )
                .background(
                    if (iconTint == PremiumGold) {
                        PremiumGoldLight
                    } else {
                        BhaktMaroonLight
                    }
                ),
            contentAlignment = Alignment.Center
        ) {

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(
            modifier = Modifier.width(13.dp)
        )

        // =================================================
        // TEXT
        // =================================================

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = title,
                color = titleColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(
                modifier = Modifier.height(2.dp)
            )

            Text(
                text = subtitle,
                color = TextMuted,
                fontSize = 10.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Icon(
            imageVector = Icons.Outlined.ChevronRight,
            contentDescription = null,
            tint = TextMuted,
            modifier = Modifier.size(20.dp)
        )
    }
}

// =================================================================
// MENU DIVIDER
// =================================================================

@Composable
private fun ProfileMenuDivider() {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 69.dp,
                end = 16.dp
            )
            .height(1.dp)
            .background(DividerColor)
    )
}

// =================================================================
// TRUST & PRIVACY CARD
// =================================================================

@Composable
private fun TrustPrivacyCard() {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = SurfaceBackground
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(18.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(
                            RoundedCornerShape(12.dp)
                        )
                        .background(
                            BhaktMaroonLight
                        ),
                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        imageVector = Icons.Outlined.Security,
                        contentDescription = null,
                        tint = BhaktMaroon,
                        modifier = Modifier.size(21.dp)
                    )
                }

                Spacer(
                    modifier = Modifier.width(12.dp)
                )

                Column {

                    Text(
                        text = "Your Privacy Matters",
                        color = TextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = FontFamily.Serif
                    )

                    Spacer(
                        modifier = Modifier.height(2.dp)
                    )

                    Text(
                        text = "Your data is in safe hands.",
                        color = BhaktMaroon,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(13.dp)
            )

            HorizontalDivider(
                color = DividerColor
            )

            Spacer(
                modifier = Modifier.height(13.dp)
            )

            Text(
                text = "At Bhakt Milan, we believe trust is the foundation of every meaningful relationship. Your personal information is handled with care and respect.",
                color = TextSecondary,
                fontSize = 11.sp,
                lineHeight = 18.sp
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "We strive to create a comfortable experience where you can connect with confidence.",
                color = TextSecondary,
                fontSize = 11.sp,
                lineHeight = 18.sp
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Outlined.Lock,
                    contentDescription = null,
                    tint = PremiumGold,
                    modifier = Modifier.size(15.dp)
                )

                Spacer(
                    modifier = Modifier.width(6.dp)
                )

                Text(
                    text = "Your trust is important to us.",
                    color = BhaktMaroonDark,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

// =================================================================
// PROFILE NOT FOUND
// =================================================================

@Composable
private fun ProfileNotFoundState() {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {

            Box(
                modifier = Modifier
                    .size(82.dp)
                    .clip(CircleShape)
                    .background(BhaktMaroonLight),
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = null,
                    tint = BhaktMaroon,
                    modifier = Modifier.size(38.dp)
                )
            }

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Text(
                text = "Profile not found",
                color = TextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text = "We couldn't load your profile information.",
                color = TextMuted,
                fontSize = 11.sp
            )
        }
    }
}