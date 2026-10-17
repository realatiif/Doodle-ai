package com.realatiif.doodleai.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = AccentCyan,
    onPrimary = NavyBg,
    primaryContainer = NavyCard,
    onPrimaryContainer = AccentCyan,
    secondary = AccentPurple,
    onSecondary = NavyBg,
    secondaryContainer = NavySurface,
    onSecondaryContainer = AccentPurple,
    tertiary = AccentYellow,
    onTertiary = NavyBg,
    background = NavyBg,
    onBackground = TextWhite,
    surface = NavySurface,
    onSurface = TextWhite,
    surfaceVariant = NavyCard,
    onSurfaceVariant = TextMuted,
    outline = NavyBorder,
    error = ErrorRed,
    onError = NavyBg
)

@Composable
fun DoodleAITheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // We enforce our curated dark gaming theme across all devices
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
