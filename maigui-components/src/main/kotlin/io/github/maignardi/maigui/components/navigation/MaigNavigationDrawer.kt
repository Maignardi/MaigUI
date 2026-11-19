package io.github.maignardi.maigui.components.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.maignardi.maigui.components.internal.MaigPlusIcon
import io.github.maignardi.maigui.components.internal.MaigSearchIcon
import io.github.maignardi.maigui.components.internal.rememberMaigRipple
import io.github.maignardi.maigui.core.theme.LocalMaigContentColor
import io.github.maignardi.maigui.core.theme.MaigTheme

/**
 * Defaults for [MaigModalNavigationDrawer], [MaigDrawerSheet], and [MaigNavigationDrawerItem].
 */
public object MaigNavigationDrawerDefaults {
    public val DrawerWidth: Dp = 300.dp

    @Composable
    public fun containerColor(): Color = MaigTheme.colors.surfaceElevated
}

/**
 * Modal side navigation drawer sliding over [content] when [drawerOpen] is `true`.
 */
@Composable
public fun MaigModalNavigationDrawer(
    drawerOpen: Boolean,
    onDrawerOpenChange: (Boolean) -> Unit,
    drawerContent: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
    drawerWidth: Dp = MaigNavigationDrawerDefaults.DrawerWidth,
    content: @Composable () -> Unit,
) {
    Box(modifier = modifier.fillMaxSize()) {
        content()

        AnimatedVisibility(
            visible = drawerOpen,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaigTheme.colors.scrim)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { onDrawerOpenChange(false) },
                    ),
            )
        }

        AnimatedVisibility(
            visible = drawerOpen,
            enter = slideInHorizontally { -it },
            exit = slideOutHorizontally { -it },
        ) {
            MaigDrawerSheet(
                modifier = Modifier.width(drawerWidth),
                content = drawerContent,
            )
        }
    }
}

/**
 * Surface container for navigation drawer items and headers.
 */
@Composable
public fun MaigDrawerSheet(
    modifier: Modifier = Modifier,
    containerColor: Color = MaigNavigationDrawerDefaults.containerColor(),
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .background(containerColor)
            .border(1.dp, MaigTheme.colors.border)
            .safeDrawingPadding()
            .padding(MaigTheme.spacing.md),
        verticalArrangement = Arrangement.spacedBy(MaigTheme.spacing.xs),
        content = content,
    )
}

/**
 * Selectable destination item inside [MaigDrawerSheet].
 */
@Composable
public fun MaigNavigationDrawerItem(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: @Composable (() -> Unit)? = null,
    badge: @Composable (() -> Unit)? = null,
) {
    val c = MaigTheme.colors
    val bg = if (selected) c.primarySubtle else Color.Transparent
    val fg = if (selected) c.onPrimarySubtle else c.textPrimary
    val interaction = remember { MutableInteractionSource() }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 48.dp)
            .clip(MaigTheme.shapes.md)
            .background(bg)
            .clickable(
                interactionSource = interaction,
                indication = rememberMaigRipple(bounded = true),
                role = Role.Tab,
                onClick = onClick,
            )
            .semantics { this.selected = selected }
            .padding(horizontal = MaigTheme.spacing.md, vertical = MaigTheme.spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CompositionLocalProvider(LocalMaigContentColor provides fg) {
            if (icon != null) {
                icon()
                Spacer(modifier = Modifier.width(MaigTheme.spacing.md))
            }
            BasicText(
                text = label,
                modifier = Modifier.weight(1f),
                style = MaigTheme.typography.label.copy(color = fg),
            )
            if (badge != null) {
                Spacer(modifier = Modifier.width(MaigTheme.spacing.sm))
                badge()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigNavigationDrawerLightPreview() {
    MaigTheme(darkTheme = false) {
        MaigDrawerSheet(modifier = Modifier.width(260.dp)) {
            BasicText(
                text = "MaigUI",
                modifier = Modifier.padding(MaigTheme.spacing.md),
                style = MaigTheme.typography.title.copy(color = MaigTheme.colors.primaryDefault),
            )
            MaigNavigationDrawerItem(label = "Dashboard", selected = true, onClick = {}, icon = { MaigPlusIcon() })
            MaigNavigationDrawerItem(label = "Explorar", selected = false, onClick = {}, icon = { MaigSearchIcon() })
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigNavigationDrawerDarkPreview() {
    MaigTheme(darkTheme = true) {
        MaigDrawerSheet(modifier = Modifier.width(260.dp)) {
            BasicText(
                text = "MaigUI",
                modifier = Modifier.padding(MaigTheme.spacing.md),
                style = MaigTheme.typography.title.copy(color = MaigTheme.colors.primaryDefault),
            )
            MaigNavigationDrawerItem(label = "Componentes", selected = true, onClick = {}, icon = { MaigPlusIcon() })
            MaigNavigationDrawerItem(label = "Formulários", selected = false, onClick = {}, icon = { MaigSearchIcon() })
        }
    }
}
