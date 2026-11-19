package io.github.maignardi.maigui.components.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.maignardi.maigui.components.buttons.MaigButton
import io.github.maignardi.maigui.components.buttons.MaigButtonSize
import io.github.maignardi.maigui.components.buttons.MaigButtonVariant
import io.github.maignardi.maigui.core.theme.MaigTheme

/**
 * Defaults for [MaigSection].
 */
public object MaigSectionDefaults {
    @Composable
    public fun titleColor(): androidx.compose.ui.graphics.Color = MaigTheme.colors.textPrimary
}

/**
 * Semantic section container pairing a header [title], optional [subtitle], optional [action],
 * and vertically spaced [content].
 */
@Composable
public fun MaigSection(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    action: @Composable (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(MaigTheme.spacing.md),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(MaigTheme.spacing.xxs),
            ) {
                BasicText(
                    text = title,
                    style = MaigTheme.typography.title.copy(color = MaigSectionDefaults.titleColor()),
                )
                if (subtitle != null) {
                    BasicText(
                        text = subtitle,
                        style = MaigTheme.typography.bodySmall.copy(color = MaigTheme.colors.textSecondary),
                    )
                }
            }
            if (action != null) {
                action()
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(MaigTheme.spacing.sm),
            content = content,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigSectionLightPreview() {
    MaigTheme(darkTheme = false) {
        Column(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(MaigTheme.spacing.lg),
        ) {
            MaigSection(
                title = "Atividade Recente",
                subtitle = "Últimas 24 horas",
                action = {
                    MaigButton(text = "Ver tudo", onClick = {}, variant = MaigButtonVariant.Link, size = MaigButtonSize.Small)
                },
            ) {
                BasicText("Conteúdo da seção", style = MaigTheme.typography.body.copy(color = MaigTheme.colors.textPrimary))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigSectionDarkPreview() {
    MaigTheme(darkTheme = true) {
        Column(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(MaigTheme.spacing.lg),
        ) {
            MaigSection(title = "Preferências") {
                BasicText("Conteúdo da seção", style = MaigTheme.typography.body.copy(color = MaigTheme.colors.textPrimary))
            }
        }
    }
}
