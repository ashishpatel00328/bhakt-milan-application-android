package com.infravo.bhaktmilan.ui.screens.requests

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.infravo.bhaktmilan.ui.theme.AppBackground
import com.infravo.bhaktmilan.ui.theme.BhaktMaroon
import com.infravo.bhaktmilan.ui.theme.BhaktMaroonLight
import com.infravo.bhaktmilan.ui.theme.BorderColor
import com.infravo.bhaktmilan.ui.theme.PremiumGold
import com.infravo.bhaktmilan.ui.theme.SuccessGreen
import com.infravo.bhaktmilan.ui.theme.SuccessGreenLight
import com.infravo.bhaktmilan.ui.theme.TextMuted
import com.infravo.bhaktmilan.ui.theme.TextPrimary
import com.infravo.bhaktmilan.ui.theme.TextSecondary

data class ReceivedRequestItem(
    val id: Int,
    val profileId: String,
    val name: String,
    val age: Int,
    val city: String,
    val state: String,
    val profilePhoto: String?,
    val message: String?,
    val status: String,
    val time: String
)

@Composable
fun ReceivedRequests(
    requests: List<ReceivedRequestItem> = emptyList(),
    onProfileClick: (ReceivedRequestItem) -> Unit = {},
    onAccept: (ReceivedRequestItem) -> Unit = {},
    onReject: (ReceivedRequestItem) -> Unit = {},
    onSentClick: () -> Unit = {}
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
    ) {

        // ---------------------------------------------------------
        // TOP BAR
        // ---------------------------------------------------------

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 20.dp,
                    end = 10.dp,
                    top = 18.dp,
                    bottom = 8.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "Requests",
                modifier = Modifier.weight(1f),
                color = TextPrimary,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )

            IconButton(
                onClick = {}
            ) {

                Icon(
                    imageVector = Icons.Outlined.NotificationsNone,
                    contentDescription = "Notifications",
                    tint = TextPrimary,
                    modifier = Modifier.size(23.dp)
                )
            }
        }

        // ---------------------------------------------------------
        // RECEIVED / SENT TABS
        // ---------------------------------------------------------

        RequestTabs(
            receivedSelected = true,
            onReceivedClick = {},
            onSentClick = onSentClick
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        // ---------------------------------------------------------
        // REQUEST LIST
        // ---------------------------------------------------------

        if (requests.isEmpty()) {

            EmptyRequests()

        } else {

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 14.dp,
                    end = 14.dp,
                    top = 4.dp,
                    bottom = 24.dp
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                items(
                    items = requests,
                    key = { it.id }
                ) { request ->

                    ReceivedRequestCard(
                        request = request,
                        onProfileClick = {
                            onProfileClick(request)
                        },
                        onAccept = {
                            onAccept(request)
                        },
                        onReject = {
                            onReject(request)
                        }
                    )
                }
            }
        }
    }
}


// =================================================================
// TABS
// =================================================================

@Composable
private fun RequestTabs(
    receivedSelected: Boolean,
    onReceivedClick: () -> Unit,
    onSentClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 20.dp
            )
            .clip(
                RoundedCornerShape(10.dp)
            )
            .background(
                Color.White
            )
            .border(
                width = 1.dp,
                color = BorderColor,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(3.dp)
    ) {

        RequestTab(
            text = "Received",
            selected = receivedSelected,
            onClick = onReceivedClick,
            modifier = Modifier.weight(1f)
        )

        RequestTab(
            text = "Sent",
            selected = !receivedSelected,
            onClick = onSentClick,
            modifier = Modifier.weight(1f)
        )
    }
}


@Composable
private fun RequestTab(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier
) {

    Box(
        modifier = modifier
            .clip(
                RoundedCornerShape(8.dp)
            )
            .background(
                if (selected) {
                    BhaktMaroon
                } else {
                    Color.Transparent
                }
            )
            .clickable {
                onClick()
            }
            .padding(
                vertical = 9.dp
            ),
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = text,
            color = if (selected) {
                Color.White
            } else {
                TextSecondary
            },
            fontSize = 13.sp,
            fontWeight = if (selected) {
                FontWeight.SemiBold
            } else {
                FontWeight.Medium
            }
        )
    }
}


// =================================================================
// RECEIVED REQUEST CARD
// =================================================================

