package io.github.maignardi.maigui.sample

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import io.github.maignardi.maigui.components.inputs.MaigMoneyField
import io.github.maignardi.maigui.components.inputs.MaigMoneyFieldDefaults
import io.github.maignardi.maigui.components.inputs.MaigNumberStepper
import io.github.maignardi.maigui.components.inputs.MaigOtpField
import io.github.maignardi.maigui.components.inputs.MaigPasswordField
import io.github.maignardi.maigui.components.inputs.MaigPhoneField
import io.github.maignardi.maigui.components.inputs.MaigSearchField
import io.github.maignardi.maigui.components.inputs.MaigTextArea
import io.github.maignardi.maigui.components.inputs.MaigTextField
import io.github.maignardi.maigui.components.inputs.MaigTextFieldVariant
import io.github.maignardi.maigui.components.layout.MaigSection
import io.github.maignardi.maigui.components.selection.MaigSwitch
import io.github.maignardi.maigui.core.theme.MaigTheme

@Composable
internal fun InputsShowcaseScreen() {
    var search by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("Matheus Maignardi") }
    var filledText by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("compose123") }
    var otp by remember { mutableStateOf("42") }
    var moneyDigits by remember { mutableStateOf("14990") }
    var phoneDigits by remember { mutableStateOf("11987654321") }
    var notes by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf(2) }
    var simulateError by remember { mutableStateOf(false) }

    MaigSection(title = "Simulação de Estado") {
        MaigSwitch(
            checked = simulateError,
            onCheckedChange = { simulateError = it },
            label = "Exibir estado de erro nos campos",
        )
    }

    MaigSection(title = "Campos de Texto") {
        MaigSearchField(
            value = search,
            onValueChange = { search = it },
            placeholder = "Buscar componente...",
            modifier = Modifier.fillMaxWidth(),
        )
        MaigTextField(
            value = name,
            onValueChange = { name = it },
            label = "Nome (Outlined)",
            placeholder = "Digite seu nome",
            helperText = "Exibido no perfil público",
            errorText = if (simulateError) "Nome inválido" else null,
            maxLength = 40,
            showCounter = true,
            modifier = Modifier.fillMaxWidth(),
        )
        MaigTextField(
            value = filledText,
            onValueChange = { filledText = it },
            label = "Apelido (Filled)",
            placeholder = "@maignardi",
            variant = MaigTextFieldVariant.Filled,
            errorText = if (simulateError) "Apelido indisponível" else null,
            modifier = Modifier.fillMaxWidth(),
        )
        MaigPasswordField(
            value = password,
            onValueChange = { password = it },
            errorText = if (simulateError) "Senha muito fraca" else null,
            modifier = Modifier.fillMaxWidth(),
        )
    }

    MaigSection(title = "Campos Especializados (OTP, BRL, Telefone, TextArea)") {
        MaigOtpField(
            value = otp,
            onValueChange = { otp = it },
            length = 6,
            label = "Código OTP (6 dígitos)",
            errorText = if (simulateError) "Código expirado" else null,
        )
        MaigMoneyField(
            value = moneyDigits,
            onValueChange = { moneyDigits = it },
            label = "Valor em Reais",
            helperText = "Formatado: R$ ${MaigMoneyFieldDefaults.formatBrlAmount(moneyDigits)}",
            errorText = if (simulateError) "Valor mínimo R$ 10,00" else null,
            modifier = Modifier.fillMaxWidth(),
        )
        MaigPhoneField(
            value = phoneDigits,
            onValueChange = { phoneDigits = it },
            errorText = if (simulateError) "Telefone inválido" else null,
            modifier = Modifier.fillMaxWidth(),
        )
        MaigNumberStepper(
            value = quantity,
            onValueChange = { quantity = it },
            minValue = 1,
            maxValue = 10,
            label = "Quantidade (NumberStepper)",
        )
        MaigTextArea(
            value = notes,
            onValueChange = { notes = it },
            label = "Observações (TextArea)",
            placeholder = "Descreva os detalhes...",
            maxLength = 200,
            errorText = if (simulateError) "Mínimo de 10 caracteres" else null,
            modifier = Modifier.fillMaxWidth(),
        )
        BasicText(
            text = "Dígitos crus -> Money: '$moneyDigits' | Phone: '$phoneDigits'",
            style = MaigTheme.typography.caption.copy(color = MaigTheme.colors.textSecondary),
        )
    }
}
