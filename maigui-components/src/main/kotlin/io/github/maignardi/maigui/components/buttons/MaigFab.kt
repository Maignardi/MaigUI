package io.github.maignardi.maigui.components.buttons

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.maignardi.maigui.components.internal.MaigPlusIcon
import io.github.maignardi.maigui.components.internal.rememberMaigRipple
import io.github.maignardi.maigui.core.theme.LocalMaigContentColor
import io.github.maignardi.maigui.core.theme.MaigTheme

/**
 * Size options for [MaigFab].
 */
public enum class MaigFabSize {
    Small,
    Medium,
    Large,
}

/**
 * Defaults for [MaigFab].
 */
public object MaigFabDefaults {
    /** Returns size in dp for a standard circular/square [MaigFab]. */
    public fun size(size: MaigFabSize): Dp = when (size) {
        MaigFabSize.Small -> 44.dp
        MaigFabSize.Medium -> 52.dp
        MaigFabSize.Large -> 62.dp
    }

    /** Returns default shape for [MaigFab]. */
    @Composable
    public fun shape(extended: Boolean): Shape =
        if (extended) MaigTheme.shapes.full else MaigTheme.shapes.lg

    /** Resolves colors for [MaigFab]. */
    @Composable
    public fun colors(color: MaigButtonColor = MaigButtonColor.Primary): MaigButtonColors =
        MaigButtonDefaults.colors(variant = MaigButtonVariant.Solid, color = color)
}

/**
 * Floating Action Button with optional extended [label].
 */
@Composable
public fun MaigFab(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    contentDescription: String? = label,
    enabled: Boolean = true,
    color: MaigButtonColor = MaigButtonColor.Primary,
    size: MaigFabSize = MaigFabSize.Medium,
    shape: Shape = MaigFabDefaults.shape(extended = label != null),
    colors: MaigButtonColors = MaigFabDefaults.colors(color = color),
    elevation: Dp = MaigTheme.elevation.lg,
    interactionSource: MutableInteractionSource? = null,
    icon: @Composable () -> Unit,
) {
    val resolvedInteraction = interactionSource ?: remember { MutableInteractionSource() }
    val containerColor = colors.containerColor(enabled)
    val contentColor = colors.contentColor(enabled)
    val fabSize = MaigFabDefaults.size(size)

    val sizeModifier = if (label == null) {
        Modifier.size(fabSize)
    } else {
        Modifier
            .defaultMinSize(minWidth = fabSize, minHeight = 48.dp)
            .padding(horizontal = MaigTheme.spacing.lg, vertical = MaigTheme.spacing.md)
    }

    val effectiveElevation = if (enabled && !MaigTheme.colors.isDark) elevation.coerceAtMost(4.dp) else 0.dp
    Box(
        modifier = modifier
            .shadow(
                elevation = effectiveElevation,
                shape = shape,
                ambientColor = containerColor.copy(alpha = 0.35f),
                spotColor = containerColor.copy(alpha = 0.35f),
            )
            .clip(shape)
            .background(containerColor)
            .border(
                BorderStroke(
                    1.dp,
                    Brush.verticalGradient(listOf(Color.White.copy(alpha = 0.28f), Color.Transparent)),
                ),
                shape,
            )
            .clickable(
                interactionSource = resolvedInteraction,
                indication = rememberMaigRipple(bounded = true, color = colors.rippleColor),
                enabled = enabled,
                role = Role.Button,
                onClick = onClick,
            )
            .semantics(mergeDescendants = true) {
                if (contentDescription != null) {
                    this.contentDescription = contentDescription
                }
            }
            .then(sizeModifier),
        contentAlignment = Alignment.Center,
    ) {
        CompositionLocalProvider(LocalMaigContentColor provides contentColor) {
            if (label == null) {
                icon()
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    icon()
                    Spacer(modifier = Modifier.width(MaigTheme.spacing.sm))
                    BasicText(
                        text = label,
                        style = MaigTheme.typography.button.copy(color = contentColor),
                        maxLines = 1,
                        softWrap = false,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigFabLightPreview() {
    MaigTheme(darkTheme = false) {
        Row(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(MaigTheme.spacing.lg),
            horizontalArrangement = Arrangement.spacedBy(MaigTheme.spacing.md),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            MaigFab(onClick = {}, contentDescription = "Novo") { MaigPlusIcon() }
            MaigFab(onClick = {}, label = "Criar item") { MaigPlusIcon() }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigFabDarkPreview() {
    MaigTheme(darkTheme = true) {
        Row(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(MaigTheme.spacing.lg),
            horizontalArrangement = Arrangement.spacedBy(MaigTheme.spacing.md),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            MaigFab(onClick = {}, contentDescription = "Novo", color = MaigButtonColor.Success) {
                MaigPlusIcon()
            }
            MaigFab(onClick = {}, label = "Adicionar") { MaigPlusIcon() }
        }
    }
}
