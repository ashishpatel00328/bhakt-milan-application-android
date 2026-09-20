package com.infravo.bhaktmilan.ui.screens.onboarding

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.infravo.bhaktmilan.data.remote.response.MasterItem
import com.infravo.bhaktmilan.ui.model.OnboardingFormData
import com.infravo.bhaktmilan.ui.theme.AppBackground
import com.infravo.bhaktmilan.ui.theme.BhaktMaroon
import com.infravo.bhaktmilan.ui.theme.BorderColor
import com.infravo.bhaktmilan.ui.theme.DividerColor
import com.infravo.bhaktmilan.ui.theme.PremiumGold
import com.infravo.bhaktmilan.ui.theme.SoftCream
import com.infravo.bhaktmilan.ui.theme.SurfaceBackground
import com.infravo.bhaktmilan.ui.theme.TextMuted
import com.infravo.bhaktmilan.ui.theme.TextPrimary
import com.infravo.bhaktmilan.ui.theme.TextSecondary
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
            .padding(
                horizontal = 16.dp,
                vertical = 14.dp
            ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // ==========================================
        // TOP HERO
        // ==========================================

        ProfileFormHeader(
            isEditMode = isEditMode
        )

        // ==========================================
        // LOADING
        // ==========================================

        if (uiState.isLoading) {

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = SurfaceBackground
                ),
                border = BorderStroke(
                    1.dp,
                    DividerColor
                )
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {

                    CircularProgressIndicator(
                        color = BhaktMaroon
                    )
                }
            }
        }

        // ==========================================
        // API ERROR
        // ==========================================

        uiState.error?.let { message ->

            ErrorCard(
                message = message
            )
        }

        // ==========================================
        // VALIDATION ERROR
        // ==========================================

        validationError?.let { message ->

            ErrorCard(
                message = message
            )
        }

        // ==========================================
        // PERSONAL DETAILS
        // ==========================================

        FormSectionCard(
            number = "01",
            title = "Personal Details",
            subtitle = "Tell us the basic details about yourself"
        ) {

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
        }

        // ==========================================
        // CONTACT & LOCATION
        // ==========================================

        FormSectionCard(
            number = "02",
            title = "Contact & Location",
            subtitle = "Where are you currently based?"
        ) {

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
        }

        // ==========================================
        // PHYSICAL & LIFESTYLE
        // ==========================================

        FormSectionCard(
            number = "03",
            title = "Physical & Lifestyle",
            subtitle = "Help your profile feel more complete"
        ) {

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
        }

        // ==========================================
        // RELIGIOUS INFORMATION
        // ==========================================

        FormSectionCard(
            number = "04",
            title = "Religious Information",
            subtitle = "Share your spiritual and community background"
        ) {

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
        }

        // ==========================================
        // HOROSCOPE
        // ==========================================

        FormSectionCard(
            number = "05",
            title = "Horoscope",
            subtitle = "Optional details for compatibility"
        ) {

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
        }

        // ==========================================
        // EDUCATION & CAREER
        // ==========================================

        FormSectionCard(
            number = "06",
            title = "Education & Career",
            subtitle = "Tell potential matches about your professional life"
        ) {

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
        }

        // ==========================================
        // FAMILY
        // ==========================================

        FormSectionCard(
            number = "07",
            title = "Family Information",
            subtitle = "A little about your family"
        ) {

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
        }

        // ==========================================
        // OTHER
        // ==========================================

        FormSectionCard(
            number = "08",
            title = "Other Details",
            subtitle = "A few additional details"
        ) {

            InputField(
                label = "Mother Tongue",
                value = form.motherTongue,
                onChange = {

                    form = form.copy(
                        motherTongue = it
                    )
                }
            )
        }

        // ==========================================
        // ABOUT YOURSELF
        // ==========================================

        FormSectionCard(
            number = "09",
            title = "About Yourself",
            subtitle = "Let your personality speak for itself"
        ) {

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
                    .heightIn(
                        min = 150.dp
                    ),
                maxLines = 8,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BhaktMaroon,
                    unfocusedBorderColor = BorderColor,
                    focusedLabelColor = BhaktMaroon,
                    unfocusedLabelColor = TextSecondary,
                    cursorColor = BhaktMaroon,
                    focusedContainerColor = SurfaceBackground,
                    unfocusedContainerColor = SurfaceBackground
                )
            )
        }

        // ==========================================
        // SUBMIT AREA
        // ==========================================

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
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Text(
                    text = if (isEditMode) {
                        "Ready to update your profile?"
                    } else {
                        "Ready to complete your profile?"
                    },
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = TextPrimary
                )

                Text(
                    text = if (isEditMode) {
                        "Review your details and save the latest changes."
                    } else {
                        "Complete your details to make your profile more meaningful."
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    enabled = !uiState.isSubmitting,
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BhaktMaroon,
                        contentColor = Color.White,
                        disabledContainerColor = BhaktMaroon.copy(
                            alpha = 0.5f
                        ),
                        disabledContentColor = Color.White
                    ),
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
                            modifier = Modifier.size(20.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )

                    } else {

                        Text(
                            text = if (isEditMode) {
                                "Save Changes"
                            } else {
                                "Complete Profile"
                            },
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Text(
                    text = "Bhakt Milan • Devotion meets destiny",
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted
                )
            }
        }

        Spacer(
            modifier = Modifier.height(12.dp)
        )
    }
}

