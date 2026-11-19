package io.github.maignardi.maigui.core.color

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import io.github.maignardi.maigui.core.theme.MaigBrand
import io.github.maignardi.maigui.core.tokens.MaigColorScale
import io.github.maignardi.maigui.core.tokens.MaigColors
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cbrt
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.pow
import kotlin.math.sin

/**
 * Generates perceptual OKLCH tonal scales and full WCAG AA compliant [MaigColors] palettes
 * from a single [MaigBrand] specification.
 */
public object MaigPaletteGenerator {

    private val brandLightnessSteps: FloatArray = floatArrayOf(
        0.975f, // 50
        0.935f, // 100
        0.865f, // 200
        0.775f, // 300
        0.675f, // 400
        0.560f, // 500
        0.475f, // 600
        0.395f, // 700
        0.315f, // 800
        0.240f, // 900
    )

    private val brandChromaFactors: FloatArray = floatArrayOf(
        0.16f, // 50
        0.32f, // 100
        0.52f, // 200
        0.74f, // 300
        0.90f, // 400
        1.00f, // 500
        0.98f, // 600
        0.90f, // 700
        0.78f, // 800
        0.66f, // 900
    )

    private val neutralLightnessSteps: FloatArray = floatArrayOf(
        0.985f, // 50
        0.960f, // 100
        0.915f, // 200
        0.840f, // 300
        0.710f, // 400
        0.560f, // 500
        0.440f, // 600
        0.340f, // 700
        0.240f, // 800
        0.165f, // 900
    )

    /**
     * Generates a 10-step tonal [MaigColorScale] (`50..900`) from a single [seed] color.
     */
    public fun generateScale(seed: Color): MaigColorScale {
        val opaqueSeed = seed.copy(alpha = 1f)
        val oklch = MaigOklch.fromColor(opaqueSeed)
        val isAchromatic = oklch.c < 0.004f
        val baseChroma = if (isAchromatic) 0f else oklch.c.coerceAtLeast(0.04f)
        val keepExactSeedAt500 = !isAchromatic && oklch.l in 0.42f..0.68f

        val shades = Array(10) { idx ->
            if (idx == 5 && keepExactSeedAt500) {
                opaqueSeed
            } else {
                val targetL = brandLightnessSteps[idx]
                val targetC = if (isAchromatic) 0f else baseChroma * brandChromaFactors[idx]
                MaigOklch(l = targetL, c = targetC, h = oklch.h).toColor()
            }
        }

        return MaigColorScale(
            shade50 = shades[0],
            shade100 = shades[1],
            shade200 = shades[2],
            shade300 = shades[3],
            shade400 = shades[4],
            shade500 = shades[5],
            shade600 = shades[6],
            shade700 = shades[7],
            shade800 = shades[8],
            shade900 = shades[9],
        )
    }

    /**
     * Generates a crisp neutral [MaigColorScale], optionally tinted slightly with [tintSeed]'s hue.
     */
    public fun generateNeutralScale(tintSeed: Color? = null): MaigColorScale {
        val hue = tintSeed?.let { MaigOklch.fromColor(it).h } ?: 250f
        val chroma = if (tintSeed != null) {
            val seedC = MaigOklch.fromColor(tintSeed).c
            if (seedC < 0.01f) 0f else 0.010f
        } else {
            0.006f
        }

        val shades = Array(10) { idx ->
            MaigOklch(
                l = neutralLightnessSteps[idx],
                c = if (idx == 0 || idx == 9) chroma * 0.7f else chroma,
                h = hue,
            ).toColor()
        }

        return MaigColorScale(
            shade50 = shades[0],
            shade100 = shades[1],
            shade200 = shades[2],
            shade300 = shades[3],
            shade400 = shades[4],
            shade500 = shades[5],
            shade600 = shades[6],
            shade700 = shades[7],
            shade800 = shades[8],
            shade900 = shades[9],
        )
    }

