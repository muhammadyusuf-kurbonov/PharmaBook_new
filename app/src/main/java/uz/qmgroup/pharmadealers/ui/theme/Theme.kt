package uz.qmgroup.pharmadealers.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF5fd4fd),
    onPrimary = Color(0xFF003544),
    primaryContainer = Color(0xFF004d62),
    onPrimaryContainer = Color(0xFFb9eaff),
    secondary = Color(0xFFb3cad5),
    onSecondary = Color(0xFF1e333c),
    secondaryContainer = Color(0xFF354a53),
    onSecondaryContainer = Color(0xFFcfe6f1),
    tertiary = Color(0xFFc4c3eb),
    onTertiary = Color(0xFF2d2d4d),
    tertiaryContainer = Color(0xFF434465),
    onTertiaryContainer = Color(0xFFe1dfff),
    error = Color(0xFFffb4ab),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000a),
    onErrorContainer = Color(0xFFffdad6),
    background = Color(0xFF24005a),
    onBackground = Color(0xFFeaddff),
    surface = Color(0xFF24005a),
    onSurface = Color(0xFFeaddff),
    outline = Color(0xFF8a9296),
    surfaceVariant = Color(0xFF40484c),
    onSurfaceVariant = Color(0xFFc0c8cc)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF006781),
    onPrimary = Color(0xFFffffff),
    primaryContainer = Color(0xFFb9eaff),
    onPrimaryContainer = Color(0xFF001f29),
    secondary = Color(0xFF4c626b),
    onSecondary = Color(0xFFffffff),
    secondaryContainer = Color(0xFFcfe6f1),
    onSecondaryContainer = Color(0xFF071e26),
    tertiary = Color(0xFF5b5b7e),
    onTertiary = Color(0xFFffffff),
    tertiaryContainer = Color(0xFFe1dfff),
    onTertiaryContainer = Color(0xFF181837),
    error = Color(0xFFba1a1a),
    onError = Color(0xFFffffff),
    errorContainer = Color(0xFFffdad6),
    onErrorContainer = Color(0xFF410002),
    background = Color(0xFFfffbff),
    onBackground = Color(0xFF24005a),
    surface = Color(0xFFfffbff),
    onSurface = Color(0xFF24005a),
    outline = Color(0xFF70787c),
    surfaceVariant = Color(0xFFdce4e8),
    onSurfaceVariant = Color(0xFF40484c)
)

@Composable
fun PharmaBookTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    useDynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current

    val colorScheme = when {
        useDynamicColor -> if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(
            context
        )
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current

    SideEffect {
        val window = (view.context as Activity).window
        window.statusBarColor = Color.Transparent.toArgb()
        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}