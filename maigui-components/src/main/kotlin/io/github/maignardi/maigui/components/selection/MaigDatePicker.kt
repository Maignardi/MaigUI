package io.github.maignardi.maigui.components.selection

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.graphics.Color
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
import java.util.Calendar
import java.util.Locale

/**
 * Simple API-23 compatible immutable calendar date representation.
 */
@Immutable
public class MaigDate(
    public val year: Int,
    public val month: Int,
    public val dayOfMonth: Int,
) : Comparable<MaigDate> {
    /** Formats as `DD/MM/YYYY`. */
    public fun formatPtBr(): String = String.format(Locale.US, "%02d/%02d/%04d", dayOfMonth, month, year)

    override fun compareTo(other: MaigDate): Int {
        if (year != other.year) return year.compareTo(other.year)
        if (month != other.month) return month.compareTo(other.month)
        return dayOfMonth.compareTo(other.dayOfMonth)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is MaigDate) return false
        return year == other.year && month == other.month && dayOfMonth == other.dayOfMonth
    }

    override fun hashCode(): Int = (year * 31 + month) * 31 + dayOfMonth

    override fun toString(): String = formatPtBr()

    public companion object {
        /** Returns today's local [MaigDate]. */
        public fun today(): MaigDate {
            val cal = Calendar.getInstance()
            return MaigDate(
                year = cal.get(Calendar.YEAR),
                month = cal.get(Calendar.MONTH) + 1,
                dayOfMonth = cal.get(Calendar.DAY_OF_MONTH),
            )
        }
    }
}

/**
 * Defaults and calendar helpers for [MaigDatePicker].
 */
public object MaigDatePickerDefaults {
    internal val monthNamesPtBr = listOf(
        "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho",
        "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro",
    )
    internal val weekdayLabelsPtBr = listOf("D", "S", "T", "Q", "Q", "S", "S")
}

/**
 * Interactive calendar month grid date picker.
 */
@Composable
public fun MaigDatePicker(
    selectedDate: MaigDate?,
    onDateSelected: (MaigDate) -> Unit,
    modifier: Modifier = Modifier,
    minDate: MaigDate? = null,
    maxDate: MaigDate? = null,
) {
    val today = remember { MaigDate.today() }
    val initial = selectedDate ?: today
    var displayYear by remember { mutableIntStateOf(initial.year) }
    var displayMonth by remember { mutableIntStateOf(initial.month) }

    val daysInMonth = remember(displayYear, displayMonth) {
        val cal = Calendar.getInstance().apply {
            clear()
            set(displayYear, displayMonth - 1, 1)
        }
        cal.getActualMaximum(Calendar.DAY_OF_MONTH)
    }
    val firstWeekdayOffset = remember(displayYear, displayMonth) {
        val cal = Calendar.getInstance().apply {
            clear()
            set(displayYear, displayMonth - 1, 1)
        }
        cal.get(Calendar.DAY_OF_WEEK) - 1 // Sunday = 0
    }

    val colors = MaigTheme.colors
    val typography = MaigTheme.typography
    val spacing = MaigTheme.spacing

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(MaigTheme.shapes.lg)
            .background(colors.surface)
            .border(1.dp, colors.border, MaigTheme.shapes.lg)
            .padding(spacing.md),
        verticalArrangement = Arrangement.spacedBy(spacing.sm),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            MaigIconButton(
                onClick = {
                    if (displayMonth == 1) {
                        displayMonth = 12
                        displayYear -= 1
                    } else {
                        displayMonth -= 1
                    }
                },
                contentDescription = "Mês anterior",
                size = MaigButtonSize.Small,
            ) {
                MaigChevronIcon(direction = ChevronDirection.Left)
            }

            BasicText(
                text = "${MaigDatePickerDefaults.monthNamesPtBr[displayMonth - 1]} $displayYear",
                style = typography.title.copy(color = colors.textPrimary),
            )

            MaigIconButton(
                onClick = {
                    if (displayMonth == 12) {
                        displayMonth = 1
                        displayYear += 1
                    } else {
                        displayMonth += 1
                    }
                },
                contentDescription = "Próximo mês",
                size = MaigButtonSize.Small,
            ) {
                MaigChevronIcon(direction = ChevronDirection.Right)
            }
        }

        Row(modifier = Modifier.fillMaxWidth()) {
            MaigDatePickerDefaults.weekdayLabelsPtBr.forEach { dayLabel ->
                BasicText(
                    text = dayLabel,
                    modifier = Modifier.weight(1f),
                    style = typography.caption.copy(
                        color = colors.textSecondary,
                        textAlign = TextAlign.Center,
                    ),
                )
            }
        }

        val totalCells = firstWeekdayOffset + daysInMonth
        val weeks = (totalCells + 6) / 7

        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            for (week in 0 until weeks) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    for (dayOfWeek in 0 until 7) {
                        val cellIndex = week * 7 + dayOfWeek
                        val dayNum = cellIndex - firstWeekdayOffset + 1
                        if (dayNum in 1..daysInMonth) {
                            val cellDate = MaigDate(displayYear, displayMonth, dayNum)
                            val isSelected = cellDate == selectedDate
                            val isToday = cellDate == today
                            val isInRange = (minDate == null || cellDate >= minDate) &&
                                (maxDate == null || cellDate <= maxDate)

                            val bg = if (isSelected) colors.primaryDefault else Color.Transparent
                            val fg = when {
                                !isInRange -> colors.textDisabled
                                isSelected -> colors.onPrimary
                                isToday -> colors.primaryDefault
                                else -> colors.textPrimary
                            }
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .clip(CircleShape)
                                    .background(bg)
                                    .then(
                                        if (isToday && !isSelected) {
                                            Modifier.border(1.dp, colors.primaryDefault, CircleShape)
                                        } else {
                                            Modifier
                                        },
                                    )
                                    .clickable(enabled = isInRange) { onDateSelected(cellDate) },
                                contentAlignment = Alignment.Center,
                            ) {
                                BasicText(
                                    text = dayNum.toString(),
                                    style = typography.label.copy(color = fg),
                                )
                            }
                        } else {
                            Spacer(modifier = Modifier.weight(1f).aspectRatio(1f))
                        }
                    }
                }
            }
        }
    }
}

/**
 * Modal dialog wrapper around [MaigDatePicker] with confirm/cancel actions.
 */
@Composable
public fun MaigDatePickerDialog(
    visible: Boolean,
    initialDate: MaigDate?,
    onConfirm: (MaigDate) -> Unit,
    onDismissRequest: () -> Unit,
    title: String = "Selecionar data",
    minDate: MaigDate? = null,
    maxDate: MaigDate? = null,
) {
    if (!visible) return
    var pendingDate by remember(initialDate) { mutableStateOf(initialDate ?: MaigDate.today()) }

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
            MaigDatePicker(
                selectedDate = pendingDate,
                onDateSelected = { pendingDate = it },
                minDate = minDate,
                maxDate = maxDate,
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
                    onClick = { onConfirm(pendingDate) },
                    size = MaigButtonSize.Small,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigDatePickerLightPreview() {
    MaigTheme(darkTheme = false) {
        Box(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(MaigTheme.spacing.md),
        ) {
            MaigDatePicker(
                selectedDate = MaigDate(2026, 11, 19),
                onDateSelected = {},
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigDatePickerDarkPreview() {
    MaigTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(MaigTheme.spacing.md),
        ) {
            MaigDatePicker(
                selectedDate = MaigDate(2026, 11, 19),
                onDateSelected = {},
            )
        }
    }
}
