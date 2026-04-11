package com.example.my_financialtracker.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = OceanBlue,
    onPrimary = Sand,
    primaryContainer = SkyBlue,
    onPrimaryContainer = NavyBlue,
    secondary = NavyBlue,
    onSecondary = Sand,
    background = ColorTokens.backgroundLight,
    onBackground = Ink,
    surface = ColorTokens.surfaceLight,
    onSurface = Ink,
    surfaceVariant = SkyBlue,
    onSurfaceVariant = NavyBlue,
)

private val DarkColors = darkColorScheme(
    primary = SkyBlue,
    onPrimary = NavyBlue,
    primaryContainer = OceanBlue,
    onPrimaryContainer = Sand,
    secondary = Sand,
    onSecondary = NavyBlue,
    background = ColorTokens.backgroundDark,
    onBackground = Sand,
    surface = ColorTokens.surfaceDark,
    onSurface = Sand,
    surfaceVariant = NavyBlue,
    onSurfaceVariant = SkyBlue,
)

@Composable
fun MyFinancialTrackerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = AppTypography,
        content = content,
    )
}

private object ColorTokens {
    val backgroundLight = Sand
    val surfaceLight = androidx.compose.ui.graphics.Color.White
    val backgroundDark = androidx.compose.ui.graphics.Color(0xFF0F1720)
    val surfaceDark = androidx.compose.ui.graphics.Color(0xFF172231)
}
