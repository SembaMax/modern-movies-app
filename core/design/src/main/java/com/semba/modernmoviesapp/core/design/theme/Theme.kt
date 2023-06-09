package com.semba.modernmoviesapp.core.design.theme

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
import androidx.core.view.ViewCompat

private val DarkColorScheme = darkColorScheme(
    primary = Primary_Dark,
    secondary = Secondary_Dark,
    background = Background_Dark,
    tertiary = Tertiary_Dark,
    error = Error_Dark,
    onBackground = On_Background_Dark,
    onTertiary = On_Tertiary_Dark,
    onPrimary = On_Primary_Dark,
    onSecondary = On_Secondary_Dark,
    onError = On_Error_Dark,
    outline = Outline,
    onPrimaryContainer = On_Container,
    surfaceVariant = Surface_Dark,
    onSurface = On_Surface_Dark,
    outlineVariant = Outline_Variant
)

private val LightColorScheme = lightColorScheme(
    primary = Primary_Light,
    secondary = Secondary_Light,
    background = Background_Light,
    tertiary = Tertiary_Light,
    error = Error_Light,
    onBackground = On_Background_Light,
    onTertiary = On_Tertiary_Light,
    onPrimary = On_Primary_Light,
    onSecondary = On_Secondary_Light,
    onError = On_Error_Light,
    outline = Outline,
    onPrimaryContainer = On_Container,
    surfaceVariant = Surface_Light,
    onSurface = On_Surface_Light,
    outlineVariant = Outline_Variant
)

@Composable
fun ModernMoviesAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
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
            (view.context as Activity).window.statusBarColor = colorScheme.primary.toArgb()
            ViewCompat.getWindowInsetsController(view)?.isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}