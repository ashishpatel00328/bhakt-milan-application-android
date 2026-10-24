package com.infravo.bhaktmilan.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import kotlinx.coroutines.delay
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import com.infravo.bhaktmilan.data.remote.response.OnlineStatusResponse
import com.infravo.bhaktmilan.ui.model.ProfileListUiModel
import com.infravo.bhaktmilan.ui.theme.AppBackground
import com.infravo.bhaktmilan.ui.theme.BhaktMaroon
import com.infravo.bhaktmilan.ui.theme.BhaktMaroonLight
import com.infravo.bhaktmilan.ui.theme.BorderColor
import com.infravo.bhaktmilan.ui.theme.DividerColor
import com.infravo.bhaktmilan.ui.theme.PremiumGoldDark
import com.infravo.bhaktmilan.ui.theme.SurfaceBackground
import com.infravo.bhaktmilan.ui.theme.TextMuted
import com.infravo.bhaktmilan.ui.theme.TextPrimary
import com.infravo.bhaktmilan.ui.theme.TextSecondary
import com.infravo.bhaktmilan.ui.viewmodel.InteractionViewModel
import com.infravo.bhaktmilan.ui.viewmodel.ProfileViewModel
import com.infravo.bhaktmilan.ui.viewmodel.PresenceViewModel
import com.infravo.bhaktmilan.utils.ProfilePhotoUtils

