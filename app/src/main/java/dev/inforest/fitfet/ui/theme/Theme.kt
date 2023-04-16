package dev.inforest.fitfet.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = ColorBackground,
    primaryVariant = ColorBackground,
    secondary = Color(0xFF333333),
    background = ColorBackground,
)

private val LightColorPalette = lightColors(
    primary = ColorBackground,
    primaryVariant = ColorBackground,
    secondary = Color(0xFF333333),
    background = ColorBackground,
)

@Composable
fun WhetherTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}