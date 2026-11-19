package io.github.maignardi.maigui.components.selection

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.maignardi.maigui.components.internal.maigTouchTarget
import io.github.maignardi.maigui.components.internal.rememberMaigRipple
import io.github.maignardi.maigui.core.theme.MaigTheme

/**
 * Layout direction for [MaigRadioGroup].
 */
public enum class MaigRadioOrientation {
    Vertical,
    Horizontal,
}

/**
 * Color tokens for [MaigRadioButton] and [MaigRadioGroup].
 */
@Immutable
public class MaigRadioColors(
    public val selectedColor: Color,
    public val unselectedColor: Color,
    public val disabledColor: Color,
    public val labelColor: Color,
    public val disabledLabelColor: Color,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is MaigRadioColors) return false
        return selectedColor == other.selectedColor &&
            unselectedColor == other.unselectedColor &&
            disabledColor == other.disabledColor &&
            labelColor == other.labelColor &&
            disabledLabelColor == other.disabledLabelColor
    }

    override fun hashCode(): Int {
        var result = selectedColor.hashCode()
        result = 31 * result + unselectedColor.hashCode()
        result = 31 * result + disabledColor.hashCode()
        result = 31 * result + labelColor.hashCode()
        result = 31 * result + disabledLabelColor.hashCode()
        return result
    }
}

/**
 * Default values for [MaigRadioButton] and [MaigRadioGroup].
 */
public object MaigRadioDefaults {
    /** Outer diameter of the radio circle. */
    public val OuterSize: Dp = 20.dp

    /** Inner dot diameter when selected. */
    public val InnerDotSize: Dp = 10.dp

    /** Default radio colors. */
    @Composable
    public fun colors(): MaigRadioColors {
        val c = MaigTheme.colors
        return MaigRadioColors(
            selectedColor = c.primaryDefault,
            unselectedColor = c.borderStrong,
            disabledColor = c.textDisabled,
            labelColor = c.textPrimary,
            disabledLabelColor = c.textDisabled,
        )
    }
}

/**
 * Single radio button with optional text [label] and 48dp accessible touch target.
 */
@Composable
public fun MaigRadioButton(
    selected: Boolean,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: String? = null,
    colors: MaigRadioColors = MaigRadioDefaults.colors(),
    interactionSource: MutableInteractionSource? = null,
) {
    val resolvedInteraction = interactionSource ?: remember { MutableInteractionSource() }
    val ringColor by animateColorAsState(
        targetValue = when {
            !enabled -> colors.disabledColor
            selected -> colors.selectedColor
            else -> colors.unselectedColor
        },
        animationSpec = tween(140),
        label = "MaigRadioRing",
    )

    val selectModifier = if (onClick != null) {
        Modifier.selectable(
            selected = selected,
            onClick = onClick,
            enabled = enabled,
            role = Role.RadioButton,
            interactionSource = resolvedInteraction,
            indication = if (label != null) rememberMaigRipple(bounded = true) else null,
        )
    } else {
        Modifier
    }

    Row(
        modifier = modifier
            .maigTouchTarget(minWidth = 48.dp, minHeight = 48.dp)
            .then(selectModifier)
            .padding(horizontal = if (label != null) MaigTheme.spacing.xs else 0.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(MaigRadioDefaults.OuterSize)
                .clip(CircleShape)
                .border(2.dp, ringColor, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            if (selected) {
                Box(
                    modifier = Modifier
                        .size(MaigRadioDefaults.InnerDotSize)
                        .clip(CircleShape)
                        .background(ringColor),
                )
            }
        }

        if (label != null) {
            Spacer(modifier = Modifier.width(MaigTheme.spacing.sm))
            BasicText(
                text = label,
                style = MaigTheme.typography.body.copy(
                    color = if (enabled) colors.labelColor else colors.disabledLabelColor,
                ),
            )
        }
    }
}

/**
 * Accessible group of mutually exclusive radio options.
 */
@Composable
public fun <T> MaigRadioGroup(
    options: List<T>,
    selectedOption: T?,
    onOptionSelected: (T) -> Unit,
    optionLabel: (T) -> String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    orientation: MaigRadioOrientation = MaigRadioOrientation.Vertical,
    colors: MaigRadioColors = MaigRadioDefaults.colors(),
) {
    when (orientation) {
        MaigRadioOrientation.Vertical -> {
            Column(modifier = modifier.selectableGroup()) {
                options.forEach { option ->
                    MaigRadioButton(
                        selected = option == selectedOption,
                        onClick = { onOptionSelected(option) },
                        enabled = enabled,
                        label = optionLabel(option),
                        colors = colors,
                    )
                }
            }
        }

        MaigRadioOrientation.Horizontal -> {
            Row(
                modifier = modifier.selectableGroup(),
                horizontalArrangement = Arrangement.spacedBy(MaigTheme.spacing.md),
            ) {
                options.forEach { option ->
                    MaigRadioButton(
                        selected = option == selectedOption,
                        onClick = { onOptionSelected(option) },
                        enabled = enabled,
                        label = optionLabel(option),
                        colors = colors,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigRadioGroupLightPreview() {
    MaigTheme(darkTheme = false) {
        Box(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(MaigTheme.spacing.md),
        ) {
            MaigRadioGroup(
                options = listOf("Pix", "Cartão de Crédito", "Boleto"),
                selectedOption = "Pix",
                onOptionSelected = {},
                optionLabel = { it },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigRadioGroupDarkPreview() {
    MaigTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(MaigTheme.spacing.md),
        ) {
            MaigRadioGroup(
                options = listOf("Mensal", "Anual"),
                selectedOption = "Anual",
                onOptionSelected = {},
                optionLabel = { it },
                orientation = MaigRadioOrientation.Horizontal,
            )
        }
    }
}
