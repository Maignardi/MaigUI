package io.github.maignardi.maigui.components.feedback

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.maignardi.maigui.core.theme.MaigTheme

/**
 * Default values for [MaigSpinner] and [MaigLoadingOverlay].
 */
public object MaigLoadingDefaults {
    /** Default spinner diameter. */
    public val SpinnerSize: Dp = 24.dp

    /** Default spinner stroke thickness. */
    public val StrokeWidth: Dp = 2.5.dp

    /** Returns default colors for [MaigLoadingOverlay]. */
    @Composable
    public fun overlayColors(
        scrimColor: Color = MaigTheme.colors.scrim,
        containerColor: Color = MaigTheme.colors.surfaceElevated,
        borderColor: Color = MaigTheme.colors.border,
        spinnerColor: Color = MaigTheme.colors.primaryDefault,
        messageColor: Color = MaigTheme.colors.textPrimary,
    ): MaigLoadingOverlayColors = MaigLoadingOverlayColors(
        scrimColor = scrimColor,
        containerColor = containerColor,
        borderColor = borderColor,
        spinnerColor = spinnerColor,
        messageColor = messageColor,
    )
}

/**
 * Color configuration for [MaigLoadingOverlay].
 */
@Immutable
public class MaigLoadingOverlayColors(
    public val scrimColor: Color,
    public val containerColor: Color,
    public val borderColor: Color,
    public val spinnerColor: Color,
    public val messageColor: Color,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is MaigLoadingOverlayColors) return false
        return scrimColor == other.scrimColor &&
            containerColor == other.containerColor &&
            borderColor == other.borderColor &&
            spinnerColor == other.spinnerColor &&
            messageColor == other.messageColor
    }

    override fun hashCode(): Int {
        var result = scrimColor.hashCode()
        result = 31 * result + containerColor.hashCode()
        result = 31 * result + borderColor.hashCode()
        result = 31 * result + spinnerColor.hashCode()
        result = 31 * result + messageColor.hashCode()
        return result
    }
}

/**
 * Animated circular loading indicator drawn on a foundation [Canvas].
 */
@Composable
public fun MaigSpinner(
    modifier: Modifier = Modifier,
    size: Dp = MaigLoadingDefaults.SpinnerSize,
    color: Color = MaigTheme.colors.primaryDefault,
    trackColor: Color = color.copy(alpha = 0.2f),
    strokeWidth: Dp = MaigLoadingDefaults.StrokeWidth,
    contentDescription: String = "Carregando",
) {
    val transition = rememberInfiniteTransition(label = "MaigSpinner")
    val startAngle by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 900, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "MaigSpinnerAngle",
    )

    Canvas(
        modifier = modifier
            .size(size)
            .semantics {
                this.contentDescription = contentDescription
                progressBarRangeInfo = ProgressBarRangeInfo.Indeterminate
            },
    ) {
        val strokePx = strokeWidth.toPx()
        drawCircle(
            color = trackColor,
            style = Stroke(width = strokePx),
        )
        drawArc(
            color = color,
            startAngle = startAngle,
            sweepAngle = 95f,
            useCenter = false,
            style = Stroke(width = strokePx, cap = StrokeCap.Round),
        )
    }
}

/**
 * Full-screen blocking loading overlay with an optional status [message].
 */
@Composable
public fun MaigLoadingOverlay(
    visible: Boolean,
    modifier: Modifier = Modifier,
    message: String? = null,
    colors: MaigLoadingOverlayColors = MaigLoadingDefaults.overlayColors(),
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(150)),
        exit = fadeOut(tween(150)),
        modifier = modifier.fillMaxSize(),
    ) {
        val blockingInteraction = remember { MutableInteractionSource() }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.scrimColor)
                .clickable(
                    interactionSource = blockingInteraction,
                    indication = null,
                    onClick = {},
                ),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                modifier = Modifier
                    .clip(MaigTheme.shapes.lg)
                    .background(colors.containerColor)
                    .border(1.dp, colors.borderColor, MaigTheme.shapes.lg)
                    .padding(horizontal = MaigTheme.spacing.xl, vertical = MaigTheme.spacing.lg),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(MaigTheme.spacing.md),
            ) {
                MaigSpinner(
                    size = 36.dp,
                    color = colors.spinnerColor,
                    strokeWidth = 3.dp,
                    contentDescription = message ?: "Carregando",
                )
                if (message != null) {
                    BasicText(
                        text = message,
                        style = MaigTheme.typography.label.copy(color = colors.messageColor),
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigSpinnerLightPreview() {
    MaigTheme(darkTheme = false) {
        Box(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(MaigTheme.spacing.lg),
        ) {
            MaigSpinner()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigSpinnerDarkPreview() {
    MaigTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(MaigTheme.spacing.lg),
        ) {
            MaigSpinner()
        }
    }
}
