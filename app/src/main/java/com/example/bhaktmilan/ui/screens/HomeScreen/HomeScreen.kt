import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.bhaktmilan.ui.model.BhaktProfile
import androidx.compose.material3.FilterChip



@Composable
fun HomeScreen(
    profiles: List<BhaktProfile>,
    onProfileClick: (BhaktProfile) -> Unit
) {

    // ---------- UI STATES ----------
    var searchQuery by remember { mutableStateOf("") }
    var selectedSampraday by remember { mutableStateOf("All") }
    var selectedGender by remember { mutableStateOf("All") }
    var expanded by remember { mutableStateOf(false) }

// ---------- DROPDOWN DATA ----------
    val sampradayList = listOf("All") +
            profiles.map { it.sampraday }.distinct()

// ---------- FILTER LOGIC ----------
    val filteredProfiles = profiles.filter { profile ->

        val matchSearch =
            profile.name.contains(searchQuery, ignoreCase = true) ||
                    profile.city.contains(searchQuery, ignoreCase = true)

        val matchSampraday =
            selectedSampraday == "All" ||
                    profile.sampraday.equals(selectedSampraday, ignoreCase = true)

        val matchGender =
            selectedGender == "All" ||
                    profile.gender.equals(selectedGender, ignoreCase = true)

        matchSearch && matchSampraday && matchGender
    }



    Column(modifier = Modifier.fillMaxSize()) {

        // 🔍 SEARCH BAR
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            placeholder = { Text("Search by name or city") },
            singleLine = true
        )

        // 🎚 FILTER DROPDOWN
        Box(modifier = Modifier.padding(horizontal = 12.dp)) {

            OutlinedButton(
                onClick = { expanded = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Sampraday: $selectedSampraday")
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                sampradayList.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(item) },
                        onClick = {
                            selectedSampraday = item
                            expanded = false
                        }
                    )
                }
            }


        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            FilterChip(
                selected = selectedGender == "All",
                onClick = { selectedGender = "All" },
                label = { Text("All") }
            )

            FilterChip(
                selected = selectedGender == "Male",
                onClick = { selectedGender = "Male" },
                label = { Text("Male") }
            )

            FilterChip(
                selected = selectedGender == "Female",
                onClick = { selectedGender = "Female" },
                label = { Text("Female") }
            )
        }



        // 📋 PROFILE LIST
        LazyColumn(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(filteredProfiles) { profile ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onProfileClick(profile) }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(profile.name, style = MaterialTheme.typography.titleMedium)
                        Text("${profile.age} yrs • ${profile.city}")
                        Text(profile.sampraday)
                    }
                }
            }
        }
    }
}
