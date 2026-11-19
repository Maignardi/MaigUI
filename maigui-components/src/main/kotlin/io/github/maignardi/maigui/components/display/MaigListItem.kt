package io.github.maignardi.maigui.components.display

import androidx.compose.foundation.background
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.maignardi.maigui.components.internal.ChevronDirection
import io.github.maignardi.maigui.components.internal.MaigChevronIcon
import io.github.maignardi.maigui.components.internal.rememberMaigRipple
import io.github.maignardi.maigui.core.theme.LocalMaigContentColor
import io.github.maignardi.maigui.core.theme.MaigTheme

/**
 * Defaults for [MaigListItem].
 */
public object MaigListItemDefaults {
    @Composable
    public fun containerColor(): Color = MaigTheme.colors.surface
}

/**
 * Structured list row supporting overline, headline, supporting subtitle, leading, and trailing slots.
 */
@Composable
public fun MaigListItem(
    headline: String,
    modifier: Modifier = Modifier,
    supportingText: String? = null,
    overlineText: String? = null,
    leadingContent: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    onClick: (() -> Unit)? = null,
    enabled: Boolean = true,
    showDivider: Boolean = false,
    containerColor: Color = MaigListItemDefaults.containerColor(),
    interactionSource: MutableInteractionSource? = null,
) {
    val resolvedInteraction = interactionSource ?: remember { MutableInteractionSource() }
    val c = MaigTheme.colors
    val titleColor = if (enabled) c.textPrimary else c.textDisabled
    val subtitleColor = if (enabled) c.textSecondary else c.textDisabled

    Column(modifier = modifier.fillMaxWidth().background(containerColor)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 56.dp)
                .then(
                    if (onClick != null) {
                        Modifier.clickable(
                            interactionSource = resolvedInteraction,
                            indication = rememberMaigRipple(bounded = true),
                            enabled = enabled,
                            role = Role.Button,
                            onClick = onClick,
                        )
                    } else {
                        Modifier
                    },
                )
                .padding(horizontal = MaigTheme.spacing.lg, vertical = MaigTheme.spacing.md),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (leadingContent != null) {
                CompositionLocalProvider(LocalMaigContentColor provides titleColor) {
                    leadingContent()
                }
                Spacer(modifier = Modifier.width(MaigTheme.spacing.md))
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                if (overlineText != null) {
                    BasicText(
                        text = overlineText,
                        style = MaigTheme.typography.caption.copy(color = subtitleColor),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                BasicText(
                    text = headline,
                    style = MaigTheme.typography.subtitle.copy(color = titleColor),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                if (supportingText != null) {
                    BasicText(
                        text = supportingText,
                        style = MaigTheme.typography.bodySmall.copy(color = subtitleColor),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }

            if (trailingContent != null) {
                Spacer(modifier = Modifier.width(MaigTheme.spacing.md))
                CompositionLocalProvider(LocalMaigContentColor provides subtitleColor) {
                    trailingContent()
                }
            }
        }

        if (showDivider) {
            MaigDivider()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigListItemLightPreview() {
    MaigTheme(darkTheme = false) {
        Column(modifier = Modifier.background(MaigTheme.colors.background)) {
            MaigListItem(
                headline = "Matheus Maignardi",
                supportingText = "Engenheiro Android • Kotlin & Compose",
                leadingContent = { MaigAvatar(name = "Matheus Maignardi") },
                trailingContent = { MaigChevronIcon(direction = ChevronDirection.Right) },
                onClick = {},
                showDivider = true,
            )
            MaigListItem(
                overlineText = "SEGURANÇA",
                headline = "Autenticação em 2 fatores",
                supportingText = "Ativada via aplicativo autenticador",
                trailingContent = { MaigTag(text = "Ativo", color = MaigTagColor.Success) },
                onClick = {},
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigListItemDarkPreview() {
    MaigTheme(darkTheme = true) {
        Box(modifier = Modifier.background(MaigTheme.colors.background)) {
            MaigListItem(
                headline = "Notificações",
                supportingText = "Gerenciar alertas por e-mail e push",
                leadingContent = { MaigAvatar(name = "App") },
                trailingContent = { MaigChevronIcon(direction = ChevronDirection.Right) },
                onClick = {},
            )
        }
    }
}
