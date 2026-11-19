package io.github.maignardi.maigui.components.overlays

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import io.github.maignardi.maigui.components.buttons.MaigButton
import io.github.maignardi.maigui.core.theme.MaigTheme

/**
 * Default values for [MaigPopover].
 */
public object MaigPopoverDefaults {
    @Composable
    public fun shape(): Shape = MaigTheme.shapes.lg

    @Composable
    public fun containerColor(): Color = MaigTheme.colors.surfaceElevated
}

/**
 * Anchored floating popover positioned relative to [anchor].
 */
@Composable
public fun MaigPopover(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    anchor: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.BottomStart,
    offset: IntOffset = IntOffset(0, 12),
    shape: Shape = MaigPopoverDefaults.shape(),
    containerColor: Color = MaigPopoverDefaults.containerColor(),
    content: @Composable ColumnScope.() -> Unit,
) {
    Box(modifier = modifier) {
        anchor()
        if (expanded) {
            Popup(
                alignment = alignment,
                offset = offset,
                onDismissRequest = onDismissRequest,
                properties = PopupProperties(focusable = true),
            ) {
                Column(
                    modifier = Modifier
                        .widthIn(min = 180.dp, max = 320.dp)
                        .shadow(MaigTheme.elevation.lg, shape)
                        .clip(shape)
                        .background(containerColor)
                        .border(1.dp, MaigTheme.colors.border, shape)
                        .padding(MaigTheme.spacing.md),
                    content = content,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigPopoverLightPreview() {
    MaigTheme(darkTheme = false) {
        Box(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(MaigTheme.spacing.lg),
        ) {
            MaigPopover(
                expanded = false,
                onDismissRequest = {},
                anchor = { MaigButton(text = "Abrir Popover", onClick = {}) },
            ) {
                BasicText("Conteúdo ancorado", style = MaigTheme.typography.body)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigPopoverDarkPreview() {
    MaigTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(MaigTheme.spacing.lg),
        ) {
            MaigPopover(
                expanded = false,
                onDismissRequest = {},
                anchor = { MaigButton(text = "Detalhes", onClick = {}) },
            ) {
                BasicText("Conteúdo ancorado", style = MaigTheme.typography.body)
            }
        }
    }
}
