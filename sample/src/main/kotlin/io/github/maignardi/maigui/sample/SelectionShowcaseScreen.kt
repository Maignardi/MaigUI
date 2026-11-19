package io.github.maignardi.maigui.sample

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.state.ToggleableState
import io.github.maignardi.maigui.components.buttons.MaigButton
import io.github.maignardi.maigui.components.buttons.MaigButtonSize
import io.github.maignardi.maigui.components.buttons.MaigButtonVariant
import io.github.maignardi.maigui.components.layout.MaigSection
import io.github.maignardi.maigui.components.selection.MaigCheckbox
import io.github.maignardi.maigui.components.selection.MaigChip
import io.github.maignardi.maigui.components.selection.MaigChipVariant
import io.github.maignardi.maigui.components.selection.MaigDate
import io.github.maignardi.maigui.components.selection.MaigDatePicker
import io.github.maignardi.maigui.components.selection.MaigDatePickerDialog
import io.github.maignardi.maigui.components.selection.MaigRadioGroup
import io.github.maignardi.maigui.components.selection.MaigRangeSlider
import io.github.maignardi.maigui.components.selection.MaigRating
import io.github.maignardi.maigui.components.selection.MaigSelect
import io.github.maignardi.maigui.components.selection.MaigSlider
import io.github.maignardi.maigui.components.selection.MaigSwitch
import io.github.maignardi.maigui.components.selection.MaigTime
import io.github.maignardi.maigui.components.selection.MaigTimePicker
import io.github.maignardi.maigui.components.selection.MaigTimePickerDialog
import io.github.maignardi.maigui.components.selection.MaigTriStateCheckbox
import io.github.maignardi.maigui.core.theme.MaigTheme
import kotlin.math.roundToInt

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun SelectionShowcaseScreen() {
    var optA by remember { mutableStateOf(true) }
    var optB by remember { mutableStateOf(false) }
    val parentState = when {
        optA && optB -> ToggleableState.On
        !optA && !optB -> ToggleableState.Off
        else -> ToggleableState.Indeterminate
    }

    var paymentMethod by remember { mutableStateOf("Pix") }
    var darkAlerts by remember { mutableStateOf(true) }
    val filterTags = remember { mutableStateListOf("Kotlin", "Compose", "Android", "Design System") }
    var selectedChoice by remember { mutableStateOf("Compose") }

    val cities = remember {
        listOf("São Paulo", "Rio de Janeiro", "Belo Horizonte", "Curitiba", "Florianópolis", "Porto Alegre", "Recife")
    }
    var selectedCity by remember { mutableStateOf<String?>("São Paulo") }

    var sliderVal by remember { mutableFloatStateOf(0.55f) }
    var rangeVal by remember { mutableStateOf(150f..650f) }
    var ratingVal by remember { mutableStateOf(4) }

    var selectedDate by remember { mutableStateOf(MaigDate.today()) }
    var showDateDialog by remember { mutableStateOf(false) }
    var selectedTime by remember { mutableStateOf(MaigTime(14, 30)) }
    var showTimeDialog by remember { mutableStateOf(false) }

    MaigSection(title = "Checkboxes (com estado indeterminado) e Switch") {
        MaigTriStateCheckbox(
            state = parentState,
            onClick = {
                val next = parentState != ToggleableState.On
                optA = next
                optB = next
            },
            label = "Selecionar todos",
        )
        MaigCheckbox(checked = optA, onCheckedChange = { optA = it }, label = "Notificações por e-mail")
        MaigCheckbox(checked = optB, onCheckedChange = { optB = it }, label = "Notificações push")
        MaigSwitch(checked = darkAlerts, onCheckedChange = { darkAlerts = it }, label = "Alertas em tempo real")
    }

    MaigSection(title = "Radio Group") {
        MaigRadioGroup(
            options = listOf("Pix", "Cartão de Crédito", "Boleto"),
            selectedOption = paymentMethod,
            onOptionSelected = { paymentMethod = it },
            optionLabel = { it },
        )
    }

    MaigSection(title = "Chips (Filter, Choice, Input removível)") {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(MaigTheme.spacing.sm),
            verticalArrangement = Arrangement.spacedBy(MaigTheme.spacing.xs),
        ) {
            MaigChip(
                label = "Compose",
                selected = selectedChoice == "Compose",
                onClick = { selectedChoice = "Compose" },
                variant = MaigChipVariant.Choice,
            )
            MaigChip(
                label = "KMP",
                selected = selectedChoice == "KMP",
                onClick = { selectedChoice = "KMP" },
                variant = MaigChipVariant.Choice,
            )
            filterTags.forEach { tag ->
                MaigChip(
                    label = tag,
                    selected = true,
                    onClick = {},
                    variant = MaigChipVariant.Input,
                    onRemove = { filterTags.remove(tag) },
                )
            }
        }
    }

    MaigSection(title = "Select com Busca") {
        MaigSelect(
            items = cities,
            selectedItem = selectedCity,
            onItemSelected = { selectedCity = it },
            itemLabel = { it },
            label = "Cidade",
            searchable = true,
            modifier = Modifier.fillMaxWidth(),
        )
    }

    MaigSection(title = "Sliders") {
        MaigSlider(
            value = sliderVal,
            onValueChange = { sliderVal = it },
            label = "Intensidade",
            valueFormatter = { "${(it * 100).roundToInt()}%" },
        )
        MaigRangeSlider(
            value = rangeVal,
            onValueChange = { rangeVal = it },
            valueRange = 0f..1000f,
            label = "Orçamento",
            rangeFormatter = { "R$ ${it.start.roundToInt()} - R$ ${it.endInclusive.roundToInt()}" },
        )
        MaigRating(
            rating = ratingVal,
            onRatingChange = { ratingVal = it },
        )
    }

    MaigSection(title = "Date & Time Pickers") {
        Row(horizontalArrangement = Arrangement.spacedBy(MaigTheme.spacing.sm)) {
            MaigButton(
                text = "Data: ${selectedDate.formatPtBr()}",
                onClick = { showDateDialog = true },
                variant = MaigButtonVariant.Outline,
                size = MaigButtonSize.Small,
            )
            MaigButton(
                text = "Hora: ${selectedTime.format24h()}",
                onClick = { showTimeDialog = true },
                variant = MaigButtonVariant.Outline,
                size = MaigButtonSize.Small,
            )
        }
        MaigDatePicker(selectedDate = selectedDate, onDateSelected = { selectedDate = it })
        MaigTimePicker(selectedTime = selectedTime, onTimeSelected = { selectedTime = it })
        BasicText(
            text = "Agendado: ${selectedDate.formatPtBr()} às ${selectedTime.format24h()}",
            style = MaigTheme.typography.caption.copy(color = MaigTheme.colors.textSecondary),
        )
    }

    MaigDatePickerDialog(
        visible = showDateDialog,
        initialDate = selectedDate,
        onConfirm = {
            selectedDate = it
            showDateDialog = false
        },
        onDismissRequest = { showDateDialog = false },
    )
    MaigTimePickerDialog(
        visible = showTimeDialog,
        initialTime = selectedTime,
        onConfirm = {
            selectedTime = it
            showTimeDialog = false
        },
        onDismissRequest = { showTimeDialog = false },
    )
}
