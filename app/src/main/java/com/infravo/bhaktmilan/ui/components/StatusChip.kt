package com.infravo.bhaktmilan.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun StatusChip(
    status: String,
    modifier: Modifier = Modifier
) {

    val normalizedStatus = status.lowercase()

    val backgroundColor: Color
    val textColor: Color

    when (normalizedStatus) {

        "pending" -> {
            backgroundColor =
                MaterialTheme.colorScheme.secondaryContainer

            textColor =
                MaterialTheme.colorScheme.onSecondaryContainer
        }

        "accepted" -> {
            backgroundColor =
                Color(0xFFE8F5E9)

            textColor =
                Color(0xFF2E7D32)
        }

        "rejected" -> {
            backgroundColor =
                MaterialTheme.colorScheme.errorContainer

            textColor =
                MaterialTheme.colorScheme.onErrorContainer
        }

        "cancelled",
        "canceled" -> {
            backgroundColor =
                MaterialTheme.colorScheme.surfaceVariant

            textColor =
                MaterialTheme.colorScheme.onSurfaceVariant
        }

        else -> {
            backgroundColor =
                MaterialTheme.colorScheme.surfaceVariant

            textColor =
                MaterialTheme.colorScheme.onSurfaceVariant
        }
    }

    Text(
        text = status.replaceFirstChar {
            if (it.isLowerCase()) {
                it.titlecase()
            } else {
                it.toString()
            }
        },

        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(backgroundColor)
            .padding(
                horizontal = 10.dp,
                vertical = 5.dp
            ),

        style = MaterialTheme.typography.labelMedium,

        color = textColor
    )
}