package io.github.maignardi.maigui.components.selection

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.error
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import io.github.maignardi.maigui.components.inputs.MaigSearchField
import io.github.maignardi.maigui.components.internal.ChevronDirection
import io.github.maignardi.maigui.components.internal.MaigCheckIcon
import io.github.maignardi.maigui.components.internal.MaigChevronIcon
import io.github.maignardi.maigui.components.internal.rememberMaigRipple
import io.github.maignardi.maigui.core.theme.MaigTheme

/**
 * Color tokens for [MaigSelect].
 */
@Immutable
public class MaigSelectColors(
    public val containerColor: Color,
    public val disabledContainerColor: Color,
    public val menuContainerColor: Color,
    public val textColor: Color,
    public val placeholderColor: Color,
    public val borderColor: Color,
    public val focusedBorderColor: Color,
    public val errorBorderColor: Color,
    public val selectedItemContainerColor: Color,
    public val selectedItemTextColor: Color,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is MaigSelectColors) return false
        return containerColor == other.containerColor &&
            disabledContainerColor == other.disabledContainerColor &&
            menuContainerColor == other.menuContainerColor &&
            textColor == other.textColor &&
            placeholderColor == other.placeholderColor &&
            borderColor == other.borderColor &&
            focusedBorderColor == other.focusedBorderColor &&
            errorBorderColor == other.errorBorderColor &&
            selectedItemContainerColor == other.selectedItemContainerColor &&
            selectedItemTextColor == other.selectedItemTextColor
    }

    override fun hashCode(): Int {
        var result = containerColor.hashCode()
        result = 31 * result + disabledContainerColor.hashCode()
        result = 31 * result + menuContainerColor.hashCode()
        result = 31 * result + textColor.hashCode()
        result = 31 * result + placeholderColor.hashCode()
        result = 31 * result + borderColor.hashCode()
        result = 31 * result + focusedBorderColor.hashCode()
        result = 31 * result + errorBorderColor.hashCode()
        result = 31 * result + selectedItemContainerColor.hashCode()
        result = 31 * result + selectedItemTextColor.hashCode()
        return result
    }
}

/**
 * Default values for [MaigSelect].
 */
public object MaigSelectDefaults {
    @Composable
    public fun shape(): Shape = MaigTheme.shapes.md

    @Composable
    public fun colors(): MaigSelectColors {
        val c = MaigTheme.colors
        return MaigSelectColors(
            containerColor = c.surface,
            disabledContainerColor = c.disabled,
            menuContainerColor = c.surfaceElevated,
            textColor = c.textPrimary,
            placeholderColor = c.textTertiary,
            borderColor = c.border,
            focusedBorderColor = c.borderFocus,
            errorBorderColor = c.dangerDefault,
            selectedItemContainerColor = c.primarySubtle,
            selectedItemTextColor = c.onPrimarySubtle,
        )
    }
}

/**
 * Dropdown select component with optional live search filter inside the anchored popup.
 */
