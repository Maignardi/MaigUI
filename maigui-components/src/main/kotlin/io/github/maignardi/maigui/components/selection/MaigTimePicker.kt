package io.github.maignardi.maigui.components.selection

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import io.github.maignardi.maigui.components.buttons.MaigButton
import io.github.maignardi.maigui.components.buttons.MaigButtonSize
import io.github.maignardi.maigui.components.buttons.MaigButtonVariant
import io.github.maignardi.maigui.components.buttons.MaigIconButton
import io.github.maignardi.maigui.components.internal.ChevronDirection
import io.github.maignardi.maigui.components.internal.MaigChevronIcon
import io.github.maignardi.maigui.core.theme.MaigTheme
import java.util.Locale

/**
 * Immutable 24-hour time value (`hour` in `0..23`, `minute` in `0..59`).
 */
@Immutable
public class MaigTime(
    hour: Int,
    minute: Int,
) {
    public val hour: Int = hour.coerceIn(0, 23)
    public val minute: Int = minute.coerceIn(0, 59)

    /** Formats as `HH:mm`. */
    public fun format24h(): String = String.format(Locale.US, "%02d:%02d", hour, minute)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is MaigTime) return false
        return hour == other.hour && minute == other.minute
    }

    override fun hashCode(): Int = 31 * hour + minute

    override fun toString(): String = format24h()
}

/**
 * Default values for [MaigTimePicker].
 */
public object MaigTimePickerDefaults {
    public val QuickPresets: List<MaigTime> = listOf(
        MaigTime(9, 0),
        MaigTime(12, 0),
        MaigTime(14, 30),
        MaigTime(18, 0),
    )
}

/**
 * Interactive 24-hour/12-hour time picker with accessible hour/minute steppers and quick presets.
 */
@Composable
public fun MaigTimePicker(
    selectedTime: MaigTime,
    onTimeSelected: (MaigTime) -> Unit,
    modifier: Modifier = Modifier,
    minuteStep: Int = 1,
    showQuickPresets: Boolean = true,
) {
    val colors = MaigTheme.colors
    val typography = MaigTheme.typography
    val spacing = MaigTheme.spacing

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(MaigTheme.shapes.lg)
            .background(colors.surface)
            .border(1.dp, colors.border, MaigTheme.shapes.lg)
            .padding(spacing.lg),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(spacing.md),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(spacing.md),
        ) {
            TimeSegmentColumn(
                label = "Hora",
                value = String.format(Locale.US, "%02d", selectedTime.hour),
                onIncrement = {
                    onTimeSelected(MaigTime((selectedTime.hour + 1) % 24, selectedTime.minute))
                },
                onDecrement = {
                    onTimeSelected(MaigTime((selectedTime.hour + 23) % 24, selectedTime.minute))
                },
            )

            BasicText(
                text = ":",
                style = typography.displayMedium.copy(color = colors.textPrimary),
                modifier = Modifier.padding(top = spacing.md),
            )

            TimeSegmentColumn(
                label = "Minuto",
                value = String.format(Locale.US, "%02d", selectedTime.minute),
                onIncrement = {
                    onTimeSelected(MaigTime(selectedTime.hour, (selectedTime.minute + minuteStep) % 60))
                },
                onDecrement = {
                    onTimeSelected(MaigTime(selectedTime.hour, (selectedTime.minute + 60 - minuteStep) % 60))
                },
            )
        }

        if (showQuickPresets) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(spacing.xs),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                MaigTimePickerDefaults.QuickPresets.forEach { preset ->
                    MaigChip(
                        label = preset.format24h(),
                        selected = preset == selectedTime,
                        onClick = { onTimeSelected(preset) },
                        variant = MaigChipVariant.Choice,
                    )
                }
            }
        }
    }
}

@Composable
private fun TimeSegmentColumn(
    label: String,
    value: String,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
) {
    val colors = MaigTheme.colors
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MaigTheme.spacing.xs),
    ) {
        BasicText(
            text = label,
            style = MaigTheme.typography.caption.copy(color = colors.textSecondary),
        )
        MaigIconButton(
            onClick = onIncrement,
            contentDescription = "Aumentar $label",
            variant = MaigButtonVariant.Soft,
            size = MaigButtonSize.Small,
        ) {
            MaigChevronIcon(direction = ChevronDirection.Up)
        }
        Box(
            modifier = Modifier
                .width(72.dp)
                .clip(MaigTheme.shapes.md)
                .background(colors.primarySubtle)
                .padding(vertical = MaigTheme.spacing.md),
            contentAlignment = Alignment.Center,
        ) {
            BasicText(
                text = value,
                style = MaigTheme.typography.displayMedium.copy(
                    color = colors.onPrimarySubtle,
                    textAlign = TextAlign.Center,
                ),
            )
        }
        MaigIconButton(
            onClick = onDecrement,
            contentDescription = "Diminuir $label",
            variant = MaigButtonVariant.Soft,
            size = MaigButtonSize.Small,
        ) {
            MaigChevronIcon(direction = ChevronDirection.Down)
        }
    }
}

/**
 * Modal dialog wrapper around [MaigTimePicker].
 */
@Composable
public fun MaigTimePickerDialog(
    visible: Boolean,
    initialTime: MaigTime,
    onConfirm: (MaigTime) -> Unit,
    onDismissRequest: () -> Unit,
    title: String = "Selecionar horário",
) {
    if (!visible) return
    var pendingTime by remember(initialTime) { mutableStateOf(initialTime) }

    Dialog(onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaigTheme.shapes.xl)
                .background(MaigTheme.colors.surfaceElevated)
                .border(1.dp, MaigTheme.colors.border, MaigTheme.shapes.xl)
                .padding(MaigTheme.spacing.lg),
            verticalArrangement = Arrangement.spacedBy(MaigTheme.spacing.md),
        ) {
            BasicText(
                text = title,
                style = MaigTheme.typography.title.copy(color = MaigTheme.colors.textPrimary),
            )
            MaigTimePicker(
                selectedTime = pendingTime,
                onTimeSelected = { pendingTime = it },
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                MaigButton(
                    text = "Cancelar",
                    onClick = onDismissRequest,
                    variant = MaigButtonVariant.Ghost,
                    size = MaigButtonSize.Small,
                )
                MaigButton(
                    text = "Confirmar",
                    onClick = { onConfirm(pendingTime) },
                    size = MaigButtonSize.Small,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigTimePickerLightPreview() {
    MaigTheme(darkTheme = false) {
        Box(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(MaigTheme.spacing.md),
        ) {
            MaigTimePicker(selectedTime = MaigTime(14, 30), onTimeSelected = {})
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigTimePickerDarkPreview() {
    MaigTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(MaigTheme.spacing.md),
        ) {
            MaigTimePicker(selectedTime = MaigTime(9, 0), onTimeSelected = {})
        }
    }
}