@Composable
private fun ReceivedRequestCard(
    request: ReceivedRequestItem,
    onProfileClick: () -> Unit,
    onAccept: () -> Unit,
    onReject: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(18.dp)
            )
            .background(Color.White)
            .border(
                width = 1.dp,
                color = BorderColor,
                shape = RoundedCornerShape(18.dp)
            )
            .padding(11.dp)
    ) {

        // ---------------------------------------------------------
        // PROFILE ROW
        // ---------------------------------------------------------

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onProfileClick()
                },
            verticalAlignment = Alignment.Top
        ) {

            ProfilePhoto(
                imageUrl = request.profilePhoto
            )

            Spacer(
                modifier = Modifier.width(11.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = request.name,
                        color = TextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

                    Icon(
                        imageVector = Icons.Outlined.Star,
                        contentDescription = null,
                        tint = PremiumGold,
                        modifier = Modifier.size(17.dp)
                    )
                }

                Spacer(
                    modifier = Modifier.height(3.dp)
                )

                Text(
                    text = request.profileId,
                    color = TextMuted,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(
                    modifier = Modifier.height(5.dp)
                )

                Text(
                    text = "${request.age} Years • ${request.city}, ${request.state}",
                    color = TextSecondary,
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(
                    modifier = Modifier.height(5.dp)
                )

                StatusBadge(
                    status = request.status
                )
            }

            Text(
                text = request.time,
                color = TextMuted,
                fontSize = 10.sp
            )
        }

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        // ---------------------------------------------------------
        // STATUS + MESSAGE
        // ---------------------------------------------------------

        Text(
            text = "Status: ${request.status}",
            color = TextSecondary,
            fontSize = 11.sp
        )

        Spacer(
            modifier = Modifier.height(3.dp)
        )

        Text(
            text = "Message: ${request.message ?: "Hi, I liked your profile."}",
            color = TextSecondary,
            fontSize = 11.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        // ---------------------------------------------------------
        // ACTION BUTTONS
        // ---------------------------------------------------------

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(9.dp)
        ) {

            Button(
                onClick = onAccept,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(9.dp),
                contentPadding = PaddingValues(
                    vertical = 8.dp
                ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SuccessGreen,
                    contentColor = Color.White
                )
            ) {

                Icon(
                    imageVector = Icons.Outlined.Check,
                    contentDescription = null,
                    modifier = Modifier.size(15.dp)
                )

                Spacer(
                    modifier = Modifier.width(5.dp)
                )

                Text(
                    text = "Accept",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            OutlinedButton(
                onClick = onReject,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(9.dp),
                contentPadding = PaddingValues(
                    vertical = 8.dp
                )
            ) {

                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = null,
                    tint = BhaktMaroon,
                    modifier = Modifier.size(15.dp)
                )

                Spacer(
                    modifier = Modifier.width(5.dp)
                )

                Text(
                    text = "Reject",
                    color = BhaktMaroon,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}


// =================================================================
// PROFILE PHOTO
// =================================================================

@Composable
private fun ProfilePhoto(
    imageUrl: String?
) {

    Box(
        modifier = Modifier
            .size(72.dp)
            .clip(CircleShape)
            .background(BhaktMaroonLight),
        contentAlignment = Alignment.Center
    ) {

        if (!imageUrl.isNullOrBlank()) {

            AsyncImage(
                model = imageUrl,
                contentDescription = "Profile photo",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

        } else {

            Icon(
                imageVector = Icons.Outlined.Person,
                contentDescription = null,
                tint = BhaktMaroon,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}


// =================================================================
// STATUS BADGE
// =================================================================

@Composable
private fun StatusBadge(
    status: String
) {

    Row(
        modifier = Modifier
            .clip(
                RoundedCornerShape(50.dp)
            )
            .background(SuccessGreenLight)
            .padding(
                horizontal = 7.dp,
                vertical = 3.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(SuccessGreen)
        )

        Spacer(
            modifier = Modifier.width(4.dp)
        )

        Text(
            text = status.replaceFirstChar {
                it.uppercase()
            },
            color = SuccessGreen,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium
        )
    }
}


// =================================================================
// EMPTY STATE
// =================================================================

@Composable
private fun EmptyRequests() {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                start = 35.dp,
                end = 35.dp,
                bottom = 35.dp
            ),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .size(78.dp)
                    .clip(CircleShape)
                    .background(BhaktMaroonLight),
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = Icons.Outlined.FavoriteBorder,
                    contentDescription = null,
                    tint = BhaktMaroon,
                    modifier = Modifier.size(35.dp)
                )
            }

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Text(
                text = "No requests found",
                color = TextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text = "When someone sends you an interest, their request will appear here.",
                color = TextSecondary,
                fontSize = 13.sp,
                lineHeight = 19.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}