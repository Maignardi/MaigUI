package io.github.maignardi.maigui.components.selection

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.triStateToggleable
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.maignardi.maigui.components.internal.maigTouchTarget
import io.github.maignardi.maigui.components.internal.rememberMaigRipple
import io.github.maignardi.maigui.core.theme.MaigTheme

/**
 * Color tokens for [MaigCheckbox] and [MaigTriStateCheckbox].
 */
@Immutable
public class MaigCheckboxColors(
    public val checkedContainerColor: Color,
    public val uncheckedContainerColor: Color,
    public val checkmarkColor: Color,
    public val checkedBorderColor: Color,
    public val uncheckedBorderColor: Color,
    public val disabledContainerColor: Color,
    public val disabledCheckmarkColor: Color,
    public val disabledBorderColor: Color,
    public val labelColor: Color,
    public val disabledLabelColor: Color,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is MaigCheckboxColors) return false
        return checkedContainerColor == other.checkedContainerColor &&
            uncheckedContainerColor == other.uncheckedContainerColor &&
            checkmarkColor == other.checkmarkColor &&
            checkedBorderColor == other.checkedBorderColor &&
            uncheckedBorderColor == other.uncheckedBorderColor &&
            disabledContainerColor == other.disabledContainerColor &&
            disabledCheckmarkColor == other.disabledCheckmarkColor &&
            disabledBorderColor == other.disabledBorderColor &&
            labelColor == other.labelColor &&
            disabledLabelColor == other.disabledLabelColor
    }

    override fun hashCode(): Int {
        var result = checkedContainerColor.hashCode()
        result = 31 * result + uncheckedContainerColor.hashCode()
        result = 31 * result + checkmarkColor.hashCode()
        result = 31 * result + checkedBorderColor.hashCode()
        result = 31 * result + uncheckedBorderColor.hashCode()
        result = 31 * result + disabledContainerColor.hashCode()
        result = 31 * result + disabledCheckmarkColor.hashCode()
        result = 31 * result + disabledBorderColor.hashCode()
        result = 31 * result + labelColor.hashCode()
        result = 31 * result + disabledLabelColor.hashCode()
        return result
    }
}

/**
 * Default values for [MaigCheckbox] and [MaigTriStateCheckbox].
 */
public object MaigCheckboxDefaults {
    /** Visual checkbox square size. */
    public val BoxSize: Dp = 20.dp

    /** Default shape of the checkbox box. */
    @Composable
    public fun shape(): Shape = MaigTheme.shapes.xs

    /** Default colors for checkboxes. */
    @Composable
    public fun colors(): MaigCheckboxColors {
        val c = MaigTheme.colors
        return MaigCheckboxColors(
            checkedContainerColor = c.primaryDefault,
            uncheckedContainerColor = c.surface,
            checkmarkColor = c.onPrimary,
            checkedBorderColor = c.primaryDefault,
            uncheckedBorderColor = c.borderStrong,
            disabledContainerColor = c.disabled,
            disabledCheckmarkColor = c.onDisabled,
            disabledBorderColor = c.borderSubtle,
            labelColor = c.textPrimary,
            disabledLabelColor = c.textDisabled,
        )
    }
}

/**
 * Standard boolean checkbox with optional text [label] and 48dp accessible touch target.
 */
@Composable
public fun MaigCheckbox(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: String? = null,
    shape: Shape = MaigCheckboxDefaults.shape(),
    colors: MaigCheckboxColors = MaigCheckboxDefaults.colors(),
    interactionSource: MutableInteractionSource? = null,
) {
    MaigTriStateCheckbox(
        state = if (checked) ToggleableState.On else ToggleableState.Off,
        onClick = onCheckedChange?.let { callback -> { callback(!checked) } },
        modifier = modifier,
        enabled = enabled,
        label = label,
        shape = shape,
        colors = colors,
        interactionSource = interactionSource,
    )
}

/**
 * Tri-state checkbox supporting Checked, Unchecked, and Indeterminate states.
 */
