package com.ceac.mvvmapp.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.dp

@Immutable
data class Elevations(
    val level0: androidx.compose.ui.unit.Dp = 0.dp,
    val level1: androidx.compose.ui.unit.Dp = 1.dp,
    val level2: androidx.compose.ui.unit.Dp = 3.dp,
    val level3: androidx.compose.ui.unit.Dp = 6.dp,
    val level4: androidx.compose.ui.unit.Dp = 12.dp
)

val LocalElevations = staticCompositionLocalOf { Elevations() }

// --- Cómo usar en la app:
// ------ MVVMAppTheme.elevations.level2 → Card(elevation = CardDefaults.cardElevation(MVVMAppTheme.elevations.level2))
val MVVMAppElevations: @Composable () -> Elevations
    get() = { LocalElevations.current }

object MVVMAppThemeExt {
    val elevations: Elevations
        @Composable get() = LocalElevations.current
}