@Composable
fun HomeScreen(
    profiles: List<ProfileListUiModel>,
    onProfileClick: (ProfileListUiModel) -> Unit,
    onSendInterest: (ProfileListUiModel) -> Unit,
    onFilterClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    interactionViewModel: InteractionViewModel = hiltViewModel()
) {

    val interactionState by
    interactionViewModel.uiState.collectAsState()

    // Live presence is shared through the existing WebSocket/repository layer.
    // The API state is retained only as a fallback; this screen no longer makes
    // one status API request per profile.
    val presenceViewModel: PresenceViewModel = hiltViewModel()
    val liveStatuses by presenceViewModel.statuses.collectAsState()

    val profileViewModel: ProfileViewModel = hiltViewModel()
    val apiFallbackStatuses by profileViewModel.onlineStatuses.collectAsState()

    // One shared clock keeps all visible last-seen labels fresh without
    // starting a separate coroutine for every profile card.
    var currentTimeMillis by remember { mutableStateOf(System.currentTimeMillis()) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(60_000L)
            currentTimeMillis = System.currentTimeMillis()
        }
    }

    // Subscribe to the backend User IDs for profiles currently loaded on Home.
    // The repository calculates subscription differences and shares ownership
    // with other screens, such as ProfileDetailScreen.
    val profileUserIds = remember(profiles) {
        profiles.asSequence()
            .map { it.userId }
            .filter { it > 0 }
            .distinct()
            .toList()
    }

    LaunchedEffect(profileUserIds) {
        android.util.Log.d(
            "PRESENCE_TRACE",
            "HomeScreen tracking ${profileUserIds.size} valid user IDs"
        )
        presenceViewModel.setTrackedUsers(
            owner = "home",
            userIds = profileUserIds
        )
    }

    LaunchedEffect(profiles) {
        android.util.Log.d(
            "PROFILE_LIST_DEBUG",
            "HomeScreen received ${profiles.size} profiles"
        )

        android.util.Log.d(
            "PROFILE_LIST_DEBUG",
            "HomeScreen profile IDs=${
                profiles.take(5).map { it.profileId }
            }"
        )
    }
    // Release only Home's ownership when this screen leaves composition.
    // Profile-detail subscriptions remain active if that screen still needs them.
    DisposableEffect(presenceViewModel) {
        onDispose {
            presenceViewModel.setTrackedUsers(
                owner = "home",
                userIds = emptyList()
            )
        }
    }

    // =========================================================
    // LOAD SHORTLISTED PROFILES
    // =========================================================

    LaunchedEffect(Unit) {

        interactionViewModel.loadShortlistedProfiles()
    }

    val shortlistedProfileIds =
        remember(interactionState.shortlistedProfiles) {

            interactionState.shortlistedProfiles
                .map {
                    it.profile_id
                }
                .toSet()
        }

    var searchQuery by remember {
        mutableStateOf("")
    }

    var selectedFilter by remember {
        mutableStateOf(HomeQuickFilter.ALL)
    }

    val filteredProfiles = remember(
        profiles,
        searchQuery
    ) {

        if (searchQuery.isBlank()) {

            profiles

        } else {

            profiles.filter { profile ->

                profile.fullName.contains(
                    searchQuery,
                    ignoreCase = true
                ) ||
                        profile.location.contains(
                            searchQuery,
                            ignoreCase = true
                        ) ||
                        profile.sampradaya.contains(
                            searchQuery,
                            ignoreCase = true
                        )
            }
        }
    }

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
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 18.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = "Bhakt Milan",
                    color = BhaktMaroon,
                    fontFamily = FontFamily.Serif,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.sp
                )

                Spacer(
                    modifier = Modifier.height(2.dp)
                )

                Text(
                    text = "Devotion meets destiny",
                    color = PremiumGoldDark,
                    fontFamily = FontFamily.Serif,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Normal,
                    letterSpacing = 0.8.sp
                )
            }

            Icon(
                imageVector =
                    Icons.Outlined.NotificationsNone,
                contentDescription = "Notifications",
                tint = TextPrimary,
                modifier = Modifier
                    .size(23.dp)
                    .clickable {
                        onNotificationClick()
                    }
            )

            Spacer(
                modifier = Modifier.width(18.dp)
            )

            Icon(
                imageVector =
                    Icons.Outlined.FilterList,
                contentDescription = "Filters",
                tint = BhaktMaroon,
                modifier = Modifier
                    .size(23.dp)
                    .clickable {
                        onFilterClick()
                    }
            )
        }

        Spacer(
            modifier = Modifier.height(18.dp)
        )

        // =========================================================
        // SEARCH
        // =========================================================

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(52.dp)
                .clip(
                    RoundedCornerShape(14.dp)
                )
                .background(SurfaceBackground)
                .border(
                    width = 1.dp,
                    color = BorderColor,
                    shape = RoundedCornerShape(14.dp)
                )
                .padding(horizontal = 14.dp),
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Icon(
                imageVector = Icons.Outlined.Search,
                contentDescription = "Search",
                tint = BhaktMaroon,
                modifier = Modifier.size(21.dp)
            )

            Spacer(
                modifier = Modifier.width(10.dp)
            )

            BasicTextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                },
                modifier = Modifier.weight(1f),
                singleLine = true,
                textStyle = TextStyle(
                    color = TextPrimary,
                    fontSize = 14.sp
                ),
                decorationBox = { innerTextField ->

                    Box {

                        if (searchQuery.isEmpty()) {

                            Text(
                                text =
                                    "Search by name, city or Sampradaya",
                                color = TextMuted,
                                fontSize = 14.sp
                            )
                        }

                        innerTextField()
                    }
                }
            )
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        // =========================================================
        // QUICK FILTERS
        // =========================================================

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement =
                Arrangement.spacedBy(8.dp)
        ) {

            HomeFilterChip(
                text = "All",
                selected =
                    selectedFilter ==
                            HomeQuickFilter.ALL,
                onClick = {
                    selectedFilter =
                        HomeQuickFilter.ALL
                }
            )

            HomeFilterChip(
                text = "New",
                selected =
                    selectedFilter ==
                            HomeQuickFilter.NEW,
                onClick = {
                    selectedFilter =
                        HomeQuickFilter.NEW
                }
            )

            HomeFilterChip(
                text = "Active Now",
                selected =
                    selectedFilter ==
                            HomeQuickFilter.ACTIVE_NOW,
                onClick = {
                    selectedFilter =
                        HomeQuickFilter.ACTIVE_NOW
                }
            )
        }

        Spacer(
            modifier = Modifier.height(22.dp)
        )

        // =========================================================
        // SECTION TITLE
        // =========================================================

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Text(
                text = "Recommended for you",
                color = TextPrimary,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = "See All",
                color = BhaktMaroon,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(
            modifier = Modifier.height(14.dp)
        )

        // =========================================================
        // PROFILE LIST
        // =========================================================

        if (filteredProfiles.isEmpty()) {

            EmptyProfilesState(
                searchQuery = searchQuery
            )

        } else {

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 110.dp
                ),
                horizontalArrangement =
                    Arrangement.spacedBy(12.dp),
                verticalArrangement =
                    Arrangement.spacedBy(16.dp)
            ) {

                items(
                    items = filteredProfiles,
                    key = { profile ->
                        profile.id
                    }
                ) { profile ->

                    ProfileCard(
                        profile = profile,
                        onlineStatus = liveStatuses[profile.userId]
                            ?: apiFallbackStatuses[profile.userId],
                        currentTimeMillis = currentTimeMillis,

                        onProfileClick = {
                            onProfileClick(profile)
                        },

                        onShortlistClick = {

                            if (
                                shortlistedProfileIds
                                    .contains(
                                        profile.profileId
                                    )
                            ) {

                                interactionViewModel
                                    .removeFromShortlist(
                                        profile.profileId
                                    )

                            } else {

                                interactionViewModel
                                    .addToShortlist(
                                        profile.profileId
                                    )
                            }
                        },

                        isShortlisted =
                            shortlistedProfileIds
                                .contains(
                                    profile.profileId
                                ),

                        // =================================================
                        // IMPORTANT FIX
                        // Only the profile currently being modified
                        // gets the loading state.
                        // =================================================

                        isShortlistActionLoading =
                            interactionState
                                .shortlistActionProfileId ==
                                    profile.profileId,

                        onSendInterest = {
                            onSendInterest(profile)
                        }
                    )
                }
            }
        }
    }
}


