package io.github.maignardi.maigui.components.display

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.maignardi.maigui.components.buttons.MaigButton
import io.github.maignardi.maigui.components.buttons.MaigButtonSize
import io.github.maignardi.maigui.components.internal.MaigSearchIcon
import io.github.maignardi.maigui.core.theme.LocalMaigContentColor
import io.github.maignardi.maigui.core.theme.MaigTheme

/**
 * Defaults for [MaigEmptyState].
 */
public object MaigEmptyStateDefaults {
    @Composable
    public fun DefaultIcon() {
        MaigSearchIcon(size = 28.dp, tint = MaigTheme.colors.primaryDefault)
    }
}

/**
 * Placeholder component displayed when lists, searches, or screens have no data.
 */
@Composable
public fun MaigEmptyState(
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    icon: @Composable (() -> Unit)? = { MaigEmptyStateDefaults.DefaultIcon() },
    action: @Composable (() -> Unit)? = null,
) {
    val c = MaigTheme.colors
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(MaigTheme.spacing.xl),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MaigTheme.spacing.md),
    ) {
        if (icon != null) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(c.primarySubtle),
                contentAlignment = Alignment.Center,
            ) {
                CompositionLocalProvider(LocalMaigContentColor provides c.onPrimarySubtle) {
                    icon()
                }
            }
        }

        BasicText(
            text = title,
            style = MaigTheme.typography.title.copy(
                color = c.textPrimary,
                textAlign = TextAlign.Center,
            ),
        )

        if (description != null) {
            BasicText(
                text = description,
                style = MaigTheme.typography.bodySmall.copy(
                    color = c.textSecondary,
                    textAlign = TextAlign.Center,
                ),
            )
        }

        if (action != null) {
            Box(modifier = Modifier.padding(top = MaigTheme.spacing.xs)) {
                action()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigEmptyStateLightPreview() {
    MaigTheme(darkTheme = false) {
        Box(modifier = Modifier.background(MaigTheme.colors.background)) {
            MaigEmptyState(
                title = "Nenhum resultado encontrado",
                description = "Tente ajustar os filtros ou buscar por outro termo.",
                action = { MaigButton(text = "Limpar filtros", onClick = {}, size = MaigButtonSize.Small) },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigEmptyStateDarkPreview() {
    MaigTheme(darkTheme = true) {
        Box(modifier = Modifier.background(MaigTheme.colors.background)) {
            MaigEmptyState(
                title = "Lista vazia",
                description = "Adicione seu primeiro item para começar.",
            )
        }
    }
}