@Composable
public fun <T> MaigSelect(
    items: List<T>,
    selectedItem: T?,
    onItemSelected: (T) -> Unit,
    itemLabel: (T) -> String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: String? = null,
    placeholder: String = "Selecionar...",
    helperText: String? = null,
    errorText: String? = null,
    isError: Boolean = errorText != null,
    searchable: Boolean = false,
    searchPlaceholder: String = "Buscar opção...",
    emptyResultsText: String = "Nenhum resultado encontrado",
    shape: Shape = MaigSelectDefaults.shape(),
    colors: MaigSelectColors = MaigSelectDefaults.colors(),
    interactionSource: MutableInteractionSource? = null,
) {
    val resolvedInteraction = interactionSource ?: remember { MutableInteractionSource() }
    var expanded by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var anchorWidthPx by remember { mutableIntStateOf(0) }
    var anchorHeightPx by remember { mutableIntStateOf(0) }
    val density = LocalDensity.current

    val borderColor = when {
        !enabled -> MaigTheme.colors.borderSubtle
        isError -> colors.errorBorderColor
        expanded -> colors.focusedBorderColor
        else -> colors.borderColor
    }

    val filteredItems = remember(items, searchQuery, searchable) {
        if (!searchable || searchQuery.isBlank()) {
            items
        } else {
            val q = searchQuery.trim().lowercase()
            items.filter { itemLabel(it).lowercase().contains(q) }
        }
    }

    Column(
        modifier = modifier.semantics {
            if (isError) {
                error(errorText ?: "Seleção obrigatória")
            }
        },
        verticalArrangement = Arrangement.spacedBy(MaigTheme.spacing.xs),
    ) {
        if (label != null) {
            BasicText(
                text = label,
                style = MaigTheme.typography.label.copy(
                    color = if (isError) colors.errorBorderColor else MaigTheme.colors.textPrimary,
                ),
            )
        }

        Box(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 48.dp)
                    .onSizeChanged {
                        anchorWidthPx = it.width
                        anchorHeightPx = it.height
                    }
                    .clip(shape)
                    .background(if (enabled) colors.containerColor else colors.disabledContainerColor)
                    .border(if (expanded || isError) 1.5.dp else 1.dp, borderColor, shape)
                    .clickable(
                        interactionSource = resolvedInteraction,
                        indication = rememberMaigRipple(bounded = true),
                        enabled = enabled,
                        role = Role.DropdownList,
                        onClick = { expanded = !expanded },
                    )
                    .padding(horizontal = MaigTheme.spacing.md, vertical = MaigTheme.spacing.sm),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                BasicText(
                    text = selectedItem?.let(itemLabel) ?: placeholder,
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaigTheme.typography.body.copy(
                        color = when {
                            !enabled -> MaigTheme.colors.textDisabled
                            selectedItem != null -> colors.textColor
                            else -> colors.placeholderColor
                        },
                    ),
                )
                MaigChevronIcon(
                    direction = if (expanded) ChevronDirection.Up else ChevronDirection.Down,
                    tint = if (enabled) MaigTheme.colors.textSecondary else MaigTheme.colors.textDisabled,
                )
            }

            if (expanded) {
                val widthDp = with(density) { anchorWidthPx.toDp() }
                val gapPx = with(density) { 4.dp.roundToPx() }
                Popup(
                    alignment = Alignment.TopStart,
                    offset = IntOffset(0, anchorHeightPx + gapPx),
                    onDismissRequest = {
                        expanded = false
                        searchQuery = ""
                    },
                    properties = PopupProperties(focusable = true),
                ) {
                    Column(
                        modifier = Modifier
                            .then(if (anchorWidthPx > 0) Modifier.width(widthDp) else Modifier.fillMaxWidth())
                            .shadow(MaigTheme.elevation.lg, shape)
                            .clip(shape)
                            .background(colors.menuContainerColor)
                            .border(1.dp, MaigTheme.colors.border, shape)
                            .padding(vertical = MaigTheme.spacing.xs),
                    ) {
                        if (searchable) {
                            Box(modifier = Modifier.padding(horizontal = MaigTheme.spacing.sm, vertical = MaigTheme.spacing.xs)) {
                                MaigSearchField(
                                    value = searchQuery,
                                    onValueChange = { searchQuery = it },
                                    placeholder = searchPlaceholder,
                                )
                            }
                        }

                        if (filteredItems.isEmpty()) {
                            BasicText(
                                text = emptyResultsText,
                                modifier = Modifier.padding(MaigTheme.spacing.md),
                                style = MaigTheme.typography.bodySmall.copy(color = MaigTheme.colors.textSecondary),
                            )
                        } else {
                            LazyColumn(
                                modifier = Modifier.heightIn(max = 260.dp),
                            ) {
                                items(filteredItems) { item ->
                                    val isSelected = item == selectedItem
                                    val rowBg = if (isSelected) colors.selectedItemContainerColor else Color.Transparent
                                    val rowFg = if (isSelected) colors.selectedItemTextColor else colors.textColor
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .defaultMinSize(minHeight = 44.dp)
                                            .background(rowBg)
                                            .clickable {
                                                onItemSelected(item)
                                                expanded = false
                                                searchQuery = ""
                                            }
                                            .padding(horizontal = MaigTheme.spacing.md, vertical = MaigTheme.spacing.sm),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                    ) {
                                        BasicText(
                                            text = itemLabel(item),
                                            modifier = Modifier.weight(1f),
                                            style = MaigTheme.typography.body.copy(color = rowFg),
                                        )
                                        if (isSelected) {
                                            MaigCheckIcon(size = 16.dp, tint = rowFg)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        val bottomMessage = if (isError && errorText != null) errorText else helperText
        if (bottomMessage != null) {
            BasicText(
                text = bottomMessage,
                style = MaigTheme.typography.caption.copy(
                    color = if (isError) colors.errorBorderColor else MaigTheme.colors.textSecondary,
                ),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigSelectLightPreview() {
    MaigTheme(darkTheme = false) {
        Box(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(MaigTheme.spacing.lg),
        ) {
            MaigSelect(
                items = listOf("São Paulo", "Rio de Janeiro", "Curitiba"),
                selectedItem = "São Paulo",
                onItemSelected = {},
                itemLabel = { it },
                label = "Estado",
                searchable = true,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigSelectDarkPreview() {
    MaigTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(MaigTheme.spacing.lg),
        ) {
            MaigSelect(
                items = listOf("Básico", "Pro"),
                selectedItem = null,
                onItemSelected = {},
                itemLabel = { it },
                label = "Plano",
                errorText = "Selecione um plano",
            )
        }
    }
}
