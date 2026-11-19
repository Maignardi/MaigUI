package io.github.maignardi.maigui.core.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import io.github.maignardi.maigui.core.color.MaigPaletteGenerator
import io.github.maignardi.maigui.core.tokens.MaigColors

/**
 * A named theme preset pairing a [MaigBrand] seed with pre-built light and dark [MaigColors].
 */
@Immutable
public class MaigThemePreset(
    public val name: String,
    public val brand: MaigBrand,
) {
    /**
     * Precomputed light palette for this preset.
     */
    public val lightColors: MaigColors = MaigPaletteGenerator.fromBrand(brand, darkTheme = false)

    /**
     * Precomputed dark palette for this preset.
     */
    public val darkColors: MaigColors = MaigPaletteGenerator.fromBrand(brand, darkTheme = true)

    /**
     * Returns [darkColors] if [darkTheme] is `true`, otherwise [lightColors].
     */
    public fun colors(darkTheme: Boolean): MaigColors = if (darkTheme) darkColors else lightColors

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is MaigThemePreset) return false
        return name == other.name && brand == other.brand
    }

    override fun hashCode(): Int = 31 * name.hashCode() + brand.hashCode()

    override fun toString(): String = "MaigThemePreset(name=$name, brand=$brand)"
}

/**
 * Built-in ready-to-use MaigUI theme presets.
 */
public object MaigThemes {
    /**
     * Crisp cobalt blue default theme inspired by modern web/mobile design systems.
     */
    public val Default: MaigThemePreset = MaigThemePreset(
        name = "Default",
        brand = MaigBrand(
            primary = Color(0xFF2563EB),
            secondary = Color(0xFF475569),
        ),
    )

    /**
     * Vivid violet/purple theme preset.
     */
    public val Purple: MaigThemePreset = MaigThemePreset(
        name = "Purple",
        brand = MaigBrand(
            primary = Color(0xFF7C3AED),
            secondary = Color(0xFF4F46E5),
        ),
    )

    /**
     * Fresh emerald green theme preset.
     */
    public val Green: MaigThemePreset = MaigThemePreset(
        name = "Green",
        brand = MaigBrand(
            primary = Color(0xFF16A34A),
            secondary = Color(0xFF0D9488),
        ),
    )

    /**
     * Warm vibrant orange theme preset.
     */
    public val Orange: MaigThemePreset = MaigThemePreset(
        name = "Orange",
        brand = MaigBrand(
            primary = Color(0xFFEA580C),
            secondary = Color(0xFFD97706),
        ),
    )

    /**
     * All built-in theme presets in display order.
     */
    public val presets: List<MaigThemePreset> = listOf(Default, Purple, Green, Orange)
}
