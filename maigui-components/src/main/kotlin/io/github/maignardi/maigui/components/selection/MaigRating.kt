package io.github.maignardi.maigui.components.selection

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.maignardi.maigui.components.internal.maigTouchTarget
import io.github.maignardi.maigui.core.theme.MaigTheme
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * Defaults for [MaigRating].
 */
public object MaigRatingDefaults {
    public const val DefaultMaxStars: Int = 5
    public val StarSize: Dp = 28.dp
}

/**
 * Interactive star rating selector drawn on Canvas with 48dp accessible touch targets.
 */
@Composable
public fun MaigRating(
    rating: Int,
    onRatingChange: ((Int) -> Unit)?,
    modifier: Modifier = Modifier,
    maxStars: Int = MaigRatingDefaults.DefaultMaxStars,
    enabled: Boolean = true,
    starSize: Dp = MaigRatingDefaults.StarSize,
    activeColor: Color = MaigTheme.colors.warning.shade400,
    inactiveColor: Color = MaigTheme.colors.borderStrong,
) {
    Row(
        modifier = modifier.semantics {
            contentDescription = "Avaliação $rating de $maxStars estrelas"
        },
        horizontalArrangement = Arrangement.spacedBy(MaigTheme.spacing.xs),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        for (star in 1..maxStars) {
            val isFilled = star <= rating
            val starModifier = if (onRatingChange != null) {
                Modifier
                    .maigTouchTarget(minWidth = 40.dp, minHeight = 48.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        enabled = enabled,
                        role = Role.Button,
                        onClick = { onRatingChange(star) },
                    )
            } else {
                Modifier
            }

            Box(
                modifier = starModifier.semantics { contentDescription = "$star estrelas" },
                contentAlignment = Alignment.Center,
            ) {
                Canvas(modifier = Modifier.size(starSize)) {
                    val path = buildStarPath(size.width / 2f, size.height / 2f, size.minDimension * 0.46f)
                    if (isFilled) {
                        drawPath(path = path, color = activeColor, style = Fill)
                    } else {
                        drawPath(path = path, color = inactiveColor, style = Stroke(width = 1.8.dp.toPx()))
                    }
                }
            }
        }
    }
}

private fun buildStarPath(cx: Float, cy: Float, outerRadius: Float): Path {
    val innerRadius = outerRadius * 0.44f
    val path = Path()
    for (i in 0 until 10) {
        val radius = if (i % 2 == 0) outerRadius else innerRadius
        val angle = (-90.0 + i * 36.0) * PI / 180.0
        val x = cx + (radius * cos(angle)).toFloat()
        val y = cy + (radius * sin(angle)).toFloat()
        if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
    }
    path.close()
    return path
}

@Preview(showBackground = true)
@Composable
private fun MaigRatingLightPreview() {
    MaigTheme(darkTheme = false) {
        Box(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(MaigTheme.spacing.md),
        ) {
            MaigRating(rating = 4, onRatingChange = {})
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigRatingDarkPreview() {
    MaigTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(MaigTheme.spacing.md),
        ) {
            MaigRating(rating = 3, onRatingChange = {})
        }
    }
}
