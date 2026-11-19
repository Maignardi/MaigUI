package io.github.maignardi.maigui.components.navigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.maignardi.maigui.components.display.MaigBadge
import io.github.maignardi.maigui.components.display.MaigBadgedBox
import io.github.maignardi.maigui.components.display.MaigDivider
import io.github.maignardi.maigui.components.internal.MaigPlusIcon
import io.github.maignardi.maigui.components.internal.MaigSearchIcon
import io.github.maignardi.maigui.components.internal.rememberMaigRipple
import io.github.maignardi.maigui.core.theme.LocalMaigContentColor
import io.github.maignardi.maigui.core.theme.MaigTheme

/**
 * Data descriptor for declarative [MaigBottomNavigation] items.
 */
@Immutable
public class MaigBottomNavItem(
    public val label: String,
    public val badgeCount: Int? = null,
    public val icon: @Composable () -> Unit,
)

/**
 * Default values for [MaigBottomNavigation].
 */
public object MaigBottomNavigationDefaults {
    @Composable
    public fun containerColor(): Color = MaigTheme.colors.surface

    @Composable
    public fun selectedColor(): Color = MaigTheme.colors.primaryDefault

    @Composable
    public fun unselectedColor(): Color = MaigTheme.colors.textSecondary
}

/**
 * Bottom navigation bar container built on foundation with top divider and navigation bar insets.
 */
@Composable
public fun MaigBottomNavigation(
    modifier: Modifier = Modifier,
    containerColor: Color = MaigBottomNavigationDefaults.containerColor(),
    applyNavigationBarsPadding: Boolean = false,
    content: @Composable RowScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(containerColor)
            .then(if (applyNavigationBarsPadding) Modifier.navigationBarsPadding() else Modifier),
    ) {
        MaigDivider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 64.dp)
                .padding(horizontal = MaigTheme.spacing.xs, vertical = MaigTheme.spacing.xs),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            content = content,
        )
    }
}

/**
 * Declarative convenience overload of [MaigBottomNavigation].
 */
@Composable
public fun MaigBottomNavigation(
    items: List<MaigBottomNavItem>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = MaigBottomNavigationDefaults.containerColor(),
    applyNavigationBarsPadding: Boolean = false,
) {
    MaigBottomNavigation(
        modifier = modifier,
        containerColor = containerColor,
        applyNavigationBarsPadding = applyNavigationBarsPadding,
    ) {
        items.forEachIndexed { index, item ->
            MaigBottomNavigationItem(
                selected = index == selectedIndex,
                onClick = { onItemSelected(index) },
                label = item.label,
                badgeCount = item.badgeCount,
                icon = item.icon,
            )
        }
    }
}

/**
 * Individual destination item inside [MaigBottomNavigation] with active pill indicator.
 */
@Composable
public fun RowScope.MaigBottomNavigationItem(
    selected: Boolean,
    onClick: () -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    badgeCount: Int? = null,
    selectedColor: Color = MaigBottomNavigationDefaults.selectedColor(),
    unselectedColor: Color = MaigBottomNavigationDefaults.unselectedColor(),
    interactionSource: MutableInteractionSource? = null,
    icon: @Composable () -> Unit,
) {
    val resolvedInteraction = interactionSource ?: remember { MutableInteractionSource() }
    val fgColor by animateColorAsState(
        targetValue = when {
            !enabled -> MaigTheme.colors.textDisabled
            selected -> selectedColor
            else -> unselectedColor
        },
        animationSpec = tween(150),
        label = "MaigBottomNavFg",
    )
    val pillBg by animateColorAsState(
        targetValue = if (selected) MaigTheme.colors.primarySubtle else Color.Transparent,
        animationSpec = tween(150),
        label = "MaigBottomNavPill",
    )

    Column(
        modifier = modifier
            .weight(1f)
            .defaultMinSize(minHeight = 48.dp)
            .clip(MaigTheme.shapes.md)
            .clickable(
                interactionSource = resolvedInteraction,
                indication = rememberMaigRipple(bounded = true),
                enabled = enabled,
                role = Role.Tab,
                onClick = onClick,
            )
            .semantics { this.selected = selected }
            .padding(vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(pillBg)
                .padding(horizontal = MaigTheme.spacing.md, vertical = 4.dp),
            contentAlignment = Alignment.Center,
        ) {
            CompositionLocalProvider(LocalMaigContentColor provides fgColor) {
                if (badgeCount != null && badgeCount > 0) {
                    MaigBadgedBox(badge = { MaigBadge(text = badgeCount.toString()) }) {
                        icon()
                    }
                } else {
                    icon()
                }
            }
        }
        BasicText(
            text = label,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaigTheme.typography.caption.copy(color = fgColor),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigBottomNavigationLightPreview() {
    MaigTheme(darkTheme = false) {
        MaigBottomNavigation(
            items = listOf(
                MaigBottomNavItem("Início") { MaigPlusIcon() },
                MaigBottomNavItem("Buscar", badgeCount = 2) { MaigSearchIcon() },
                MaigBottomNavItem("Perfil") { MaigPlusIcon() },
            ),
            selectedIndex = 1,
            onItemSelected = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigBottomNavigationDarkPreview() {
    MaigTheme(darkTheme = true) {
        MaigBottomNavigation(
            items = listOf(
                MaigBottomNavItem("Início") { MaigPlusIcon() },
                MaigBottomNavItem("Buscar") { MaigSearchIcon() },
            ),
            selectedIndex = 0,
            onItemSelected = {},
        )
    }
}
