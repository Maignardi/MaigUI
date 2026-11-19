package io.github.maignardi.maigui.components.overlays

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import io.github.maignardi.maigui.core.theme.MaigTheme
import kotlin.math.roundToInt

/**
 * Default values for [MaigBottomSheet].
 */
public object MaigBottomSheetDefaults {
    @Composable
    public fun shape(): Shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)

    @Composable
    public fun containerColor(): Color = MaigTheme.colors.surfaceElevated

    @Composable
    public fun scrimColor(): Color = MaigTheme.colors.scrim
}

/**
 * Modal bottom sheet with scrim fade, slide-up animation, drag handle, and swipe-to-dismiss.
 */
@Composable
public fun MaigBottomSheet(
    visible: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    showDragHandle: Boolean = true,
    dismissOnSwipeDown: Boolean = true,
    shape: Shape = MaigBottomSheetDefaults.shape(),
    containerColor: Color = MaigBottomSheetDefaults.containerColor(),
    scrimColor: Color = MaigBottomSheetDefaults.scrimColor(),
    content: @Composable ColumnScope.() -> Unit,
) {
    if (!visible) return

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
        ),
    ) {
        var dragOffsetY by remember { mutableFloatStateOf(0f) }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(scrimColor)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onDismissRequest,
                ),
            contentAlignment = Alignment.BottomCenter,
        ) {
            AnimatedVisibility(
                visible = true,
                enter = fadeIn() + slideInVertically { it },
                exit = fadeOut() + slideOutVertically { it },
            ) {
                MaigBottomSheetSurface(
                    modifier = modifier
                        .offset { IntOffset(0, dragOffsetY.roundToInt().coerceAtLeast(0)) }
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = {},
                        )
                        .then(
                            if (dismissOnSwipeDown) {
                                Modifier.pointerInput(Unit) {
                                    detectVerticalDragGestures(
                                        onDragEnd = {
                                            if (dragOffsetY > 160f) {
                                                onDismissRequest()
                                            }
                                            dragOffsetY = 0f
                                        },
                                        onDragCancel = { dragOffsetY = 0f },
                                    ) { _, dragAmount ->
                                        dragOffsetY = (dragOffsetY + dragAmount).coerceAtLeast(0f)
                                    }
                                }
                            } else {
                                Modifier
                            },
                        ),
                    title = title,
                    showDragHandle = showDragHandle,
                    shape = shape,
                    containerColor = containerColor,
                    content = content,
                )
            }
        }
    }
}

/**
 * Reusable bottom sheet container surface useful both modally and in previews.
 */
@Composable
public fun MaigBottomSheetSurface(
    modifier: Modifier = Modifier,
    title: String? = null,
    showDragHandle: Boolean = true,
    shape: Shape = MaigBottomSheetDefaults.shape(),
    containerColor: Color = MaigBottomSheetDefaults.containerColor(),
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(containerColor)
            .border(1.dp, MaigTheme.colors.border, shape)
            .navigationBarsPadding()
            .padding(horizontal = MaigTheme.spacing.lg, vertical = MaigTheme.spacing.md),
        verticalArrangement = Arrangement.spacedBy(MaigTheme.spacing.md),
    ) {
        if (showDragHandle) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(40.dp)
                    .height(4.dp)
                    .clip(CircleShape)
                    .background(MaigTheme.colors.borderStrong),
            )
        }
        if (title != null) {
            BasicText(
                text = title,
                style = MaigTheme.typography.title.copy(color = MaigTheme.colors.textPrimary),
            )
        }
        content()
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigBottomSheetLightPreview() {
    MaigTheme(darkTheme = false) {
        Box(
            modifier = Modifier
                .background(MaigTheme.colors.backgroundSubtle)
                .padding(top = MaigTheme.spacing.xl),
        ) {
            MaigBottomSheetSurface(title = "Opções da conta") {
                BasicText("Editar perfil", style = MaigTheme.typography.body.copy(color = MaigTheme.colors.textPrimary))
                BasicText("Alterar senha", style = MaigTheme.typography.body.copy(color = MaigTheme.colors.textPrimary))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigBottomSheetDarkPreview() {
    MaigTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(top = MaigTheme.spacing.xl),
        ) {
            MaigBottomSheetSurface(title = "Filtros") {
                BasicText("Mais recentes", style = MaigTheme.typography.body.copy(color = MaigTheme.colors.textPrimary))
            }
        }
    }
}
