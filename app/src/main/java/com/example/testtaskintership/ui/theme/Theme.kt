package com.example.testtaskintership.ui.theme

import android.content.Context
import android.content.ContextWrapper
import androidx.activity.ComponentActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import com.example.testtaskintership.ui.theme.findComponentActivity as findComponentActivity1

private val DarkColorScheme = darkColors(
    primary = Purple80,
    secondary = PurpleGrey80,
    surface = Pink80
)

private val LightColorScheme = lightColors(
    primary = Purple40,
    secondary = PurpleGrey40,
    surface = Pink40
)

@Composable
fun TestTaskInternshipTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable (MaterialTheme: androidx.compose.material.MaterialTheme) -> Unit
) {
    val context = LocalContext.current
    val colors = if (darkTheme) {
        MaterialTheme.colors.copy(
            primary = DarkColorScheme.primary,
            primaryVariant = DarkColorScheme.primary,
            secondary = DarkColorScheme.secondary
        )
    } else {
        MaterialTheme.colors.copy(
            primary = LightColorScheme.primary,
            primaryVariant = LightColorScheme.primary,
            secondary = LightColorScheme.secondary
        )
    }

    val typography = MaterialTheme.typography
    val view = LocalView.current
    val coroutineScope = rememberCoroutineScope()

    DisposableEffect(Unit) {
        var statusBarJob: Job? = null

        // Запуск корутины должен причисляться к скоупу receiver
        statusBarJob = coroutineScope.launch {
            val window = context.findComponentActivity1()?.window
            if (window != null) {
                window.statusBarColor = colors.onPrimary.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars =
                    !darkTheme
            }
        }

        // Отменяем задачу настроения статус-бара при изменении параметров
        onDispose {
            statusBarJob.cancel()
        }
    }

    MaterialTheme(
        colors = colors,
        typography = typography,
        content = { content(MaterialTheme) }
    )
}

private fun Context.findComponentActivity(): ComponentActivity? {
    var curContext = this
    while (curContext !is ComponentActivity && curContext is ContextWrapper) {
        curContext = curContext.baseContext
    }
    return curContext as? ComponentActivity
}

