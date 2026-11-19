package io.github.maignardi.maigui.components.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.maignardi.maigui.components.internal.ChevronDirection
import io.github.maignardi.maigui.components.internal.MaigChevronIcon
import io.github.maignardi.maigui.core.theme.MaigTheme

/**
 * Clickable horizontal navigation breadcrumb trail (`Início > Produtos > Detalhes`).
 */
@Composable
public fun MaigBreadcrumb(
    items: List<String>,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (items.isEmpty()) return
    val c = MaigTheme.colors

    Row(
        modifier = modifier.horizontalScroll(rememberScrollState()),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MaigTheme.spacing.xs),
    ) {
        items.forEachIndexed { index, label ->
            val isLast = index == items.lastIndex
            BasicText(
                text = label,
                modifier = Modifier
                    .clip(MaigTheme.shapes.xs)
                    .then(
                        if (!isLast) {
                            Modifier.clickable(role = Role.Button) { onItemClick(index) }
                        } else {
                            Modifier
                        },
                    )
                    .padding(horizontal = 6.dp, vertical = 4.dp),
                style = MaigTheme.typography.label.copy(
                    color = if (isLast) c.textPrimary else c.textSecondary,
                ),
            )
            if (!isLast) {
                MaigChevronIcon(
                    direction = ChevronDirection.Right,
                    size = 12.dp,
                    tint = c.textTertiary,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigBreadcrumbLightPreview() {
    MaigTheme(darkTheme = false) {
        Box(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(MaigTheme.spacing.md),
        ) {
            MaigBreadcrumb(items = listOf("Início", "Componentes", "Navegação"), onItemClick = {})
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigBreadcrumbDarkPreview() {
    MaigTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(MaigTheme.spacing.md),
        ) {
            MaigBreadcrumb(items = listOf("App", "Conta", "Segurança"), onItemClick = {})
        }
    }
}
