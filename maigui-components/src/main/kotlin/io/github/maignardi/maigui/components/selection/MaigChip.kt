package io.github.maignardi.maigui.components.selection

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.maignardi.maigui.components.internal.MaigCheckIcon
import io.github.maignardi.maigui.components.internal.MaigCloseIcon
import io.github.maignardi.maigui.components.internal.maigTouchTarget
import io.github.maignardi.maigui.components.internal.rememberMaigRipple
import io.github.maignardi.maigui.core.theme.LocalMaigContentColor
import io.github.maignardi.maigui.core.theme.MaigTheme

/**
 * Behavioral & visual chip style.
 */
public enum class MaigChipVariant {
    /** Toggleable filter chip showing a checkmark when selected. */
    Filter,

    /** Single-select choice pill. */
    Choice,

    /** Removable input tag chip with trailing close icon. */
    Input,
}

/**
 * Color tokens for [MaigChip].
 */
@Immutable
public class MaigChipColors(
    public val containerColor: Color,
    public val selectedContainerColor: Color,
    public val contentColor: Color,
    public val selectedContentColor: Color,
    public val borderColor: Color,
    public val selectedBorderColor: Color,
    public val disabledContainerColor: Color,
    public val disabledContentColor: Color,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is MaigChipColors) return false
        return containerColor == other.containerColor &&
            selectedContainerColor == other.selectedContainerColor &&
            contentColor == other.contentColor &&
            selectedContentColor == other.selectedContentColor &&
            borderColor == other.borderColor &&
            selectedBorderColor == other.selectedBorderColor &&
            disabledContainerColor == other.disabledContainerColor &&
            disabledContentColor == other.disabledContentColor
    }

    override fun hashCode(): Int {
        var result = containerColor.hashCode()
        result = 31 * result + selectedContainerColor.hashCode()
        result = 31 * result + contentColor.hashCode()
        result = 31 * result + selectedContentColor.hashCode()
        result = 31 * result + borderColor.hashCode()
        result = 31 * result + selectedBorderColor.hashCode()
        result = 31 * result + disabledContainerColor.hashCode()
        result = 31 * result + disabledContentColor.hashCode()
        return result
    }
}

/**
 * Default configuration for [MaigChip].
 */
public object MaigChipDefaults {
    @Composable
    public fun shape(variant: MaigChipVariant): Shape = when (variant) {
        MaigChipVariant.Choice -> MaigTheme.shapes.full
        MaigChipVariant.Filter, MaigChipVariant.Input -> MaigTheme.shapes.md
    }

    @Composable
    public fun colors(variant: MaigChipVariant = MaigChipVariant.Filter): MaigChipColors {
        val c = MaigTheme.colors
        return when (variant) {
            MaigChipVariant.Choice -> MaigChipColors(
                containerColor = c.surface,
                selectedContainerColor = c.primaryDefault,
                contentColor = c.textPrimary,
                selectedContentColor = c.onPrimary,
                borderColor = c.border,
                selectedBorderColor = c.primaryDefault,
                disabledContainerColor = c.disabled,
                disabledContentColor = c.onDisabled,
            )

            MaigChipVariant.Filter, MaigChipVariant.Input -> MaigChipColors(
                containerColor = c.surface,
                selectedContainerColor = c.primarySubtle,
                contentColor = c.textPrimary,
                selectedContentColor = c.onPrimarySubtle,
                borderColor = c.border,
                selectedBorderColor = c.primaryDefault,
                disabledContainerColor = c.disabled,
                disabledContentColor = c.onDisabled,
            )
        }
    }
}

/**
 * Interactive chip supporting Filter, Choice, and removable Input variants.
 */
@Composable
public fun MaigChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: MaigChipVariant = MaigChipVariant.Filter,
    enabled: Boolean = true,
    leadingIcon: @Composable (() -> Unit)? = null,
    onRemove: (() -> Unit)? = null,
    shape: Shape = MaigChipDefaults.shape(variant),
    colors: MaigChipColors = MaigChipDefaults.colors(variant),
    interactionSource: MutableInteractionSource? = null,
) {
    val resolvedInteraction = interactionSource ?: remember { MutableInteractionSource() }
    val bgColor by animateColorAsState(
        targetValue = when {
            !enabled -> colors.disabledContainerColor
            selected -> colors.selectedContainerColor
            else -> colors.containerColor
        },
        animationSpec = tween(140),
        label = "MaigChipBg",
    )
    val fgColor by animateColorAsState(
        targetValue = when {
            !enabled -> colors.disabledContentColor
            selected -> colors.selectedContentColor
            else -> colors.contentColor
        },
        animationSpec = tween(140),
        label = "MaigChipFg",
    )
    val borderColor = when {
        !enabled -> MaigTheme.colors.borderSubtle
        selected -> colors.selectedBorderColor
        else -> colors.borderColor
    }

    Box(
        modifier = modifier.maigTouchTarget(minWidth = 48.dp, minHeight = 48.dp),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            modifier = Modifier
                .defaultMinSize(minHeight = 34.dp)
                .clip(shape)
                .background(bgColor)
                .border(1.dp, borderColor, shape)
                .clickable(
                    interactionSource = resolvedInteraction,
                    indication = rememberMaigRipple(bounded = true, color = fgColor.copy(alpha = 0.14f)),
                    enabled = enabled,
                    role = Role.Button,
                    onClick = onClick,
                )
                .semantics { this.selected = selected }
                .padding(horizontal = MaigTheme.spacing.md, vertical = MaigTheme.spacing.xs),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            CompositionLocalProvider(LocalMaigContentColor provides fgColor) {
                if (variant == MaigChipVariant.Filter && selected && leadingIcon == null) {
                    MaigCheckIcon(size = 14.dp, tint = fgColor)
                    Spacer(modifier = Modifier.width(MaigTheme.spacing.xs))
                } else if (leadingIcon != null) {
                    leadingIcon()
                    Spacer(modifier = Modifier.width(MaigTheme.spacing.xs))
                }

                BasicText(
                    text = label,
                    style = MaigTheme.typography.label.copy(color = fgColor),
                )

                if (variant == MaigChipVariant.Input || onRemove != null) {
                    Spacer(modifier = Modifier.width(MaigTheme.spacing.xs))
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(MaigTheme.shapes.full)
                            .clickable(
                                enabled = enabled && onRemove != null,
                                role = Role.Button,
                                onClick = { onRemove?.invoke() },
                            )
                            .semantics { contentDescription = "Remover $label" },
                        contentAlignment = Alignment.Center,
                    ) {
                        MaigCloseIcon(size = 12.dp, tint = fgColor)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigChipLightPreview() {
    MaigTheme(darkTheme = false) {
        Row(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(MaigTheme.spacing.md),
            horizontalArrangement = Arrangement.spacedBy(MaigTheme.spacing.sm),
        ) {
            MaigChip(label = "Kotlin", selected = true, onClick = {}, variant = MaigChipVariant.Filter)
            MaigChip(label = "Compose", selected = false, onClick = {}, variant = MaigChipVariant.Choice)
            MaigChip(label = "Android", selected = false, onClick = {}, variant = MaigChipVariant.Input, onRemove = {})
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigChipDarkPreview() {
    MaigTheme(darkTheme = true) {
        Row(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(MaigTheme.spacing.md),
            horizontalArrangement = Arrangement.spacedBy(MaigTheme.spacing.sm),
        ) {
            MaigChip(label = "Ativo", selected = true, onClick = {}, variant = MaigChipVariant.Choice)
            MaigChip(label = "Tag", selected = false, onClick = {}, variant = MaigChipVariant.Input, onRemove = {})
        }
    }
}