// =============================================================
// QUICK FILTER TYPE
// =============================================================

private enum class HomeQuickFilter {
    ALL,
    NEW,
    ACTIVE_NOW
}


// =============================================================
// FILTER CHIP
// =============================================================

@Composable
private fun HomeFilterChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {

    Box(
        modifier = Modifier
            .clip(
                RoundedCornerShape(10.dp)
            )
            .background(
                if (selected) {
                    BhaktMaroon
                } else {
                    SurfaceBackground
                }
            )
            .border(
                width = 1.dp,
                color =
                    if (selected) {
                        BhaktMaroon
                    } else {
                        BorderColor
                    },
                shape =
                    RoundedCornerShape(10.dp)
            )
            .clickable {
                onClick()
            }
            .padding(
                horizontal = 15.dp,
                vertical = 8.dp
            )
    ) {

        Text(
            text = text,
            color =
                if (selected) {
                    SurfaceBackground
                } else {
                    TextSecondary
                },
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}


// =============================================================
// PROFILE CARD
// =============================================================

@Composable
private fun ProfileCard(
    profile: ProfileListUiModel,
    onlineStatus: OnlineStatusResponse?,
    currentTimeMillis: Long,
    onProfileClick: () -> Unit,
    onShortlistClick: () -> Unit,
    isShortlisted: Boolean,
    isShortlistActionLoading: Boolean,
    onSendInterest: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(18.dp)
            )
            .background(SurfaceBackground)
            .border(
                width = 1.dp,
                color = BorderColor,
                shape = RoundedCornerShape(18.dp)
            )
            .clickable {
                onProfileClick()
            }
    ) {

        // ---------------------------------------------------------
        // IMAGE
        // ---------------------------------------------------------

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.82f)
                .clip(
                    RoundedCornerShape(
                        topStart = 18.dp,
                        topEnd = 18.dp
                    )
                )
                .background(BhaktMaroonLight)
        ) {

            if (
                profile.profilePhoto.isNullOrBlank()
            ) {

                Box(
                    modifier =
                        Modifier.fillMaxSize(),
                    contentAlignment =
                        Alignment.Center
                ) {

                    Icon(
                        imageVector =
                            Icons.Outlined.FavoriteBorder,
                        contentDescription = null,
                        tint = BhaktMaroon,
                        modifier =
                            Modifier.size(42.dp)
                    )
                }

            } else {

                AsyncImage(
                    model = ProfilePhotoUtils.resolveUrl(profile.profilePhoto),
                    contentDescription = profile.fullName,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }

        // ---------------------------------------------------------
        // CARD CONTENT
        // ---------------------------------------------------------

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 10.dp,
                    end = 10.dp,
                    top = 10.dp,
                    bottom = 12.dp
                )
        ) {

            Text(
                text = profile.fullName.ifBlank {
                    "Bhakt Milan Member"
                },
                color = TextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow =
                    TextOverflow.Ellipsis,
                modifier =
                    Modifier.fillMaxWidth()
            )

            // Prefer the live WebSocket status; use the API value only as a fallback.
            // If neither source has a record yet, show unknown/checking status.
            Spacer(modifier = Modifier.height(5.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(
                            when {
                                onlineStatus == null -> Color.LightGray
                                onlineStatus.isOnline -> Color(0xFF2EAD63)
                                else -> Color.Gray
                            }
                        )
                )

                val lastSeenLabel = formatLastSeen(
                    lastSeen = onlineStatus?.lastSeen,
                    nowMillis = currentTimeMillis
                )

                // API minutesAgo is a fallback for missing/unparseable timestamps.
                // When lastSeen is available, calculate elapsed time locally.
                val offlineLabel = lastSeenLabel ?: onlineStatus?.minutesAgo?.let { minutes ->
                    when {
                        minutes <= 0 -> "Just now"
                        minutes == 1 -> "1 min ago"
                        minutes < 60 -> "$minutes min ago"
                        minutes < 1_440 -> {
                            val hours = minutes / 60
                            if (hours == 1) "1 hour ago" else "$hours hours ago"
                        }
                        else -> {
                            val days = minutes / 1_440
                            if (days == 1) "1 day ago" else "$days days ago"
                        }
                    }
                }

                val statusText = when {
                    onlineStatus == null -> "Checking status…"
                    onlineStatus.isOnline -> "Online"
                    offlineLabel != null -> "Offline · $offlineLabel"
                    else -> "Offline"
                }

                Text(
                    text = statusText,
                    color = when {
                        onlineStatus == null -> TextMuted
                        onlineStatus.isOnline -> Color(0xFF2EAD63)
                        else -> TextSecondary
                    },
                    fontSize = 11.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text =
                    buildAgeLocationText(profile),
                color = TextSecondary,
                fontSize = 12.sp,
                maxLines = 1,
                overflow =
                    TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(18.dp)
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text =
                    if (
                        profile.sampradaya.isBlank()
                    ) {
                        "Sampradaya not specified"
                    } else {
                        profile.sampradaya
                    },
                color = BhaktMaroon,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow =
                    TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(18.dp)
            )

            Spacer(
                modifier = Modifier.height(11.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(DividerColor)
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            // =====================================================
            // SHORTLIST + INTEREST ACTIONS
            // =====================================================

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(38.dp),
                horizontalArrangement =
                    Arrangement.spacedBy(6.dp)
            ) {

                // -------------------------------------------------
                // SHORTLIST
                // -------------------------------------------------

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .clip(
                            RoundedCornerShape(10.dp)
                        )
                        .background(
                            SurfaceBackground
                        )
                        .border(
                            width = 1.dp,
                            color = BorderColor,
                            shape =
                                RoundedCornerShape(10.dp)
                        )
                        .clickable(
                            enabled =
                                !isShortlistActionLoading
                        ) {
                            onShortlistClick()
                        },
                    contentAlignment =
                        Alignment.Center
                ) {

                    if (
                        isShortlistActionLoading
                    ) {

                        CircularProgressIndicator(
                            modifier =
                                Modifier.size(16.dp),
                            strokeWidth = 2.dp,
                            color = BhaktMaroon
                        )

                    } else {

                        Icon(
                            imageVector =
                                if (isShortlisted) {
                                    Icons.Filled.Star
                                } else {
                                    Icons.Outlined.Star
                                },
                            contentDescription =
                                if (isShortlisted) {
                                    "Remove from shortlist"
                                } else {
                                    "Add to shortlist"
                                },
                            tint =
                                if (isShortlisted) {
                                    PremiumGoldDark
                                } else {
                                    TextSecondary
                                },
                            modifier =
                                Modifier.size(17.dp)
                        )
                    }
                }

                // -------------------------------------------------
                // INTEREST
                // -------------------------------------------------

                Button(
                    modifier =
                        Modifier
                            .weight(2f)
                            .fillMaxSize(),

                    onClick = {
                        onSendInterest()
                    },

                    shape =
                        RoundedCornerShape(10.dp),

                    colors =
                        ButtonDefaults.outlinedButtonColors(
                            containerColor =
                                SurfaceBackground,
                            contentColor =
                                BhaktMaroon
                        ),

                    border =
                        androidx.compose.foundation.BorderStroke(
                            width = 1.dp,
                            color = BhaktMaroon
                        ),

                    contentPadding =
                        PaddingValues(
                            horizontal = 8.dp
                        )
                ) {

                    Icon(
                        imageVector =
                            Icons.Outlined.FavoriteBorder,
                        contentDescription =
                            "Send Interest",
                        tint = BhaktMaroon,
                        modifier =
                            Modifier.size(16.dp)
                    )

                    Spacer(
                        modifier =
                            Modifier.width(5.dp)
                    )

                    Text(
                        text = "Interest",
                        color = BhaktMaroon,
                        fontSize = 12.sp,
                        fontWeight =
                            FontWeight.SemiBold
                    )
                }
            }
        }
    }
}


