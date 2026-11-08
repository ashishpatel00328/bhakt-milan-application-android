package com.infravo.bhaktmilan.ui.screens.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material.icons.outlined.LocationOn
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
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
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
    val isLoadingMore by profileViewModel.isLoadingMore.collectAsState()

    // Keep one grid state so we can detect when the user approaches the end.
    val gridState = rememberLazyGridState()

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
            "HomeScreen tracking ${profileUserIds.size} valid backend user IDs: $profileUserIds"
        )

        // Keep the live WebSocket subscription and also load the existing API
        // status as a fallback. The UI observes both maps below.
        presenceViewModel.setTrackedUsers(
            owner = "home",
            userIds = profileUserIds
        )

        profileUserIds.forEach { userId ->
            android.util.Log.d(
                "PRESENCE_TRACE",
                "Requesting API online-status fallback for backend userId=$userId"
            )
            profileViewModel.loadOnlineStatus(userId)
        }
    }

    LaunchedEffect(liveStatuses, apiFallbackStatuses) {
        android.util.Log.d(
            "PRESENCE_TRACE",
            "Home presence updated: websocketIds=${liveStatuses.keys}, apiFallbackIds=${apiFallbackStatuses.keys}"
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

    // Search is intentionally limited to Sampradaya only.
    val filteredProfiles = remember(
        profiles,
        searchQuery
    ) {
        if (searchQuery.isBlank()) {
            profiles
        } else {
            profiles.filter { profile ->
                profile.sampradaya.contains(
                    searchQuery.trim(),
                    ignoreCase = true
                )
            }
        }
    }

    // Load the next cursor page when the user is within five grid items of
    // the end. Search is local to the profiles already fetched, so pagination
    // is triggered only when the full list (no active search) is displayed.
    LaunchedEffect(gridState, profiles.size, searchQuery, isLoadingMore) {
        if (searchQuery.isNotBlank()) return@LaunchedEffect

        snapshotFlow {
            val layoutInfo = gridState.layoutInfo
            val lastVisibleIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1
            val totalItems = layoutInfo.totalItemsCount
            lastVisibleIndex to totalItems
        }.collect { (lastVisibleIndex, totalItems) ->
            if (
                totalItems > 0 &&
                lastVisibleIndex >= totalItems - 5 &&
                !isLoadingMore
            ) {
                profileViewModel.loadNextProfiles()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
    ) {

        // =========================================================
        // REDESIGNED HEADER
        // =========================================================

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 18.dp, end = 18.dp, top = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
//            Box(
//                modifier = Modifier
//                    .size(48.dp)
//                    .clip(RoundedCornerShape(17.dp))
//                    .background(BhaktMaroon),
//                contentAlignment = Alignment.Center
//            ) {
//                // Keep a built-in Material icon so no new image asset is required.
//                Icon(
//                    imageVector = Icons.Outlined.FavoriteBorder,
//                    contentDescription = "Bhakt Milan logo",
//                      tint = Color(0xFFF6DDD7),
//                    modifier = Modifier.size(25.dp)
//                )
//            }

            Spacer(Modifier.width(10.dp))

//            Column(modifier = Modifier.weight(1f)) {
//                Text(
//                    text = "Bhakt Milan",
//                    color = Color(0xFF2A1218),
//                    fontFamily = FontFamily.Serif,
//                    fontSize = 23.sp,
//                    fontWeight = FontWeight.Bold,
//                    letterSpacing = (-0.5).sp,
//                    maxLines = 1
//                )
//                Spacer(Modifier.height(2.dp))
//                Text(
//                    text = "Devotion meets destiny",
//                    color = Color(0xFF80636A),
//                    fontSize = 12.sp,
//                    fontWeight = FontWeight.Normal,
//                    maxLines = 1
//                )
//            }

//            Box(
//                modifier = Modifier
//                    .size(44.dp)
//                    .shadow(6.dp, CircleShape)
//                    .clip(CircleShape)
//                    .background(Color.White)
//                    .clickable(
//                        interactionSource = remember { MutableInteractionSource() },
//                        indication = null
//                    ) { onNotificationClick() },
//                contentAlignment = Alignment.Center
//            ) {
//                Icon(
//                    imageVector = Icons.Outlined.NotificationsNone,
//                    contentDescription = "Notifications",
//                    tint = Color(0xFF2A1218),
//                    modifier = Modifier.size(22.dp)
//                )
//                Box(
//                    modifier = Modifier
//                        .align(Alignment.TopEnd)
//                        .padding(top = 9.dp, end = 8.dp)
//                        .size(8.dp)
//                        .clip(CircleShape)
//                        .background(Color(0xFFC23B63))
//                )
//            }
        }

//        Spacer(Modifier.height(17.dp))

        // =========================================================
        // SAMPRADAYA-ONLY SEARCH + FILTER ACTION
        // =========================================================

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp)
                    .shadow(7.dp, RoundedCornerShape(40.dp))
                    .clip(RoundedCornerShape(40.dp))
                    .background(Color.White)
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = "Search Sampradaya",
                    tint = BhaktMaroon,
                    modifier = Modifier.size(22.dp)
                )

                Spacer(Modifier.width(10.dp))

                Box(modifier = Modifier.weight(1f)) {
                    if (searchQuery.isEmpty()) {
                        Text(
                            text = "Search by Sampradaya",
                            color = Color(0xFF80636A),
                            fontSize = 15.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    BasicTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        textStyle = TextStyle(
                            color = Color(0xFF2A1218),
                            fontSize = 14.sp
                        ),
                        cursorBrush = Brush.verticalGradient(
                            colors = listOf(BhaktMaroon, BhaktMaroon)
                        )
                    )
                }
            }

//            Box(
//                modifier = Modifier
//                    .size(52.dp)
//                    .clip(CircleShape)
//                    .background(BhaktMaroon)
//                    .clickable(
//                        indication = null,
//                        interactionSource = remember { MutableInteractionSource() }
//                    ) { onFilterClick() },
//                contentAlignment = Alignment.Center
//            ) {
//                Icon(
//                    imageVector = Icons.Outlined.FilterList,
//                    contentDescription = "Filters",
//                    tint = Color.White,
//                    modifier = Modifier.size(23.dp)
//                )
//            }
        }

        Spacer(Modifier.height(14.dp))

        // =========================================================
        // QUICK FILTER CHIPS
        // =========================================================

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(7.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HomeFilterChip(
                text = "All",
                selected = selectedFilter == HomeQuickFilter.ALL,
                onClick = { selectedFilter = HomeQuickFilter.ALL }
            )
            HomeFilterChip(
                text = "New",
                selected = selectedFilter == HomeQuickFilter.NEW,
                onClick = { selectedFilter = HomeQuickFilter.NEW }
            )
            HomeFilterChip(
                text = "Online",
                selected = selectedFilter == HomeQuickFilter.ACTIVE_NOW,
                onClick = { selectedFilter = HomeQuickFilter.ACTIVE_NOW },
                showOnlineDot = true
            )
            HomeFilterChip(
                text = "Nearby",
                selected = false,
                onClick = { onFilterClick() },
                showLocationIcon = true
            )
        }

        Spacer(Modifier.height(18.dp))

        // =========================================================
        // SECTION TITLE
        // =========================================================

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Recommended for you",
                color = Color(0xFF2A1218),
                fontFamily = FontFamily.Serif,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = "See All",
                color = BhaktMaroon,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable { onFilterClick() }
            )
        }

        Spacer(Modifier.height(12.dp))

        // =========================================================
        // PROFILE LIST
        // =========================================================

        if (filteredProfiles.isEmpty()) {

            EmptyProfilesState(
                searchQuery = searchQuery
            )

        } else {

            LazyVerticalGrid(
                state = gridState,
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
                        // Prefer a live WebSocket online signal. If the socket has
                        // a stale/offline value while the API confirms online, keep
                        // the profile marked Online; otherwise use live status first.
                        onlineStatus = when {
                            liveStatuses[profile.userId]?.isOnline == true ->
                                liveStatuses[profile.userId]
                            apiFallbackStatuses[profile.userId]?.isOnline == true ->
                                apiFallbackStatuses[profile.userId]
                            liveStatuses[profile.userId] != null ->
                                liveStatuses[profile.userId]
                            else -> apiFallbackStatuses[profile.userId]
                        },
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

                if (isLoadingMore) {
                    item(
                        key = "pagination_loading_indicator",
                        span = { GridItemSpan(maxLineSpan) }
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 18.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(26.dp),
                                strokeWidth = 2.dp,
                                color = BhaktMaroon
                            )
                        }
                    }
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
    onClick: () -> Unit,
    showOnlineDot: Boolean = false,
    showLocationIcon: Boolean = false
) {
    Row(
        modifier = Modifier
            .clip(CircleShape)
            .background(if (selected) BhaktMaroon else Color.White)
            .border(
                width = 1.dp,
                color = if (selected) BhaktMaroon else Color(0xFFEBD6C9),
                shape = CircleShape
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(7.dp)
    ) {
        if (showOnlineDot) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF16845B))
            )
        }
        if (showLocationIcon) {
            Icon(
                imageVector = Icons.Outlined.LocationOn,
                contentDescription = null,
                tint = if (selected) Color.White else Color(0xFF2A1218),
                modifier = Modifier.size(17.dp)
            )
        }
        Text(
            text = text,
            color = if (selected) Color.White else Color(0xFF2A1218),
            fontSize = 12.sp,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
            maxLines = 1
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
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.78f)
            .shadow(4.dp, RoundedCornerShape(26.dp))
            .clip(RoundedCornerShape(26.dp))
            .background(Color(0xFF3F0A1A))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onProfileClick
            )
    ) {
        if (!profile.profilePhoto.isNullOrBlank()) {
            AsyncImage(
                model = ProfilePhotoUtils.resolveUrl(profile.profilePhoto),
                contentDescription = profile.fullName,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(BhaktMaroonLight),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.FavoriteBorder,
                    contentDescription = null,
                    tint = BhaktMaroon,
                    modifier = Modifier.size(42.dp)
                )
            }
        }

        // Darken the lower image area so the profile details remain legible.
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colorStops = arrayOf(
                            0.0f to Color.Transparent,
                            0.38f to Color.Transparent,
                            0.64f to Color(0x330F0308),
                            1.0f to Color(0xF03F0A1A)
                        )
                    )
                )
        )

        val isOnline = onlineStatus?.isOnline == true

        // Calculate minutes locally from lastSeen first (same UI-side approach
        // as before); use the API minutesAgo only if lastSeen is unavailable.
        val minutesSinceLastSeen = minutesSinceLastSeen(
            lastSeen = onlineStatus?.lastSeen,
            nowMillis = currentTimeMillis
        ) ?: onlineStatus?.minutesAgo?.toLong()

        // Rules:
        // 1) Online users always show "Online".
        // 2) Offline users show "Recently Active" only for 1..20 minutes ago.
        // 3) Older/offline/unknown users have no status badge at all.
        val showRecentlyActive = !isOnline &&
                minutesSinceLastSeen != null &&
                minutesSinceLastSeen in 0L..20L

        if (isOnline || showRecentlyActive) {
            Row(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 8.dp, top = 9.dp)
                    .clip(CircleShape)
                    .background(
                        if (isOnline) Color(0xFF16845B)
                        else Color(0xCC2A1218)
                    )
                    .padding(horizontal = 8.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                if (isOnline) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                    )
                } else {
                    Text(
                        text = "◷",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = if (isOnline) "Online" else "Recently Active",
                    color = Color.White,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1
                )
            }
        }

        // Shortlist action remains connected to the existing ViewModel.
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 8.dp, end = 8.dp)
                .size(34.dp)
                .shadow(3.dp, CircleShape)
                .clip(CircleShape)
                .background(Color(0xFFFDF9F7))
                .clickable(
                    enabled = !isShortlistActionLoading,
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onShortlistClick
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isShortlistActionLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(14.dp),
                    strokeWidth = 2.dp,
                    color = BhaktMaroon
                )
            } else {
                Icon(
                    imageVector = if (isShortlisted) Icons.Filled.Star else Icons.Outlined.Star,
                    contentDescription = if (isShortlisted) {
                        "Remove from shortlist"
                    } else {
                        "Add to shortlist"
                    },
                    tint = if (isShortlisted) Color(0xFFC99A35) else Color(0xFF2A1218),
                    modifier = Modifier.size(19.dp)
                )
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
        ) {
            Text(
                text = profile.fullName.ifBlank { "Bhakt Milan Member" },
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(Modifier.height(3.dp))

            // Keep age/location on the left and Sampradaya on the right.
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = buildAgeLocationText(profile),
                    color = Color(0xFFF9EDEA),
                    fontSize = 11.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = if (profile.sampradaya.isBlank()) {
                        "Other"
                    } else {
                        profile.sampradaya
                    },
                    color = Color(0xFFF6C9D5),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    textAlign = androidx.compose.ui.text.style.TextAlign.End,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(7.dp))

            Button(
                onClick = onSendInterest,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(37.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = BhaktMaroon
                ),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.FavoriteBorder,
                    contentDescription = "Send Interest",
                    tint = BhaktMaroon,
                    modifier = Modifier.size(15.dp)
                )
                Spacer(Modifier.width(5.dp))
                Text(
                    text = "Interest",
                    color = BhaktMaroon,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
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
                        "Try searching for a different Sampradaya."
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
 * Returns elapsed whole minutes from an ISO-8601 last-seen timestamp.
 * The UI refreshes currentTimeMillis every minute, so this value stays fresh
 * while Home remains visible. Returns null when the timestamp cannot be parsed.
 */
private fun minutesSinceLastSeen(
    lastSeen: String?,
    nowMillis: Long
): Long? {
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

    for (pattern in patterns) {
        try {
            val formatter = SimpleDateFormat(pattern, Locale.US).apply {
                isLenient = false
                timeZone = TimeZone.getTimeZone("UTC")
            }
            val position = ParsePosition(0)
            val candidate = formatter.parse(normalizedTimestamp, position)
            if (candidate != null && position.index == normalizedTimestamp.length) {
                return ((nowMillis - candidate.time).coerceAtLeast(0L)) / 60_000L
            }
        } catch (_: IllegalArgumentException) {
            // Try the next supported ISO-8601 pattern.
        }
    }
    return null
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