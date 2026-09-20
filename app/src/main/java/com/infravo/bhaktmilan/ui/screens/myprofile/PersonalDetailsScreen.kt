package com.infravo.bhaktmilan.ui.screens.myprofile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Cake
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.SelfImprovement
import androidx.compose.material.icons.outlined.WorkOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
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
import com.infravo.bhaktmilan.ui.theme.TextOnGold
import com.infravo.bhaktmilan.ui.theme.TextOnPrimary
import com.infravo.bhaktmilan.ui.theme.TextPrimary
import com.infravo.bhaktmilan.ui.theme.TextSecondary

@Composable
fun PersonalDetailsScreen(
    profile: ProfileDetail,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
    ) {

        // =========================================================
        // TOP BAR
        // =========================================================

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 10.dp,
                    vertical = 8.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(
                onClick = onBack,
                modifier = Modifier.size(42.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = "Back",
                    tint = TextPrimary
                )
            }

            Column(
                modifier = Modifier.padding(start = 2.dp)
            ) {

                Text(
                    text = "Personal Details",
                    color = TextPrimary,
                    fontSize = 21.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = "Your profile information",
                    color = TextMuted,
                    fontSize = 12.sp
                )
            }
        }

        // =========================================================
        // CONTENT
        // =========================================================

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(
                    horizontal = 16.dp,
                    vertical = 6.dp
                )
                .padding(bottom = 28.dp)
        ) {

            // =====================================================
            // HERO PROFILE CARD
            // =====================================================

            ProfileHeroCard(
                profile = profile
            )

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            // =====================================================
            // BASIC INFORMATION
            // =====================================================

            DetailSectionCard(
                title = "Basic Information",
                subtitle = "Essential profile details",
                icon = Icons.Outlined.Person
            ) {

                DetailGrid(
                    items = listOf(
                        DetailItem(
                            label = "Full Name",
                            value = profile.full_name
                                .orEmpty()
                                .ifBlank { "Not specified" }
                        ),
                        DetailItem(
                            label = "Age",
                            value = profile.age?.let {
                                "$it years"
                            } ?: "Not specified"
                        ),
                        DetailItem(
                            label = "Date of Birth",
                            value = profile.date_of_birth
                                .orEmpty()
                                .ifBlank { "Not specified" },
                            icon = Icons.Outlined.Cake
                        ),
                        DetailItem(
                            label = "Gender",
                            value = displayIdValue(
                                profile.gender
                            )
                        ),
                        DetailItem(
                            label = "Marital Status",
                            value = displayIdValue(
                                profile.marital_status
                            )
                        ),
                        DetailItem(
                            label = "Height",
                            value = profile.height_cm?.let {
                                "$it cm"
                            } ?: "Not specified"
                        ),
                        DetailItem(
                            label = "Weight",
                            value = profile.weight_kg?.let {
                                "$it kg"
                            } ?: "Not specified"
                        ),
                        DetailItem(
                            label = "Manglik",
                            value = yesNoValue(
                                profile.manglik
                            )
                        )
                    )
                )
            }

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            // =====================================================
            // RELIGIOUS & COMMUNITY
            // =====================================================

            DetailSectionCard(
                title = "Religious & Community",
                subtitle = "Spiritual and community background",
                icon = Icons.Outlined.SelfImprovement
            ) {

                DetailGrid(
                    items = listOf(
                        DetailItem(
                            label = "Sampradaya",
                            value = profile.sampraday
                                .orEmpty()
                                .ifBlank { "Not specified" },
                            highlight = true
                        ),
                        DetailItem(
                            label = "Guru",
                            value = profile.guru
                                .orEmpty()
                                .ifBlank { "Not specified" }
                        ),
                        DetailItem(
                            label = "Caste",
                            value = profile.caste
                                .orEmpty()
                                .ifBlank { "Not specified" }
                        ),
                        DetailItem(
                            label = "Sub Caste",
                            value = profile.sub_caste
                                .orEmpty()
                                .ifBlank { "Not specified" }
                        ),
                        DetailItem(
                            label = "Gotra",
                            value = profile.gotra
                                .orEmpty()
                                .ifBlank { "Not specified" }
                        ),
                        DetailItem(
                            label = "Nakshatra",
                            value = profile.nakshatra
                                .orEmpty()
                                .ifBlank { "Not specified" }
                        ),
                        DetailItem(
                            label = "Zodiac",
                            value = profile.zodiac
                                .orEmpty()
                                .ifBlank { "Not specified" }
                        ),
                        DetailItem(
                            label = "Family is Satsangi",
                            value = yesNoValue(
                                profile.family_is_satsangi
                            )
                        )
                    )
                )
            }

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            // =====================================================
            // EDUCATION & CAREER
            // =====================================================

            DetailSectionCard(
                title = "Education & Career",
                subtitle = "Professional and educational background",
                icon = Icons.Outlined.School
            ) {

                DetailGrid(
                    items = listOf(
                        DetailItem(
                            label = "Education",
                            value = profile.education
                                .orEmpty()
                                .ifBlank { "Not specified" },
                            icon = Icons.Outlined.MenuBook
                        ),
                        DetailItem(
                            label = "Occupation",
                            value = profile.occupation
                                .orEmpty()
                                .ifBlank { "Not specified" },
                            icon = Icons.Outlined.WorkOutline
                        ),
                        DetailItem(
                            label = "Annual Income",
                            value = profile.annual_income
                                .orEmpty()
                                .ifBlank { "Not specified" }
                        ),
                        DetailItem(
                            label = "Father's Occupation",
                            value = profile.father_occupation
                                .orEmpty()
                                .ifBlank { "Not specified" }
                        )
                    )
                )
            }

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            // =====================================================
            // FAMILY
            // =====================================================

            DetailSectionCard(
                title = "Family Information",
                subtitle = "Family and sibling details",
                icon = Icons.Outlined.FavoriteBorder
            ) {

                Column(
                    verticalArrangement = Arrangement.spacedBy(
                        14.dp
                    )
                ) {

                    FamilyNameRow(
                        label = "Father",
                        value = profile.father_name
                            .orEmpty()
                            .ifBlank { "Not specified" }
                    )

                    HorizontalDivider(
                        color = DividerColor
                    )

                    FamilyNameRow(
                        label = "Mother",
                        value = profile.mother_name
                            .orEmpty()
                            .ifBlank { "Not specified" }
                    )

                    HorizontalDivider(
                        color = DividerColor
                    )

                    SiblingStatsRow(
                        title = "Brothers",
                        married = profile.married_brothers ?: 0,
                        unmarried = profile.unmarried_brothers ?: 0
                    )

                    HorizontalDivider(
                        color = DividerColor
                    )

                    SiblingStatsRow(
                        title = "Sisters",
                        married = profile.married_sisters ?: 0,
                        unmarried = profile.unmarried_sisters ?: 0
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            // =====================================================
            // LOCATION & CONTACT
            // =====================================================

            DetailSectionCard(
                title = "Location & Contact",
                subtitle = "Location and communication details",
                icon = Icons.Outlined.LocationOn
            ) {

                DetailGrid(
                    items = listOf(
                        DetailItem(
                            label = "Birth Place",
                            value = profile.birth_place
                                .orEmpty()
                                .ifBlank { "Not specified" },
                            icon = Icons.Outlined.LocationOn
                        ),
                        DetailItem(
                            label = "Country",
                            value = displayIdValue(
                                profile.country
                            )
                        ),
                        DetailItem(
                            label = "State",
                            value = displayIdValue(
                                profile.state
                            )
                        ),
                        DetailItem(
                            label = "City",
                            value = displayIdValue(
                                profile.city
                            )
                        ),
                        DetailItem(
                            label = "Mother Tongue",
                            value = profile.mother_tongue
                                .orEmpty()
                                .ifBlank { "Not specified" }
                        ),
                        DetailItem(
                            label = "WhatsApp",
                            value = profile.whatsapp_number
                                .orEmpty()
                                .ifBlank { "Not specified" },
                            icon = Icons.Outlined.Phone
                        )
                    )
                )
            }

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            // =====================================================
            // ABOUT ME
            // =====================================================

            AboutMeCard(
                about = profile.about_me
            )

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            // =====================================================
            // PROFILE STATUS
            // =====================================================

            ProfileStatusCard(
                profile = profile
            )

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            // =====================================================
            // PRIVACY MESSAGE
            // =====================================================

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 6.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(
                            BhaktMaroonLight
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Lock,
                        contentDescription = null,
                        tint = BhaktMaroon,
                        modifier = Modifier.size(16.dp)
                    )
                }

                Spacer(
                    modifier = Modifier.width(10.dp)
                )

                Text(
                    text = "Your profile information is kept secure and private.",
                    color = TextMuted,
                    fontSize = 11.sp,
                    lineHeight = 16.sp
                )
            }
        }
    }
}

