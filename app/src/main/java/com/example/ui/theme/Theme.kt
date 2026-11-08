package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val RoyalDarkColorScheme = darkColorScheme(
    primary = GoldMetallic,
    onPrimary = RoyalBlueDeep,
    primaryContainer = RoyalBlueSurface,
    onPrimaryContainer = GoldLight,
    secondary = GoldPrimary,
    onSecondary = RoyalBlueDeep,
    secondaryContainer = Color(0xFF1E293B),
    onSecondaryContainer = SilverHighlight,
    tertiary = RoyalBlueLight,
    onTertiary = Color.White,
    background = RoyalBlueDeep,
    onBackground = Color(0xFFF1F5F9),
    surface = RoyalBlueDark,
    onSurface = Color(0xFFF8FAFC),
    surfaceVariant = RoyalBlueSurface,
    onSurfaceVariant = SilverMuted,
    outline = GoldDark,
    outlineVariant = Color(0xFF283B6B),
    error = ErrorRed,
    onError = Color.White
)

private val RoyalLightColorScheme = lightColorScheme(
    primary = RoyalBluePrimary,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFDBEAFE),
    onPrimaryContainer = RoyalBlueDeep,
    secondary = GoldDark,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFEF3C7),
    onSecondaryContainer = Color(0xFF78350F),
    tertiary = RoyalBlueBright,
    onTertiary = Color.White,
    background = Color(0xFFF8FAFC),
    onBackground = Color(0xFF0F172A),
    surface = Color.White,
    onSurface = Color(0xFF0F172A),
    surfaceVariant = Color(0xFFEEF2F6),
    onSurfaceVariant = Color(0xFF475569),
    outline = GoldPrimary,
    outlineVariant = Color(0xFFCBD5E1),
    error = ErrorRed,
    onError = Color.White
)

@Composable
fun ARDownloaderTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) RoyalDarkColorScheme else RoyalLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}
