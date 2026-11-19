package io.github.maignardi.maigui.components.buttons

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.maignardi.maigui.components.feedback.MaigSpinner
import io.github.maignardi.maigui.components.internal.maigTouchTarget
import io.github.maignardi.maigui.components.internal.rememberMaigRipple
import io.github.maignardi.maigui.core.theme.LocalMaigContentColor
import io.github.maignardi.maigui.core.theme.LocalMaigTextStyle
import io.github.maignardi.maigui.core.theme.MaigTheme

/**
 * Visual hierarchy style for [MaigButton].
 */
public enum class MaigButtonVariant {
    /** High-emphasis filled button. */
    Solid,

    /** Medium-emphasis bordered button with transparent background. */
    Outline,

    /** Low-emphasis text/icon button with transparent background and no border. */
    Ghost,

    /** Tinted subtle-fill button with matching foreground ink. */
    Soft,

    /** Inline hyperlink-style button with underline decoration. */
    Link,
}

/**
 * Semantic color intent for [MaigButton], [MaigIconButton], and [MaigFab].
 */
public enum class MaigButtonColor {
    Primary,
    Secondary,
    Success,
    Warning,
    Danger,
    Neutral,
}

/**
 * Size presets for [MaigButton].
 */
public enum class MaigButtonSize {
    Small,
    Medium,
    Large,
}

/**
 * Color tokens resolved for a [MaigButton] across enabled and disabled states.
 */
@Immutable
public class MaigButtonColors(
    public val containerColor: Color,
    public val contentColor: Color,
    public val borderColor: Color,
    public val disabledContainerColor: Color,
    public val disabledContentColor: Color,
    public val disabledBorderColor: Color,
    public val rippleColor: Color,
) {
    internal fun containerColor(enabled: Boolean): Color =
        if (enabled) containerColor else disabledContainerColor

    internal fun contentColor(enabled: Boolean): Color =
        if (enabled) contentColor else disabledContentColor

    internal fun borderColor(enabled: Boolean): Color =
        if (enabled) borderColor else disabledBorderColor

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is MaigButtonColors) return false
        return containerColor == other.containerColor &&
            contentColor == other.contentColor &&
            borderColor == other.borderColor &&
            disabledContainerColor == other.disabledContainerColor &&
            disabledContentColor == other.disabledContentColor &&
            disabledBorderColor == other.disabledBorderColor &&
            rippleColor == other.rippleColor
    }

    override fun hashCode(): Int {
        var result = containerColor.hashCode()
        result = 31 * result + contentColor.hashCode()
        result = 31 * result + borderColor.hashCode()
        result = 31 * result + disabledContainerColor.hashCode()
        result = 31 * result + disabledContentColor.hashCode()
        result = 31 * result + disabledBorderColor.hashCode()
        result = 31 * result + rippleColor.hashCode()
        return result
    }
}

/**
 * Default values, dimensions, and color resolvers for [MaigButton].
 */
public object MaigButtonDefaults {
    /** Resolves [MaigButtonColors] for the given [variant] and semantic [color]. */
    @Composable
    public fun colors(
        variant: MaigButtonVariant = MaigButtonVariant.Solid,
        color: MaigButtonColor = MaigButtonColor.Primary,
    ): MaigButtonColors {
        val c = MaigTheme.colors
        val (baseFill, onBase, subtleFill, onSubtle) = when (color) {
            MaigButtonColor.Primary -> QuadColors(c.primaryDefault, c.onPrimary, c.primarySubtle, c.onPrimarySubtle)
            MaigButtonColor.Secondary -> QuadColors(c.secondaryDefault, c.onSecondary, c.secondarySubtle, c.onSecondarySubtle)
            MaigButtonColor.Success -> QuadColors(c.successDefault, c.onSuccess, c.successSubtle, c.onSuccessSubtle)
            MaigButtonColor.Warning -> QuadColors(c.warningDefault, c.onWarning, c.warningSubtle, c.onWarningSubtle)
            MaigButtonColor.Danger -> QuadColors(c.dangerDefault, c.onDanger, c.dangerSubtle, c.onDangerSubtle)
            MaigButtonColor.Neutral -> QuadColors(
                solid = if (c.isDark) c.neutral.shade200 else c.neutral.shade800,
                onSolid = if (c.isDark) c.neutral.shade900 else Color.White,
                subtle = c.surfaceVariant,
                onSubtle = c.textPrimary,
            )
        }

        val outlineInk = if (color == MaigButtonColor.Neutral) c.textPrimary else baseFill
        val outlineBorder = if (color == MaigButtonColor.Neutral) c.borderStrong else baseFill.copy(alpha = 0.65f)

        return when (variant) {
            MaigButtonVariant.Solid -> MaigButtonColors(
                containerColor = baseFill,
                contentColor = onBase,
                borderColor = Color.Transparent,
                disabledContainerColor = c.disabled,
                disabledContentColor = c.onDisabled,
                disabledBorderColor = Color.Transparent,
                rippleColor = onBase.copy(alpha = 0.2f),
            )

            MaigButtonVariant.Outline -> MaigButtonColors(
                containerColor = Color.Transparent,
                contentColor = outlineInk,
                borderColor = outlineBorder,
                disabledContainerColor = Color.Transparent,
                disabledContentColor = c.textDisabled,
                disabledBorderColor = c.borderSubtle,
                rippleColor = outlineInk.copy(alpha = 0.14f),
            )

            MaigButtonVariant.Ghost, MaigButtonVariant.Link -> MaigButtonColors(
                containerColor = Color.Transparent,
                contentColor = outlineInk,
                borderColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                disabledContentColor = c.textDisabled,
                disabledBorderColor = Color.Transparent,
                rippleColor = outlineInk.copy(alpha = 0.14f),
            )

            MaigButtonVariant.Soft -> MaigButtonColors(
                containerColor = subtleFill,
                contentColor = onSubtle,
                borderColor = Color.Transparent,
                disabledContainerColor = c.disabled,
                disabledContentColor = c.onDisabled,
                disabledBorderColor = Color.Transparent,
                rippleColor = onSubtle.copy(alpha = 0.14f),
            )
        }
    }