// =================================================================
// HERO PROFILE CARD
// =================================================================

@Composable
private fun ProfileHeroCard(
    profile: ProfileDetail
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = SurfaceBackground
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Profile photo

            Box(
                modifier = Modifier
                    .size(112.dp)
                    .clip(CircleShape)
                    .background(
                        BhaktMaroonLight
                    )
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
                            .size(106.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )

                } else {

                    Icon(
                        imageVector = Icons.Outlined.Person,
                        contentDescription = null,
                        tint = BhaktMaroon,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            Text(
                text = profile.full_name
                    .orEmpty()
                    .ifBlank { "Your Profile" },
                color = TextPrimary,
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.Serif,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            val headline = buildHeadline(
                profile = profile
            )

            if (headline.isNotBlank()) {
                Text(
                    text = headline,
                    color = TextSecondary,
                    fontSize = 13.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(
                    8.dp
                )
            ) {

                if (profile.age != null) {
                    ProfilePill(
                        text = "${profile.age} years"
                    )
                }

                if (!profile.sampraday.isNullOrBlank()) {
                    ProfilePill(
                        text = profile.sampraday
                    )
                }

                ProfilePill(
                    text = if (profile.is_active == true) {
                        "Active"
                    } else {
                        "Profile"
                    },
                    isGold = profile.is_active == true
                )
            }
        }
    }
}

// =================================================================
// SECTION CARD
// =================================================================

@Composable
private fun DetailSectionCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    content: @Composable () -> Unit
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
                        imageVector = icon,
                        contentDescription = null,
                        tint = BhaktMaroon,
                        modifier = Modifier.size(21.dp)
                    )
                }

                Spacer(
                    modifier = Modifier.width(12.dp)
                )

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = title,
                        color = TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(
                        modifier = Modifier.height(2.dp)
                    )

                    Text(
                        text = subtitle,
                        color = TextMuted,
                        fontSize = 11.sp
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            content()
        }
    }
}

