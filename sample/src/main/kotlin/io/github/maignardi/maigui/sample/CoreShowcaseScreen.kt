package io.github.maignardi.maigui.sample

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.maignardi.maigui.core.color.MaigColorUtils
import io.github.maignardi.maigui.core.theme.MaigBrand
import io.github.maignardi.maigui.core.theme.MaigTheme
import io.github.maignardi.maigui.core.theme.MaigThemes
import io.github.maignardi.maigui.core.tokens.MaigColorScale
import java.util.Locale

internal enum class ThemeMode {
    System,
    Light,
    Dark,
}

@Composable
fun MaigSampleApp() {
    var themeMode by remember { mutableStateOf(ThemeMode.System) }
    var selectedBrand by remember { mutableStateOf(MaigThemes.Default.brand) }
    var activePresetName by remember { mutableStateOf(MaigThemes.Default.name) }

    val isDark = when (themeMode) {
        ThemeMode.System -> isSystemInDarkTheme()
        ThemeMode.Light -> false
        ThemeMode.Dark -> true
    }

    MaigTheme(
        brand = selectedBrand,
        darkTheme = isDark,
    ) {
        CoreShowcaseScreen(
            themeMode = themeMode,
            onThemeModeChange = { themeMode = it },
            activePresetName = activePresetName,
            onSelectPreset = { name, brand ->
                activePresetName = name
                selectedBrand = brand
            },
        )
    }
}

@Composable
internal fun CoreShowcaseScreen(
    themeMode: ThemeMode,
    onThemeModeChange: (ThemeMode) -> Unit,
    activePresetName: String,
    onSelectPreset: (String, MaigBrand) -> Unit,
) {
    val colors = MaigTheme.colors
    val typography = MaigTheme.typography
    val spacing = MaigTheme.spacing
    val shapes = MaigTheme.shapes
    val elevation = MaigTheme.elevation

    val customSeeds = remember {
        listOf(
            "Rose" to Color(0xFFE11D48),
            "Teal" to Color(0xFF0D9488),
            "Indigo" to Color(0xFF4F46E5),
            "Amber" to Color(0xFFD97706),
            "Cyan" to Color(0xFF0891B2),
            "Fuchsia" to Color(0xFFC026D3),
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(spacing.lg),
        verticalArrangement = Arrangement.spacedBy(spacing.xl),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(spacing.xs)) {
            BasicText(
                text = "MaigUI Design System",
                style = typography.displayMedium.copy(color = colors.textPrimary),
            )
            BasicText(
                text = "Core tokens, OKLCH palette generator & WCAG AA contrast verifier",
                style = typography.body.copy(color = colors.textSecondary),
            )
        }

        TokenSection(title = "Theme Controls") {
            Column(verticalArrangement = Arrangement.spacedBy(spacing.md)) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(spacing.sm),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    ThemeMode.entries.forEach { mode ->
                        val selected = mode == themeMode
                        PillChip(
                            label = mode.name,
                            selected = selected,
                            onClick = { onThemeModeChange(mode) },
                        )
                    }
                }

                BasicText(
                    text = "Built-in Presets",
                    style = typography.label.copy(color = colors.textSecondary),
                )
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(spacing.sm),
                ) {
                    MaigThemes.presets.forEach { preset ->
                        PillChip(
                            label = preset.name,
                            selected = activePresetName == preset.name,
                            swatch = preset.brand.primary,
                            onClick = { onSelectPreset(preset.name, preset.brand) },
                        )
                    }
                }

                BasicText(
                    text = "Dynamic Brand Seed (single color -> full palette)",
                    style = typography.label.copy(color = colors.textSecondary),
                )
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(spacing.sm),
                ) {
                    customSeeds.forEach { (name, color) ->
                        PillChip(
                            label = name,
                            selected = activePresetName == name,
                            swatch = color,
                            onClick = { onSelectPreset(name, MaigBrand(primary = color)) },
                        )
                    }
                }
            }
        }

        TokenSection(title = "Semantic Roles & WCAG AA Contrast") {
            val semanticPairs = listOf(
                Triple("Primary", colors.primaryDefault, colors.onPrimary),
                Triple("Primary Subtle", colors.primarySubtle, colors.onPrimarySubtle),
                Triple("Secondary", colors.secondaryDefault, colors.onSecondary),
                Triple("Success", colors.successDefault, colors.onSuccess),
                Triple("Warning", colors.warningDefault, colors.onWarning),
                Triple("Danger", colors.dangerDefault, colors.onDanger),
                Triple("Info", colors.infoDefault, colors.onInfo),
            )

            Column(verticalArrangement = Arrangement.spacedBy(spacing.sm)) {
                semanticPairs.forEach { (name, bg, fg) ->
                    val ratio = MaigColorUtils.contrastRatio(fg, bg)
                    val formatted = String.format(Locale.US, "%.2f:1 AA", ratio)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(shapes.md)
                            .background(bg)
                            .padding(horizontal = spacing.lg, vertical = spacing.md),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        BasicText(
                            text = name,
                            style = typography.button.copy(color = fg),
                        )
                        BasicText(
                            text = formatted,
                            style = typography.label.copy(color = fg),
                        )
                    }
                }
            }
        }

        TokenSection(title = "Tonal Color Scales (50-900)") {
            Column(verticalArrangement = Arrangement.spacedBy(spacing.md)) {
                ScaleRow("Primary", colors.primary)
                ScaleRow("Secondary", colors.secondary)
                ScaleRow("Success", colors.success)
                ScaleRow("Warning", colors.warning)
                ScaleRow("Danger", colors.danger)
                ScaleRow("Info", colors.info)
                ScaleRow("Neutral", colors.neutral)
            }
        }

        TokenSection(title = "Typography (Inter)") {
            val styles: List<Pair<String, TextStyle>> = listOf(
                "displayLarge (34sp Bold)" to typography.displayLarge,
                "displayMedium (28sp Bold)" to typography.displayMedium,
                "headline (22sp SemiBold)" to typography.headline,
                "title (18sp SemiBold)" to typography.title,
                "subtitle (16sp Medium)" to typography.subtitle,
                "body (15sp Regular)" to typography.body,
                "bodySmall (13sp Regular)" to typography.bodySmall,
                "label (13sp Medium)" to typography.label,
                "caption (11sp Regular)" to typography.caption,
                "button (14sp SemiBold)" to typography.button,
            )

            Column(verticalArrangement = Arrangement.spacedBy(spacing.sm)) {
                styles.forEach { (label, style) ->
                    BasicText(
                        text = label,
                        style = style.copy(color = colors.textPrimary),
                    )
                }
            }
        }

        TokenSection(title = "Spacing Scale") {
            val spacings: List<Pair<String, Dp>> = listOf(
                "xxs (2dp)" to spacing.xxs,
                "xs (4dp)" to spacing.xs,
                "sm (8dp)" to spacing.sm,
                "md (12dp)" to spacing.md,
                "lg (16dp)" to spacing.lg,
                "xl (24dp)" to spacing.xl,
                "xxl (32dp)" to spacing.xxl,
                "xxxl (48dp)" to spacing.xxxl,
            )

            Column(verticalArrangement = Arrangement.spacedBy(spacing.sm)) {
                spacings.forEach { (label, value) ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(spacing.md),
                    ) {
                        BasicText(
                            text = label,
                            modifier = Modifier.width(104.dp),
                            style = typography.caption.copy(color = colors.textSecondary),
                        )
                        Box(
                            modifier = Modifier
                                .width(value * 3)
                                .height(12.dp)
                                .clip(shapes.xs)
                                .background(colors.primaryDefault),
                        )
                    }
                }
            }
        }

        TokenSection(title = "Shapes & Elevation") {
            val shapeTokens = listOf(
                "none" to shapes.none,
                "xs" to shapes.xs,
                "sm" to shapes.sm,
                "md" to shapes.md,
                "lg" to shapes.lg,
                "xl" to shapes.xl,
                "full" to shapes.full,
            )

            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(spacing.md),
            ) {
                shapeTokens.forEach { (name, shape) ->
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .shadow(elevation.sm, shape = shape)
                            .clip(shape)
                            .background(colors.surfaceElevated)
                            .border(1.dp, colors.border, shape),
                        contentAlignment = Alignment.Center,
                    ) {
                        BasicText(
                            text = name,
                            style = typography.label.copy(color = colors.textPrimary),
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(spacing.lg))
    }
}