    /**
     * Generates a complete light or dark [MaigColors] palette from [brand], guaranteeing WCAG AA
     * contrast (`>= 4.5:1`) for all foreground/background "on" color pairings.
     */
    public fun fromBrand(brand: MaigBrand, darkTheme: Boolean): MaigColors {
        val primaryScale = generateScale(brand.primary)
        val secondarySeed = brand.secondary ?: deriveSecondarySeed(brand.primary)
        val secondaryScale = generateScale(secondarySeed)
        val successScale = generateScale(brand.success)
        val warningScale = generateScale(brand.warning)
        val dangerScale = generateScale(brand.danger)
        val infoScale = generateScale(brand.info)
        val neutralScale = if (brand.neutral != null) {
            generateScale(brand.neutral)
        } else {
            generateNeutralScale(brand.primary)
        }

        return if (darkTheme) {
            buildDarkColors(
                primary = primaryScale,
                secondary = secondaryScale,
                success = successScale,
                warning = warningScale,
                danger = dangerScale,
                info = infoScale,
                neutral = neutralScale,
            )
        } else {
            buildLightColors(
                primary = primaryScale,
                secondary = secondaryScale,
                success = successScale,
                warning = warningScale,
                danger = dangerScale,
                info = infoScale,
                neutral = neutralScale,
            )
        }
    }

    private fun deriveSecondarySeed(primary: Color): Color {
        val oklch = MaigOklch.fromColor(primary)
        val secondaryHue = (oklch.h + 42f) % 360f
        val secondaryChroma = (oklch.c * 0.55f).coerceIn(0.03f, 0.14f)
        return MaigOklch(
            l = 0.52f,
            c = secondaryChroma,
            h = secondaryHue,
        ).toColor()
    }

    private fun buildLightColors(
        primary: MaigColorScale,
        secondary: MaigColorScale,
        success: MaigColorScale,
        warning: MaigColorScale,
        danger: MaigColorScale,
        info: MaigColorScale,
        neutral: MaigColorScale,
    ): MaigColors {
        val background = Color.White
        val backgroundSubtle = neutral.shade50
        val surface = Color.White
        val surfaceVariant = neutral.shade100
        val surfaceElevated = Color.White

        val primaryDefault = primary.shade500
        val primaryHover = primary.shade600
        val primaryActive = primary.shade700
        val primarySubtle = primary.shade50
        val onPrimary = MaigColorUtils.bestOnColor(primaryDefault)
        val onPrimarySubtle = MaigColorUtils.ensureContrast(primary.shade800, primarySubtle)

        val secondaryDefault = secondary.shade600
        val secondaryHover = secondary.shade700
        val secondaryActive = secondary.shade800
        val secondarySubtle = secondary.shade50
        val onSecondary = MaigColorUtils.bestOnColor(secondaryDefault)
        val onSecondarySubtle = MaigColorUtils.ensureContrast(secondary.shade800, secondarySubtle)

        val successDefault = success.shade600
        val successSubtle = success.shade50
        val onSuccess = MaigColorUtils.bestOnColor(successDefault)
        val onSuccessSubtle = MaigColorUtils.ensureContrast(success.shade800, successSubtle)

        val warningDefault = warning.shade500
        val warningSubtle = warning.shade50
        val onWarning = MaigColorUtils.bestOnColor(warningDefault)
        val onWarningSubtle = MaigColorUtils.ensureContrast(warning.shade800, warningSubtle)

        val dangerDefault = danger.shade600
        val dangerSubtle = danger.shade50
        val onDanger = MaigColorUtils.bestOnColor(dangerDefault)
        val onDangerSubtle = MaigColorUtils.ensureContrast(danger.shade800, dangerSubtle)

        val infoDefault = info.shade600
        val infoSubtle = info.shade50
        val onInfo = MaigColorUtils.bestOnColor(infoDefault)
        val onInfoSubtle = MaigColorUtils.ensureContrast(info.shade800, infoSubtle)

        val textPrimary = MaigColorUtils.ensureContrast(neutral.shade900, surface)
        val textSecondary = MaigColorUtils.ensureContrast(neutral.shade600, surface)
        val textTertiary = MaigColorUtils.ensureContrast(neutral.shade500, surface)
        val disabled = neutral.shade100
        val onDisabled = neutral.shade400

        return MaigColors(
            isDark = false,
            primary = primary,
            secondary = secondary,
            success = success,
            warning = warning,
            danger = danger,
            info = info,
            neutral = neutral,
            primaryDefault = primaryDefault,
            primaryHover = primaryHover,
            primaryActive = primaryActive,
            primarySubtle = primarySubtle,
            onPrimary = onPrimary,
            onPrimarySubtle = onPrimarySubtle,
            secondaryDefault = secondaryDefault,
            secondaryHover = secondaryHover,
            secondaryActive = secondaryActive,
            secondarySubtle = secondarySubtle,
            onSecondary = onSecondary,
            onSecondarySubtle = onSecondarySubtle,
            successDefault = successDefault,
            successSubtle = successSubtle,
            onSuccess = onSuccess,
            onSuccessSubtle = onSuccessSubtle,
            warningDefault = warningDefault,
            warningSubtle = warningSubtle,
            onWarning = onWarning,
            onWarningSubtle = onWarningSubtle,
            dangerDefault = dangerDefault,
            dangerSubtle = dangerSubtle,
            onDanger = onDanger,
            onDangerSubtle = onDangerSubtle,
            infoDefault = infoDefault,
            infoSubtle = infoSubtle,
            onInfo = onInfo,
            onInfoSubtle = onInfoSubtle,
            background = background,
            backgroundSubtle = backgroundSubtle,
            surface = surface,
            surfaceVariant = surfaceVariant,
            surfaceElevated = surfaceElevated,
            onBackground = textPrimary,
            onSurface = textPrimary,
            onSurfaceVariant = textSecondary,
            textPrimary = textPrimary,
            textSecondary = textSecondary,
            textTertiary = textTertiary,
            textDisabled = neutral.shade400,
            textInverse = Color.White,
            border = neutral.shade200,
            borderSubtle = neutral.shade100,
            borderStrong = neutral.shade300,
            borderFocus = primaryDefault,
            divider = neutral.shade200,
            disabled = disabled,
            onDisabled = onDisabled,
            scrim = Color(0x8009090B),
            shadow = Color(0x1A09090B),
            focusRing = primaryDefault.copy(alpha = 0.25f),
            ripple = primaryDefault.copy(alpha = 0.12f),
        )
    }

