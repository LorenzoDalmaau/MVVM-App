package com.ceac.mvvmapp.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.dp

// --- Sistema de espaciados homogéneo para toda la app
@Immutable
data class Spacing(
    val xs: androidx.compose.ui.unit.Dp = 4.dp,
    val sm: androidx.compose.ui.unit.Dp = 8.dp,
    val md: androidx.compose.ui.unit.Dp = 12.dp,
    val lg: androidx.compose.ui.unit.Dp = 16.dp,
    val xl: androidx.compose.ui.unit.Dp = 24.dp,
    val xxl: androidx.compose.ui.unit.Dp = 32.dp
)

val LocalSpacing = staticCompositionLocalOf { Spacing() }

// --- Cómo usar en la app:
// ----- MVVMAppTheme.spacing.lg  → padding( MVVMAppTheme.spacing.lg )
object MVVMAppTheme {
    val spacing: Spacing
        @Composable get() = LocalSpacing.current
}
