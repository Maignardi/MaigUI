package io.github.maignardi.maigui.components.inputs

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.maignardi.maigui.components.internal.MaigPlusIcon
import io.github.maignardi.maigui.components.internal.maigTouchTarget
import io.github.maignardi.maigui.components.internal.rememberMaigRipple
import io.github.maignardi.maigui.core.theme.MaigTheme

/**
 * Sleek pill-shaped numeric quantity stepper (`-  1  +`) with tabular numerals and min/max clamping.
 */
@Composable
public fun MaigNumberStepper(
    value: Int,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    minValue: Int = 0,
    maxValue: Int = Int.MAX_VALUE,
    step: Int = 1,
    enabled: Boolean = true,
    label: String? = null,
) {
    val c = MaigTheme.colors
    val canDecrement = enabled && value - step >= minValue
    val canIncrement = enabled && value + step <= maxValue

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MaigTheme.spacing.md),
    ) {
        if (label != null) {
            BasicText(
                text = label,
                modifier = Modifier.weight(1f),
                style = MaigTheme.typography.body.copy(
                    color = if (enabled) c.textPrimary else c.textDisabled,
                ),
            )
        }

        Row(
            modifier = Modifier
                .clip(CircleShape)
                .background(c.surface)
                .border(1.dp, c.border, CircleShape)
                .padding(horizontal = 4.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .maigTouchTarget(minWidth = 40.dp, minHeight = 44.dp)
                    .defaultMinSize(minWidth = 36.dp, minHeight = 36.dp)
                    .clip(CircleShape)
                    .background(if (canDecrement) c.surfaceVariant else c.disabled.copy(alpha = 0.5f))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberMaigRipple(bounded = true),
                        enabled = canDecrement,
                        role = Role.Button,
                        onClick = { onValueChange((value - step).coerceAtLeast(minValue)) },
                    )
                    .semantics { contentDescription = "Diminuir" },
                contentAlignment = Alignment.Center,
            ) {
                Canvas(modifier = Modifier.size(14.dp)) {
                    drawLine(
                        color = if (canDecrement) c.textPrimary else c.textDisabled,
                        start = Offset(size.width * 0.2f, size.height * 0.5f),
                        end = Offset(size.width * 0.8f, size.height * 0.5f),
                        strokeWidth = 2.dp.toPx(),
                        cap = StrokeCap.Round,
                    )
                }
            }

            BasicText(
                text = value.toString(),
                modifier = Modifier
                    .widthIn(min = 36.dp)
                    .padding(horizontal = MaigTheme.spacing.sm),
                style = MaigTheme.typography.title.copy(
                    color = if (enabled) c.textPrimary else c.textDisabled,
                    textAlign = TextAlign.Center,
                    fontFeatureSettings = "tnum",
                ),
            )

            Box(
                modifier = Modifier
                    .maigTouchTarget(minWidth = 40.dp, minHeight = 44.dp)
                    .defaultMinSize(minWidth = 36.dp, minHeight = 36.dp)
                    .clip(CircleShape)
                    .background(if (canIncrement) c.primaryDefault else c.disabled)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberMaigRipple(bounded = true),
                        enabled = canIncrement,
                        role = Role.Button,
                        onClick = { onValueChange((value + step).coerceAtMost(maxValue)) },
                    )
                    .semantics { contentDescription = "Aumentar" },
                contentAlignment = Alignment.Center,
            ) {
                MaigPlusIcon(
                    size = 16.dp,
                    tint = if (canIncrement) c.onPrimary else c.onDisabled,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigNumberStepperLightPreview() {
    MaigTheme(darkTheme = false) {
        Box(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(MaigTheme.spacing.lg),
        ) {
            MaigNumberStepper(value = 3, onValueChange = {}, label = "Quantidade")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigNumberStepperDarkPreview() {
    MaigTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(MaigTheme.spacing.lg),
        ) {
            MaigNumberStepper(value = 1, onValueChange = {}, label = "Passageiros")
        }
    }
}