@Composable
public fun MaigTriStateCheckbox(
    state: ToggleableState,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: String? = null,
    shape: Shape = MaigCheckboxDefaults.shape(),
    colors: MaigCheckboxColors = MaigCheckboxDefaults.colors(),
    interactionSource: MutableInteractionSource? = null,
) {
    val resolvedInteraction = interactionSource ?: remember { MutableInteractionSource() }
    val isFilled = state != ToggleableState.Off

    val containerColor by animateColorAsState(
        targetValue = when {
            !enabled && isFilled -> colors.disabledContainerColor
            !enabled -> Color.Transparent
            isFilled -> colors.checkedContainerColor
            else -> colors.uncheckedContainerColor
        },
        animationSpec = tween(140),
        label = "MaigCheckboxContainer",
    )
    val borderColor by animateColorAsState(
        targetValue = when {
            !enabled -> colors.disabledBorderColor
            isFilled -> colors.checkedBorderColor
            else -> colors.uncheckedBorderColor
        },
        animationSpec = tween(140),
        label = "MaigCheckboxBorder",
    )
    val markColor = if (enabled) colors.checkmarkColor else colors.disabledCheckmarkColor

    val toggleModifier = if (onClick != null) {
        Modifier.triStateToggleable(
            state = state,
            onClick = onClick,
            enabled = enabled,
            role = Role.Checkbox,
            interactionSource = resolvedInteraction,
            indication = if (label != null) rememberMaigRipple(bounded = true) else null,
        )
    } else {
        Modifier
    }

    Row(
        modifier = modifier
            .maigTouchTarget(minWidth = 48.dp, minHeight = 48.dp)
            .then(toggleModifier)
            .padding(horizontal = if (label != null) MaigTheme.spacing.xs else 0.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(MaigCheckboxDefaults.BoxSize)
                .clip(shape)
                .background(containerColor)
                .border(1.5.dp, borderColor, shape),
            contentAlignment = Alignment.Center,
        ) {
            if (state != ToggleableState.Off) {
                Canvas(modifier = Modifier.size(14.dp)) {
                    val strokePx = 2.dp.toPx()
                    when (state) {
                        ToggleableState.On -> {
                            val path = Path().apply {
                                moveTo(size.width * 0.18f, size.height * 0.52f)
                                lineTo(size.width * 0.42f, size.height * 0.76f)
                                lineTo(size.width * 0.84f, size.height * 0.24f)
                            }
                            drawPath(
                                path = path,
                                color = markColor,
                                style = Stroke(strokePx, cap = StrokeCap.Round, join = StrokeJoin.Round),
                            )
                        }
                        ToggleableState.Indeterminate -> {
                            drawLine(
                                color = markColor,
                                start = Offset(size.width * 0.20f, size.height * 0.50f),
                                end = Offset(size.width * 0.80f, size.height * 0.50f),
                                strokeWidth = strokePx,
                                cap = StrokeCap.Round,
                            )
                        }
                        ToggleableState.Off -> Unit
                    }
                }
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

@Preview(showBackground = true)
@Composable
private fun MaigCheckboxLightPreview() {
    MaigTheme(darkTheme = false) {
        Column(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(MaigTheme.spacing.md),
        ) {
            MaigCheckbox(checked = true, onCheckedChange = {}, label = "Aceito os termos")
            MaigCheckbox(checked = false, onCheckedChange = {}, label = "Receber novidades")
            MaigTriStateCheckbox(state = ToggleableState.Indeterminate, onClick = {}, label = "Selecionar todos")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigCheckboxDarkPreview() {
    MaigTheme(darkTheme = true) {
        Column(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(MaigTheme.spacing.md),
        ) {
            MaigCheckbox(checked = true, onCheckedChange = {}, label = "Ativo no Dark Mode")
            MaigCheckbox(checked = true, onCheckedChange = {}, enabled = false, label = "Desabilitado")
        }
    }
}
