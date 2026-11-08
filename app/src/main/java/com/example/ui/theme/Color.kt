package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Royal Blue Palette
val RoyalBlueDeep = Color(0xFF070E22)       // Dark background
val RoyalBlueDark = Color(0xFF0D1B3E)       // Surface / Card background
val RoyalBlueSurface = Color(0xFF142452)    // Elevated surface
val RoyalBluePrimary = Color(0xFF1E3A8A)    // Core Royal Blue
val RoyalBlueBright = Color(0xFF2563EB)     // Accent Royal Blue
val RoyalBlueLight = Color(0xFF60A5FA)      // Glow / Highlights

// Pure Gold & Metallic Palette
val GoldPrimary = Color(0xFFD4AF37)         // Classic Rich Gold
val GoldMetallic = Color(0xFFFFD700)        // Bright Radiant Gold
val GoldLight = Color(0xFFFFF099)           // Golden Shimmer / Text
val GoldDark = Color(0xFF997A15)            // Deep Gold border
val GoldAccent = Color(0xFFE5A93C)          // Warm Gold

// Platinum & Silver Highlights
val SilverHighlight = Color(0xFFE2E8F0)
val SilverMuted = Color(0xFF94A3B8)

// Status colors
val SuccessGreen = Color(0xFF10B981)
val ErrorRed = Color(0xFFEF4444)
val WarningAmber = Color(0xFFF59E0B)

// Luxury Gradients
val GoldGradient = Brush.linearGradient(
    colors = listOf(GoldLight, GoldPrimary, GoldAccent, GoldMetallic)
)

val RoyalBlueGradient = Brush.verticalGradient(
    colors = listOf(RoyalBlueDark, RoyalBlueDeep)
)

val CardLuxuryGradient = Brush.linearGradient(
    colors = listOf(Color(0xFF1A2E63), Color(0xFF0F1E44))
)

val ButtonGoldGradient = Brush.horizontalGradient(
    colors = listOf(Color(0xFFFFDF70), Color(0xFFD4AF37), Color(0xFFB8860B))
)
