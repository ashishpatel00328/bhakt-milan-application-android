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
import coil.compose.AsyncImage
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder

import androidx.compose.ui.graphics.Color








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
    val likedMap = remember { mutableStateMapOf<Int, Boolean>() }
    val shortlistedMap = remember { mutableStateMapOf<Int, Boolean>() }


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
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(filteredProfiles) { profile ->

                val isLiked = likedMap[profile.id] == true
                val isShortlisted = shortlistedMap[profile.id] == true

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onProfileClick(profile) } // ✅ YAHI CLICK
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        // Profile Image
                        AsyncImage(
                            model = profile.profileImageUrl,
                            contentDescription = profile.name,
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        // Info Section
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                profile.name,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text("${profile.age} yrs • ${profile.city}")
                            Text(
                                profile.sampraday,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }

                        // Action Icons (Separate clicks)
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            IconButton(onClick = {
                                likedMap[profile.id] = !isLiked
                            }) {
                                Icon(
                                    imageVector = if (isLiked)
                                        Icons.Filled.Favorite
                                    else
                                        Icons.Filled.FavoriteBorder,
                                    contentDescription = "Send Interest",
                                    tint = if (isLiked) Color.Red else Color.Black
                                )
                            }

                            IconButton(onClick = {
                                shortlistedMap[profile.id] = !isShortlisted
                            }) {
                                Icon(
                                    imageVector = if (isShortlisted)
                                        Icons.Filled.Star
                                    else
                                        Icons.Filled.Star,
                                    contentDescription = "Shortlist",
                                    tint = if (isShortlisted) Color(0xFFFFC107) else Color.Black
                                )
                            }
                        }
                    }
                }

            }
        }





    }
}
