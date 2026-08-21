package com.infravo.bhaktmilan.ui.screens.profile

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.infravo.bhaktmilan.data.remote.cache.LocationCache
import com.infravo.bhaktmilan.data.remote.cache.MastersCache
import com.infravo.bhaktmilan.ui.viewmodel.ProfileDetailViewModel

@Composable
fun ProfileDetailScreen(
    profileCode: String,
    viewModel: ProfileDetailViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsState()

    val context = LocalContext.current

    // ==========================================
    // Load Profile
    // ==========================================

    LaunchedEffect(profileCode) {

        viewModel.loadProfile(profileCode)

    }

    // ==========================================
    // Error
    // ==========================================

    LaunchedEffect(uiState.error) {

        uiState.error?.let {

            Toast.makeText(
                context,
                it,
                Toast.LENGTH_SHORT
            ).show()

            viewModel.clearError()
        }
    }

    // ==========================================
    // UI State
    // ==========================================

    when {

        uiState.isLoading -> {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                CircularProgressIndicator()

            }
        }

        uiState.profile != null -> {

            val profile = uiState.profile!!

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {

                // =========================
                // BASIC INFO
                // =========================

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                AsyncImage(
                    model = profile.profile_photo,
                    contentDescription = profile.full_name,
                    modifier = Modifier
                        .size(140.dp)
                        .align(Alignment.CenterHorizontally)
                )

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                Text(
                    text = profile.full_name ?: "-",
                    modifier = Modifier.align(
                        Alignment.CenterHorizontally
                    ),
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(
                    modifier = Modifier.height(6.dp)
                )

                Text(
                    text = profile.status ?: "-",
                    modifier = Modifier.align(
                        Alignment.CenterHorizontally
                    ),
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                HorizontalDivider()

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                // =========================
                // BASIC INFORMATION
                // =========================

                SectionTitle("Basic Information")

                InfoRow(
                    "Age",
                    profile.age.toString()
                )

                InfoRow(
                    "Gender",
                    MastersCache.getGenderName(
                        profile.gender
                    )
                )

                InfoRow(
                    "Marital Status",
                    MastersCache.getMaritalStatusName(
                        profile.marital_status
                    )
                )

                InfoRow(
                    label = "Active",
                    value = if (profile.is_active == true) {
                        "Yes"
                    } else {
                        "No"
                    }
                )

                InfoRow(
                    "Manglik",
                    when (profile.manglik) {
                        true -> "Yes"
                        false -> "No"
                        null -> "-"
                    }
                )

                InfoRow(
                    "About Me",
                    profile.about_me ?: "-"
                )

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                HorizontalDivider()

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                // =========================
                // PERSONAL INFORMATION
                // =========================

                SectionTitle("Personal Information")

                InfoRow(
                    "Date of Birth",
                    profile.date_of_birth ?: "-"
                )

                InfoRow(
                    "Height",
                    profile.height_cm?.let {
                        "$it cm"
                    } ?: "-"
                )

                InfoRow(
                    "Weight",
                    profile.weight_kg?.let {
                        "$it kg"
                    } ?: "-"
                )

                InfoRow(
                    "Blood Group",
                    MastersCache.getBloodGroupName(
                        profile.blood_group
                    )
                )

                InfoRow(
                    "Diet Preference",
                    MastersCache.getDietPreferenceName(
                        profile.diet_preference
                    )
                )

                InfoRow(
                    label = "Mother Tongue",
                    value = profile.mother_tongue ?: "-"
                )

                InfoRow(
                    "Disability",
                    MastersCache.getDisabilityName(
                        profile.disability
                    )
                )

                InfoRow(
                    "Birth Place",
                    profile.birth_place ?: "-"
                )

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                HorizontalDivider()

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                // =========================
                // RELIGIOUS INFORMATION
                // =========================

                SectionTitle("Religious Information")

                InfoRow(
                    label = "Sampraday",
                    value = profile.sampraday ?: "-"
                )

                InfoRow(
                    label = "Guru",
                    value = profile.guru ?: "-"
                )

                InfoRow(
                    label = "Caste",
                    value = profile.caste ?: "-"
                )

                InfoRow(
                    label = "Sub Caste",
                    value = profile.sub_caste ?: "-"
                )

                InfoRow(
                    label = "Gotra",
                    value = profile.gotra ?: "-"
                )

                InfoRow(
                    label = "Nakshatra",
                    value = profile.nakshatra ?: "-"
                )

                InfoRow(
                    label = "Zodiac",
                    value = profile.zodiac ?: "-"
                )

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                HorizontalDivider()

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                // =========================
                // EDUCATION & CAREER
                // =========================

                SectionTitle("Education & Career")

                InfoRow(
                    label = "Education",
                    value = profile.education ?: "-"
                )

                InfoRow(
                    label = "Occupation",
                    value = profile.occupation ?: "-"
                )

                InfoRow(
                    "Annual Income",
                    profile.annual_income ?: "-"
                )

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                HorizontalDivider()

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                // =========================
                // FAMILY INFORMATION
                // =========================

                SectionTitle("Family Information")

                InfoRow(
                    "Father Name",
                    profile.father_name ?: "-"
                )

                InfoRow(
                    "Mother Name",
                    profile.mother_name ?: "-"
                )

                InfoRow(
                    label = "Occupation",
                    value = profile.father_occupation ?: "-"
                )

                InfoRow(
                    "Married Brothers",
                    profile.married_brothers?.toString()
                        ?: "-"
                )

                InfoRow(
                    "Married Sisters",
                    profile.married_sisters?.toString()
                        ?: "-"
                )

                InfoRow(
                    "Unmarried Brothers",
                    profile.unmarried_brothers?.toString()
                        ?: "-"
                )

                InfoRow(
                    "Unmarried Sisters",
                    profile.unmarried_sisters?.toString()
                        ?: "-"
                )

                InfoRow(
                    "Family Satsangi",
                    when (profile.family_is_satsangi) {
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

                // =========================
                // LOCATION
                // =========================

                SectionTitle("Location")

                InfoRow(
                    "Country",
                    LocationCache.getCountryName(
                        profile.country
                    )
                )

                InfoRow(
                    "State",
                    LocationCache.getStateName(
                        countryId = profile.country,
                        stateId = profile.state
                    )
                )

                InfoRow(
                    "City",
                    LocationCache.getCityName(
                        stateId = profile.state,
                        cityId = profile.city
                    )
                )

                InfoRow(
                    "Birth Place",
                    profile.birth_place ?: "-"
                )

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                HorizontalDivider()

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                // =========================
                // CONTACT
                // =========================

                SectionTitle("Contact")

                InfoRow(
                    "WhatsApp",
                    profile.whatsapp_number ?: "-"
                )

                InfoRow(
                    "Profile Managed By",
                    MastersCache.getProfileManagedByName(
                        profile.profile_managed_by
                    )
                )

                Spacer(
                    modifier = Modifier.height(20.dp)
                )
            }
        }

        else -> {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                Text("Profile not found")

            }
        }
    }
}

// ==========================================
// Section Title
// ==========================================

@Composable
fun SectionTitle(
    title: String
) {

    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge
    )

    Spacer(
        modifier = Modifier.height(12.dp)
    )
}

// ==========================================
// Info Row
// ==========================================

@Composable
fun InfoRow(
    label: String,
    value: String
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            text = if (value.isBlank()) "-" else value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}