    private fun buildDarkColors(
        primary: MaigColorScale,
        secondary: MaigColorScale,
        success: MaigColorScale,
        warning: MaigColorScale,
        danger: MaigColorScale,
        info: MaigColorScale,
        neutral: MaigColorScale,
    ): MaigColors {
        val bgHue = MaigOklch.fromColor(primary.shade500).h
        val bgChroma = if (MaigOklch.fromColor(primary.shade500).c < 0.01f) 0f else 0.008f

        val background = MaigOklch(l = 0.145f, c = bgChroma, h = bgHue).toColor()
        val backgroundSubtle = MaigOklch(l = 0.175f, c = bgChroma, h = bgHue).toColor()
        val surface = MaigOklch(l = 0.185f, c = bgChroma, h = bgHue).toColor()
        val surfaceVariant = MaigOklch(l = 0.235f, c = bgChroma, h = bgHue).toColor()
        val surfaceElevated = MaigOklch(l = 0.255f, c = bgChroma, h = bgHue).toColor()

        val primaryDefault = primary.shade400
        val primaryHover = primary.shade300
        val primaryActive = primary.shade200
        val primarySubtle = primary.shade900.copy(alpha = 0.48f).compositeOver(surface)
        val onPrimary = MaigColorUtils.bestOnColor(primaryDefault)
        val onPrimarySubtle = MaigColorUtils.ensureContrast(primary.shade200, primarySubtle)

        val secondaryDefault = secondary.shade400
        val secondaryHover = secondary.shade300
        val secondaryActive = secondary.shade200
        val secondarySubtle = secondary.shade900.copy(alpha = 0.48f).compositeOver(surface)
        val onSecondary = MaigColorUtils.bestOnColor(secondaryDefault)
        val onSecondarySubtle = MaigColorUtils.ensureContrast(secondary.shade200, secondarySubtle)

        val successDefault = success.shade400
        val successSubtle = success.shade900.copy(alpha = 0.45f).compositeOver(surface)
        val onSuccess = MaigColorUtils.bestOnColor(successDefault)
        val onSuccessSubtle = MaigColorUtils.ensureContrast(success.shade200, successSubtle)

        val warningDefault = warning.shade400
        val warningSubtle = warning.shade900.copy(alpha = 0.45f).compositeOver(surface)
        val onWarning = MaigColorUtils.bestOnColor(warningDefault)
        val onWarningSubtle = MaigColorUtils.ensureContrast(warning.shade200, warningSubtle)

        val dangerDefault = danger.shade400
        val dangerSubtle = danger.shade900.copy(alpha = 0.45f).compositeOver(surface)
        val onDanger = MaigColorUtils.bestOnColor(dangerDefault)
        val onDangerSubtle = MaigColorUtils.ensureContrast(danger.shade200, dangerSubtle)

        val infoDefault = info.shade400
        val infoSubtle = info.shade900.copy(alpha = 0.45f).compositeOver(surface)
        val onInfo = MaigColorUtils.bestOnColor(infoDefault)
        val onInfoSubtle = MaigColorUtils.ensureContrast(info.shade200, infoSubtle)

        val textPrimary = MaigColorUtils.ensureContrast(neutral.shade50, surface)
        val textSecondary = MaigColorUtils.ensureContrast(neutral.shade300, surface)
        val textTertiary = MaigColorUtils.ensureContrast(neutral.shade400, surface)
        val disabled = MaigOklch(l = 0.24f, c = bgChroma, h = bgHue).toColor()
        val onDisabled = neutral.shade500

        return MaigColors(
            isDark = true,
            primary = primary,
            secondary = secondary,
            success = success,
            warning = warning,
            danger = danger,
            info = info,
            neutral = neutral,
            primaryDefault = primaryDefault,
            primaryHover = primaryHover,
            primaryActive = primaryActive,
            primarySubtle = primarySubtle,
            onPrimary = onPrimary,
            onPrimarySubtle = onPrimarySubtle,
            secondaryDefault = secondaryDefault,
            secondaryHover = secondaryHover,
            secondaryActive = secondaryActive,
            secondarySubtle = secondarySubtle,
            onSecondary = onSecondary,
            onSecondarySubtle = onSecondarySubtle,
            successDefault = successDefault,
            successSubtle = successSubtle,
            onSuccess = onSuccess,
            onSuccessSubtle = onSuccessSubtle,
            warningDefault = warningDefault,
            warningSubtle = warningSubtle,
            onWarning = onWarning,
            onWarningSubtle = onWarningSubtle,
            dangerDefault = dangerDefault,
            dangerSubtle = dangerSubtle,
            onDanger = onDanger,
            onDangerSubtle = onDangerSubtle,
            infoDefault = infoDefault,
            infoSubtle = infoSubtle,
            onInfo = onInfo,
            onInfoSubtle = onInfoSubtle,
            background = background,
            backgroundSubtle = backgroundSubtle,
            surface = surface,
            surfaceVariant = surfaceVariant,
            surfaceElevated = surfaceElevated,
            onBackground = textPrimary,
            onSurface = textPrimary,
            onSurfaceVariant = textSecondary,
            textPrimary = textPrimary,
            textSecondary = textSecondary,
            textTertiary = textTertiary,
            textDisabled = neutral.shade500,
            textInverse = neutral.shade900,
            border = MaigOklch(l = 0.30f, c = bgChroma, h = bgHue).toColor(),
            borderSubtle = MaigOklch(l = 0.24f, c = bgChroma, h = bgHue).toColor(),
            borderStrong = MaigOklch(l = 0.40f, c = bgChroma, h = bgHue).toColor(),
            borderFocus = primaryDefault,
            divider = MaigOklch(l = 0.26f, c = bgChroma, h = bgHue).toColor(),
            disabled = disabled,
            onDisabled = onDisabled,
            scrim = Color(0xB3000000),
            shadow = Color(0x66000000),
            focusRing = primaryDefault.copy(alpha = 0.32f),
            ripple = primaryDefault.copy(alpha = 0.16f),
        )
    }
}