    /** Returns the visual minimum height for [size]. */
    public fun minHeight(size: MaigButtonSize): Dp = when (size) {
        MaigButtonSize.Small -> 36.dp
        MaigButtonSize.Medium -> 44.dp
        MaigButtonSize.Large -> 52.dp
    }

    /** Returns default internal padding for [size] and [variant]. */
    @Composable
    public fun contentPadding(
        size: MaigButtonSize,
        variant: MaigButtonVariant = MaigButtonVariant.Solid,
    ): PaddingValues {
        val spacing = MaigTheme.spacing
        if (variant == MaigButtonVariant.Link) {
            return PaddingValues(horizontal = spacing.xs, vertical = spacing.xs)
        }
        return when (size) {
            MaigButtonSize.Small -> PaddingValues(horizontal = spacing.md, vertical = spacing.xs)
            MaigButtonSize.Medium -> PaddingValues(horizontal = spacing.lg, vertical = spacing.sm)
            MaigButtonSize.Large -> PaddingValues(horizontal = spacing.xl, vertical = spacing.md)
        }
    }

    /** Returns default button corner [Shape] for [size]. */
    @Composable
    public fun shape(size: MaigButtonSize): Shape = when (size) {
        MaigButtonSize.Small -> MaigTheme.shapes.sm
        MaigButtonSize.Medium -> MaigTheme.shapes.md
        MaigButtonSize.Large -> MaigTheme.shapes.lg
    }

    /** Returns label [TextStyle] for [size] and [variant]. */
    @Composable
    public fun textStyle(
        size: MaigButtonSize,
        variant: MaigButtonVariant,
    ): TextStyle {
        val base = when (size) {
            MaigButtonSize.Small -> MaigTheme.typography.label
            MaigButtonSize.Medium -> MaigTheme.typography.button
            MaigButtonSize.Large -> MaigTheme.typography.subtitle
        }
        return if (variant == MaigButtonVariant.Link) {
            base.copy(textDecoration = TextDecoration.Underline)
        } else {
            base
        }
    }

    /** Returns spinner diameter when `loading = true`. */
    public fun spinnerSize(size: MaigButtonSize): Dp = when (size) {
        MaigButtonSize.Small -> 14.dp
        MaigButtonSize.Medium -> 18.dp
        MaigButtonSize.Large -> 20.dp
    }
}

private data class QuadColors(
    val solid: Color,
    val onSolid: Color,
    val subtle: Color,
    val onSubtle: Color,
)

/**
 * Bootstrap-style interactive button built on compose foundation with support for variants,
 * semantic colors, sizes, loading spinner, leading/trailing icons, and full-width layout.
 */
