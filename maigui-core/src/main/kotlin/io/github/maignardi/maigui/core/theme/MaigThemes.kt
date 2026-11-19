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
 * Built-in ready-to-use MaigUI designer theme presets.
 */
public object MaigThemes {
    /** Crisp cobalt blue default theme. */
    public val Default: MaigThemePreset = MaigThemePreset(
        name = "Default",
        brand = MaigBrand(primary = Color(0xFF2563EB), secondary = Color(0xFF475569)),
    )

    /** Deep electric indigo preset. */
    public val Indigo: MaigThemePreset = MaigThemePreset(
        name = "Indigo",
        brand = MaigBrand(primary = Color(0xFF4F46E5), secondary = Color(0xFF6366F1)),
    )

    /** Vivid violet/purple preset. */
    public val Purple: MaigThemePreset = MaigThemePreset(
        name = "Purple",
        brand = MaigBrand(primary = Color(0xFF7C3AED), secondary = Color(0xFF4F46E5)),
    )

    /** Modern rose/magenta preset. */
    public val Rose: MaigThemePreset = MaigThemePreset(
        name = "Rose",
        brand = MaigBrand(primary = Color(0xFFE11D48), secondary = Color(0xFFBE185D)),
    )

    /** Bold crimson red preset. */
    public val Crimson: MaigThemePreset = MaigThemePreset(
        name = "Crimson",
        brand = MaigBrand(primary = Color(0xFFDC2626), secondary = Color(0xFF991B1B)),
    )

    /** Warm vibrant orange preset. */
    public val Orange: MaigThemePreset = MaigThemePreset(
        name = "Orange",
        brand = MaigBrand(primary = Color(0xFFEA580C), secondary = Color(0xFFD97706)),
    )

    /** Rich golden amber preset. */
    public val Amber: MaigThemePreset = MaigThemePreset(
        name = "Amber",
        brand = MaigBrand(primary = Color(0xFFD97706), secondary = Color(0xFFB45309)),
    )

    /** Fresh emerald green preset. */
    public val Green: MaigThemePreset = MaigThemePreset(
        name = "Green",
        brand = MaigBrand(primary = Color(0xFF16A34A), secondary = Color(0xFF0D9488)),
    )

    /** Sleek ocean teal preset. */
    public val Teal: MaigThemePreset = MaigThemePreset(
        name = "Teal",
        brand = MaigBrand(primary = Color(0xFF0D9488), secondary = Color(0xFF0284C7)),
    )

    /** Minimalist monochrome slate preset. */
    public val Slate: MaigThemePreset = MaigThemePreset(
        name = "Slate",
        brand = MaigBrand(primary = Color(0xFF334155), secondary = Color(0xFF64748B)),
    )

    /**
     * All built-in theme presets in display order.
     */
    public val presets: List<MaigThemePreset> = listOf(
        Default,
        Indigo,
        Purple,
        Rose,
        Crimson,
        Orange,
        Amber,
        Green,
        Teal,
        Slate,
    )
}
