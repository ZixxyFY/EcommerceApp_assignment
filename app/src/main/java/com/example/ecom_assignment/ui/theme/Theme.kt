// app/src/main/java/com/example/ecom_assign/ui/theme/Theme.kt
package com.example.ecom_assignment.ui.theme
import androidx.compose.ui.graphics.Color
import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryPurple, // Use our defined primary color
    secondary = PurpleGrey80,
    tertiary = Pink80,
    background = Color.White, // Figma background is light
    surface = BackgroundLightGray // For text fields, etc.
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryPurple, // Use our defined primary color
    secondary = PurpleGrey40,
    tertiary = Pink40,
    background = Color.White, // Figma background is light
    surface = BackgroundLightGray // For text fields, etc.
)

@Composable
fun EcommerceApp_assignmentTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Set to false for strict adherence to Figma colors
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
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb() // Status bar color matching primary
            // Adjust status bar icon colors based on theme. If your status bar is translucent/transparent,
            // you might want to set this to the background color's light/darkness.
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // Use our defined typography
        content = content
    )
}