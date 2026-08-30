package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Modern Vibrant Travel Theme Colors
val TravelSkyBlue = Color(0xFF0284C7)
val TravelSkyBlueLight = Color(0xFF38BDF8)
val TravelSkyBlueDark = Color(0xFF0369A1)
val TravelCyan = Color(0xFF06B6D4)

val TravelSunsetCoral = Color(0xFFFF5722)
val TravelSunsetOrange = Color(0xFFF97316)
val TravelSunGold = Color(0xFFF59E0B)
val TravelSunAmber = Color(0xFFFBBF24)

val TravelEmerald = Color(0xFF10B981)
val TravelTeal = Color(0xFF0D9488)
val TravelRose = Color(0xFFF43F5E)
val TravelPurple = Color(0xFF8B5CF6)

// Dark Theme Surfaces - Sleek Deep Oceanic Midnight
val DarkBackground = Color(0xFF0B1120)
val DarkSurface = Color(0xFF111D38)
val DarkSurfaceVariant = Color(0xFF1E293B)
val DarkCard = Color(0xFF16203A)
val DarkBorder = Color(0xFF334155)
val TextPrimaryDark = Color(0xFFF8FAFC)
val TextSecondaryDark = Color(0xFF94A3B8)

// Light Theme Surfaces - Crisp Breezy Canvas
val LightBackground = Color(0xFFF0F6FF)
val LightSurface = Color(0xFFFFFFFF)
val LightSurfaceVariant = Color(0xFFE2E8F0)
val LightCard = Color(0xFFFFFFFF)
val LightBorder = Color(0xFFCBD5E1)
val TextPrimaryLight = Color(0xFF0F172A)
val TextSecondaryLight = Color(0xFF475569)

// Beautiful Gradient Brushes for Travel Visuals
val HeroSunsetGradient = Brush.horizontalGradient(
    colors = listOf(Color(0xFF0284C7), Color(0xFF0EA5E9), Color(0xFF06B6D4))
)

val CardGlowGradient = Brush.linearGradient(
    colors = listOf(Color(0xFF0284C7).copy(alpha = 0.15f), Color(0xFFF97316).copy(alpha = 0.08f))
)

val CategoryGradientNature = Brush.horizontalGradient(
    colors = listOf(Color(0xFF059669), Color(0xFF10B981))
)

val CategoryGradientHistorical = Brush.horizontalGradient(
    colors = listOf(Color(0xFFD97706), Color(0xFFF59E0B))
)

val CategoryGradientSpiritual = Brush.horizontalGradient(
    colors = listOf(Color(0xFF7C3AED), Color(0xFF8B5CF6))
)

val CategoryGradientBeach = Brush.horizontalGradient(
    colors = listOf(Color(0xFF0284C7), Color(0xFF06B6D4))
)
