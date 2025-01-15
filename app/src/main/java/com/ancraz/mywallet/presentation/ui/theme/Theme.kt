package com.ancraz.mywallet.presentation.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView

private val colorScheme = lightColorScheme(
    primary = primaryColor,
    onPrimary = onPrimaryColor,
    primaryContainer = primaryContainerColor,
    onPrimaryContainer = onPrimaryContainerColor,
    secondary = secondaryColor,
    onSecondary = onSecondaryColor,
    secondaryContainer = secondaryContainer,
    onSecondaryContainer = onSecondaryContainer,
    tertiary = tertiaryColor,
    onTertiary = onTertiaryColor,
    tertiaryContainer = tertiaryContainer,
    onTertiaryContainer = onTertiaryContainer,
    error = errorColor,
    onError = onError,
    errorContainer = errorContainer,
    onErrorContainer = onErrorContainer,
    background = backgroundColor,
    onBackground = onBackgroundColor,
    surface = surfaceColor,
    onSurface = onSurfaceColor,
    surfaceVariant = surfaceVariant,
    onSurfaceVariant = onSurfaceVariantColor,
    outline = outlineColor,
    outlineVariant = outlineVariant,
    scrim = scrimLight,
    inverseSurface = inverseSurfaceLight
)


@Composable
fun MyWalletTheme(
    content: @Composable() () -> Unit
) {

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            (view.context as Activity).window.statusBarColor = colorScheme.surface.toArgb()
        }
    }


    MaterialTheme(
    colorScheme = colorScheme,
    typography = AppTypography,
    content = content
  )
}

