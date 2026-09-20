package com.infravo.bhaktmilan.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(

    // ==========================================
    // Primary
    // ==========================================

    primary = BhaktMaroon,
    onPrimary = SurfaceBackground,

    primaryContainer = BhaktMaroonLight,
    onPrimaryContainer = BhaktMaroonDark,

    // ==========================================
    // Secondary
    // ==========================================

    secondary = PremiumGold,
    onSecondary = SurfaceBackground,

    secondaryContainer = PremiumGoldLight,
    onSecondaryContainer = PremiumGoldDark,

    // ==========================================
    // Background
    // ==========================================

    background = AppBackground,
    onBackground = TextPrimary,

    // ==========================================
    // Surface / Cards
    // ==========================================

    surface = SurfaceBackground,
    onSurface = TextPrimary,

    surfaceVariant = SoftCream,
    onSurfaceVariant = TextSecondary,

    // ==========================================
    // Error
    // ==========================================

    error = ErrorRed,
    onError = SurfaceBackground,

    errorContainer = ErrorRedLight,
    onErrorContainer = ErrorRed,

    // ==========================================
    // Outline / Borders
    // ==========================================

    outline = BorderColor,
    outlineVariant = DividerColor
)

@Composable
fun BhaktMilanTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColors,
        typography = AppTypography,
        content = content
    )
}