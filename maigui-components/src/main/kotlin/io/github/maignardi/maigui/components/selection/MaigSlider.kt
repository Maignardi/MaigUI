package io.github.maignardi.maigui.components.selection

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.setProgress
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.maignardi.maigui.core.theme.MaigTheme
import kotlin.math.abs
import kotlin.math.roundToInt

/**
 * Color tokens for [MaigSlider] and [MaigRangeSlider].
 */
@Immutable
public class MaigSliderColors(
    public val activeTrackColor: Color,
    public val inactiveTrackColor: Color,
    public val thumbColor: Color,
    public val thumbBorderColor: Color,
    public val disabledActiveTrackColor: Color,
    public val disabledInactiveTrackColor: Color,
    public val disabledThumbColor: Color,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is MaigSliderColors) return false
        return activeTrackColor == other.activeTrackColor &&
            inactiveTrackColor == other.inactiveTrackColor &&
            thumbColor == other.thumbColor &&
            thumbBorderColor == other.thumbBorderColor &&
            disabledActiveTrackColor == other.disabledActiveTrackColor &&
            disabledInactiveTrackColor == other.disabledInactiveTrackColor &&
            disabledThumbColor == other.disabledThumbColor
    }

    override fun hashCode(): Int {
        var result = activeTrackColor.hashCode()
        result = 31 * result + inactiveTrackColor.hashCode()
        result = 31 * result + thumbColor.hashCode()
        result = 31 * result + thumbBorderColor.hashCode()
        result = 31 * result + disabledActiveTrackColor.hashCode()
        result = 31 * result + disabledInactiveTrackColor.hashCode()
        result = 31 * result + disabledThumbColor.hashCode()
        return result
    }
}

/**
 * Default values for [MaigSlider] and [MaigRangeSlider].
 */
public object MaigSliderDefaults {
    public val TrackHeight: Dp = 8.dp
    public val ThumbRadius: Dp = 11.dp

    @Composable
    public fun colors(): MaigSliderColors {
        val c = MaigTheme.colors
        return MaigSliderColors(
            activeTrackColor = c.primaryDefault,
            inactiveTrackColor = if (c.isDark) c.neutral.shade700 else c.neutral.shade200,
            thumbColor = Color.White,
            thumbBorderColor = c.primaryDefault,
            disabledActiveTrackColor = c.textDisabled,
            disabledInactiveTrackColor = c.disabled,
            disabledThumbColor = c.onDisabled,
        )
    }
}

/**
 * Single-value interactive slider built on a custom [Canvas] with step snapping and accessibility.
 */
