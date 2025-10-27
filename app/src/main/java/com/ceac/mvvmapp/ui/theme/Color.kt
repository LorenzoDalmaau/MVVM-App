package com.ceac.mvvmapp.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// --- Paleta base (tokens de marca)
// ------ Cómo usar en la app: MaterialTheme.colorScheme.primary (ver Theme.kt)
val BrandPrimary = Color(0xFF4F46E5)   // Indigo 600
val BrandOnPrimary = Color(0xFFFFFFFF)
val BrandSecondary = Color(0xFF14B8A6) // Teal 500
val BrandOnSecondary = Color(0xFF0B0F0E)
val BrandTertiary = Color(0xFFFB7185)  // Rose 400
val BrandOnTertiary = Color(0xFF140607)

val BrandBackgroundLight = Color(0xFFF7F7FB)
val BrandSurfaceLight = Color(0xFFFFFFFF)
val BrandOutlineLight = Color(0xFFE3E5EA)

val BrandBackgroundDark = Color(0xFF0F1115)
val BrandSurfaceDark = Color(0xFF141820)
val BrandOutlineDark = Color(0xFF2A2F3A)

// ----- Esquema claro (Material 3)
val LightColors = lightColorScheme(
    primary = BrandPrimary,
    onPrimary = BrandOnPrimary,
    secondary = BrandSecondary,
    onSecondary = BrandOnSecondary,
    tertiary = BrandTertiary,
    onTertiary = BrandOnTertiary,

    background = BrandBackgroundLight,
    onBackground = Color(0xFF0B0F0E),
    surface = BrandSurfaceLight,
    onSurface = Color(0xFF0B0F0E),

    outline = BrandOutlineLight
)

// ----- Esquema oscuro (Material 3)
val DarkColors = darkColorScheme(
    primary = BrandPrimary,
    onPrimary = BrandOnPrimary,
    secondary = BrandSecondary,
    onSecondary = BrandOnSecondary,
    tertiary = BrandTertiary,
    onTertiary = BrandOnTertiary,

    background = BrandBackgroundDark,
    onBackground = Color(0xFFE7E9EE),
    surface = BrandSurfaceDark,
    onSurface = Color(0xFFE7E9EE),

    outline = BrandOutlineDark
)