// =============================================================
// AGE + LOCATION
// =============================================================

private fun buildAgeLocationText(
    profile: ProfileListUiModel
): String {

    val age = profile.age.trim()
    val location = profile.location.trim()

    return when {

        age.isNotBlank() &&
                location.isNotBlank() ->
            "$age • $location"

        age.isNotBlank() ->
            age

        location.isNotBlank() ->
            location

        else ->
            "Age and location not specified"
    }
}


// =============================================================
// EMPTY STATE
// =============================================================

@Composable
private fun EmptyProfilesState(
    searchQuery: String
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        contentAlignment =
            Alignment.Center
    ) {

        Column(
            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(
                        RoundedCornerShape(20.dp)
                    )
                    .background(
                        BhaktMaroonLight
                    ),
                contentAlignment =
                    Alignment.Center
            ) {

                Icon(
                    imageVector =
                        Icons.Outlined.FavoriteBorder,
                    contentDescription = null,
                    tint = BhaktMaroon,
                    modifier =
                        Modifier.size(30.dp)
                )
            }

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            Text(
                text = "No profiles found",
                color = TextPrimary,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text =
                    if (searchQuery.isBlank()) {
                        "There are no profiles available right now."
                    } else {
                        "Try a different name, city or Sampradaya."
                    },
                color = TextSecondary,
                fontSize = 13.sp,
                maxLines = 2,
                overflow =
                    TextOverflow.Ellipsis
            )
        }
    }
}


