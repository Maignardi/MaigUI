package io.github.maignardi.maigui.components.internal

import androidx.compose.foundation.IndicationNodeFactory
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.createRippleModifierNode
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorProducer
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.node.DelegatableNode
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.maignardi.maigui.core.theme.MaigTheme

internal val MinTouchTargetSize: Dp = 48.dp

private val DefaultMaigRippleAlpha = RippleAlpha(
    draggedAlpha = 0.16f,
    focusedAlpha = 0.12f,
    hoveredAlpha = 0.08f,
    pressedAlpha = 0.14f,
)

@Composable
internal fun rememberMaigRipple(
    bounded: Boolean = true,
    color: Color = Color.Unspecified,
    radius: Dp = Dp.Unspecified,
): IndicationNodeFactory {
    val resolvedColor = if (color.isSpecified) color else MaigTheme.colors.ripple
    return remember(bounded, resolvedColor, radius) {
        MaigRippleNodeFactory(
            bounded = bounded,
            radius = radius,
            color = resolvedColor,
        )
    }
}

private class MaigRippleNodeFactory(
    private val bounded: Boolean,
    private val radius: Dp,
    private val color: Color,
) : IndicationNodeFactory {
    override fun create(interactionSource: InteractionSource): DelegatableNode {
        val colorProducer = ColorProducer { color }
        return createRippleModifierNode(
            interactionSource = interactionSource,
            bounded = bounded,
            radius = radius,
            color = colorProducer,
            rippleAlpha = { DefaultMaigRippleAlpha },
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is MaigRippleNodeFactory) return false
        return bounded == other.bounded && radius == other.radius && color == other.color
    }

    override fun hashCode(): Int {
        var result = bounded.hashCode()
        result = 31 * result + radius.hashCode()
        result = 31 * result + color.hashCode()
        return result
    }
}

internal fun Modifier.maigTouchTarget(
    minWidth: Dp = MinTouchTargetSize,
    minHeight: Dp = MinTouchTargetSize,
): Modifier = defaultMinSize(minWidth = minWidth, minHeight = minHeight)
