package io.github.maignardi.maigui.components.feedback

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.maignardi.maigui.core.theme.MaigTheme

/**
 * Defaults for [MaigSkeleton] shimmer placeholders.
 */
public object MaigSkeletonDefaults {
    public const val ShimmerDurationMillis: Int = 1100
}

/**
 * Animated shimmer loading placeholder block.
 */
@Composable
public fun MaigSkeleton(
    modifier: Modifier = Modifier,
    shape: Shape = MaigTheme.shapes.md,
    animated: Boolean = true,
) {
    val c = MaigTheme.colors
    val baseColor = if (c.isDark) c.neutral.shade800 else c.neutral.shade200
    val highlightColor = if (c.isDark) c.neutral.shade700 else c.neutral.shade100

    val brush = if (animated) {
        val transition = rememberInfiniteTransition(label = "MaigSkeletonShimmer")
        val shift by transition.animateFloat(
            initialValue = 0f,
            targetValue = 900f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = MaigSkeletonDefaults.ShimmerDurationMillis, easing = LinearEasing),
                repeatMode = RepeatMode.Restart,
            ),
            label = "MaigSkeletonShift",
        )
        Brush.linearGradient(
            colors = listOf(baseColor, highlightColor, baseColor),
            start = Offset(shift - 300f, 0f),
            end = Offset(shift, 200f),
        )
    } else {
        Brush.linearGradient(listOf(baseColor, baseColor))
    }

    Box(
        modifier = modifier
            .clip(shape)
            .background(brush),
    )
}

/**
 * Circular shimmer placeholder (ideal for avatars).
 */
@Composable
public fun MaigSkeletonCircle(
    size: Dp = 44.dp,
    modifier: Modifier = Modifier,
    animated: Boolean = true,
) {
    MaigSkeleton(
        modifier = modifier.size(size),
        shape = CircleShape,
        animated = animated,
    )
}

/**
 * Multi-line text skeleton block.
 */
@Composable
public fun MaigSkeletonText(
    lines: Int = 3,
    modifier: Modifier = Modifier,
    animated: Boolean = true,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(MaigTheme.spacing.sm),
    ) {
        repeat(lines) { index ->
            val widthFraction = if (index == lines - 1 && lines > 1) 0.65f else 1f
            MaigSkeleton(
                modifier = Modifier
                    .fillMaxWidth(widthFraction)
                    .height(14.dp),
                shape = MaigTheme.shapes.xs,
                animated = animated,
            )
        }
    }
}

/**
 * Card skeleton placeholder combining avatar circle and text lines.
 */
@Composable
public fun MaigSkeletonCard(
    modifier: Modifier = Modifier,
    animated: Boolean = true,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(MaigTheme.shapes.lg)
            .background(MaigTheme.colors.surface)
            .border(1.dp, MaigTheme.colors.border, MaigTheme.shapes.lg)
            .padding(MaigTheme.spacing.lg),
        verticalArrangement = Arrangement.spacedBy(MaigTheme.spacing.md),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MaigTheme.spacing.md),
        ) {
            MaigSkeletonCircle(size = 44.dp, animated = animated)
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(MaigTheme.spacing.xs),
            ) {
                MaigSkeleton(modifier = Modifier.fillMaxWidth(0.55f).height(14.dp), animated = animated)
                MaigSkeleton(modifier = Modifier.fillMaxWidth(0.35f).height(10.dp), animated = animated)
            }
        }
        MaigSkeletonText(lines = 2, animated = animated)
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigSkeletonLightPreview() {
    MaigTheme(darkTheme = false) {
        Box(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(MaigTheme.spacing.lg),
        ) {
            MaigSkeletonCard(animated = false)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigSkeletonDarkPreview() {
    MaigTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(MaigTheme.spacing.lg),
        ) {
            MaigSkeletonCard(animated = false)
        }
    }
}
