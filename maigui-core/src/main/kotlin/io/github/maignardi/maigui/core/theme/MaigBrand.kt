package io.github.maignardi.maigui.core.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

/**
 * Brand color seed specification used to generate complete light and dark MaigUI palettes.
 *
 * Passing just [primary] automatically derives a full 50-900 tonal scale, a harmonious
 * [secondary] scale, a primary-tinted [neutral] scale, and light/dark semantic tokens
 * satisfying WCAG AA contrast.
 */
@Immutable
public class MaigBrand(
    public val primary: Color,
    public val secondary: Color? = null,
    public val success: Color = Color(0xFF16A34A),
    public val warning: Color = Color(0xFFD97706),
    public val danger: Color = Color(0xFFDC2626),
    public val info: Color = Color(0xFF0284C7),
    public val neutral: Color? = null,
) {
    /**
     * Returns a copy of this [MaigBrand] with optionally overridden seed colors.
     */
    public fun copy(
        primary: Color = this.primary,
        secondary: Color? = this.secondary,
        success: Color = this.success,
        warning: Color = this.warning,
        danger: Color = this.danger,
        info: Color = this.info,
        neutral: Color? = this.neutral,
    ): MaigBrand = MaigBrand(
        primary = primary,
        secondary = secondary,
        success = success,
        warning = warning,
        danger = danger,
        info = info,
        neutral = neutral,
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is MaigBrand) return false
        return primary == other.primary &&
            secondary == other.secondary &&
            success == other.success &&
            warning == other.warning &&
            danger == other.danger &&
            info == other.info &&
            neutral == other.neutral
    }

    override fun hashCode(): Int {
        var result = primary.hashCode()
        result = 31 * result + (secondary?.hashCode() ?: 0)
        result = 31 * result + success.hashCode()
        result = 31 * result + warning.hashCode()
        result = 31 * result + danger.hashCode()
        result = 31 * result + info.hashCode()
        result = 31 * result + (neutral?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String = "MaigBrand(primary=$primary, secondary=$secondary)"
}
