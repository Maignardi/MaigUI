package io.github.maignardi.maigui.components.display

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.maignardi.maigui.components.internal.ChevronDirection
import io.github.maignardi.maigui.components.internal.MaigChevronIcon
import io.github.maignardi.maigui.components.internal.rememberMaigRipple
import io.github.maignardi.maigui.core.theme.LocalMaigContentColor
import io.github.maignardi.maigui.core.theme.MaigTheme

/**
 * Defaults for [MaigAccordion].
 */
public object MaigAccordionDefaults {
    @Composable
    public fun shape(): Shape = MaigTheme.shapes.lg
}

/**
 * Expandable/collapsible accordion panel with animated chevron rotation and smooth content reveal.
 */
@Composable
public fun MaigAccordion(
    title: String,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    enabled: Boolean = true,
    leadingIcon: @Composable (() -> Unit)? = null,
    shape: Shape = MaigAccordionDefaults.shape(),
    interactionSource: MutableInteractionSource? = null,
    content: @Composable () -> Unit,
) {
    val resolvedInteraction = interactionSource ?: remember { MutableInteractionSource() }
    val c = MaigTheme.colors
    val rotation by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        animationSpec = tween(200),
        label = "MaigAccordionChevron",
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(c.surface)
            .border(1.dp, c.border, shape),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 52.dp)
                .clickable(
                    interactionSource = resolvedInteraction,
                    indication = rememberMaigRipple(bounded = true),
                    enabled = enabled,
                    role = Role.Button,
                    onClick = { onExpandedChange(!expanded) },
                )
                .semantics {
                    stateDescription = if (expanded) "Expandido" else "Recolhido"
                }
                .padding(horizontal = MaigTheme.spacing.lg, vertical = MaigTheme.spacing.md),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (leadingIcon != null) {
                CompositionLocalProvider(LocalMaigContentColor provides c.primaryDefault) {
                    leadingIcon()
                }
                Spacer(modifier = Modifier.width(MaigTheme.spacing.md))
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                BasicText(
                    text = title,
                    style = MaigTheme.typography.subtitle.copy(
                        color = if (enabled) c.textPrimary else c.textDisabled,
                    ),
                )
                if (subtitle != null) {
                    BasicText(
                        text = subtitle,
                        style = MaigTheme.typography.caption.copy(color = c.textSecondary),
                    )
                }
            }

            MaigChevronIcon(
                direction = ChevronDirection.Down,
                modifier = Modifier.rotate(rotation),
                tint = c.textSecondary,
            )
        }

        AnimatedVisibility(
            visible = expanded,
            enter = fadeIn(tween(150)) + expandVertically(tween(200)),
            exit = fadeOut(tween(150)) + shrinkVertically(tween(200)),
        ) {
            Column {
                MaigDivider()
                Box(modifier = Modifier.padding(MaigTheme.spacing.lg)) {
                    content()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigAccordionLightPreview() {
    MaigTheme(darkTheme = false) {
        Column(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(MaigTheme.spacing.md),
            verticalArrangement = Arrangement.spacedBy(MaigTheme.spacing.sm),
        ) {
            MaigAccordion(
                title = "Como instalar o MaigUI?",
                subtitle = "Gradle Kotlin DSL & Version Catalog",
                expanded = true,
                onExpandedChange = {},
            ) {
                BasicText(
                    text = "Adicione io.github.maignardi:maig-ui:0.1.0 nas dependências e envolva seu app em MaigTheme {}.",
                    style = MaigTheme.typography.bodySmall.copy(color = MaigTheme.colors.textSecondary),
                )
            }
            MaigAccordion(title = "Suporta Dark Mode automático?", expanded = false, onExpandedChange = {}) {}
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigAccordionDarkPreview() {
    MaigTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(MaigTheme.spacing.md),
        ) {
            MaigAccordion(
                title = "Detalhes técnicos",
                expanded = true,
                onExpandedChange = {},
            ) {
                BasicText(
                    text = "Construído 100% sobre androidx.compose.foundation.",
                    style = MaigTheme.typography.bodySmall.copy(color = MaigTheme.colors.textSecondary),
                )
            }
        }
    }
}
