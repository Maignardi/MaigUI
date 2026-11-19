package io.github.maignardi.maigui.components.overlays

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import io.github.maignardi.maigui.core.theme.MaigTheme
import kotlinx.coroutines.delay

/**
 * Default values for [MaigTooltip].
 */
public object MaigTooltipDefaults {
    public const val AutoHideDelayMillis: Long = 2200L

    @Composable
    public fun shape(): Shape = MaigTheme.shapes.sm

    @Composable
    public fun containerColor(): Color =
        if (MaigTheme.colors.isDark) MaigTheme.colors.neutral.shade100 else MaigTheme.colors.neutral.shade900

    @Composable
    public fun contentColor(): Color =
        if (MaigTheme.colors.isDark) MaigTheme.colors.neutral.shade900 else Color.White
}

/**
 * Floating tooltip bubble triggered by long-press or controlled manually via [visible].
 */
@Composable
public fun MaigTooltip(
    text: String,
    modifier: Modifier = Modifier,
    visible: Boolean? = null,
    onDismissRequest: (() -> Unit)? = null,
    shape: Shape = MaigTooltipDefaults.shape(),
    containerColor: Color = MaigTooltipDefaults.containerColor(),
    contentColor: Color = MaigTooltipDefaults.contentColor(),
    content: @Composable () -> Unit,
) {
    var internalVisible by remember { mutableStateOf(false) }
    val isVisible = visible ?: internalVisible

    if (visible == null && internalVisible) {
        LaunchedEffect(internalVisible) {
            delay(MaigTooltipDefaults.AutoHideDelayMillis)
            internalVisible = false
        }
    }

    Box(
        modifier = modifier
            .semantics { contentDescription = text }
            .combinedClickable(
                onClick = {},
                onLongClick = {
                    if (visible == null) {
                        internalVisible = true
                    }
                },
            ),
    ) {
        content()
        if (isVisible) {
            Popup(
                alignment = Alignment.TopCenter,
                offset = IntOffset(0, -84),
                onDismissRequest = {
                    internalVisible = false
                    onDismissRequest?.invoke()
                },
                properties = PopupProperties(focusable = false),
            ) {
                MaigTooltipBubble(
                    text = text,
                    shape = shape,
                    containerColor = containerColor,
                    contentColor = contentColor,
                )
            }
        }
    }
}

/**
 * Inline tooltip bubble surface useful for custom positioning and previews.
 */
@Composable
public fun MaigTooltipBubble(
    text: String,
    modifier: Modifier = Modifier,
    shape: Shape = MaigTooltipDefaults.shape(),
    containerColor: Color = MaigTooltipDefaults.containerColor(),
    contentColor: Color = MaigTooltipDefaults.contentColor(),
) {
    Box(
        modifier = modifier
            .shadow(MaigTheme.elevation.md, shape)
            .clip(shape)
            .background(containerColor)
            .border(1.dp, MaigTheme.colors.borderSubtle, shape)
            .padding(horizontal = MaigTheme.spacing.md, vertical = MaigTheme.spacing.xs),
    ) {
        BasicText(
            text = text,
            style = MaigTheme.typography.caption.copy(color = contentColor),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigTooltipLightPreview() {
    MaigTheme(darkTheme = false) {
        Box(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(MaigTheme.spacing.lg),
        ) {
            MaigTooltipBubble(text = "Copiar link para a área de transferência")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigTooltipDarkPreview() {
    MaigTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(MaigTheme.spacing.lg),
        ) {
            MaigTooltipBubble(text = "Atalho: Ctrl + K")
        }
    }
}
