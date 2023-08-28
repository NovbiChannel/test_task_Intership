package com.example.testtaskintership.ui.theme

import android.content.Context
import android.content.ContextWrapper
import androidx.activity.ComponentActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import com.example.testtaskintership.ui.theme.findComponentActivity as findComponentActivity1

//private val DarkColorScheme = darkColors(
//    primary = Purple80,
//    secondary = PurpleGrey80,
//    surface = Pink80
//)

private val LightColorScheme = lightColors(
    primary = White,
    secondary = Blue,
    surface = Pink40,
    background = Grey
)

@Composable
fun TestTaskInternshipTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable (MaterialTheme: androidx.compose.material.MaterialTheme) -> Unit
) {
    val context = LocalContext.current
    val colors = MaterialTheme.colors.copy(
        primary = LightColorScheme.primary,
        primaryVariant = LightColorScheme.primary,
        secondary = LightColorScheme.secondary,
        background = LightColorScheme.background
    )

    val typography = MaterialTheme.typography
    val view = LocalView.current
    val coroutineScope = rememberCoroutineScope()

    DisposableEffect(Unit) {
        var statusBarJob: Job? = null

        // Запуск корутины должен причисляться к скоупу receiver
        statusBarJob = coroutineScope.launch {
            val window = context.findComponentActivity1()?.window
            if (window != null) {
                window.statusBarColor = colors.background.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
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

