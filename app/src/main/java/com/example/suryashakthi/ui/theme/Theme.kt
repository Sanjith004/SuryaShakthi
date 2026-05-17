package com.example.suryashakthi.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = SolarAmber,
    onPrimary = Color.Black,
    secondary = EnergyCyan,
    onSecondary = Color.Black,
    tertiary = NatureGreen,
    onTertiary = Color.White,
    background = Slate900,
    onBackground = ChalkWhite,
    surface = Slate800,
    onSurface = ChalkWhite,
    surfaceVariant = Slate700,
    onSurfaceVariant = Color.LightGray,
    outline = Color.DarkGray
)

private val LightColorScheme = lightColorScheme(
    primary = SolarAmber,
    onPrimary = Color.Black,
    secondary = EnergyCyan,
    onSecondary = Color.White,
    tertiary = NatureGreen,
    onTertiary = Color.White,
    background = Color.White,
    onBackground = Color.Black,
    surface = ChalkWhite,
    onSurface = Color.Black,
    surfaceVariant = FogGray,
    onSurfaceVariant = TextGray,
    outline = Color.LightGray
)

@Composable
fun SuryaShakthiTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Disabled for a consistent brand look
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
