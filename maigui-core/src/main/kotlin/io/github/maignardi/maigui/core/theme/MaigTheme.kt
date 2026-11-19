package io.github.maignardi.maigui.core.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import io.github.maignardi.maigui.core.color.MaigPaletteGenerator
import io.github.maignardi.maigui.core.tokens.MaigColors
import io.github.maignardi.maigui.core.tokens.MaigElevation
import io.github.maignardi.maigui.core.tokens.MaigShapes
import io.github.maignardi.maigui.core.tokens.MaigSpacing
import io.github.maignardi.maigui.core.tokens.MaigTypography

/**
 * CompositionLocal providing the active [MaigColors].
 */
public val LocalMaigColors: ProvidableCompositionLocal<MaigColors> = staticCompositionLocalOf {
    MaigThemes.Default.lightColors
}

/**
 * CompositionLocal providing the active [MaigTypography].
 */
public val LocalMaigTypography: ProvidableCompositionLocal<MaigTypography> = staticCompositionLocalOf {
    MaigTypography()
}

/**
 * CompositionLocal providing the active [MaigShapes].
 */
public val LocalMaigShapes: ProvidableCompositionLocal<MaigShapes> = staticCompositionLocalOf {
    MaigShapes()
}

/**
 * CompositionLocal providing the active [MaigSpacing].
 */
public val LocalMaigSpacing: ProvidableCompositionLocal<MaigSpacing> = staticCompositionLocalOf {
    MaigSpacing()
}

/**
 * CompositionLocal providing the active [MaigElevation].
 */
public val LocalMaigElevation: ProvidableCompositionLocal<MaigElevation> = staticCompositionLocalOf {
    MaigElevation()
}

/**
 * CompositionLocal providing the preferred foreground content [Color] inside surfaces/components.
 */
public val LocalMaigContentColor: ProvidableCompositionLocal<Color> = staticCompositionLocalOf {
    MaigThemes.Default.lightColors.textPrimary
}

/**
 * CompositionLocal providing the preferred [TextStyle] inside MaigUI components.
 */
public val LocalMaigTextStyle: ProvidableCompositionLocal<TextStyle> = staticCompositionLocalOf {
    MaigTypography().body
}

/**
 * Entry point for accessing the active MaigUI design tokens inside a `@Composable` scope.
 */
public object MaigTheme {
    /**
     * Current [MaigColors] token palette.
     */
    public val colors: MaigColors
        @Composable
        @ReadOnlyComposable
        get() = LocalMaigColors.current

    /**
     * Current [MaigTypography] scale.
     */
    public val typography: MaigTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalMaigTypography.current

    /**
     * Current [MaigShapes] corner radius tokens.
     */
    public val shapes: MaigShapes
        @Composable
        @ReadOnlyComposable
        get() = LocalMaigShapes.current

    /**
     * Current [MaigSpacing] scale tokens.
     */
    public val spacing: MaigSpacing
        @Composable
        @ReadOnlyComposable
        get() = LocalMaigSpacing.current

    /**
     * Current [MaigElevation] tokens.
     */
    public val elevation: MaigElevation
        @Composable
        @ReadOnlyComposable
        get() = LocalMaigElevation.current

    /**
     * Current preferred foreground content [Color].
     */
    public val contentColor: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalMaigContentColor.current

    /**
     * Current preferred [TextStyle].
     */
    public val textStyle: TextStyle
        @Composable
        @ReadOnlyComposable
        get() = LocalMaigTextStyle.current
}

/**
 * Applies a MaigUI design system theme generated dynamically from [brand].
 *
 * Respects [isSystemInDarkTheme] by default, with optional manual [darkTheme] override.
 * Passing [fontFamily] replaces the default Inter font family across the entire [typography] scale.
 */
@Composable
public fun MaigTheme(
    brand: MaigBrand = MaigThemes.Default.brand,
    darkTheme: Boolean = isSystemInDarkTheme(),
    fontFamily: FontFamily? = null,
    typography: MaigTypography = MaigTypography(),
    shapes: MaigShapes = MaigShapes(),
    spacing: MaigSpacing = MaigSpacing(),
    elevation: MaigElevation = MaigElevation(),
    content: @Composable () -> Unit,
) {
    val colors = remember(brand, darkTheme) {
        MaigPaletteGenerator.fromBrand(brand = brand, darkTheme = darkTheme)
    }
    val resolvedTypography = remember(typography, fontFamily) {
        if (fontFamily != null) typography.withFontFamily(fontFamily) else typography
    }

    MaigTheme(
        colors = colors,
        typography = resolvedTypography,
        shapes = shapes,
        spacing = spacing,
        elevation = elevation,
        content = content,
    )
}

/**
 * Applies a MaigUI design system theme from a built-in [preset].
 */
@Composable
public fun MaigTheme(
    preset: MaigThemePreset,
    darkTheme: Boolean = isSystemInDarkTheme(),
    fontFamily: FontFamily? = null,
    typography: MaigTypography = MaigTypography(),
    shapes: MaigShapes = MaigShapes(),
    spacing: MaigSpacing = MaigSpacing(),
    elevation: MaigElevation = MaigElevation(),
    content: @Composable () -> Unit,
) {
    val colors = remember(preset, darkTheme) {
        preset.colors(darkTheme = darkTheme)
    }
    val resolvedTypography = remember(typography, fontFamily) {
        if (fontFamily != null) typography.withFontFamily(fontFamily) else typography
    }

    MaigTheme(
        colors = colors,
        typography = resolvedTypography,
        shapes = shapes,
        spacing = spacing,
        elevation = elevation,
        content = content,
    )
}

/**
 * Applies a MaigUI design system theme using an explicit [MaigColors] palette.
 */
@Composable
public fun MaigTheme(
    colors: MaigColors,
    typography: MaigTypography = MaigTypography(),
    shapes: MaigShapes = MaigShapes(),
    spacing: MaigSpacing = MaigSpacing(),
    elevation: MaigElevation = MaigElevation(),
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalMaigColors provides colors,
        LocalMaigTypography provides typography,
        LocalMaigShapes provides shapes,
        LocalMaigSpacing provides spacing,
        LocalMaigElevation provides elevation,
        LocalMaigContentColor provides colors.textPrimary,
        LocalMaigTextStyle provides typography.body,
        content = content,
    )
}
