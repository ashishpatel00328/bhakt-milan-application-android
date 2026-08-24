package com.infravo.bhaktmilan.ui.screens.profile

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.infravo.bhaktmilan.data.remote.cache.LocationCache
import com.infravo.bhaktmilan.data.remote.cache.MastersCache
import com.infravo.bhaktmilan.ui.screens.premium.PremiumGate
import com.infravo.bhaktmilan.ui.viewmodel.InteractionViewModel
import com.infravo.bhaktmilan.ui.viewmodel.PremiumViewModel
import com.infravo.bhaktmilan.ui.viewmodel.ProfileDetailViewModel
import com.infravo.bhaktmilan.ui.screens.interactions.SendInterestDialog

@Composable
fun ProfileDetailScreen(
    profileCode: String,
    viewModel: ProfileDetailViewModel = hiltViewModel(),
    interactionViewModel: InteractionViewModel = hiltViewModel(),
    premiumViewModel: PremiumViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsState()

    val interactionState by
    interactionViewModel.uiState.collectAsState()

    val premiumState by
    premiumViewModel.uiState.collectAsState()

    val context = LocalContext.current

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

    when {

        // ==========================================
        // Loading
        // ==========================================

        uiState.isLoading -> {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                CircularProgressIndicator()
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
                    .padding(16.dp)
            ) {

                // ==========================================
                // PROFILE HEADER
                // ==========================================

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                AsyncImage(
                    model = profile.profile_photo,
                    contentDescription =
                        profile.full_name ?: "Profile",

                    modifier = Modifier
                        .size(140.dp)
                        .align(
                            Alignment.CenterHorizontally
                        )
                )

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                Text(
                    text = profile.full_name ?: "-",
                    modifier = Modifier.align(
                        Alignment.CenterHorizontally
                    ),
                    style =
                        MaterialTheme.typography.headlineSmall
                )

                Spacer(
                    modifier = Modifier.height(6.dp)
                )

                Text(
                    text = profile.status ?: "-",
                    modifier = Modifier.align(
                        Alignment.CenterHorizontally
                    ),
                    style =
                        MaterialTheme.typography.bodyMedium
                )

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                HorizontalDivider()

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                // ==========================================
                // BASIC INFORMATION
                // ==========================================

                SectionTitle(
                    "Basic Information"
                )

                InfoRow(
                    label = "Age",
                    value =
                        profile.age?.toString()
                            ?: "-"
                )

                InfoRow(
                    label = "Gender",
                    value =
                        MastersCache.getGenderName(
                            profile.gender
                        )
                )

                InfoRow(
                    label = "Marital Status",
                    value =
                        MastersCache
                            .getMaritalStatusName(
                                profile.marital_status
                            )
                )

                InfoRow(
                    label = "Active",
                    value = when (
                        profile.is_active
                    ) {

                        true -> "Yes"
                        false -> "No"
                        null -> "-"
                    }
                )

                InfoRow(
                    label = "Manglik",
                    value = when (
                        profile.manglik
                    ) {

                        true -> "Yes"
                        false -> "No"
                        null -> "-"
                    }
                )

                InfoRow(
                    label = "About Me",
                    value =
                        profile.about_me ?: "-"
                )

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                HorizontalDivider()

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                // ==========================================
                // PERSONAL INFORMATION
                // ==========================================

                SectionTitle(
                    "Personal Information"
                )

                InfoRow(
                    label = "Date of Birth",
                    value =
                        profile.date_of_birth
                            ?: "-"
                )

                InfoRow(
                    label = "Height",
                    value =
                        profile.height_cm?.let {
                            "$it cm"
                        } ?: "-"
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
                        profile.mother_tongue
                            ?: "-"
                )

                InfoRow(
                    label = "Disability",
                    value =
                        MastersCache
                            .getDisabilityName(
                                profile.disability
                            )
                )

                InfoRow(
                    label = "Birth Place",
                    value =
                        profile.birth_place
                            ?: "-"
                )

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                HorizontalDivider()

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                // ==========================================
                // RELIGIOUS INFORMATION
                // ==========================================

                SectionTitle(
                    "Religious Information"
                )

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

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                HorizontalDivider()

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                // ==========================================
                // EDUCATION & CAREER
                // ==========================================

                SectionTitle(
                    "Education & Career"
                )

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

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                HorizontalDivider()

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                // ==========================================
                // FAMILY INFORMATION
                // ==========================================

                SectionTitle(
                    "Family Information"
                )

                InfoRow(
                    label = "Father Name",
                    value =
                        profile.father_name ?: "-"
                )

                InfoRow(
                    label = "Mother Name",
                    value =
                        profile.mother_name ?: "-"
                )

                InfoRow(
                    label = "Father Occupation",
                    value =
                        profile.father_occupation
                            ?: "-"
                )

                InfoRow(
                    label = "Married Brothers",
                    value =
                        profile.married_brothers
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
                    label = "Unmarried Brothers",
                    value =
                        profile.unmarried_brothers
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
                    value = when (
                        profile.family_is_satsangi
                    ) {

                        true -> "Yes"
                        false -> "No"
                        null -> "-"
                    }
                )

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                HorizontalDivider()

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                // ==========================================
                // LOCATION
                // ==========================================

                SectionTitle(
                    "Location"
                )

                InfoRow(
                    label = "Country",
                    value =
                        LocationCache.getCountryName(
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

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                HorizontalDivider()

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                // ==========================================
                // CONTACT
                // ==========================================

                SectionTitle(
                    "Contact"
                )

                InfoRow(
                    label = "WhatsApp",
                    value =
                        profile.whatsapp_number
                            ?: "-"
                )

                InfoRow(
                    label = "Profile Managed By",
                    value =
                        MastersCache
                            .getProfileManagedByName(
                                profile.profile_managed_by
                            )
                )

                Spacer(
                    modifier = Modifier.height(24.dp)
                )

                // ==========================================
// SEND INTEREST
// ==========================================

                Spacer(
                    modifier = Modifier.height(24.dp)
                )

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

                    // ==========================================
                    // Fresh Premium Check
                    // ==========================================

                    onCheckPremium = {

                        premiumViewModel
                            .getLatestSubscription()
                    },

                    // ==========================================
                    // Premium Plan
                    // ==========================================

                    onPlanSelected = { planId ->

                        premiumViewModel
                            .selectPlan(planId)
                    },

                    // ==========================================
                    // Premium Request
                    // ==========================================

                    onPremiumRequest = {

                        premiumViewModel
                            .subscribe()
                    },

                    onClearSubscribeSuccess = {

                        premiumViewModel
                            .clearSubscribeSuccess()
                    },

                    // ==========================================
                    // Actual Protected Action
                    // ==========================================

                    onProtectedAction = {

                        showInterestDialog = true
                    },

                    // ==========================================
                    // Button
                    // ==========================================

                    content = { onClick ->

                        Button(
                            modifier =
                                Modifier.fillMaxWidth(),

                            enabled =
                                !interactionState
                                    .isActionLoading,

                            onClick = onClick
                        ) {

                            if (
                                interactionState
                                    .isActionLoading
                            ) {

                                CircularProgressIndicator(
                                    modifier =
                                        Modifier.size(20.dp)
                                )

                            } else {

                                Text(
                                    text = "Send Interest"
                                )
                            }
                        }
                    }
                )

                // ==========================================
// Send Interest Dialog
// ==========================================

                if (showInterestDialog) {

                    SendInterestDialog(

                        message = interestMessage,

                        isLoading =
                            interactionState.isActionLoading,

                        error =
                            interactionState.error,

                        onMessageChange = { message ->

                            interestMessage = message
                        },

                        onSend = {

                            profile.id?.let { profileId ->

                                interactionViewModel.sendInterest(
                                    receiverProfileId = profileId,
                                    message = interestMessage.trim()
                                )
                            }
                        },

                        onDismiss = {

                            if (
                                !interactionState.isActionLoading
                            ) {

                                showInterestDialog = false

                                interestMessage = ""

                                interactionViewModel.clearError()
                            }
                        }
                    )
                }
                Spacer(
                    modifier = Modifier.height(20.dp)
                )
            }
        }

        // ==========================================
        // Profile Not Found
        // ==========================================

        else -> {

            Box(
                modifier =
                    Modifier.fillMaxSize(),

                contentAlignment =
                    Alignment.Center
            ) {

                Text(
                    text = "Profile not found"
                )
            }
        }
    }
}

// ==========================================
// Section Title
// ==========================================

@Composable
private fun SectionTitle(
    title: String
) {

    Text(
        text = title,
        style =
            MaterialTheme.typography.titleLarge
    )

    Spacer(
        modifier = Modifier.height(12.dp)
    )
}

// ==========================================
// Info Row
// ==========================================

@Composable
private fun InfoRow(
    label: String,
    value: String
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical = 6.dp
            ),

        horizontalArrangement =
            Arrangement.SpaceBetween
    ) {

        Text(
            text = label,
            style =
                MaterialTheme.typography.bodyMedium
        )

        Text(
            text = value.ifBlank {
                "-"
            },

            style =
                MaterialTheme.typography.bodyMedium
        )
    }
}