// =================================================================
// DETAIL GRID
// =================================================================

@Composable
private fun DetailGrid(
    items: List<DetailItem>
) {

    Column(
        verticalArrangement = Arrangement.spacedBy(
            12.dp
        )
    ) {

        items.chunked(2).forEach { rowItems ->

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(
                    10.dp
                )
            ) {

                rowItems.forEach { item ->

                    DetailValueBox(
                        item = item,
                        modifier = Modifier.weight(1f)
                    )
                }

                if (rowItems.size == 1) {
                    Spacer(
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

// =================================================================
// VALUE BOX
// =================================================================

@Composable
private fun DetailValueBox(
    item: DetailItem,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .wrapContentHeight()
            .clip(
                RoundedCornerShape(14.dp)
            )
            .background(
                if (item.highlight) {
                    PremiumGoldLight
                } else {
                    AppBackground
                }
            )
            .border(
                width = 1.dp,
                color = if (item.highlight) {
                    PremiumGold.copy(alpha = 0.35f)
                } else {
                    DividerColor
                },
                shape = RoundedCornerShape(14.dp)
            )
            .padding(
                horizontal = 13.dp,
                vertical = 11.dp
            )
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            if (item.icon != null) {

                Icon(
                    imageVector = item.icon,
                    contentDescription = null,
                    tint = BhaktMaroon,
                    modifier = Modifier.size(15.dp)
                )

                Spacer(
                    modifier = Modifier.width(5.dp)
                )
            }

            Text(
                text = item.label,
                color = TextMuted,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(
            modifier = Modifier.height(5.dp)
        )

        Text(
            text = item.value,
            color = if (item.highlight) {
                TextOnGold
            } else {
                TextPrimary
            },
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            lineHeight = 18.sp,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )
    }
}

// =================================================================
// FAMILY NAME ROW
// =================================================================

@Composable
private fun FamilyNameRow(
    label: String,
    value: String
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(
                    BhaktMaroonLight
                ),
            contentAlignment = Alignment.Center
        ) {

            Icon(
                imageVector = Icons.Outlined.Person,
                contentDescription = null,
                tint = BhaktMaroon,
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(
            modifier = Modifier.width(11.dp)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = label,
                color = TextMuted,
                fontSize = 10.sp
            )

            Spacer(
                modifier = Modifier.height(2.dp)
            )

            Text(
                text = value,
                color = TextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

// =================================================================
// SIBLING STATS
// =================================================================

@Composable
private fun SiblingStatsRow(
    title: String,
    married: Int,
    unmarried: Int
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = title,
            color = TextPrimary,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )

        MiniStatChip(
            label = "Married",
            value = married
        )

        Spacer(
            modifier = Modifier.width(8.dp)
        )

        MiniStatChip(
            label = "Unmarried",
            value = unmarried
        )
    }
}

@Composable
private fun MiniStatChip(
    label: String,
    value: Int
) {

    Row(
        modifier = Modifier
            .clip(
                RoundedCornerShape(10.dp)
            )
            .background(
                AppBackground
            )
            .border(
                width = 1.dp,
                color = DividerColor,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(
                horizontal = 9.dp,
                vertical = 6.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = value.toString(),
            color = BhaktMaroon,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.width(4.dp)
        )

        Text(
            text = label,
            color = TextMuted,
            fontSize = 9.sp
        )
    }
}

// =================================================================
// ABOUT ME
// =================================================================

@Composable
private fun AboutMeCard(
    about: String?
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
                        imageVector = Icons.Outlined.MenuBook,
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
                        text = "About Me",
                        color = TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Text(
                        text = "A little about yourself",
                        color = TextMuted,
                        fontSize = 11.sp
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            Text(
                text = about
                    .orEmpty()
                    .ifBlank {
                        "No introduction has been added yet."
                    },
                color = if (about.isNullOrBlank()) {
                    TextMuted
                } else {
                    TextSecondary
                },
                fontSize = 13.sp,
                lineHeight = 21.sp
            )
        }
    }
}

// =================================================================
// STATUS CARD
// =================================================================

@Composable
private fun ProfileStatusCard(
    profile: ProfileDetail
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
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

            Text(
                text = "Profile Status",
                color = TextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(
                    10.dp
                )
            ) {

                StatusPill(
                    title = "Active",
                    value = if (profile.is_active == true) {
                        "Yes"
                    } else {
                        "No"
                    },
                    active = profile.is_active == true
                )

                StatusPill(
                    title = "Status",
                    value = profile.status
                        .orEmpty()
                        .ifBlank { "Not specified" },
                    active = false
                )
            }
        }
    }
}

// =================================================================
// STATUS PILL
// =================================================================

@Composable
private fun StatusPill(
    title: String,
    value: String,
    active: Boolean
) {

    Column(
        modifier = Modifier
            .clip(
                RoundedCornerShape(14.dp)
            )
            .background(
                if (active) {
                    BhaktMaroonLight
                } else {
                    AppBackground
                }
            )
            .padding(
                horizontal = 12.dp,
                vertical = 10.dp
            )
    ) {

        Text(
            text = title,
            color = TextMuted,
            fontSize = 9.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(
            modifier = Modifier.height(4.dp)
        )

        Text(
            text = value,
            color = if (active) {
                BhaktMaroonDark
            } else {
                TextPrimary
            },
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

// =================================================================
// PROFILE PILL
// =================================================================

@Composable
private fun ProfilePill(
    text: String,
    isGold: Boolean = false
) {

    Box(
        modifier = Modifier
            .clip(
                RoundedCornerShape(50.dp)
            )
            .background(
                if (isGold) {
                    PremiumGoldLight
                } else {
                    BhaktMaroonLight
                }
            )
            .padding(
                horizontal = 11.dp,
                vertical = 6.dp
            )
    ) {

        Text(
            text = text,
            color = if (isGold) {
                TextOnGold
            } else {
                BhaktMaroon
            },
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

// =================================================================
// DATA MODELS
// =================================================================

private data class DetailItem(
    val label: String,
    val value: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    val highlight: Boolean = false
)

// =================================================================
// HELPERS
// =================================================================

private fun yesNoValue(
    value: Boolean?
): String {
    return when (value) {
        true -> "Yes"
        false -> "No"
        null -> "Not specified"
    }
}

private fun displayIdValue(
    value: Int?
): String {
    return value?.toString() ?: "Not specified"
}

private fun buildHeadline(
    profile: ProfileDetail
): String {

    val parts = mutableListOf<String>()

    profile.age?.let {
        parts.add("$it years")
    }

    if (!profile.education.isNullOrBlank()) {
        parts.add(profile.education)
    }

    if (!profile.occupation.isNullOrBlank()) {
        parts.add(profile.occupation)
    }

    return parts.joinToString(
        separator = " • "
    )
}