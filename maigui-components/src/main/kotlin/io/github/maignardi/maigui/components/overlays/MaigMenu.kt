package io.github.maignardi.maigui.components.overlays

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import io.github.maignardi.maigui.components.internal.rememberMaigRipple
import io.github.maignardi.maigui.core.theme.LocalMaigContentColor
import io.github.maignardi.maigui.core.theme.MaigTheme

/**
 * Default values for [MaigDropdownMenu] and [MaigMenuItem].
 */
public object MaigMenuDefaults {
    @Composable
    public fun shape(): Shape = MaigTheme.shapes.lg

    @Composable
    public fun containerColor(): Color = MaigTheme.colors.surfaceElevated
}

/**
 * Anchored dropdown menu container.
 */
@Composable
public fun MaigDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    offset: IntOffset = IntOffset(0, 8),
    shape: Shape = MaigMenuDefaults.shape(),
    containerColor: Color = MaigMenuDefaults.containerColor(),
    content: @Composable ColumnScope.() -> Unit,
) {
    if (!expanded) return

    Popup(
        alignment = Alignment.TopStart,
        offset = offset,
        onDismissRequest = onDismissRequest,
        properties = PopupProperties(focusable = true),
    ) {
        MaigMenuSurface(
            modifier = modifier,
            shape = shape,
            containerColor = containerColor,
            content = content,
        )
    }
}

/**
 * Menu panel surface reusable both in [MaigDropdownMenu] and standalone previews.
 */
@Composable
public fun MaigMenuSurface(
    modifier: Modifier = Modifier,
    shape: Shape = MaigMenuDefaults.shape(),
    containerColor: Color = MaigMenuDefaults.containerColor(),
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .widthIn(min = 180.dp, max = 280.dp)
            .shadow(MaigTheme.elevation.lg, shape)
            .clip(shape)
            .background(containerColor)
            .border(1.dp, MaigTheme.colors.border, shape)
            .padding(vertical = MaigTheme.spacing.xs),
        content = content,
    )
}

/**
 * Action row item inside a [MaigDropdownMenu].
 */
@Composable
public fun MaigMenuItem(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    destructive: Boolean = false,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    interactionSource: MutableInteractionSource? = null,
) {
    val resolvedInteraction = interactionSource ?: remember { MutableInteractionSource() }
    val c = MaigTheme.colors
    val fgColor = when {
        !enabled -> c.textDisabled
        destructive -> c.dangerDefault
        else -> c.textPrimary
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 44.dp)
            .clickable(
                interactionSource = resolvedInteraction,
                indication = rememberMaigRipple(bounded = true),
                enabled = enabled,
                role = Role.Button,
                onClick = onClick,
            )
            .padding(horizontal = MaigTheme.spacing.md, vertical = MaigTheme.spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        CompositionLocalProvider(LocalMaigContentColor provides fgColor) {
            if (leadingIcon != null) {
                leadingIcon()
                Spacer(modifier = Modifier.width(MaigTheme.spacing.sm))
            }
            BasicText(
                text = text,
                modifier = Modifier.weight(1f),
                style = MaigTheme.typography.body.copy(color = fgColor),
            )
            if (trailingIcon != null) {
                Spacer(modifier = Modifier.width(MaigTheme.spacing.sm))
                trailingIcon()
            }
        }
    }
}

/**
 * Subtle divider between groups inside [MaigDropdownMenu].
 */
@Composable
public fun MaigMenuDivider(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = MaigTheme.spacing.xs)
            .height(1.dp)
            .background(MaigTheme.colors.divider),
    )
}

@Preview(showBackground = true)
@Composable
private fun MaigMenuLightPreview() {
    MaigTheme(darkTheme = false) {
        Box(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(MaigTheme.spacing.lg),
        ) {
            MaigMenuSurface {
                MaigMenuItem(text = "Editar", onClick = {})
                MaigMenuItem(text = "Duplicar", onClick = {})
                MaigMenuDivider()
                MaigMenuItem(text = "Excluir", onClick = {}, destructive = true)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigMenuDarkPreview() {
    MaigTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(MaigTheme.spacing.lg),
        ) {
            MaigMenuSurface {
                MaigMenuItem(text = "Compartilhar", onClick = {})
                MaigMenuItem(text = "Remover", onClick = {}, destructive = true)
            }
        }
    }
}