// ==========================================
// HEADER
// ==========================================

@Composable
private fun ProfileFormHeader(
    isEditMode: Boolean
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = SoftCream
        ),
        border = BorderStroke(
            1.dp,
            BorderColor
        )
    ) {

        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

//                Box(
//                    modifier = Modifier
//                        .size(42.dp),
//                    contentAlignment = Alignment.Center
//                ) {
//
//                    Text(
//                        text = "ॐ",
//                        color = BhaktMaroon,
//                        style = MaterialTheme.typography.titleLarge,
//                        fontWeight = FontWeight.Bold
//                    )
//                }

                Column {

                    Text(
                        text = if (isEditMode) {
                            "Edit Profile"
                        } else {
                            "Create Your Profile"
                        },
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = TextPrimary
                    )

                    Text(
                        text = "Bhakt Milan",
                        style = MaterialTheme.typography.labelMedium,
                        color = PremiumGold,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Text(
                text = if (isEditMode) {
                    "Keep your profile fresh and let your personality shine through."
                } else {
                    "A meaningful profile helps you connect with the right person."
                },
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )

            HorizontalDivider(
                color = DividerColor
            )

            Text(
                text = if (isEditMode) {
                    "Update your details below"
                } else {
                    "Complete your details step by step"
                },
                style = MaterialTheme.typography.labelMedium,
                color = BhaktMaroon,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

// ==========================================
// FORM SECTION CARD
// ==========================================

@Composable
private fun FormSectionCard(
    number: String,
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
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Box(
                    modifier = Modifier
                        .size(38.dp),
                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text = number,
                        color = PremiumGoldDarkSafe(),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = TextPrimary
                    )

                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
            }

            HorizontalDivider(
                color = DividerColor
            )

            content()
        }
    }
}

// ==========================================
// ERROR CARD
// ==========================================

@Composable
private fun ErrorCard(
    message: String
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {

        Text(
            text = message,
            modifier = Modifier.padding(14.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onErrorContainer
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
            .heightIn(
                min = 54.dp
            ),
        singleLine = true,
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = BhaktMaroon,
            unfocusedBorderColor = BorderColor,
            focusedLabelColor = BhaktMaroon,
            unfocusedLabelColor = TextSecondary,
            cursorColor = BhaktMaroon,
            focusedContainerColor = SurfaceBackground,
            unfocusedContainerColor = SurfaceBackground
        )
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

    Box(
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
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = BhaktMaroon,
                unfocusedBorderColor = BorderColor,
                focusedLabelColor = BhaktMaroon,
                unfocusedLabelColor = TextSecondary,
                cursorColor = BhaktMaroon,
                focusedContainerColor = SurfaceBackground,
                unfocusedContainerColor = SurfaceBackground
            ),
            trailingIcon = {

                IconButton(
                    onClick = {
                        expanded = !expanded
                    }
                ) {

                    Text(
                        text = if (expanded) "⌃" else "⌄",
                        color = BhaktMaroon,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            containerColor = SurfaceBackground,
            tonalElevation = 4.dp,
            shadowElevation = 10.dp,
            shape = RoundedCornerShape(16.dp)
        ) {

            items.forEach { item ->

                val isSelected = item.id == selectedId

                DropdownMenuItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 6.dp,
                            vertical = 2.dp
                        ),
                    text = {

                        Text(
                            text = item.name,
                            color = if (isSelected) {
                                BhaktMaroon
                            } else {
                                TextPrimary
                            },
                            fontWeight = if (isSelected) {
                                FontWeight.SemiBold
                            } else {
                                FontWeight.Normal
                            }
                        )
                    },
                    trailingIcon = {

                        if (isSelected) {

                            Text(
                                text = "✓",
                                color = PremiumGold,
                                fontWeight = FontWeight.Bold
                            )
                        }
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
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = BhaktMaroon,
                unfocusedBorderColor = BorderColor,
                focusedLabelColor = BhaktMaroon,
                unfocusedLabelColor = TextSecondary,
                cursorColor = BhaktMaroon,
                focusedContainerColor = SurfaceBackground,
                unfocusedContainerColor = SurfaceBackground
            ),
            trailingIcon = {

                IconButton(
                    onClick = {
                        expanded = true
                    }
                ) {

                    Text(
                        text = "⌄",
                        color = BhaktMaroon,
                        style = MaterialTheme.typography.titleMedium
                    )
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

// ==========================================
// Premium Gold helper
// ==========================================

@Composable
private fun PremiumGoldDarkSafe(): Color {
    return MaterialTheme.colorScheme.secondary
}