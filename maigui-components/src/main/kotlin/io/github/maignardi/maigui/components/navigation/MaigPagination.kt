package io.github.maignardi.maigui.components.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.maignardi.maigui.components.internal.ChevronDirection
import io.github.maignardi.maigui.components.internal.MaigChevronIcon
import io.github.maignardi.maigui.components.internal.maigTouchTarget
import io.github.maignardi.maigui.core.theme.MaigTheme

/**
 * Defaults for [MaigPagination].
 */
public object MaigPaginationDefaults {
    public const val DefaultMaxVisiblePages: Int = 5
}

/**
 * Page navigation control with previous/next chevrons, numbered pills, and ellipsis truncation.
 */
@Composable
public fun MaigPagination(
    currentPage: Int,
    totalPages: Int,
    onPageChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    maxVisiblePages: Int = MaigPaginationDefaults.DefaultMaxVisiblePages,
    enabled: Boolean = true,
) {
    if (totalPages <= 0) return
    val safeCurrent = currentPage.coerceIn(1, totalPages)
    val items = computePaginationItems(safeCurrent, totalPages, maxVisiblePages.coerceAtLeast(3))
    val c = MaigTheme.colors

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(MaigTheme.spacing.xs),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        PaginationArrowButton(
            direction = ChevronDirection.Left,
            contentDescription = "Página anterior",
            enabled = enabled && safeCurrent > 1,
            onClick = { onPageChange(safeCurrent - 1) },
        )

        items.forEach { item ->
            when (item) {
                is PaginationEntry.Page -> {
                    val isSelected = item.number == safeCurrent
                    val bg = if (isSelected) c.primaryDefault else c.surface
                    val fg = when {
                        !enabled -> c.textDisabled
                        isSelected -> c.onPrimary
                        else -> c.textPrimary
                    }
                    val border = if (isSelected) c.primaryDefault else c.border
                    Box(
                        modifier = Modifier
                            .maigTouchTarget(minWidth = 40.dp, minHeight = 40.dp)
                            .clip(MaigTheme.shapes.md)
                            .background(bg)
                            .border(1.dp, border, MaigTheme.shapes.md)
                            .clickable(
                                enabled = enabled && !isSelected,
                                role = Role.Button,
                                onClick = { onPageChange(item.number) },
                            )
                            .semantics { selected = isSelected },
                        contentAlignment = Alignment.Center,
                    ) {
                        BasicText(
                            text = item.number.toString(),
                            style = MaigTheme.typography.label.copy(color = fg),
                        )
                    }
                }

                PaginationEntry.Ellipsis -> {
                    Box(
                        modifier = Modifier.size(width = 24.dp, height = 40.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        BasicText(
                            text = "...",
                            style = MaigTheme.typography.label.copy(color = c.textSecondary),
                        )
                    }
                }
            }
        }

        PaginationArrowButton(
            direction = ChevronDirection.Right,
            contentDescription = "Próxima página",
            enabled = enabled && safeCurrent < totalPages,
            onClick = { onPageChange(safeCurrent + 1) },
        )
    }
}

@Composable
private fun PaginationArrowButton(
    direction: ChevronDirection,
    contentDescription: String,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    val c = MaigTheme.colors
    Box(
        modifier = Modifier
            .maigTouchTarget(minWidth = 40.dp, minHeight = 40.dp)
            .clip(MaigTheme.shapes.md)
            .background(if (enabled) c.surface else c.disabled)
            .border(1.dp, if (enabled) c.border else c.borderSubtle, MaigTheme.shapes.md)
            .clickable(enabled = enabled, role = Role.Button, onClick = onClick)
            .semantics { this.contentDescription = contentDescription },
        contentAlignment = Alignment.Center,
    ) {
        MaigChevronIcon(
            direction = direction,
            tint = if (enabled) c.textPrimary else c.textDisabled,
        )
    }
}

private sealed interface PaginationEntry {
    data class Page(val number: Int) : PaginationEntry
    data object Ellipsis : PaginationEntry
}

private fun computePaginationItems(
    current: Int,
    total: Int,
    maxVisible: Int,
): List<PaginationEntry> {
    if (total <= maxVisible) {
        return (1..total).map { PaginationEntry.Page(it) }
    }
    val half = (maxVisible - 2) / 2
    val start = (current - half).coerceIn(2, (total - maxVisible + 2).coerceAtLeast(2))
    val end = (start + maxVisible - 3).coerceAtMost(total - 1)

    return buildList {
        add(PaginationEntry.Page(1))
        if (start > 2) add(PaginationEntry.Ellipsis)
        for (p in start..end) {
            add(PaginationEntry.Page(p))
        }
        if (end < total - 1) add(PaginationEntry.Ellipsis)
        add(PaginationEntry.Page(total))
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigPaginationLightPreview() {
    MaigTheme(darkTheme = false) {
        Box(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(MaigTheme.spacing.md),
        ) {
            MaigPagination(currentPage = 4, totalPages = 12, onPageChange = {})
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigPaginationDarkPreview() {
    MaigTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(MaigTheme.spacing.md),
        ) {
            MaigPagination(currentPage = 1, totalPages = 5, onPageChange = {})
        }
    }
}
