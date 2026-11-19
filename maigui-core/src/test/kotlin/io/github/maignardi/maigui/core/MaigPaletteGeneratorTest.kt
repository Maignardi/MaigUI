package io.github.maignardi.maigui.core

import androidx.compose.ui.graphics.Color
import io.github.maignardi.maigui.core.color.MaigColorUtils
import io.github.maignardi.maigui.core.color.MaigPaletteGenerator
import io.github.maignardi.maigui.core.theme.MaigBrand
import io.github.maignardi.maigui.core.theme.MaigThemes
import io.github.maignardi.maigui.core.tokens.MaigColors
import io.github.maignardi.maigui.core.tokens.MaigElevation
import io.github.maignardi.maigui.core.tokens.MaigSpacing
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MaigPaletteGeneratorTest {

    @Test
    fun `all built-in presets meet WCAG AA contrast in light and dark modes`() {
        for (preset in MaigThemes.presets) {
            assertPaletteMeetsWcagAa("${preset.name}-Light", preset.lightColors)
            assertPaletteMeetsWcagAa("${preset.name}-Dark", preset.darkColors)
            assertFalse(preset.lightColors.isDark)
            assertTrue(preset.darkColors.isDark)
        }
    }

    @Test
    fun `arbitrary and extreme brand colors meet WCAG AA contrast in light and dark modes`() {
        val edgeSeeds = listOf(
            Color(0xFFFEF08A), // Bright pastel yellow
            Color(0xFF22D3EE), // Neon cyan
            Color(0xFF000000), // Pure black
            Color(0xFFFFFFFF), // Pure white
            Color(0xFF0F172A), // Very dark navy
            Color(0xFFEC4899), // Hot pink
            Color(0xFF84CC16), // Lime green
            Color(0xFF8B5CF6), // Lavender violet
        )

        edgeSeeds.forEachIndexed { index, seed ->
            val brand = MaigBrand(primary = seed)
            val light = MaigPaletteGenerator.fromBrand(brand, darkTheme = false)
            val dark = MaigPaletteGenerator.fromBrand(brand, darkTheme = true)
            assertPaletteMeetsWcagAa("Seed[$index]-Light", light)
            assertPaletteMeetsWcagAa("Seed[$index]-Dark", dark)
        }
    }

    @Test
    fun `generated scale preserves descending relative luminance from 50 to 900`() {
        val scale = MaigPaletteGenerator.generateScale(Color(0xFF2563EB))
        val luminances = scale.asList().map { (_, color) -> MaigColorUtils.relativeLuminance(color) }

        for (i in 0 until luminances.lastIndex) {
            assertTrue(
                "Expected luminance[${scale.asList()[i].first}] (${luminances[i]}) > luminance[${scale.asList()[i + 1].first}] (${luminances[i + 1]})",
                luminances[i] > luminances[i + 1],
            )
        }
    }

    @Test
    fun `scale weight operator returns matching shades and rejects invalid weight`() {
        val scale = MaigPaletteGenerator.generateScale(Color(0xFF7C3AED))
        assertEquals(scale.shade50, scale[50])
        assertEquals(scale.shade500, scale[500])
        assertEquals(scale.shade900, scale[900])

        val thrown = runCatching { scale[450] }.exceptionOrNull()
        assertTrue(thrown is IllegalArgumentException)
    }

    @Test
    fun `spacing and elevation defaults match specification`() {
        val spacing = MaigSpacing()
        assertEquals(2f, spacing.xxs.value)
        assertEquals(4f, spacing.xs.value)
        assertEquals(8f, spacing.sm.value)
        assertEquals(12f, spacing.md.value)
        assertEquals(16f, spacing.lg.value)
        assertEquals(24f, spacing.xl.value)
        assertEquals(32f, spacing.xxl.value)
        assertEquals(48f, spacing.xxxl.value)

        val elevation = MaigElevation()
        assertEquals(0f, elevation.none.value)
        assertEquals(1f, elevation.xs.value)
        assertEquals(2f, elevation.sm.value)
        assertEquals(4f, elevation.md.value)
        assertEquals(8f, elevation.lg.value)
        assertEquals(16f, elevation.xl.value)
    }

    private fun assertPaletteMeetsWcagAa(label: String, colors: MaigColors) {
        val pairs = listOf(
            "onPrimary / primaryDefault" to (colors.onPrimary to colors.primaryDefault),
            "onPrimarySubtle / primarySubtle" to (colors.onPrimarySubtle to colors.primarySubtle),
            "onSecondary / secondaryDefault" to (colors.onSecondary to colors.secondaryDefault),
            "onSecondarySubtle / secondarySubtle" to (colors.onSecondarySubtle to colors.secondarySubtle),
            "onSuccess / successDefault" to (colors.onSuccess to colors.successDefault),
            "onSuccessSubtle / successSubtle" to (colors.onSuccessSubtle to colors.successSubtle),
            "onWarning / warningDefault" to (colors.onWarning to colors.warningDefault),
            "onWarningSubtle / warningSubtle" to (colors.onWarningSubtle to colors.warningSubtle),
            "onDanger / dangerDefault" to (colors.onDanger to colors.dangerDefault),
            "onDangerSubtle / dangerSubtle" to (colors.onDangerSubtle to colors.dangerSubtle),
            "onInfo / infoDefault" to (colors.onInfo to colors.infoDefault),
            "onInfoSubtle / infoSubtle" to (colors.onInfoSubtle to colors.infoSubtle),
            "textPrimary / background" to (colors.textPrimary to colors.background),
            "textSecondary / background" to (colors.textSecondary to colors.background),
            "textPrimary / surface" to (colors.textPrimary to colors.surface),
            "textSecondary / surface" to (colors.textSecondary to colors.surface),
        )

        for ((pairName, pair) in pairs) {
            val (fg, bg) = pair
            val ratio = MaigColorUtils.contrastRatio(fg, bg)
            assertTrue(
                "$label -> $pairName contrast $ratio < 4.5 (WCAG AA)",
                ratio >= MaigColorUtils.WCAG_AA_NORMAL,
            )
        }
    }
}