/**
 * Internal perceptual OKLCH color representation with sRGB gamut clamping.
 */
internal data class MaigOklch(
    val l: Float,
    val c: Float,
    val h: Float,
) {
    fun toColor(): Color {
        val clampedL = l.coerceIn(0f, 1f)
        if (clampedL <= 0.0001f) return Color.Black
        if (clampedL >= 0.9999f) return Color.White

        val hRad = (h.toDouble() * PI / 180.0)
        val cosH = cos(hRad)
        val sinH = sin(hRad)

        // Binary-search chroma down if outside sRGB gamut so hue & lightness remain faithful.
        var lowC = 0.0
        var highC = c.coerceAtLeast(0f).toDouble()
        var validRgb = oklchToLinearRgb(clampedL.toDouble(), 0.0, cosH, sinH)

        if (isInGamut(oklchToLinearRgb(clampedL.toDouble(), highC, cosH, sinH))) {
            validRgb = oklchToLinearRgb(clampedL.toDouble(), highC, cosH, sinH)
        } else {
            repeat(14) {
                val mid = (lowC + highC) * 0.5
                val candidate = oklchToLinearRgb(clampedL.toDouble(), mid, cosH, sinH)
                if (isInGamut(candidate)) {
                    validRgb = candidate
                    lowC = mid
                } else {
                    highC = mid
                }
            }
        }

        return Color(
            red = linearToSrgb(validRgb[0].coerceIn(0.0, 1.0)).toFloat(),
            green = linearToSrgb(validRgb[1].coerceIn(0.0, 1.0)).toFloat(),
            blue = linearToSrgb(validRgb[2].coerceIn(0.0, 1.0)).toFloat(),
            alpha = 1f,
        )
    }

    companion object {
        fun fromColor(color: Color): MaigOklch {
            val r = srgbToLinear(color.red.toDouble())
            val g = srgbToLinear(color.green.toDouble())
            val b = srgbToLinear(color.blue.toDouble())

            val l = 0.4122214708 * r + 0.5363325363 * g + 0.0514459929 * b
            val m = 0.2119034982 * r + 0.6806995451 * g + 0.1073969566 * b
            val s = 0.0883024619 * r + 0.2817188376 * g + 0.6299787005 * b

            val lRoot = cbrt(l)
            val mRoot = cbrt(m)
            val sRoot = cbrt(s)

            val okL = 0.2104542553 * lRoot + 0.7936177850 * mRoot - 0.0040720468 * sRoot
            val okA = 1.9779984951 * lRoot - 2.4285922050 * mRoot + 0.4505937099 * sRoot
            val okB = 0.0259040371 * lRoot + 0.7827717662 * mRoot - 0.8086757660 * sRoot

            val chroma = hypot(okA, okB)
            val hueDeg = if (chroma < 1e-5) {
                0.0
            } else {
                var deg = atan2(okB, okA) * 180.0 / PI
                if (deg < 0.0) deg += 360.0
                deg
            }

            return MaigOklch(
                l = okL.toFloat().coerceIn(0f, 1f),
                c = chroma.toFloat().coerceAtLeast(0f),
                h = hueDeg.toFloat(),
            )
        }

        private fun oklchToLinearRgb(
            okL: Double,
            chroma: Double,
            cosH: Double,
            sinH: Double,
        ): DoubleArray {
            val okA = chroma * cosH
            val okB = chroma * sinH

            val lRoot = okL + 0.3963377774 * okA + 0.2158037573 * okB
            val mRoot = okL - 0.1055613458 * okA - 0.0638541728 * okB
            val sRoot = okL - 0.0894841775 * okA - 1.2914855480 * okB

            val l = lRoot * lRoot * lRoot
            val m = mRoot * mRoot * mRoot
            val s = sRoot * sRoot * sRoot

            val r = +4.0767416621 * l - 3.3077115913 * m + 0.2309699292 * s
            val g = -1.2684380046 * l + 2.6097574011 * m - 0.3413193965 * s
            val b = -0.0041960863 * l - 0.7034186147 * m + 1.7076147010 * s
            return doubleArrayOf(r, g, b)
        }

        private fun isInGamut(rgb: DoubleArray): Boolean {
            val eps = -0.0005
            val maxEps = 1.0005
            return rgb[0] in eps..maxEps && rgb[1] in eps..maxEps && rgb[2] in eps..maxEps
        }

        private fun srgbToLinear(c: Double): Double =
            if (c <= 0.04045) c / 12.92 else ((c + 0.055) / 1.055).pow(2.4)

        private fun linearToSrgb(c: Double): Double =
            if (c <= 0.0031308) 12.92 * c else 1.055 * c.pow(1.0 / 2.4) - 0.055
    }
}