@Composable
public fun MaigSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0,
    label: String? = null,
    valueFormatter: ((Float) -> String)? = null,
    onValueChangeFinished: (() -> Unit)? = null,
    colors: MaigSliderColors = MaigSliderDefaults.colors(),
) {
    val fraction = valueToFraction(value, valueRange)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MaigTheme.spacing.xs),
    ) {
        if (label != null || valueFormatter != null) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (label != null) {
                    BasicText(
                        text = label,
                        style = MaigTheme.typography.label.copy(color = MaigTheme.colors.textPrimary),
                    )
                }
                if (valueFormatter != null) {
                    BasicText(
                        text = valueFormatter(value),
                        style = MaigTheme.typography.label.copy(color = MaigTheme.colors.primaryDefault),
                    )
                }
            }
        }

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .semantics {
                    setProgress { targetValue ->
                        val snapped = snapValue(targetValue, valueRange, steps)
                        if (snapped != value) {
                            onValueChange(snapped)
                            true
                        } else {
                            false
                        }
                    }
                }
                .then(
                    if (enabled) {
                        Modifier
                            .pointerInput(valueRange, steps) {
                                detectTapGestures { offset ->
                                    val thumbPx = MaigSliderDefaults.ThumbRadius.toPx()
                                    val usable = (size.width - 2 * thumbPx).coerceAtLeast(1f)
                                    val rawFrac = ((offset.x - thumbPx) / usable).coerceIn(0f, 1f)
                                    onValueChange(fractionToSnappedValue(rawFrac, valueRange, steps))
                                    onValueChangeFinished?.invoke()
                                }
                            }
                            .pointerInput(valueRange, steps) {
                                detectDragGestures(
                                    onDragEnd = { onValueChangeFinished?.invoke() },
                                    onDragCancel = { onValueChangeFinished?.invoke() },
                                ) { change, _ ->
                                    change.consume()
                                    val thumbPx = MaigSliderDefaults.ThumbRadius.toPx()
                                    val usable = (size.width - 2 * thumbPx).coerceAtLeast(1f)
                                    val rawFrac = ((change.position.x - thumbPx) / usable).coerceIn(0f, 1f)
                                    onValueChange(fractionToSnappedValue(rawFrac, valueRange, steps))
                                }
                            }
                    } else {
                        Modifier
                    },
                ),
        ) {
            val thumbPx = MaigSliderDefaults.ThumbRadius.toPx()
            val trackHeightPx = MaigSliderDefaults.TrackHeight.toPx()
            val trackStart = thumbPx
            val trackEnd = (size.width - thumbPx).coerceAtLeast(trackStart)
            val usableWidth = trackEnd - trackStart
            val centerY = size.height / 2f
            val activeEnd = trackStart + usableWidth * fraction

            val inactiveColor = if (enabled) colors.inactiveTrackColor else colors.disabledInactiveTrackColor
            val activeColor = if (enabled) colors.activeTrackColor else colors.disabledActiveTrackColor
            val thumbColor = if (enabled) colors.thumbColor else colors.disabledThumbColor
            val thumbBorder = if (enabled) colors.thumbBorderColor else colors.disabledActiveTrackColor

            drawRoundRect(
                color = inactiveColor,
                topLeft = Offset(trackStart, centerY - trackHeightPx / 2f),
                size = Size(usableWidth, trackHeightPx),
                cornerRadius = CornerRadius(trackHeightPx / 2f, trackHeightPx / 2f),
            )
            drawRoundRect(
                color = activeColor,
                topLeft = Offset(trackStart, centerY - trackHeightPx / 2f),
                size = Size(usableWidth * fraction, trackHeightPx),
                cornerRadius = CornerRadius(trackHeightPx / 2f, trackHeightPx / 2f),
            )

            drawCircle(
                color = thumbBorder.copy(alpha = 0.16f),
                radius = thumbPx * 1.55f,
                center = Offset(activeEnd, centerY),
            )
            drawCircle(
                color = thumbBorder,
                radius = thumbPx,
                center = Offset(activeEnd, centerY),
            )
            drawCircle(
                color = thumbColor,
                radius = thumbPx - 3.dp.toPx(),
                center = Offset(activeEnd, centerY),
            )
        }
    }
}

/**
 * Dual-thumb interactive range slider supporting min/max bounds and step snapping.
 */
