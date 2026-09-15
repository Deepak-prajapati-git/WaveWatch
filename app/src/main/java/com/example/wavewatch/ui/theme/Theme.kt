package com.example.wavewatch.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val MarineDarkColorScheme = darkColorScheme(
    primary = NeonCyan,
    onPrimary = AbyssalNavy,
    primaryContainer = SurfaceVariantDark,
    onPrimaryContainer = NeonCyan,
    secondary = Amber,
    onSecondary = AbyssalNavy,
    tertiary = SuccessGreen,
    onTertiary = AbyssalNavy,
    error = CrimsonRed,
    onError = TextPrimary,
    background = AbyssalNavy,
    onBackground = TextPrimary,
    surface = SurfaceDark,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = MutedText
)

@Composable
fun WaveWatchTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = MarineDarkColorScheme,
        typography = Typography,
        content = content
    )
}
