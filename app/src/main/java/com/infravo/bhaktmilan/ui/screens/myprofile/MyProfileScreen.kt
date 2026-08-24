package com.infravo.bhaktmilan.ui.screens.myprofile

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.infravo.bhaktmilan.data.remote.cache.LocationCache
import com.infravo.bhaktmilan.data.remote.cache.MastersCache
import com.infravo.bhaktmilan.data.remote.response.ProfileDetail
import com.infravo.bhaktmilan.ui.viewmodel.MyProfileUiState

@Composable
fun MyProfileScreen(
    uiState: MyProfileUiState,
    onClearError: () -> Unit,
    onEditProfile: (ProfileDetail) -> Unit,
    onPremiumClick: () -> Unit
) {

    val context = LocalContext.current

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
        // Profile
        // ==========================================

        uiState.profile != null -> {

            val profile = uiState.profile

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {

                // ==========================================
                // Header
                // ==========================================

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = "My Profile",
                        style = MaterialTheme.typography.headlineSmall
                    )

                    OutlinedButton(
                        onClick = {
                            onEditProfile(profile)
                        }
                    ) {
                        Text("Edit")
                    }
                }

                Spacer(
                    modifier = Modifier.height(20.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    OutlinedButton(
                        onClick = onPremiumClick
                    ) {
                        Text("Premium")
                    }

                    Spacer(
                        modifier = Modifier.width(8.dp)
                    )

                    OutlinedButton(
                        onClick = {
                            onEditProfile(profile)
                        }
                    ) {
                        Text("Edit")
                    }
                }
                // ==========================================
                // BASIC
                // ==========================================

                InfoRow(
                    "Name",
                    profile.full_name ?: "-"
                )

                InfoRow(
                    "Age",
                    profile.age?.toString() ?: "-"
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
                    "Active",
                    when (profile.is_active) {
                        true -> "Yes"
                        false -> "No"
                        null -> "-"
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

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                HorizontalDivider()

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                // ==========================================
                // PERSONAL
                // ==========================================

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
                    "Mother Tongue",
                    profile.mother_tongue ?: "-"
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

                // ==========================================
                // RELIGIOUS
                // ==========================================

                SectionTitle("Religious Information")

                InfoRow(
                    "Sampraday",
                    profile.sampraday ?: "-"
                )

                InfoRow(
                    "Guru",
                    profile.guru ?: "-"
                )

                InfoRow(
                    "Caste",
                    profile.caste ?: "-"
                )

                InfoRow(
                    "Sub Caste",
                    profile.sub_caste ?: "-"
                )

                InfoRow(
                    "Gotra",
                    profile.gotra ?: "-"
                )

                InfoRow(
                    "Nakshatra",
                    profile.nakshatra ?: "-"
                )

                InfoRow(
                    "Zodiac",
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

                SectionTitle("Education & Career")

                InfoRow(
                    "Education",
                    profile.education ?: "-"
                )

                InfoRow(
                    "Occupation",
                    profile.occupation ?: "-"
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

                // ==========================================
                // FAMILY
                // ==========================================

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
                    "Father Occupation",
                    profile.father_occupation ?: "-"
                )

                InfoRow(
                    "Married Brothers",
                    profile.married_brothers?.toString() ?: "-"
                )

                InfoRow(
                    "Married Sisters",
                    profile.married_sisters?.toString() ?: "-"
                )

                InfoRow(
                    "Unmarried Brothers",
                    profile.unmarried_brothers?.toString() ?: "-"
                )

                InfoRow(
                    "Unmarried Sisters",
                    profile.unmarried_sisters?.toString() ?: "-"
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

                // ==========================================
                // LOCATION
                // ==========================================

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

                // ==========================================
                // CONTACT
                // ==========================================

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

        // ==========================================
        // Empty
        // ==========================================

        else -> {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = "Profile not found"
                )
            }
        }
    }
}

@Composable
private fun SectionTitle(
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

@Composable
private fun InfoRow(
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
            text = value.ifBlank { "-" },
            style = MaterialTheme.typography.bodyMedium
        )
    }
}