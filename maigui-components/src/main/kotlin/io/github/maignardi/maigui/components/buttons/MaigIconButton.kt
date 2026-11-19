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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.maignardi.maigui.components.feedback.MaigSpinner
import io.github.maignardi.maigui.components.internal.MaigPlusIcon
import io.github.maignardi.maigui.components.internal.MaigSearchIcon
import io.github.maignardi.maigui.components.internal.maigTouchTarget
import io.github.maignardi.maigui.components.internal.rememberMaigRipple
import io.github.maignardi.maigui.core.theme.LocalMaigContentColor
import io.github.maignardi.maigui.core.theme.MaigTheme

/**
 * Default dimensions and colors for [MaigIconButton].
 */
public object MaigIconButtonDefaults {
    /** Returns the visual container size for [size]. */
    public fun containerSize(size: MaigButtonSize): Dp = when (size) {
        MaigButtonSize.Small -> 32.dp
        MaigButtonSize.Medium -> 40.dp
        MaigButtonSize.Large -> 48.dp
    }

    /** Returns default shape for [MaigIconButton]. */
    @Composable
    public fun shape(): Shape = MaigTheme.shapes.md

    /** Resolves colors for [MaigIconButton]. */
    @Composable
    public fun colors(
        variant: MaigButtonVariant = MaigButtonVariant.Ghost,
        color: MaigButtonColor = MaigButtonColor.Neutral,
    ): MaigButtonColors = MaigButtonDefaults.colors(variant = variant, color = color)
}

/**
 * Square icon-only interactive button with a minimum 48dp accessible touch target.
 */
@Composable
public fun MaigIconButton(
    onClick: () -> Unit,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    variant: MaigButtonVariant = MaigButtonVariant.Ghost,
    color: MaigButtonColor = MaigButtonColor.Neutral,
    size: MaigButtonSize = MaigButtonSize.Medium,
    shape: Shape = MaigIconButtonDefaults.shape(),
    colors: MaigButtonColors = MaigIconButtonDefaults.colors(variant = variant, color = color),
    interactionSource: MutableInteractionSource? = null,
    icon: @Composable () -> Unit,
) {
    val resolvedInteraction = interactionSource ?: remember { MutableInteractionSource() }
    val isInteractive = enabled && !loading
    val visualSize = MaigIconButtonDefaults.containerSize(size)

    val containerColor by animateColorAsState(
        targetValue = colors.containerColor(enabled),
        animationSpec = tween(150),
        label = "MaigIconButtonContainer",
    )
    val contentColor by animateColorAsState(
        targetValue = colors.contentColor(enabled),
        animationSpec = tween(150),
        label = "MaigIconButtonContent",
    )
    val borderColor by animateColorAsState(
        targetValue = colors.borderColor(enabled),
        animationSpec = tween(150),
        label = "MaigIconButtonBorder",
    )

    Box(
        modifier = modifier
            .maigTouchTarget()
            .semantics(mergeDescendants = true) {
                if (contentDescription != null) {
                    this.contentDescription = contentDescription
                }
            },
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .size(visualSize)
                .clip(shape)
                .background(containerColor)
                .then(
                    if (borderColor != Color.Transparent) {
                        Modifier.border(BorderStroke(1.dp, borderColor), shape)
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
                ),
            contentAlignment = Alignment.Center,
        ) {
            CompositionLocalProvider(LocalMaigContentColor provides contentColor) {
                if (loading) {
                    MaigSpinner(
                        size = MaigButtonDefaults.spinnerSize(size),
                        color = contentColor,
                        strokeWidth = 2.dp,
                    )
                } else {
                    icon()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigIconButtonLightPreview() {
    MaigTheme(darkTheme = false) {
        Row(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(MaigTheme.spacing.md),
            horizontalArrangement = Arrangement.spacedBy(MaigTheme.spacing.sm),
        ) {
            MaigIconButton(
                onClick = {},
                contentDescription = "Adicionar",
                variant = MaigButtonVariant.Solid,
                color = MaigButtonColor.Primary,
            ) { MaigPlusIcon() }

            MaigIconButton(
                onClick = {},
                contentDescription = "Buscar",
                variant = MaigButtonVariant.Outline,
                color = MaigButtonColor.Primary,
            ) { MaigSearchIcon() }

            MaigIconButton(
                onClick = {},
                contentDescription = "Carregando",
                variant = MaigButtonVariant.Soft,
                color = MaigButtonColor.Secondary,
                loading = true,
            ) { MaigPlusIcon() }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigIconButtonDarkPreview() {
    MaigTheme(darkTheme = true) {
        Row(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(MaigTheme.spacing.md),
            horizontalArrangement = Arrangement.spacedBy(MaigTheme.spacing.sm),
        ) {
            MaigIconButton(
                onClick = {},
                contentDescription = "Adicionar",
                variant = MaigButtonVariant.Solid,
                color = MaigButtonColor.Primary,
            ) { MaigPlusIcon() }

            MaigIconButton(
                onClick = {},
                contentDescription = "Buscar",
                variant = MaigButtonVariant.Ghost,
            ) { MaigSearchIcon() }
        }
    }
}
