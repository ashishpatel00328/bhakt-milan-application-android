package com.infravo.bhaktmilan.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.infravo.bhaktmilan.ui.theme.PrimaryButtonHeight

@Composable
fun BhaktOutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {

    OutlinedButton(
        onClick = onClick,

        modifier = modifier
            .fillMaxWidth()
            .height(PrimaryButtonHeight),

        enabled = enabled,

        shape = MaterialTheme.shapes.medium,

        border = BorderStroke(
            width = 1.dp,

            color = if (enabled) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.outline.copy(
                    alpha = 0.4f
                )
            }
        )
    ) {

        Text(
            text = text,

            style = MaterialTheme.typography.labelLarge,

            color = if (enabled) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurface.copy(
                    alpha = 0.38f
                )
            }
        )
    }
}