@Composable
public fun MaigRangeSlider(
    value: ClosedFloatingPointRange<Float>,
    onValueChange: (ClosedFloatingPointRange<Float>) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0,
    label: String? = null,
    rangeFormatter: ((ClosedFloatingPointRange<Float>) -> String)? = null,
    onValueChangeFinished: (() -> Unit)? = null,
    colors: MaigSliderColors = MaigSliderDefaults.colors(),
) {
    val startFrac = valueToFraction(value.start, valueRange)
    val endFrac = valueToFraction(value.endInclusive, valueRange)
    var draggingStart by remember { mutableStateOf<Boolean?>(null) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MaigTheme.spacing.xs),
    ) {
        if (label != null || rangeFormatter != null) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (label != null) {
                    BasicText(
                        text = label,
                        style = MaigTheme.typography.label.copy(color = MaigTheme.colors.textPrimary),
                    )
                }
                if (rangeFormatter != null) {
                    BasicText(
                        text = rangeFormatter(value),
                        style = MaigTheme.typography.label.copy(color = MaigTheme.colors.primaryDefault),
                    )
                }
            }
        }

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .then(
                    if (enabled) {
                        Modifier.pointerInput(value, valueRange, steps) {
                            detectDragGestures(
                                onDragStart = { offset ->
                                    val thumbPx = MaigSliderDefaults.ThumbRadius.toPx()
                                    val usable = (size.width - 2 * thumbPx).coerceAtLeast(1f)
                                    val tapFrac = ((offset.x - thumbPx) / usable).coerceIn(0f, 1f)
                                    draggingStart = abs(tapFrac - startFrac) <= abs(tapFrac - endFrac)
                                },
                                onDragEnd = {
                                    draggingStart = null
                                    onValueChangeFinished?.invoke()
                                },
                                onDragCancel = {
                                    draggingStart = null
                                    onValueChangeFinished?.invoke()
                                },
                            ) { change, _ ->
                                change.consume()
                                val thumbPx = MaigSliderDefaults.ThumbRadius.toPx()
                                val usable = (size.width - 2 * thumbPx).coerceAtLeast(1f)
                                val frac = ((change.position.x - thumbPx) / usable).coerceIn(0f, 1f)
                                val snapped = fractionToSnappedValue(frac, valueRange, steps)
                                if (draggingStart == true) {
                                    onValueChange(snapped.coerceAtMost(value.endInclusive)..value.endInclusive)
                                } else {
                                    onValueChange(value.start..snapped.coerceAtLeast(value.start))
                                }
                            }
                        }
                    } else {
                        Modifier
                    },
                ),
        ) {
            val thumbPx = MaigSliderDefaults.ThumbRadius.toPx()
            val trackHeightPx = MaigSliderDefaults.TrackHeight.toPx()
            val trackStart = thumbPx
            val usableWidth = (size.width - 2 * thumbPx).coerceAtLeast(1f)
            val centerY = size.height / 2f
            val startX = trackStart + usableWidth * startFrac
            val endX = trackStart + usableWidth * endFrac

            val inactiveColor = if (enabled) colors.inactiveTrackColor else colors.disabledInactiveTrackColor
            val activeColor = if (enabled) colors.activeTrackColor else colors.disabledActiveTrackColor
            val thumbColor = if (enabled) colors.thumbColor else colors.disabledThumbColor
            val thumbBorder = if (enabled) colors.thumbBorderColor else colors.disabledActiveTrackColor

            drawRoundRect(
                color = inactiveColor,
                topLeft = Offset(trackStart, centerY - trackHeightPx / 2f),
                size = Size(usableWidth, trackHeightPx),
                cornerRadius = CornerRadius(trackHeightPx / 2f, trackHeightPx / 2f),
            )
            drawRoundRect(
                color = activeColor,
                topLeft = Offset(startX, centerY - trackHeightPx / 2f),
                size = Size((endX - startX).coerceAtLeast(0f), trackHeightPx),
                cornerRadius = CornerRadius(trackHeightPx / 2f, trackHeightPx / 2f),
            )

            listOf(startX, endX).forEach { x ->
                drawCircle(thumbBorder.copy(alpha = 0.16f), radius = thumbPx * 1.5f, center = Offset(x, centerY))
                drawCircle(thumbBorder, radius = thumbPx, center = Offset(x, centerY))
                drawCircle(thumbColor, radius = thumbPx - 3.dp.toPx(), center = Offset(x, centerY))
            }
        }
    }
}

private fun valueToFraction(value: Float, range: ClosedFloatingPointRange<Float>): Float {
    val span = range.endInclusive - range.start
    if (span <= 0f) return 0f
    return ((value - range.start) / span).coerceIn(0f, 1f)
}

private fun fractionToSnappedValue(
    fraction: Float,
    range: ClosedFloatingPointRange<Float>,
    steps: Int,
): Float {
    val clampedFrac = if (steps > 0) {
        val stepCount = steps + 1
        (fraction * stepCount).roundToInt().toFloat() / stepCount
    } else {
        fraction
    }
    return (range.start + (range.endInclusive - range.start) * clampedFrac)
        .coerceIn(range.start, range.endInclusive)
}

private fun snapValue(
    value: Float,
    range: ClosedFloatingPointRange<Float>,
    steps: Int,
): Float = fractionToSnappedValue(valueToFraction(value, range), range, steps)

@Preview(showBackground = true)
@Composable
private fun MaigSliderLightPreview() {
    MaigTheme(darkTheme = false) {
        Column(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(MaigTheme.spacing.lg),
            verticalArrangement = Arrangement.spacedBy(MaigTheme.spacing.md),
        ) {
            MaigSlider(
                value = 0.65f,
                onValueChange = {},
                label = "Volume",
                valueFormatter = { "${(it * 100).roundToInt()}%" },
            )
            MaigRangeSlider(
                value = 20f..80f,
                onValueChange = {},
                valueRange = 0f..100f,
                label = "Faixa de preço",
                rangeFormatter = { "R$ ${it.start.roundToInt()} - R$ ${it.endInclusive.roundToInt()}" },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigSliderDarkPreview() {
    MaigTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(MaigTheme.spacing.lg),
        ) {
            MaigSlider(
                value = 0.4f,
                onValueChange = {},
                label = "Brilho",
                valueFormatter = { "${(it * 100).roundToInt()}%" },
            )
        }
    }
}