/**
 * Formats an ISO-8601 last-seen timestamp as a relative label.
 *
 * Examples: "Just now", "5 min ago", "2 hours ago", "3 days ago".
 * Returns null when the timestamp is missing or cannot be parsed; callers can
 * then fall back to the API-provided minutesAgo value.
 */
private fun formatLastSeen(
    lastSeen: String?,
    nowMillis: Long
): String? {
    if (lastSeen.isNullOrBlank()) return null

    val timestamp = lastSeen.trim()
    val normalizedTimestamp = timestamp.replace(
        Regex("""\.(\d{3})\d+(?=[Zz]|[+-]\d{2}:?\d{2}$)"""),
        ".$1"
    )

    val patterns = listOf(
        "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
        "yyyy-MM-dd'T'HH:mm:ssXXX",
        "yyyy-MM-dd'T'HH:mm:ss.SSSX",
        "yyyy-MM-dd'T'HH:mm:ssX"
    )

    var parsedDate: Date? = null
    for (pattern in patterns) {
        try {
            val formatter = SimpleDateFormat(pattern, Locale.US).apply {
                isLenient = false
                timeZone = TimeZone.getTimeZone("UTC")
            }
            val position = ParsePosition(0)
            val candidate = formatter.parse(normalizedTimestamp, position)
            if (candidate != null && position.index == normalizedTimestamp.length) {
                parsedDate = candidate
                break
            }
        } catch (_: IllegalArgumentException) {
            // Try the next supported ISO-8601 pattern.
        }
    }

    val lastSeenMillis = parsedDate?.time ?: return null
    val elapsedMinutes = ((nowMillis - lastSeenMillis).coerceAtLeast(0L)) / 60_000L

    return when {
        elapsedMinutes == 0L -> "Just now"
        elapsedMinutes < 60L -> "$elapsedMinutes min ago"
        elapsedMinutes < 1_440L -> {
            val hours = elapsedMinutes / 60L
            if (hours == 1L) "1 hour ago" else "$hours hours ago"
        }
        else -> {
            val days = elapsedMinutes / 1_440L
            if (days == 1L) "1 day ago" else "$days days ago"
        }
    }
}
