package io.github.maignardi.maigui.core.tokens

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

/**
 * A 10-step tonal color scale ranging from [shade50] (lightest) to [shade900] (darkest).
 *
 * @property shade50 Lightest tint, typically used for subtle backgrounds.
 * @property shade100 Very light tint, used for hover or soft surfaces.
 * @property shade200 Light tint, used for subtle borders and dividers.
 * @property shade300 Soft tint, used for strong borders or secondary accents.
 * @property shade400 Medium-light tone.
 * @property shade500 Base anchor tone representing the core color.
 * @property shade600 Medium-dark tone, commonly used as the default light-mode interactive fill.
 * @property shade700 Dark tone, commonly used for hover/pressed states in light mode.
 * @property shade800 Very dark tone, useful for high-contrast text on subtle surfaces.
 * @property shade900 Darkest shade, useful for deep surfaces or highest-contrast ink.
 */
@Immutable
public class MaigColorScale(
    public val shade50: Color,
    public val shade100: Color,
    public val shade200: Color,
    public val shade300: Color,
    public val shade400: Color,
    public val shade500: Color,
    public val shade600: Color,
    public val shade700: Color,
    public val shade800: Color,
    public val shade900: Color,
) {
    /**
     * Returns the shade matching the given numeric weight (`50`, `100`, `200`, ..., `900`).
     *
     * @throws IllegalArgumentException if [weight] is not one of the supported scale steps.
     */
    public operator fun get(weight: Int): Color = when (weight) {
        50 -> shade50
        100 -> shade100
        200 -> shade200
        300 -> shade300
        400 -> shade400
        500 -> shade500
        600 -> shade600
        700 -> shade700
        800 -> shade800
        900 -> shade900
        else -> throw IllegalArgumentException("Unsupported color scale weight: $weight. Expected 50 or 100..900.")
    }

    /**
     * Returns all shades ordered from [shade50] to [shade900] paired with their numeric weight.
     */
    public fun asList(): List<Pair<Int, Color>> = listOf(
        50 to shade50,
        100 to shade100,
        200 to shade200,
        300 to shade300,
        400 to shade400,
        500 to shade500,
        600 to shade600,
        700 to shade700,
        800 to shade800,
        900 to shade900,
    )

    /**
     * Creates a copy of this [MaigColorScale] with optionally overridden shades.
     */
    public fun copy(
        shade50: Color = this.shade50,
        shade100: Color = this.shade100,
        shade200: Color = this.shade200,
        shade300: Color = this.shade300,
        shade400: Color = this.shade400,
        shade500: Color = this.shade500,
        shade600: Color = this.shade600,
        shade700: Color = this.shade700,
        shade800: Color = this.shade800,
        shade900: Color = this.shade900,
    ): MaigColorScale = MaigColorScale(
        shade50 = shade50,
        shade100 = shade100,
        shade200 = shade200,
        shade300 = shade300,
        shade400 = shade400,
        shade500 = shade500,
        shade600 = shade600,
        shade700 = shade700,
        shade800 = shade800,
        shade900 = shade900,
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is MaigColorScale) return false
        return shade50 == other.shade50 &&
            shade100 == other.shade100 &&
            shade200 == other.shade200 &&
            shade300 == other.shade300 &&
            shade400 == other.shade400 &&
            shade500 == other.shade500 &&
            shade600 == other.shade600 &&
            shade700 == other.shade700 &&
            shade800 == other.shade800 &&
            shade900 == other.shade900
    }

    override fun hashCode(): Int {
        var result = shade50.hashCode()
        result = 31 * result + shade100.hashCode()
        result = 31 * result + shade200.hashCode()
        result = 31 * result + shade300.hashCode()
        result = 31 * result + shade400.hashCode()
        result = 31 * result + shade500.hashCode()
        result = 31 * result + shade600.hashCode()
        result = 31 * result + shade700.hashCode()
        result = 31 * result + shade800.hashCode()
        result = 31 * result + shade900.hashCode()
        return result
    }

    override fun toString(): String =
        "MaigColorScale(50=$shade50, 500=$shade500, 900=$shade900)"
}
