package com.example.bhaktmilan.ui.screens.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.bhaktmilan.ui.model.OnboardingFormData

@Composable
fun OnboardingScreen(
    onSubmit: (OnboardingFormData) -> Unit
) {
    var form by remember { mutableStateOf(OnboardingFormData()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {


        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Create Your Profile",
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Help us find the right match for you",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }


        SectionTitle("Personal Details")

        DropdownField(
            label = "Profile Managed By",
            options = listOf("Self", "Parents"),
            selected = form.profileManagedBy
        ) { form = form.copy(profileManagedBy = it) }

        InputField("Full Name", form.name) {
            form = form.copy(name = it)
        }

        InputField("Date of Birth", form.dob) {
            form = form.copy(dob = it)
        }

        InputField("Blood Group", form.bloodGroup) {
            form = form.copy(bloodGroup = it)
        }

        SectionTitle("Contact & Location")

        InputField("WhatsApp Number", form.whatsapp) {
            form = form.copy(whatsapp = it)
        }

        InputField("State", form.state) {
            form = form.copy(state = it)
        }

        InputField("City", form.city) {
            form = form.copy(city = it)
        }

        SectionTitle("Physical & Lifestyle")

        InputField("Height (cm)", form.height) {
            form = form.copy(height = it)
        }

        InputField("Weight (kg)", form.weight) {
            form = form.copy(weight = it)
        }

        DropdownField(
            label = "Eating Habit",
            options = listOf("Eat onion & garlic", "Trying to leave", "Never eat"),
            selected = form.diet
        ) { form = form.copy(diet = it) }

        SectionTitle("Religious Info")

        InputField("Sampraday", form.sampraday) {
            form = form.copy(sampraday = it)
        }

        InputField("Guru Name", form.guruName) {
            form = form.copy(guruName = it)
        }

        DropdownField(
            label = "Family also Satsangi?",
            options = listOf("Yes", "No"),
            selected = form.familySatsangi
        ) { form = form.copy(familySatsangi = it) }

        SectionTitle("About Yourself")

        OutlinedTextField(
            value = form.aboutMe,
            onValueChange = { form = form.copy(aboutMe = it) },
            label = { Text("About Yourself (max 250 words)") },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 160.dp),
            textStyle = MaterialTheme.typography.bodyMedium,
            maxLines = 8
        )


        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { onSubmit(form) }
        ) {
            Text("Complete Profile")
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.primary
    )
}


@Composable
fun InputField(
    label: String,
    value: String,
    onChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = { Text(label) },
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 52.dp),
        textStyle = MaterialTheme.typography.bodyMedium,
        singleLine = true
    )
}


@Composable
fun DropdownField(
    label: String,
    options: List<String>,
    selected: String,
    onSelect: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        OutlinedTextField(
            value = selected,
            onValueChange = {},
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { expanded = true }) {
                    Text("▼")
                }
            }
        )

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach {
                DropdownMenuItem(
                    text = { Text(it) },
                    onClick = {
                        onSelect(it)
                        expanded = false
                    }
                )
            }
        }
    }
}
