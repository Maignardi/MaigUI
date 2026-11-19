package io.github.maignardi.maigui.components.feedback

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.maignardi.maigui.core.theme.MaigTheme
import kotlin.math.roundToInt

/**
 * Default configuration for [MaigLinearProgressBar] and [MaigCircularProgressBar].
 */
public object MaigProgressDefaults {
    public val LinearHeight: Dp = 8.dp
    public val CircularSize: Dp = 44.dp
    public val CircularStrokeWidth: Dp = 4.dp
}

/**
 * Linear progress bar supporting both determinate (`progress != null`) and indeterminate (`progress == null`) modes.
 */
@Composable
public fun MaigLinearProgressBar(
    modifier: Modifier = Modifier,
    progress: Float? = null,
    height: Dp = MaigProgressDefaults.LinearHeight,
    color: Color = MaigTheme.colors.primaryDefault,
    trackColor: Color = MaigTheme.colors.surfaceVariant,
    showPercentage: Boolean = false,
) {
    val clampedProgress = progress?.coerceIn(0f, 1f)
    val animatedProgress by animateFloatAsState(
        targetValue = clampedProgress ?: 0f,
        animationSpec = tween(250, easing = FastOutSlowInEasing),
        label = "MaigLinearProgress",
    )

    val indeterminateFraction = if (clampedProgress == null) {
        val transition = rememberInfiniteTransition(label = "MaigLinearIndeterminate")
        val value by transition.animateFloat(
            initialValue = -0.35f,
            targetValue = 1.05f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 1200, easing = LinearEasing),
                repeatMode = RepeatMode.Restart,
            ),
            label = "MaigLinearOffset",
        )
        value
    } else {
        0f
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MaigTheme.spacing.sm),
    ) {
        Canvas(
            modifier = Modifier
                .weight(1f)
                .height(height)
                .semantics {
                    progressBarRangeInfo = if (clampedProgress != null) {
                        ProgressBarRangeInfo(clampedProgress, 0f..1f)
                    } else {
                        ProgressBarRangeInfo.Indeterminate
                    }
                },
        ) {
            val radius = CornerRadius(size.height / 2f, size.height / 2f)
            drawRoundRect(
                color = trackColor,
                size = size,
                cornerRadius = radius,
            )

            if (clampedProgress != null) {
                val fillW = size.width * animatedProgress
                if (fillW > 0f) {
                    drawRoundRect(
                        color = color,
                        size = Size(fillW, size.height),
                        cornerRadius = radius,
                    )
                }
            } else {
                val barW = size.width * 0.35f
                val startX = (size.width * indeterminateFraction).coerceIn(0f, size.width)
                val endX = (size.width * indeterminateFraction + barW).coerceIn(0f, size.width)
                val visibleW = (endX - startX).coerceAtLeast(0f)
                if (visibleW > 0f) {
                    drawRoundRect(
                        color = color,
                        topLeft = Offset(startX, 0f),
                        size = Size(visibleW, size.height),
                        cornerRadius = radius,
                    )
                }
            }
        }

        if (showPercentage && clampedProgress != null) {
            BasicText(
                text = "${(clampedProgress * 100).roundToInt()}%",
                style = MaigTheme.typography.caption.copy(color = MaigTheme.colors.textSecondary),
            )
        }
    }
}

/**
 * Circular progress indicator supporting determinate (`progress != null`) and indeterminate (`progress == null`) modes.
 */
@Composable
public fun MaigCircularProgressBar(
    modifier: Modifier = Modifier,
    progress: Float? = null,
    size: Dp = MaigProgressDefaults.CircularSize,
    strokeWidth: Dp = MaigProgressDefaults.CircularStrokeWidth,
    color: Color = MaigTheme.colors.primaryDefault,
    trackColor: Color = MaigTheme.colors.surfaceVariant,
    showPercentage: Boolean = false,
) {
    val clamped = progress?.coerceIn(0f, 1f)
    val animatedSweep by animateFloatAsState(
        targetValue = (clamped ?: 0f) * 360f,
        animationSpec = tween(250),
        label = "MaigCircularSweep",
    )

    val indeterminateStart = if (clamped == null) {
        val transition = rememberInfiniteTransition(label = "MaigCircularIndeterminate")
        val angle by transition.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(950, easing = LinearEasing),
                repeatMode = RepeatMode.Restart,
            ),
            label = "MaigCircularAngle",
        )
        angle
    } else {
        -90f
    }

    Box(
        modifier = modifier
            .size(size)
            .semantics {
                progressBarRangeInfo = if (clamped != null) {
                    ProgressBarRangeInfo(clamped, 0f..1f)
                } else {
                    ProgressBarRangeInfo.Indeterminate
                }
            },
        contentAlignment = Alignment.Center,
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val strokePx = strokeWidth.toPx()
            val inset = strokePx / 2f
            val arcDiameter = (this.size.minDimension - strokePx).coerceAtLeast(0f)
            val arcTopLeft = Offset(
                x = (this.size.width - arcDiameter) / 2f,
                y = (this.size.height - arcDiameter) / 2f,
            )
            drawCircle(
                color = trackColor,
                radius = (arcDiameter / 2f).coerceAtLeast(0f),
                style = Stroke(width = strokePx),
            )
            drawArc(
                color = color,
                startAngle = indeterminateStart,
                sweepAngle = if (clamped != null) animatedSweep else 100f,
                useCenter = false,
                topLeft = arcTopLeft,
                size = Size(arcDiameter, arcDiameter),
                style = Stroke(width = strokePx, cap = StrokeCap.Round),
            )
        }

        if (showPercentage && clamped != null) {
            BasicText(
                text = "${(clamped * 100).roundToInt()}%",
                style = MaigTheme.typography.caption.copy(color = MaigTheme.colors.textPrimary),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigProgressLightPreview() {
    MaigTheme(darkTheme = false) {
        Column(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(MaigTheme.spacing.lg),
            verticalArrangement = Arrangement.spacedBy(MaigTheme.spacing.md),
        ) {
            MaigLinearProgressBar(progress = 0.72f, showPercentage = true)
            MaigLinearProgressBar(progress = null)
            Row(horizontalArrangement = Arrangement.spacedBy(MaigTheme.spacing.lg)) {
                MaigCircularProgressBar(progress = 0.65f, showPercentage = true)
                MaigCircularProgressBar(progress = null)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigProgressDarkPreview() {
    MaigTheme(darkTheme = true) {
        Column(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(MaigTheme.spacing.lg),
            verticalArrangement = Arrangement.spacedBy(MaigTheme.spacing.md),
        ) {
            MaigLinearProgressBar(progress = 0.45f, showPercentage = true)
            MaigCircularProgressBar(progress = 0.85f, showPercentage = true)
        }
    }
}
