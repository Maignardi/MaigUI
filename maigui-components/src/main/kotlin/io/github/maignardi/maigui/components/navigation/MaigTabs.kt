package io.github.maignardi.maigui.components.navigation

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import io.github.maignardi.maigui.components.display.MaigDivider
import io.github.maignardi.maigui.components.internal.rememberMaigRipple
import io.github.maignardi.maigui.core.theme.MaigTheme

/**
 * Visual indicator style for [MaigTabs].
 */
public enum class MaigTabVariant {
    /** Classic underline indicator that slides smoothly beneath the active tab. */
    Underline,

    /** Contained pill tabs inside a soft track container. */
    Pill,
}

/**
 * Defaults for [MaigTabs].
 */
public object MaigTabsDefaults {
    @Composable
    public fun activeColor(): Color = MaigTheme.colors.primaryDefault

    @Composable
    public fun inactiveColor(): Color = MaigTheme.colors.textSecondary
}

private data class TabBounds(val leftPx: Int, val widthPx: Int)

/**
 * Fixed or horizontally scrollable tab bar with smooth animated indicator.
 */
@Composable
public fun MaigTabs(
    tabs: List<String>,
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    scrollable: Boolean = false,
    variant: MaigTabVariant = MaigTabVariant.Underline,
    enabled: Boolean = true,
) {
    val c = MaigTheme.colors
    val density = LocalDensity.current
    val tabBounds = remember { mutableStateMapOf<Int, TabBounds>() }

    when (variant) {
        MaigTabVariant.Underline -> {
            Column(modifier = modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .then(if (scrollable) Modifier.horizontalScroll(rememberScrollState()) else Modifier),
                ) {
                    Row(
                        modifier = if (scrollable) Modifier else Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        tabs.forEachIndexed { index, title ->
                            val isSelected = index == selectedIndex
                            val interaction = remember { MutableInteractionSource() }
                            val textColor = when {
                                !enabled -> c.textDisabled
                                isSelected -> MaigTabsDefaults.activeColor()
                                else -> MaigTabsDefaults.inactiveColor()
                            }

                            Box(
                                modifier = Modifier
                                    .then(if (scrollable) Modifier else Modifier.weight(1f))
                                    .defaultMinSize(minWidth = 64.dp, minHeight = 48.dp)
                                    .onGloballyPositioned { coords ->
                                        tabBounds[index] = TabBounds(
                                            leftPx = coords.positionInParent().x.toInt(),
                                            widthPx = coords.size.width,
                                        )
                                    }
                                    .clickable(
                                        interactionSource = interaction,
                                        indication = rememberMaigRipple(bounded = true),
                                        enabled = enabled,
                                        role = Role.Tab,
                                        onClick = { onTabSelected(index) },
                                    )
                                    .semantics { selected = isSelected }
                                    .padding(horizontal = MaigTheme.spacing.lg, vertical = MaigTheme.spacing.md),
                                contentAlignment = Alignment.Center,
                            ) {
                                BasicText(
                                    text = title,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    style = MaigTheme.typography.label.copy(color = textColor),
                                )
                            }
                        }
                    }

                    val activeBounds = tabBounds[selectedIndex]
                    if (activeBounds != null) {
                        val targetOffsetDp = with(density) { activeBounds.leftPx.toDp() }
                        val targetWidthDp = with(density) { activeBounds.widthPx.toDp() }
                        val animatedOffset by animateDpAsState(
                            targetValue = targetOffsetDp,
                            animationSpec = tween(220),
                            label = "MaigTabOffset",
                        )
                        val animatedWidth by animateDpAsState(
                            targetValue = targetWidthDp,
                            animationSpec = tween(220),
                            label = "MaigTabWidth",
                        )

                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .offset { IntOffset(animatedOffset.roundToPx(), 0) }
                                .width(animatedWidth)
                                .height(2.5.dp)
                                .clip(CircleShape)
                                .background(MaigTabsDefaults.activeColor()),
                        )
                    }
                }
                MaigDivider()
            }
        }

        MaigTabVariant.Pill -> {
            Row(
                modifier = modifier
                    .then(if (scrollable) Modifier.horizontalScroll(rememberScrollState()) else Modifier.fillMaxWidth())
                    .clip(MaigTheme.shapes.lg)
                    .background(c.surfaceVariant)
                    .border(1.dp, c.borderSubtle, MaigTheme.shapes.lg)
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                tabs.forEachIndexed { index, title ->
                    val isSelected = index == selectedIndex
                    val bg = if (isSelected) c.surface else Color.Transparent
                    val fg = when {
                        !enabled -> c.textDisabled
                        isSelected -> c.textPrimary
                        else -> c.textSecondary
                    }
                    Box(
                        modifier = Modifier
                            .then(if (scrollable) Modifier else Modifier.weight(1f))
                            .defaultMinSize(minHeight = 40.dp)
                            .clip(MaigTheme.shapes.md)
                            .background(bg)
                            .clickable(
                                enabled = enabled,
                                role = Role.Tab,
                                onClick = { onTabSelected(index) },
                            )
                            .semantics { selected = isSelected }
                            .padding(horizontal = MaigTheme.spacing.md, vertical = MaigTheme.spacing.xs),
                        contentAlignment = Alignment.Center,
                    ) {
                        BasicText(
                            text = title,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaigTheme.typography.label.copy(color = fg),
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigTabsLightPreview() {
    MaigTheme(darkTheme = false) {
        Column(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(MaigTheme.spacing.md),
            verticalArrangement = Arrangement.spacedBy(MaigTheme.spacing.md),
        ) {
            MaigTabs(
                tabs = listOf("Visão Geral", "Código", "API"),
                selectedIndex = 0,
                onTabSelected = {},
            )
            MaigTabs(
                tabs = listOf("Mensal", "Anual"),
                selectedIndex = 1,
                onTabSelected = {},
                variant = MaigTabVariant.Pill,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigTabsDarkPreview() {
    MaigTheme(darkTheme = true) {
        Column(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(MaigTheme.spacing.md),
            verticalArrangement = Arrangement.spacedBy(MaigTheme.spacing.md),
        ) {
            MaigTabs(
                tabs = listOf("Tudo", "Botões", "Inputs", "Seleção", "Overlays"),
                selectedIndex = 2,
                onTabSelected = {},
                scrollable = true,
            )
        }
    }
}