@Composable
public fun MaigButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    variant: MaigButtonVariant = MaigButtonVariant.Solid,
    color: MaigButtonColor = MaigButtonColor.Primary,
    size: MaigButtonSize = MaigButtonSize.Medium,
    fullWidth: Boolean = false,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    shape: Shape = MaigButtonDefaults.shape(size),
    colors: MaigButtonColors = MaigButtonDefaults.colors(variant = variant, color = color),
    contentPadding: PaddingValues = MaigButtonDefaults.contentPadding(size = size, variant = variant),
    interactionSource: MutableInteractionSource? = null,
    content: @Composable RowScope.() -> Unit,
) {
    val resolvedInteraction = interactionSource ?: remember { MutableInteractionSource() }
    val isInteractive = enabled && !loading

    val targetContainer by animateColorAsState(
        targetValue = colors.containerColor(enabled),
        animationSpec = tween(150),
        label = "MaigButtonContainer",
    )
    val targetContent by animateColorAsState(
        targetValue = colors.contentColor(enabled),
        animationSpec = tween(150),
        label = "MaigButtonContent",
    )
    val targetBorder by animateColorAsState(
        targetValue = colors.borderColor(enabled),
        animationSpec = tween(150),
        label = "MaigButtonBorder",
    )

    val minHeight = MaigButtonDefaults.minHeight(size)
    val textStyle = MaigButtonDefaults.textStyle(size = size, variant = variant).copy(
        color = targetContent,
    )

    val outerModifier = modifier
        .then(if (fullWidth) Modifier.fillMaxWidth() else Modifier)
        .maigTouchTarget(minWidth = 48.dp, minHeight = 48.dp)

    Box(
        modifier = outerModifier,
        contentAlignment = Alignment.Center,
    ) {
        val visualModifier = Modifier
            .then(if (fullWidth) Modifier.fillMaxWidth() else Modifier)
            .defaultMinSize(minHeight = minHeight)
            .clip(shape)
            .background(targetContainer)
            .then(
                if (targetBorder != Color.Transparent) {
                    Modifier.border(BorderStroke(1.dp, targetBorder), shape)
                } else {
                    Modifier
                },
            )
            .clickable(
                interactionSource = resolvedInteraction,
                indication = rememberMaigRipple(bounded = true, color = colors.rippleColor),
                enabled = isInteractive,
                role = Role.Button,
                onClick = onClick,
            )
            .padding(contentPadding)

        Row(
            modifier = visualModifier,
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CompositionLocalProvider(
                LocalMaigContentColor provides targetContent,
                LocalMaigTextStyle provides textStyle,
            ) {
                if (loading) {
                    MaigSpinner(
                        size = MaigButtonDefaults.spinnerSize(size),
                        color = targetContent,
                        strokeWidth = 2.dp,
                    )
                    Spacer(modifier = Modifier.width(MaigTheme.spacing.sm))
                } else if (leadingIcon != null) {
                    leadingIcon()
                    Spacer(modifier = Modifier.width(MaigTheme.spacing.sm))
                }

                content()

                if (!loading && trailingIcon != null) {
                    Spacer(modifier = Modifier.width(MaigTheme.spacing.sm))
                    trailingIcon()
                }
            }
        }
    }
}

/**
 * Convenience overload of [MaigButton] displaying a single text label.
 */
@Composable
public fun MaigButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    variant: MaigButtonVariant = MaigButtonVariant.Solid,
    color: MaigButtonColor = MaigButtonColor.Primary,
    size: MaigButtonSize = MaigButtonSize.Medium,
    fullWidth: Boolean = false,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    shape: Shape = MaigButtonDefaults.shape(size),
    colors: MaigButtonColors = MaigButtonDefaults.colors(variant = variant, color = color),
    contentPadding: PaddingValues = MaigButtonDefaults.contentPadding(size = size, variant = variant),
    interactionSource: MutableInteractionSource? = null,
) {
    MaigButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        loading = loading,
        variant = variant,
        color = color,
        size = size,
        fullWidth = fullWidth,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        shape = shape,
        colors = colors,
        contentPadding = contentPadding,
        interactionSource = interactionSource,
    ) {
        BasicText(
            text = text,
            style = LocalMaigTextStyle.current,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigButtonLightPreview() {
    MaigTheme(darkTheme = false) {
        Column(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(MaigTheme.spacing.lg),
            verticalArrangement = Arrangement.spacedBy(MaigTheme.spacing.sm),
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(MaigTheme.spacing.sm)) {
                MaigButton(text = "Solid", onClick = {})
                MaigButton(text = "Outline", onClick = {}, variant = MaigButtonVariant.Outline)
                MaigButton(text = "Soft", onClick = {}, variant = MaigButtonVariant.Soft)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(MaigTheme.spacing.sm)) {
                MaigButton(text = "Ghost", onClick = {}, variant = MaigButtonVariant.Ghost)
                MaigButton(text = "Link", onClick = {}, variant = MaigButtonVariant.Link)
                MaigButton(text = "Loading", onClick = {}, loading = true)
            }
            MaigButton(text = "Danger Full", onClick = {}, color = MaigButtonColor.Danger, fullWidth = true)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigButtonDarkPreview() {
    MaigTheme(darkTheme = true) {
        Column(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(MaigTheme.spacing.lg),
            verticalArrangement = Arrangement.spacedBy(MaigTheme.spacing.sm),
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(MaigTheme.spacing.sm)) {
                MaigButton(text = "Solid", onClick = {})
                MaigButton(text = "Outline", onClick = {}, variant = MaigButtonVariant.Outline)
                MaigButton(text = "Soft", onClick = {}, variant = MaigButtonVariant.Soft)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(MaigTheme.spacing.sm)) {
                MaigButton(text = "Success", onClick = {}, color = MaigButtonColor.Success)
                MaigButton(text = "Disabled", onClick = {}, enabled = false)
            }
        }
    }
}
