package com.infravo.bhaktmilan.ui.screens.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.infravo.bhaktmilan.data.remote.response.MasterItem
import com.infravo.bhaktmilan.ui.model.OnboardingFormData
import com.infravo.bhaktmilan.ui.viewmodel.OnboardingViewModel

@Composable
fun OnboardingScreen(
    onSuccess: () -> Unit,
    initialForm: OnboardingFormData? = null,
    isEditMode: Boolean = false,
    viewModel: OnboardingViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsState()

    var form by remember(initialForm) {
        mutableStateOf(
            initialForm ?: OnboardingFormData()
        )
    }

    var validationError by remember {
        mutableStateOf<String?>(null)
    }

    // ==========================================
    // Load dependent data for edit mode
    // ==========================================

    LaunchedEffect(initialForm) {

        initialForm?.country?.let { countryId ->
            viewModel.loadStates(countryId)
        }

        initialForm?.state?.let { stateId ->
            viewModel.loadCities(stateId)
        }
    }

    // ==========================================
    // Success
    // ==========================================

    LaunchedEffect(uiState.isCreated) {

        if (uiState.isCreated) {

            viewModel.clearCreatedState()

            onSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(
                rememberScrollState()
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        // ==========================================
        // Header
        // ==========================================

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor =
                    MaterialTheme.colorScheme.primaryContainer
            )
        ) {

            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                Text(
                    text = if (isEditMode) {
                        "Edit Your Profile"
                    } else {
                        "Create Your Profile"
                    },
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Text(
                    text = if (isEditMode) {
                        "Update your profile information"
                    } else {
                        "Help us find the right match for you"
                    },
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // ==========================================
        // Loading
        // ==========================================

        if (uiState.isLoading) {

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {

                CircularProgressIndicator()
            }
        }

        // ==========================================
        // API Error
        // ==========================================

        uiState.error?.let { message ->

            Text(
                text = message,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        // ==========================================
        // Validation Error
        // ==========================================

        validationError?.let { message ->

            Text(
                text = message,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        // ==========================================
        // PERSONAL DETAILS
        // ==========================================

        SectionTitle("Personal Details")

        MasterDropdown(
            label = "Profile Managed By *",
            items = uiState.profileManagedBy,
            selectedId = form.profileManagedBy,
            onSelected = {
                validationError = null

                form = form.copy(
                    profileManagedBy = it
                )
            }
        )

        MasterDropdown(
            label = "Gender *",
            items = uiState.genders,
            selectedId = form.gender,
            onSelected = {
                validationError = null

                form = form.copy(
                    gender = it
                )
            }
        )

        InputField(
            label = "Full Name *",
            value = form.fullName,
            onChange = {
                validationError = null

                form = form.copy(
                    fullName = it
                )
            }
        )

        InputField(
            label = "Date of Birth *",
            value = form.dateOfBirth,
            onChange = {
                validationError = null

                form = form.copy(
                    dateOfBirth = it
                )
            }
        )

        MasterDropdown(
            label = "Blood Group *",
            items = uiState.bloodGroups,
            selectedId = form.bloodGroup,
            onSelected = {
                validationError = null

                form = form.copy(
                    bloodGroup = it
                )
            }
        )

        MasterDropdown(
            label = "Marital Status *",
            items = uiState.maritalStatuses,
            selectedId = form.maritalStatus,
            onSelected = {
                validationError = null

                form = form.copy(
                    maritalStatus = it
                )
            }
        )

        // ==========================================
        // LOCATION
        // ==========================================

        SectionTitle("Contact & Location")

        MasterDropdown(
            label = "Country *",
            items = uiState.countries,
            selectedId = form.country,
            onSelected = { countryId ->

                validationError = null

                form = form.copy(
                    country = countryId,
                    state = null,
                    city = null
                )

                viewModel.loadStates(countryId)
            }
        )

        MasterDropdown(
            label = "State *",
            items = uiState.states,
            selectedId = form.state,
            onSelected = { stateId ->

                validationError = null

                form = form.copy(
                    state = stateId,
                    city = null
                )

                viewModel.loadCities(stateId)
            }
        )

        MasterDropdown(
            label = "City *",
            items = uiState.cities,
            selectedId = form.city,
            onSelected = {
                validationError = null

                form = form.copy(
                    city = it
                )
            }
        )

        InputField(
            label = "WhatsApp Number",
            value = form.whatsappNumber,
            onChange = {
                form = form.copy(
                    whatsappNumber = it
                )
            }
        )

        // ==========================================
        // PHYSICAL & LIFESTYLE
        // ==========================================

        SectionTitle("Physical & Lifestyle")

        InputField(
            label = "Height (cm)",
            value = form.heightCm,
            onChange = {
                form = form.copy(
                    heightCm = it
                )
            }
        )

        InputField(
            label = "Weight (kg)",
            value = form.weightKg,
            onChange = {
                form = form.copy(
                    weightKg = it
                )
            }
        )

        MasterDropdown(
            label = "Diet Preference *",
            items = uiState.dietPreferences,
            selectedId = form.dietPreference,
            onSelected = {
                validationError = null

                form = form.copy(
                    dietPreference = it
                )
            }
        )

        MasterDropdown(
            label = "Disability *",
            items = uiState.disabilities,
            selectedId = form.disability,
            onSelected = {
                validationError = null

                form = form.copy(
                    disability = it
                )
            }
        )

        // ==========================================
        // RELIGIOUS
        // ==========================================

        SectionTitle("Religious Information")

        InputField(
            label = "Sampraday",
            value = form.sampraday,
            onChange = {
                form = form.copy(
                    sampraday = it
                )
            }
        )

        InputField(
            label = "Guru Name",
            value = form.guruName,
            onChange = {
                form = form.copy(
                    guruName = it
                )
            }
        )

        InputField(
            label = "Caste",
            value = form.caste,
            onChange = {
                form = form.copy(
                    caste = it
                )
            }
        )

        InputField(
            label = "Sub Caste",
            value = form.subCaste,
            onChange = {
                form = form.copy(
                    subCaste = it
                )
            }
        )

        InputField(
            label = "Gotra",
            value = form.gotra,
            onChange = {
                form = form.copy(
                    gotra = it
                )
            }
        )

        // ==========================================
        // HOROSCOPE
        // ==========================================

        SectionTitle("Horoscope")

        InputField(
            label = "Nakshatra",
            value = form.nakshatra,
            onChange = {
                form = form.copy(
                    nakshatra = it
                )
            }
        )

        InputField(
            label = "Zodiac",
            value = form.zodiac,
            onChange = {
                form = form.copy(
                    zodiac = it
                )
            }
        )

        BooleanDropdown(
            label = "Manglik",
            selected = form.manglik,
            onSelected = {
                form = form.copy(
                    manglik = it
                )
            }
        )

        InputField(
            label = "Birth Place",
            value = form.birthPlace,
            onChange = {
                form = form.copy(
                    birthPlace = it
                )
            }
        )

        // ==========================================
        // EDUCATION & CAREER
        // ==========================================

        SectionTitle("Education & Career")

        InputField(
            label = "Education",
            value = form.education,
            onChange = {
                form = form.copy(
                    education = it
                )
            }
        )

        InputField(
            label = "Occupation",
            value = form.occupation,
            onChange = {
                form = form.copy(
                    occupation = it
                )
            }
        )

        InputField(
            label = "Annual Income",
            value = form.annualIncome,
            onChange = {
                form = form.copy(
                    annualIncome = it
                )
            }
        )

        // ==========================================
        // FAMILY
        // ==========================================

        SectionTitle("Family Information")

        InputField(
            label = "Father Name",
            value = form.fatherName,
            onChange = {
                form = form.copy(
                    fatherName = it
                )
            }
        )

        InputField(
            label = "Father Occupation",
            value = form.fatherOccupation,
            onChange = {
                form = form.copy(
                    fatherOccupation = it
                )
            }
        )

        InputField(
            label = "Mother Name",
            value = form.motherName,
            onChange = {
                form = form.copy(
                    motherName = it
                )
            }
        )

        InputField(
            label = "Married Brothers",
            value = form.marriedBrothers?.toString() ?: "",
            onChange = {
                form = form.copy(
                    marriedBrothers = it.toIntOrNull()
                )
            }
        )

        InputField(
            label = "Married Sisters",
            value = form.marriedSisters?.toString() ?: "",
            onChange = {
                form = form.copy(
                    marriedSisters = it.toIntOrNull()
                )
            }
        )

        InputField(
            label = "Unmarried Brothers",
            value = form.unmarriedBrothers?.toString() ?: "",
            onChange = {
                form = form.copy(
                    unmarriedBrothers = it.toIntOrNull()
                )
            }
        )

        InputField(
            label = "Unmarried Sisters",
            value = form.unmarriedSisters?.toString() ?: "",
            onChange = {
                form = form.copy(
                    unmarriedSisters = it.toIntOrNull()
                )
            }
        )

        BooleanDropdown(
            label = "Family also Satsangi?",
            selected = form.familyIsSatsangi,
            onSelected = {
                form = form.copy(
                    familyIsSatsangi = it
                )
            }
        )

        // ==========================================
        // CONTACT / OTHER
        // ==========================================

        InputField(
            label = "Mother Tongue",
            value = form.motherTongue,
            onChange = {
                form = form.copy(
                    motherTongue = it
                )
            }
        )

        // ==========================================
        // ABOUT
        // ==========================================

        SectionTitle("About Yourself")

        OutlinedTextField(
            value = form.aboutMe,
            onValueChange = {
                form = form.copy(
                    aboutMe = it
                )
            },
            label = {
                Text("About Yourself (max 250 words)")
            },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 160.dp),
            maxLines = 8
        )

        // ==========================================
        // SUBMIT
        // ==========================================

        Button(
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isSubmitting,
            onClick = {

                validationError =
                    validateRequiredFields(form)

                if (validationError == null) {

                    if (isEditMode) {
                        viewModel.updateProfile(form)
                    } else {
                        viewModel.createProfile(form)
                    }
                }
            }
        ) {

            if (uiState.isSubmitting) {

                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp)
                )

            } else {

                Text(
                    text = if (isEditMode) {
                        "Save Changes"
                    } else {
                        "Complete Profile"
                    }
                )
            }
        }

        Spacer(
            modifier = Modifier.height(20.dp)
        )
    }
}

// ==========================================
// Required Validation
// ==========================================

private fun validateRequiredFields(
    form: OnboardingFormData
): String? {

    if (form.fullName.isBlank()) {
        return "Full Name is required."
    }

    if (form.gender == null) {
        return "Gender is required."
    }

    if (form.dateOfBirth.isBlank()) {
        return "Date of Birth is required."
    }

    if (form.bloodGroup == null) {
        return "Blood Group is required."
    }

    if (form.maritalStatus == null) {
        return "Marital Status is required."
    }

    if (form.country == null) {
        return "Country is required."
    }

    if (form.state == null) {
        return "State is required."
    }

    if (form.city == null) {
        return "City is required."
    }

    if (form.disability == null) {
        return "Disability is required."
    }

    if (form.dietPreference == null) {
        return "Diet Preference is required."
    }

    if (form.profileManagedBy == null) {
        return "Profile Managed By is required."
    }

    return null
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
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.primary
    )
}

// ==========================================
// Text Input
// ==========================================

@Composable
fun InputField(
    label: String,
    value: String,
    onChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = {
            Text(label)
        },
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 52.dp),
        singleLine = true
    )
}

// ==========================================
// Master Dropdown
// ==========================================

@Composable
fun MasterDropdown(
    label: String,
    items: List<MasterItem>,
    selectedId: Int?,
    onSelected: (Int) -> Unit
) {

    var expanded by remember {
        mutableStateOf(false)
    }

    val selectedName =
        items.firstOrNull {
            it.id == selectedId
        }?.name ?: ""

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        OutlinedTextField(
            value = selectedName,
            onValueChange = {},
            label = {
                Text(label)
            },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            trailingIcon = {

                IconButton(
                    onClick = {
                        expanded = true
                    }
                ) {
                    Text("▼")
                }
            }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {

            items.forEach { item ->

                DropdownMenuItem(
                    text = {
                        Text(item.name)
                    },
                    onClick = {

                        onSelected(item.id)

                        expanded = false
                    }
                )
            }
        }
    }
}

// ==========================================
// Boolean Dropdown
// ==========================================

@Composable
fun BooleanDropdown(
    label: String,
    selected: Boolean?,
    onSelected: (Boolean) -> Unit
) {

    var expanded by remember {
        mutableStateOf(false)
    }

    val selectedText =
        when (selected) {
            true -> "Yes"
            false -> "No"
            null -> ""
        }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        OutlinedTextField(
            value = selectedText,
            onValueChange = {},
            label = {
                Text(label)
            },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            trailingIcon = {

                IconButton(
                    onClick = {
                        expanded = true
                    }
                ) {
                    Text("▼")
                }
            }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {

            DropdownMenuItem(
                text = {
                    Text("Yes")
                },
                onClick = {

                    onSelected(true)

                    expanded = false
                }
            )

            DropdownMenuItem(
                text = {
                    Text("No")
                },
                onClick = {

                    onSelected(false)

                    expanded = false
                }
            )
        }
    }
}