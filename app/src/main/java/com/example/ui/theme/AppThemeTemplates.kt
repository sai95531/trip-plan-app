package com.example.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

enum class AppTemplate(
    val id: String,
    val title: String,
    val subtitle: String,
    val emoji: String,
    val primaryColor: Color,
    val secondaryColor: Color,
    val accentColor: Color,
    val gradientColors: List<Color>
) {
    OCEANIC_AZURE(
        id = "oceanic_azure",
        title = "Oceanic Azure",
        subtitle = "Deep ocean blues, electric cyan & sapphire vibes",
        emoji = "🌊",
        primaryColor = Color(0xFF0284C7),
        secondaryColor = Color(0xFF06B6D4),
        accentColor = Color(0xFFF59E0B),
        gradientColors = listOf(Color(0xFF0284C7), Color(0xFF0EA5E9), Color(0xFF06B6D4))
    ),
    TROPICAL_SUNSET(
        id = "tropical_sunset",
        title = "Tropical Sunset",
        subtitle = "Warm coral orange, golden amber & resort glow",
        emoji = "🌅",
        primaryColor = Color(0xFFEA580C),
        secondaryColor = Color(0xFFF59E0B),
        accentColor = Color(0xFF0D9488),
        gradientColors = listOf(Color(0xFFEA580C), Color(0xFFF97316), Color(0xFFFBBF24))
    ),
    ALPINE_EMERALD(
        id = "alpine_emerald",
        title = "Alpine Emerald",
        subtitle = "Lush Nordic forest, fresh pine & mountain sage",
        emoji = "🌲",
        primaryColor = Color(0xFF059669),
        secondaryColor = Color(0xFF10B981),
        accentColor = Color(0xFFF59E0B),
        gradientColors = listOf(Color(0xFF047857), Color(0xFF10B981), Color(0xFF34D399))
    ),
    MIDNIGHT_CYBER(
        id = "midnight_cyber",
        title = "Midnight Galaxy",
        subtitle = "Electric neon violet, cyber magenta & dark nebula",
        emoji = "🌌",
        primaryColor = Color(0xFF7C3AED),
        secondaryColor = Color(0xFFC026D3),
        accentColor = Color(0xFF38BDF8),
        gradientColors = listOf(Color(0xFF6D28D9), Color(0xFF9333EA), Color(0xFFC026D3))
    ),
    ROYAL_HERITAGE(
        id = "royal_heritage",
        title = "Royal Heritage",
        subtitle = "Imperial gold, sandstone amber & palace crimson",
        emoji = "🏰",
        primaryColor = Color(0xFFD97706),
        secondaryColor = Color(0xFFB91C1C),
        accentColor = Color(0xFF0284C7),
        gradientColors = listOf(Color(0xFFB45309), Color(0xFFD97706), Color(0xFFF59E0B))
    ),
    SAKURA_BLOSSOM(
        id = "sakura_blossom",
        title = "Kyoto Sakura",
        subtitle = "Soft cherry blossoms, rose quartz & dreamy slate",
        emoji = "🌸",
        primaryColor = Color(0xFFDB2777),
        secondaryColor = Color(0xFFF472B6),
        accentColor = Color(0xFF6366F1),
        gradientColors = listOf(Color(0xFFBE185D), Color(0xFFDB2777), Color(0xFFF472B6))
    );

    val heroGradient: Brush
        get() = Brush.horizontalGradient(gradientColors)

    fun getDarkColorScheme(): ColorScheme {
        return when (this) {
            OCEANIC_AZURE -> darkColorScheme(
                primary = Color(0xFF38BDF8),
                onPrimary = Color(0xFF002F4D),
                primaryContainer = Color(0xFF0369A1),
                onPrimaryContainer = Color.White,
                secondary = Color(0xFFFBBF24),
                onSecondary = Color.Black,
                secondaryContainer = Color(0xFF78350F),
                onSecondaryContainer = Color(0xFFFEF3C7),
                tertiary = Color(0xFF34D399),
                onTertiary = Color.Black,
                background = Color(0xFF0B1120),
                onBackground = Color(0xFFF8FAFC),
                surface = Color(0xFF111D38),
                onSurface = Color(0xFFF8FAFC),
                surfaceVariant = Color(0xFF1E293B),
                onSurfaceVariant = Color(0xFF94A3B8),
                outline = Color(0xFF334155),
                error = Color(0xFFF43F5E),
                onError = Color.White
            )
            TROPICAL_SUNSET -> darkColorScheme(
                primary = Color(0xFFFB923C),
                onPrimary = Color(0xFF431407),
                primaryContainer = Color(0xFFC2410C),
                onPrimaryContainer = Color.White,
                secondary = Color(0xFFFBBF24),
                onSecondary = Color.Black,
                secondaryContainer = Color(0xFF78350F),
                onSecondaryContainer = Color(0xFFFEF3C7),
                tertiary = Color(0xFF2DD4BF),
                onTertiary = Color.Black,
                background = Color(0xFF180E1C),
                onBackground = Color(0xFFFFF7ED),
                surface = Color(0xFF24142B),
                onSurface = Color(0xFFFFF7ED),
                surfaceVariant = Color(0xFF331E3D),
                onSurfaceVariant = Color(0xFFFED7AA),
                outline = Color(0xFF523348),
                error = Color(0xFFF43F5E),
                onError = Color.White
            )
            ALPINE_EMERALD -> darkColorScheme(
                primary = Color(0xFF34D399),
                onPrimary = Color(0xFF022C22),
                primaryContainer = Color(0xFF065F46),
                onPrimaryContainer = Color.White,
                secondary = Color(0xFFFBBF24),
                onSecondary = Color.Black,
                secondaryContainer = Color(0xFF78350F),
                onSecondaryContainer = Color(0xFFFEF3C7),
                tertiary = Color(0xFF38BDF8),
                onTertiary = Color.Black,
                background = Color(0xFF091813),
                onBackground = Color(0xFFECFDF5),
                surface = Color(0xFF102820),
                onSurface = Color(0xFFECFDF5),
                surfaceVariant = Color(0xFF18382C),
                onSurfaceVariant = Color(0xFFA7F3D0),
                outline = Color(0xFF234F3F),
                error = Color(0xFFF43F5E),
                onError = Color.White
            )
            MIDNIGHT_CYBER -> darkColorScheme(
                primary = Color(0xFFA78BFA),
                onPrimary = Color(0xFF2E1065),
                primaryContainer = Color(0xFF6D28D9),
                onPrimaryContainer = Color.White,
                secondary = Color(0xFFE879F9),
                onSecondary = Color.Black,
                secondaryContainer = Color(0xFF701A75),
                onSecondaryContainer = Color(0xFFFDF4FF),
                tertiary = Color(0xFF38BDF8),
                onTertiary = Color.Black,
                background = Color(0xFF0A081D),
                onBackground = Color(0xFFFAF5FF),
                surface = Color(0xFF131034),
                onSurface = Color(0xFFFAF5FF),
                surfaceVariant = Color(0xFF201B4B),
                onSurfaceVariant = Color(0xFFDDD6FE),
                outline = Color(0xFF3B3377),
                error = Color(0xFFF43F5E),
                onError = Color.White
            )
            ROYAL_HERITAGE -> darkColorScheme(
                primary = Color(0xFFFBBF24),
                onPrimary = Color(0xFF451A03),
                primaryContainer = Color(0xFF92400E),
                onPrimaryContainer = Color.White,
                secondary = Color(0xFFF87171),
                onSecondary = Color.Black,
                secondaryContainer = Color(0xFF7F1D1D),
                onSecondaryContainer = Color(0xFFFEE2E2),
                tertiary = Color(0xFF38BDF8),
                onTertiary = Color.Black,
                background = Color(0xFF17100B),
                onBackground = Color(0xFFFFFBEB),
                surface = Color(0xFF241912),
                onSurface = Color(0xFFFFFBEB),
                surfaceVariant = Color(0xFF36251A),
                onSurfaceVariant = Color(0xFFFDE68A),
                outline = Color(0xFF573B29),
                error = Color(0xFFF43F5E),
                onError = Color.White
            )
            SAKURA_BLOSSOM -> darkColorScheme(
                primary = Color(0xFFF472B6),
                onPrimary = Color(0xFF500724),
                primaryContainer = Color(0xFF9D174D),
                onPrimaryContainer = Color.White,
                secondary = Color(0xFF818CF8),
                onSecondary = Color.Black,
                secondaryContainer = Color(0xFF312E81),
                onSecondaryContainer = Color(0xFFEEF2FF),
                tertiary = Color(0xFF34D399),
                onTertiary = Color.Black,
                background = Color(0xFF1A0D18),
                onBackground = Color(0xFFFDF2F8),
                surface = Color(0xFF261424),
                onSurface = Color(0xFFFDF2F8),
                surfaceVariant = Color(0xFF3A1F36),
                onSurfaceVariant = Color(0xFFFBCFE8),
                outline = Color(0xFF5B3055),
                error = Color(0xFFF43F5E),
                onError = Color.White
            )
        }
    }

    fun getLightColorScheme(): ColorScheme {
        return when (this) {
            OCEANIC_AZURE -> lightColorScheme(
                primary = Color(0xFF0284C7),
                onPrimary = Color.White,
                primaryContainer = Color(0xFFE0F2FE),
                onPrimaryContainer = Color(0xFF0369A1),
                secondary = Color(0xFFF97316),
                onSecondary = Color.White,
                secondaryContainer = Color(0xFFFFEDD5),
                onSecondaryContainer = Color(0xFF9A3412),
                tertiary = Color(0xFF0D9488),
                onTertiary = Color.White,
                background = Color(0xFFF0F6FF),
                onBackground = Color(0xFF0F172A),
                surface = Color(0xFFFFFFFF),
                onSurface = Color(0xFF0F172A),
                surfaceVariant = Color(0xFFE2E8F0),
                onSurfaceVariant = Color(0xFF475569),
                outline = Color(0xFFCBD5E1),
                error = Color(0xFFE11D48),
                onError = Color.White
            )
            TROPICAL_SUNSET -> lightColorScheme(
                primary = Color(0xFFEA580C),
                onPrimary = Color.White,
                primaryContainer = Color(0xFFFFEDD5),
                onPrimaryContainer = Color(0xFF9A3412),
                secondary = Color(0xFF0D9488),
                onSecondary = Color.White,
                secondaryContainer = Color(0xFFCCFBF1),
                onSecondaryContainer = Color(0xFF115E59),
                tertiary = Color(0xFFD97706),
                onTertiary = Color.White,
                background = Color(0xFFFFF8F1),
                onBackground = Color(0xFF291508),
                surface = Color(0xFFFFFFFF),
                onSurface = Color(0xFF291508),
                surfaceVariant = Color(0xFFFFEDE0),
                onSurfaceVariant = Color(0xFF7C2D12),
                outline = Color(0xFFFED7AA),
                error = Color(0xFFE11D48),
                onError = Color.White
            )
            ALPINE_EMERALD -> lightColorScheme(
                primary = Color(0xFF059669),
                onPrimary = Color.White,
                primaryContainer = Color(0xFFD1FAE5),
                onPrimaryContainer = Color(0xFF065F46),
                secondary = Color(0xFF0284C7),
                onSecondary = Color.White,
                secondaryContainer = Color(0xFFE0F2FE),
                onSecondaryContainer = Color(0xFF0369A1),
                tertiary = Color(0xFFD97706),
                onTertiary = Color.White,
                background = Color(0xFFF2FBF6),
                onBackground = Color(0xFF062E1F),
                surface = Color(0xFFFFFFFF),
                onSurface = Color(0xFF062E1F),
                surfaceVariant = Color(0xFFE1F5EC),
                onSurfaceVariant = Color(0xFF0F5132),
                outline = Color(0xFFA7F3D0),
                error = Color(0xFFE11D48),
                onError = Color.White
            )
            MIDNIGHT_CYBER -> lightColorScheme(
                primary = Color(0xFF7C3AED),
                onPrimary = Color.White,
                primaryContainer = Color(0xFFEDE9FE),
                onPrimaryContainer = Color(0xFF5B21B6),
                secondary = Color(0xFFC026D3),
                onSecondary = Color.White,
                secondaryContainer = Color(0xFFFAE8FF),
                onSecondaryContainer = Color(0xFF86198F),
                tertiary = Color(0xFF0284C7),
                onTertiary = Color.White,
                background = Color(0xFFF9F7FE),
                onBackground = Color(0xFF1E1338),
                surface = Color(0xFFFFFFFF),
                onSurface = Color(0xFF1E1338),
                surfaceVariant = Color(0xFFEFE8FC),
                onSurfaceVariant = Color(0xFF4C1D95),
                outline = Color(0xFFDDD6FE),
                error = Color(0xFFE11D48),
                onError = Color.White
            )
            ROYAL_HERITAGE -> lightColorScheme(
                primary = Color(0xFFD97706),
                onPrimary = Color.White,
                primaryContainer = Color(0xFFFEF3C7),
                onPrimaryContainer = Color(0xFF92400E),
                secondary = Color(0xFFB91C1C),
                onSecondary = Color.White,
                secondaryContainer = Color(0xFFFEE2E2),
                onSecondaryContainer = Color(0xFF991B1B),
                tertiary = Color(0xFF0284C7),
                onTertiary = Color.White,
                background = Color(0xFFFFFDF5),
                onBackground = Color(0xFF2C1C07),
                surface = Color(0xFFFFFFFF),
                onSurface = Color(0xFF2C1C07),
                surfaceVariant = Color(0xFFFBF2DC),
                onSurfaceVariant = Color(0xFF78350F),
                outline = Color(0xFFFDE68A),
                error = Color(0xFFE11D48),
                onError = Color.White
            )
            SAKURA_BLOSSOM -> lightColorScheme(
                primary = Color(0xFFDB2777),
                onPrimary = Color.White,
                primaryContainer = Color(0xFFFCE7F3),
                onPrimaryContainer = Color(0xFF9D174D),
                secondary = Color(0xFF6366F1),
                onSecondary = Color.White,
                secondaryContainer = Color(0xFFEEF2FF),
                onSecondaryContainer = Color(0xFF3730A3),
                tertiary = Color(0xFF059669),
                onTertiary = Color.White,
                background = Color(0xFFFDF7FA),
                onBackground = Color(0xFF2E0F23),
                surface = Color(0xFFFFFFFF),
                onSurface = Color(0xFF2E0F23),
                surfaceVariant = Color(0xFFFCE6F1),
                onSurfaceVariant = Color(0xFF831843),
                outline = Color(0xFFFBCFE8),
                error = Color(0xFFE11D48),
                onError = Color.White
            )
        }
    }
}