@Composable
private fun TokenSection(
    title: String,
    content: @Composable () -> Unit,
) {
    val colors = MaigTheme.colors
    val typography = MaigTheme.typography
    val spacing = MaigTheme.spacing
    val shapes = MaigTheme.shapes

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shapes.lg)
            .background(colors.surface)
            .border(1.dp, colors.border, shapes.lg)
            .padding(spacing.lg),
        verticalArrangement = Arrangement.spacedBy(spacing.md),
    ) {
        BasicText(
            text = title,
            style = typography.title.copy(color = colors.textPrimary),
        )
        content()
    }
}

@Composable
private fun PillChip(
    label: String,
    selected: Boolean,
    swatch: Color? = null,
    onClick: () -> Unit,
) {
    val colors = MaigTheme.colors
    val typography = MaigTheme.typography
    val spacing = MaigTheme.spacing
    val shapes = MaigTheme.shapes

    val bg = if (selected) colors.primaryDefault else colors.surfaceVariant
    val fg = if (selected) colors.onPrimary else colors.textPrimary
    val borderColor = if (selected) colors.primaryDefault else colors.border

    Row(
        modifier = Modifier
            .clip(shapes.full)
            .background(bg)
            .border(1.dp, borderColor, shapes.full)
            .clickable(onClick = onClick)
            .padding(horizontal = spacing.md, vertical = spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(spacing.xs),
    ) {
        if (swatch != null) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(shapes.full)
                    .background(swatch)
                    .border(1.dp, Color.White.copy(alpha = 0.6f), shapes.full),
            )
        }
        BasicText(
            text = label,
            style = typography.label.copy(color = fg),
        )
    }
}

@Composable
private fun ScaleRow(
    name: String,
    scale: MaigColorScale,
) {
    val colors = MaigTheme.colors
    val typography = MaigTheme.typography
    val spacing = MaigTheme.spacing
    val shapes = MaigTheme.shapes

    Column(verticalArrangement = Arrangement.spacedBy(spacing.xs)) {
        BasicText(
            text = name,
            style = typography.label.copy(color = colors.textSecondary),
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            scale.asList().forEach { (weight, shade) ->
                val textColor = MaigColorUtils.bestOnColor(shade)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(34.dp)
                        .clip(shapes.xs)
                        .background(shade),
                    contentAlignment = Alignment.Center,
                ) {
                    BasicText(
                        text = weight.toString(),
                        style = typography.caption.copy(color = textColor),
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CoreShowcaseLightPreview() {
    MaigTheme(darkTheme = false) {
        CoreShowcaseScreen(
            themeMode = ThemeMode.Light,
            onThemeModeChange = {},
            activePresetName = "Default",
            onSelectPreset = { _, _ -> },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CoreShowcaseDarkPreview() {
    MaigTheme(darkTheme = true) {
        CoreShowcaseScreen(
            themeMode = ThemeMode.Dark,
            onThemeModeChange = {},
            activePresetName = "Default",
            onSelectPreset = { _, _ -> },
        )
    }
}
