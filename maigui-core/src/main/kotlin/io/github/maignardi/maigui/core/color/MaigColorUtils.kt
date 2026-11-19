package io.github.maignardi.maigui.core.color

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow

/**
 * Color luminance and WCAG 2.1 contrast calculation utilities.
 */
public object MaigColorUtils {
    /**
     * WCAG 2.1 minimum contrast ratio for normal text (AA).
     */
    public const val WCAG_AA_NORMAL: Float = 4.5f

    /**
     * WCAG 2.1 minimum contrast ratio for large text and UI components (AA).
     */
    public const val WCAG_AA_LARGE: Float = 3.0f

    /**
     * WCAG 2.1 minimum contrast ratio for enhanced normal text (AAA).
     */
    public const val WCAG_AAA_NORMAL: Float = 7.0f

    /**
     * Computes the WCAG 2.1 relative luminance of [color] in range `0f..1f`.
     */
    public fun relativeLuminance(color: Color): Float {
        val r = linearizeChannel(color.red)
        val g = linearizeChannel(color.green)
        val b = linearizeChannel(color.blue)
        return (0.2126f * r + 0.7152f * g + 0.0722f * b).coerceIn(0f, 1f)
    }

    /**
     * Computes the WCAG 2.1 contrast ratio (`1f..21f`) between [foreground] and [background].
     *
     * If [foreground] is translucent, it is composited over [background] before calculation.
     */
    public fun contrastRatio(foreground: Color, background: Color): Float {
        val opaqueBg = if (background.alpha < 1f) {
            background.copy(alpha = 1f)
        } else {
            background
        }
        val compositedFg = if (foreground.alpha < 1f) {
            foreground.compositeOver(opaqueBg)
        } else {
            foreground
        }
        val l1 = relativeLuminance(compositedFg)
        val l2 = relativeLuminance(opaqueBg)
        val lighter = max(l1, l2)
        val darker = min(l1, l2)
        return (lighter + 0.05f) / (darker + 0.05f)
    }

    /**
     * Returns `true` if [foreground] on [background] satisfies WCAG AA contrast.
     */
    public fun meetsWcagAa(
        foreground: Color,
        background: Color,
        isLargeText: Boolean = false,
    ): Boolean {
        val threshold = if (isLargeText) WCAG_AA_LARGE else WCAG_AA_NORMAL
        return contrastRatio(foreground, background) >= threshold
    }

    /**
     * Picks whichever of [lightCandidate] or [darkCandidate] achieves higher contrast on [background].
     */
    public fun bestOnColor(
        background: Color,
        lightCandidate: Color = Color.White,
        darkCandidate: Color = Color(0xFF09090B),
    ): Color {
        val lightRatio = contrastRatio(lightCandidate, background)
        val darkRatio = contrastRatio(darkCandidate, background)
        val preferred = if (lightRatio >= darkRatio) lightCandidate else darkCandidate
        if (max(lightRatio, darkRatio) >= WCAG_AA_NORMAL) {
            return preferred
        }
        val pureWhiteRatio = contrastRatio(Color.White, background)
        val pureBlackRatio = contrastRatio(Color.Black, background)
        return if (pureWhiteRatio >= pureBlackRatio) Color.White else Color.Black
    }

    /**
     * Adjusts [foreground] lightness in OKLCH until it reaches at least [minRatio] against [background].
     *
     * Moves darker when [background] is light and lighter when [background] is dark.
     */
    public fun ensureContrast(
        foreground: Color,
        background: Color,
        minRatio: Float = WCAG_AA_NORMAL,
    ): Color {
        if (contrastRatio(foreground, background) >= minRatio) {
            return foreground
        }

        val bgLuminance = relativeLuminance(background)
        val moveLighter = bgLuminance < 0.5f
        val baseOklch = MaigOklch.fromColor(foreground)

        var low = if (moveLighter) baseOklch.l else 0f
        var high = if (moveLighter) 1f else baseOklch.l
        var best = if (moveLighter) Color.White else Color(0xFF09090B)

        repeat(16) {
            val mid = (low + high) * 0.5f
            val candidate = baseOklch.copy(l = mid).toColor()
            val ratio = contrastRatio(candidate, background)
            if (ratio >= minRatio) {
                best = candidate
                if (moveLighter) {
                    high = mid
                } else {
                    low = mid
                }
            } else {
                if (moveLighter) {
                    low = mid
                } else {
                    high = mid
                }
            }
        }

        return if (contrastRatio(best, background) >= minRatio) {
            best
        } else {
            bestOnColor(background)
        }
    }

    private fun linearizeChannel(channel: Float): Float {
        return if (channel <= 0.04045f) {
            channel / 12.92f
        } else {
            ((channel + 0.055f) / 1.055f).pow(2.4f)
        }
    }
}
