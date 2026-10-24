package com.infravo.bhaktmilan.ui.screens.profile

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.infravo.bhaktmilan.data.remote.cache.LocationCache
import com.infravo.bhaktmilan.data.remote.cache.MastersCache
import com.infravo.bhaktmilan.ui.screens.interactions.SendInterestDialog
import com.infravo.bhaktmilan.ui.screens.premium.PremiumGate
import com.infravo.bhaktmilan.ui.theme.AppBackground
import com.infravo.bhaktmilan.ui.theme.BhaktMaroon
import com.infravo.bhaktmilan.ui.theme.BhaktMaroonLight
import com.infravo.bhaktmilan.ui.theme.BorderColor
import com.infravo.bhaktmilan.ui.theme.DividerColor
import com.infravo.bhaktmilan.ui.theme.PremiumGold
import com.infravo.bhaktmilan.ui.theme.PremiumGoldLight
import com.infravo.bhaktmilan.ui.theme.SurfaceBackground
import com.infravo.bhaktmilan.ui.theme.TextMuted
import com.infravo.bhaktmilan.ui.theme.TextPrimary
import com.infravo.bhaktmilan.ui.theme.TextSecondary
import com.infravo.bhaktmilan.ui.viewmodel.InteractionViewModel
import com.infravo.bhaktmilan.ui.viewmodel.PremiumViewModel
import com.infravo.bhaktmilan.ui.viewmodel.ProfileDetailViewModel
import com.infravo.bhaktmilan.ui.viewmodel.PresenceViewModel
import com.infravo.bhaktmilan.data.remote.response.OnlineStatusResponse
import com.infravo.bhaktmilan.utils.ProfilePhotoUtils
import kotlinx.coroutines.delay
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@Composable
fun ProfileDetailScreen(
    profileCode: String,
    userId: Int = 0,
    viewModel: ProfileDetailViewModel = hiltViewModel(),
    interactionViewModel: InteractionViewModel = hiltViewModel(),
    premiumViewModel: PremiumViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsState()

    // Use the same shared live-presence stream as HomeScreen.
    val presenceViewModel: PresenceViewModel = hiltViewModel()
    val liveStatuses by presenceViewModel.statuses.collectAsState()
    val detailOwner = remember(profileCode) { "profile-detail-$profileCode" }
    var currentTimeMillis by remember { mutableStateOf(System.currentTimeMillis()) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(60_000L)
            currentTimeMillis = System.currentTimeMillis()
        }
    }

    LaunchedEffect(detailOwner, userId) {
        presenceViewModel.setTrackedUsers(
            owner = detailOwner,
            userIds = if (userId > 0) listOf(userId) else emptyList()
        )
    }

    DisposableEffect(presenceViewModel, detailOwner) {
        onDispose {
            presenceViewModel.setTrackedUsers(owner = detailOwner, userIds = emptyList())
        }
    }

    val interactionState by
    interactionViewModel.uiState.collectAsState()

    val premiumState by
    premiumViewModel.uiState.collectAsState()

    val context = androidx.compose.ui.platform.LocalContext.current

    var showInterestDialog by remember {
        mutableStateOf(false)
    }

    var interestMessage by remember {
        mutableStateOf("")
    }

    // ==========================================
    // Load Profile
    // ==========================================

    LaunchedEffect(profileCode) {

        viewModel.loadProfile(profileCode)
    }

    // ==========================================
    // Profile Error
    // ==========================================

    LaunchedEffect(uiState.error) {

        uiState.error?.let { message ->

            Toast.makeText(
                context,
                message,
                Toast.LENGTH_SHORT
            ).show()

            viewModel.clearError()
        }
    }

    // ==========================================
    // Interaction Error
    // ==========================================

    LaunchedEffect(interactionState.error) {

        interactionState.error?.let { message ->

            Toast.makeText(
                context,
                message,
                Toast.LENGTH_SHORT
            ).show()

            interactionViewModel.clearError()
        }
    }

    // ==========================================
    // Interaction Success
    // ==========================================

    LaunchedEffect(
        interactionState.lastActionSuccess
    ) {

        if (interactionState.lastActionSuccess) {

            interactionState.message?.let { message ->

                Toast.makeText(
                    context,
                    message,
                    Toast.LENGTH_SHORT
                ).show()
            }

            showInterestDialog = false
            interestMessage = ""

            interactionViewModel.clearActionState()
        }
    }

    // ==========================================
    // UI
    // ==========================================

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
    ) {

        when {

            // ==========================================
            // Loading
            // ==========================================

            uiState.isLoading -> {

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {

                    CircularProgressIndicator(
                        color = BhaktMaroon
                    )
                }
            }

            // ==========================================
            // Profile Loaded
            // ==========================================

            uiState.profile != null -> {

                val profile = uiState.profile!!

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(
                            rememberScrollState()
                        )
                        .padding(
                            horizontal = 16.dp,
                            vertical = 14.dp
                        ),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {

                    // ==========================================
                    // PROFILE HERO
                    // ==========================================

                    ProfileHeroCard(
                        profile = profile,
                        onlineStatus = liveStatuses[userId],
                        currentTimeMillis = currentTimeMillis
                    )

                    // ==========================================
                    // ABOUT
                    // ==========================================

                    if (!profile.about_me.isNullOrBlank()) {

                        DetailSectionCard(
                            title = "About",
                            subtitle = "A little about this person"
                        ) {

                            Text(
                                text = profile.about_me.orEmpty(),
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextSecondary,
                                lineHeight = MaterialTheme.typography.bodyMedium.lineHeight
                            )
                        }
                    }

                    // ==========================================
                    // QUICK FACTS
                    // ==========================================

                    DetailSectionCard(
                        title = "Quick Facts",
                        subtitle = "At a glance"
                    ) {

                        QuickFactsGrid(
                            items = listOf(
                                QuickFact(
                                    "Age",
                                    profile.age?.toString() ?: "-"
                                ),
                                QuickFact(
                                    "Height",
                                    profile.height_cm?.let {
                                        "$it cm"
                                    } ?: "-"
                                ),
                                QuickFact(
                                    "Marital Status",
                                    MastersCache
                                        .getMaritalStatusName(
                                            profile.marital_status
                                        )
                                ),
                                QuickFact(
                                    "Gender",
                                    MastersCache
                                        .getGenderName(
                                            profile.gender
                                        )
                                ),
                                QuickFact(
                                    "Education",
                                    profile.education ?: "-"
                                ),
                                QuickFact(
                                    "Occupation",
                                    profile.occupation ?: "-"
                                )
                            )
                        )
                    }

                    // ==========================================
                    // PERSONAL INFORMATION
                    // ==========================================

                    DetailSectionCard(
                        title = "Personal Information",
                        subtitle = "Personal and lifestyle details"
                    ) {

                        InfoRow(
                            label = "Date of Birth",
                            value =
                                profile.date_of_birth ?: "-"
                        )

                        InfoRow(
                            label = "Weight",
                            value =
                                profile.weight_kg?.let {
                                    "$it kg"
                                } ?: "-"
                        )

                        InfoRow(
                            label = "Blood Group",
                            value =
                                MastersCache
                                    .getBloodGroupName(
                                        profile.blood_group
                                    )
                        )

                        InfoRow(
                            label = "Diet Preference",
                            value =
                                MastersCache
                                    .getDietPreferenceName(
                                        profile.diet_preference
                                    )
                        )

                        InfoRow(
                            label = "Mother Tongue",
                            value =
                                profile.mother_tongue ?: "-"
                        )

                        InfoRow(
                            label = "Disability",
                            value =
                                MastersCache
                                    .getDisabilityName(
                                        profile.disability
                                    )
                        )
                    }

                    // ==========================================
                    // RELIGIOUS & COMMUNITY
                    // ==========================================

                    DetailSectionCard(
                        title = "Religious & Community",
                        subtitle = "Spiritual and family background"
                    ) {

                        InfoRow(
                            label = "Sampraday",
                            value =
                                profile.sampraday ?: "-"
                        )

                        InfoRow(
                            label = "Guru",
                            value =
                                profile.guru ?: "-"
                        )

                        InfoRow(
                            label = "Caste",
                            value =
                                profile.caste ?: "-"
                        )

                        InfoRow(
                            label = "Sub Caste",
                            value =
                                profile.sub_caste ?: "-"
                        )

                        InfoRow(
                            label = "Gotra",
                            value =
                                profile.gotra ?: "-"
                        )

                        InfoRow(
                            label = "Nakshatra",
                            value =
                                profile.nakshatra ?: "-"
                        )

                        InfoRow(
                            label = "Zodiac",
                            value =
                                profile.zodiac ?: "-"
                        )

                        InfoRow(
                            label = "Manglik",
                            value =
                                when (profile.manglik) {
                                    true -> "Yes"
                                    false -> "No"
                                    null -> "-"
                                }
                        )
                    }

                    // ==========================================
                    // EDUCATION & CAREER
                    // ==========================================

                    DetailSectionCard(
                        title = "Education & Career",
                        subtitle = "Professional background"
                    ) {

                        InfoRow(
                            label = "Education",
                            value =
                                profile.education ?: "-"
                        )

                        InfoRow(
                            label = "Occupation",
                            value =
                                profile.occupation ?: "-"
                        )

                        InfoRow(
                            label = "Annual Income",
                            value =
                                profile.annual_income ?: "-"
                        )
                    }

                    // ==========================================
                    // FAMILY
                    // ==========================================

                    DetailSectionCard(
                        title = "Family Information",
                        subtitle = "Family background"
                    ) {

                        InfoRow(
                            label = "Father Name",
                            value =
                                profile.father_name ?: "-"
                        )

                        InfoRow(
                            label = "Father Occupation",
                            value =
                                profile.father_occupation ?: "-"
                        )

                        InfoRow(
                            label = "Mother Name",
                            value =
                                profile.mother_name ?: "-"
                        )

                        InfoRow(
                            label = "Married Brothers",
                            value =
                                profile.married_brothers
                                    ?.toString()
                                    ?: "-"
                        )

                        InfoRow(
                            label = "Unmarried Brothers",
                            value =
                                profile.unmarried_brothers
                                    ?.toString()
                                    ?: "-"
                        )

                        InfoRow(
                            label = "Married Sisters",
                            value =
                                profile.married_sisters
                                    ?.toString()
                                    ?: "-"
                        )

                        InfoRow(
                            label = "Unmarried Sisters",
                            value =
                                profile.unmarried_sisters
                                    ?.toString()
                                    ?: "-"
                        )

                        InfoRow(
                            label = "Family Satsangi",
                            value =
                                when (
                                    profile.family_is_satsangi
                                ) {
                                    true -> "Yes"
                                    false -> "No"
                                    null -> "-"
                                }
                        )
                    }

                    // ==========================================
                    // LOCATION
                    // ==========================================

                    DetailSectionCard(
                        title = "Location",
                        subtitle = "Current and birth location"
                    ) {

                        InfoRow(
                            label = "Country",
                            value =
                                LocationCache
                                    .getCountryName(
                                        profile.country
                                    )
                        )

                        InfoRow(
                            label = "State",
                            value =
                                LocationCache.getStateName(
                                    countryId =
                                        profile.country,
                                    stateId =
                                        profile.state
                                )
                        )

                        InfoRow(
                            label = "City",
                            value =
                                LocationCache.getCityName(
                                    stateId =
                                        profile.state,
                                    cityId =
                                        profile.city
                                )
                        )

                        InfoRow(
                            label = "Birth Place",
                            value =
                                profile.birth_place ?: "-"
                        )
                    }

                    // ==========================================
                    // CONTACT
                    // ==========================================

                    DetailSectionCard(
                        title = "Contact",
                        subtitle = "Contact information"
                    ) {

                        InfoRow(
                            label = "WhatsApp",
                            value =
                                profile.whatsapp_number ?: "-"
                        )

                        InfoRow(
                            label = "Profile Managed By",
                            value =
                                MastersCache
                                    .getProfileManagedByName(
                                        profile.profile_managed_by
                                    )
                        )
                    }

                    // ==========================================
                    // SEND INTEREST
                    // ==========================================

                    PremiumGate(

                        subscription =
                            premiumState.subscription,

                        plans =
                            premiumState.plans,

                        selectedPlanId =
                            premiumState.selectedPlanId,

                        isSubmitting =
                            premiumState.isSubscribing,

                        subscribeSuccess =
                            premiumState.subscribeSuccess,

                        // Subscribe Response Code
                        subscribeResponseCode =
                            premiumState.subscribeResponseCode,

                        // Fresh Premium Check
                        onCheckPremium = {

                            premiumViewModel
                                .getLatestSubscription()
                        },

                        // Premium Plan
                        onPlanSelected = { planId ->

                            premiumViewModel
                                .selectPlan(planId)
                        },

                        // Premium Request
                        onPremiumRequest = {

                            premiumViewModel
                                .subscribe()
                        },

                        onClearSubscribeSuccess = {

                            premiumViewModel
                                .clearSubscribeSuccess()
                        },

                        // Protected Action
                        onProtectedAction = {

                            showInterestDialog = true
                        },

                        // Send Interest Button
                        content = { onClick ->

                            Button(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),
                                enabled =
                                    !interactionState
                                        .isActionLoading,
                                shape =
                                    RoundedCornerShape(16.dp),
                                colors =
                                    ButtonDefaults.buttonColors(
                                        containerColor =
                                            BhaktMaroon,
                                        contentColor =
                                            SurfaceBackground,
                                        disabledContainerColor =
                                            BhaktMaroon
                                                .copy(
                                                    alpha = 0.45f
                                                )
                                    ),
                                onClick = onClick
                            ) {

                                if (
                                    interactionState
                                        .isActionLoading
                                ) {

                                    CircularProgressIndicator(
                                        modifier =
                                            Modifier.size(
                                                21.dp
                                            ),
                                        color =
                                            SurfaceBackground,
                                        strokeWidth = 2.dp
                                    )

                                } else {

                                    Text(
                                        text = "Send Interest",
                                        fontWeight =
                                            FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    )

                    Spacer(
                        modifier =
                            Modifier.height(10.dp)
                    )

                    // ==========================================
                    // FOOTER
                    // ==========================================

                    Text(
                        text =
                            "Bhakt Milan • Devotion meets destiny",
                        modifier =
                            Modifier.fillMaxWidth(),
                        style =
                            MaterialTheme.typography.labelSmall,
                        color = TextMuted
                    )

                    Spacer(
                        modifier =
                            Modifier.height(12.dp)
                    )
                }

                // ==========================================
                // SEND INTEREST DIALOG
                // ==========================================

                if (showInterestDialog) {

                    SendInterestDialog(

                        message = interestMessage,

                        isLoading =
                            interactionState
                                .isActionLoading,

                        error =
                            interactionState.error,

                        onMessageChange = { message ->

                            interestMessage = message
                        },

                        onSend = {

                            profile.id?.let { profileId ->

                                interactionViewModel
                                    .sendInterest(
                                        receiverProfileId =
                                            profileId,
                                        message =
                                            interestMessage
                                                .trim()
                                    )
                            }
                        },

                        onDismiss = {

                            if (
                                !interactionState
                                    .isActionLoading
                            ) {

                                showInterestDialog = false

                                interestMessage = ""

                                interactionViewModel
                                    .clearError()
                            }
                        }
                    )
                }
            }

            // ==========================================
            // PROFILE NOT FOUND
            // ==========================================

            else -> {

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {

                    Card(
                        modifier =
                            Modifier.padding(24.dp),
                        shape =
                            RoundedCornerShape(20.dp),
                        colors =
                            CardDefaults.cardColors(
                                containerColor =
                                    SurfaceBackground
                            ),
                        border =
                            BorderStroke(
                                1.dp,
                                DividerColor
                            )
                    ) {

                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment =
                                Alignment.CenterHorizontally,
                            verticalArrangement =
                                Arrangement.spacedBy(8.dp)
                        ) {

                            Text(
                                text = "Profile not found",
                                style =
                                    MaterialTheme.typography
                                        .titleMedium,
                                color = TextPrimary,
                                fontWeight =
                                    FontWeight.SemiBold
                            )

                            Text(
                                text =
                                    "This profile may no longer be available.",
                                style =
                                    MaterialTheme.typography
                                        .bodySmall,
                                color = TextSecondary
                            )
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// PROFILE HERO
// ==========================================

@Composable
private fun ProfileHeroCard(
    profile: com.infravo.bhaktmilan.data.remote.response.ProfileDetail,
    onlineStatus: OnlineStatusResponse?,
    currentTimeMillis: Long
) {
    val isOnline = onlineStatus?.isOnline ?: profile.is_online
    val lastSeen = onlineStatus?.lastSeen ?: profile.last_seen
    val minutesAgo = onlineStatus?.minutesAgo ?: profile.minutes_ago
    val relativeLastSeen = formatProfileLastSeen(lastSeen, currentTimeMillis)
        ?: minutesAgo?.let { minutes ->
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

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(440.dp),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceBackground),
        border = BorderStroke(1.dp, BorderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Photo fills the complete hero card; no empty column beside it.
            AsyncImage(
                model = ProfilePhotoUtils.resolveUrl(profile.profile_photo),
                contentDescription = profile.full_name ?: "Profile",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Bottom gradient keeps the overlaid profile text readable.
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colorStops = arrayOf(
                                0.0f to Color.Transparent,
                                0.42f to Color.Transparent,
                                1.0f to Color(0xE6000000)
                            )
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 22.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = profile.full_name ?: "-",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.White
                )

                val location = LocationCache.getCityName(
                    stateId = profile.state,
                    cityId = profile.city
                )
                val basicMeta = buildList {
                    profile.age?.let { add("$it years") }
                    if (location.isNotBlank()) add(location)
                }.joinToString("  •  ")

                if (basicMeta.isNotBlank()) {
                    Text(
                        text = basicMeta,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White.copy(alpha = 0.92f)
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (!profile.sampraday.isNullOrBlank()) {
                        HeroBadge(text = profile.sampraday.orEmpty(), isGold = true)
                    }
                    profile.status?.takeIf { it.isNotBlank() }?.let {
                        HeroBadge(text = it, isGold = false)
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(9.dp)
                            .clip(CircleShape)
                            .background(
                                when (isOnline) {
                                    true -> Color(0xFF36D477)
                                    false -> Color(0xFFBDBDBD)
                                    null -> Color.LightGray
                                }
                            )
                    )
                    Text(
                        text = when (isOnline) {
                            true -> "Online"
                            false -> if (relativeLastSeen != null) {
                                "Offline · $relativeLastSeen"
                            } else {
                                "Offline"
                            }
                            null -> "Status unavailable"
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White
                    )
                }
            }
        }
    }
}

// ==========================================
// HERO BADGE
// ==========================================

@Composable
private fun HeroBadge(
    text: String,
    isGold: Boolean
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
                horizontal = 12.dp,
                vertical = 7.dp
            )
    ) {

        Text(
            text = text,
            style =
                MaterialTheme.typography.labelSmall,
            color = if (isGold) {
                PremiumGold
            } else {
                BhaktMaroon
            },
            fontWeight = FontWeight.SemiBold
        )
    }
}

// ==========================================
// SECTION CARD
// ==========================================

@Composable
private fun DetailSectionCard(
    title: String,
    subtitle: String,
    content: @Composable () -> Unit
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = SurfaceBackground
        ),
        border = BorderStroke(
            1.dp,
            DividerColor
        )
    ) {

        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement =
                Arrangement.spacedBy(10.dp)
        ) {

            Text(
                text = title,
                style =
                    MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                color = TextPrimary
            )

            Text(
                text = subtitle,
                style =
                    MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )

            Spacer(
                modifier = Modifier.height(2.dp)
            )

            HorizontalDivider(
                color = DividerColor
            )

            Spacer(
                modifier = Modifier.height(2.dp)
            )

            content()
        }
    }
}

// ==========================================
// QUICK FACT
// ==========================================

private data class QuickFact(
    val label: String,
    val value: String
)

// ==========================================
// QUICK FACTS GRID
// ==========================================

@Composable
private fun QuickFactsGrid(
    items: List<QuickFact>
) {

    Column(
        verticalArrangement =
            Arrangement.spacedBy(10.dp)
    ) {

        items.chunked(2).forEach { rowItems ->

            Row(
                modifier =
                    Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.spacedBy(10.dp)
            ) {

                rowItems.forEach { item ->

                    QuickFactCard(
                        item = item,
                        modifier =
                            Modifier.weight(1f)
                    )
                }

                if (rowItems.size == 1) {

                    Spacer(
                        modifier =
                            Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

// ==========================================
// QUICK FACT CARD
// ==========================================

@Composable
private fun QuickFactCard(
    item: QuickFact,
    modifier: Modifier = Modifier
) {

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = AppBackground
        ),
        border = BorderStroke(
            1.dp,
            DividerColor
        )
    ) {

        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement =
                Arrangement.spacedBy(4.dp)
        ) {

            Text(
                text = item.label,
                style =
                    MaterialTheme.typography.labelSmall,
                color = TextMuted
            )

            Text(
                text = item.value.ifBlank {
                    "-"
                },
                style =
                    MaterialTheme.typography.bodyMedium,
                color = TextPrimary,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

// ==========================================
// INFO ROW
// ==========================================

@Composable
private fun InfoRow(
    label: String,
    value: String
) {

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = 8.dp
                ),
            horizontalArrangement =
                Arrangement.SpaceBetween,
            verticalAlignment =
                Alignment.Top
        ) {

            Text(
                text = label,
                modifier =
                    Modifier.weight(
                        0.42f
                    ),
                style =
                    MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )

            Text(
                text = value.ifBlank {
                    "-"
                },
                modifier =
                    Modifier.weight(
                        0.58f
                    ),
                style =
                    MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium
                    ),
                color = TextPrimary
            )
        }

        HorizontalDivider(
            color = DividerColor
        )
    }
}

/** Formats an ISO-8601 timestamp as a relative last-seen label. */
private fun formatProfileLastSeen(lastSeen: String?, nowMillis: Long): String? {
    if (lastSeen.isNullOrBlank()) return null
    val normalized = lastSeen.trim().replace(
        Regex("""\.(\d{3})\d+(?=[Zz]|[+-]\d{2}:?\d{2}$)"""),
        ".$1"
    )
    val patterns = listOf(
        "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
        "yyyy-MM-dd'T'HH:mm:ssXXX",
        "yyyy-MM-dd'T'HH:mm:ss.SSSX",
        "yyyy-MM-dd'T'HH:mm:ssX"
    )
    var parsed: Date? = null
    for (pattern in patterns) {
        try {
            val formatter = SimpleDateFormat(pattern, Locale.US).apply {
                isLenient = false
                timeZone = TimeZone.getTimeZone("UTC")
            }
            val position = ParsePosition(0)
            val candidate = formatter.parse(normalized, position)
            if (candidate != null && position.index == normalized.length) {
                parsed = candidate
                break
            }
        } catch (_: IllegalArgumentException) {
            // Try the next supported ISO-8601 pattern.
        }
    }
    val timestamp = parsed?.time ?: return null
    val minutes = ((nowMillis - timestamp).coerceAtLeast(0L)) / 60_000L
    return when {
        minutes == 0L -> "Just now"
        minutes < 60L -> "$minutes min ago"
        minutes < 1_440L -> {
            val hours = minutes / 60L
            if (hours == 1L) "1 hour ago" else "$hours hours ago"
        }
        else -> {
            val days = minutes / 1_440L
            if (days == 1L) "1 day ago" else "$days days ago"
        }
    }
}
