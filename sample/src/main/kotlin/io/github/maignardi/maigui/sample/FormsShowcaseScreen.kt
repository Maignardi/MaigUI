package io.github.maignardi.maigui.sample

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import io.github.maignardi.maigui.components.buttons.MaigButton
import io.github.maignardi.maigui.components.buttons.MaigButtonVariant
import io.github.maignardi.maigui.components.display.MaigCard
import io.github.maignardi.maigui.components.display.MaigCardVariant
import io.github.maignardi.maigui.components.display.MaigTag
import io.github.maignardi.maigui.components.display.MaigTagColor
import io.github.maignardi.maigui.components.feedback.LocalMaigToastState
import io.github.maignardi.maigui.components.layout.MaigSection
import io.github.maignardi.maigui.core.theme.MaigTheme
import io.github.maignardi.maigui.forms.MaigMasks
import io.github.maignardi.maigui.forms.MaigMoneyField
import io.github.maignardi.maigui.forms.MaigPasswordField
import io.github.maignardi.maigui.forms.MaigPhoneField
import io.github.maignardi.maigui.forms.MaigTextArea
import io.github.maignardi.maigui.forms.MaigTextField
import io.github.maignardi.maigui.forms.rememberMaigForm

@Composable
internal fun FormsShowcaseScreen() {
    val toast = LocalMaigToastState.current

    val form = rememberMaigForm {
        field("nome") { required() }
        field("email") { required(); email() }
        field("cpf") { required(); cpf() }
        field("cep") { required(); cep() }
        field("telefone") { required(); phone() }
        field("valor") { required() }
        field("senha") { required(); minLength(8) }
        field("confirmarSenha") { required(); matches("senha", "As senhas não coincidem") }
        field("bio") { maxLength(150) }
    }

    MaigSection(
        title = "Formulário Declarativo (maigui-forms)",
        subtitle = "Erros ao perder foco ou validar • Foco automático Next/Done • Sobrevive à rotação",
        action = {
            MaigTag(
                text = if (form.isValid) "Válido" else "Incompleto",
                color = if (form.isValid) MaigTagColor.Success else MaigTagColor.Warning,
                showDot = true,
            )
        },
    ) {
        MaigTextField(
            field = form["nome"],
            label = "Nome completo",
            placeholder = "Ex.: Matheus Maignardi",
            modifier = Modifier.fillMaxWidth(),
        )
        MaigTextField(
            field = form["email"],
            label = "E-mail",
            placeholder = "dev@maigui.io",
            keyboardType = KeyboardType.Email,
            modifier = Modifier.fillMaxWidth(),
        )
        MaigTextField(
            field = form["cpf"],
            label = "CPF",
            placeholder = "000.000.000-00",
            helperText = "Teste válido: 52998224725",
            visualTransformation = MaigMasks.cpf(),
            keyboardType = KeyboardType.Number,
            maxLength = 11,
            modifier = Modifier.fillMaxWidth(),
        )
        MaigTextField(
            field = form["cep"],
            label = "CEP",
            placeholder = "00000-000",
            visualTransformation = MaigMasks.cep(),
            keyboardType = KeyboardType.Number,
            maxLength = 8,
            modifier = Modifier.fillMaxWidth(),
        )
        MaigPhoneField(
            field = form["telefone"],
            label = "Telefone / WhatsApp",
            modifier = Modifier.fillMaxWidth(),
        )
        MaigMoneyField(
            field = form["valor"],
            label = "Renda mensal",
            modifier = Modifier.fillMaxWidth(),
        )
        MaigPasswordField(
            field = form["senha"],
            label = "Senha",
            modifier = Modifier.fillMaxWidth(),
        )
        MaigPasswordField(
            field = form["confirmarSenha"],
            label = "Confirmar senha",
            onSubmit = {
                if (form.validate()) {
                    toast.showSuccess("Formulário validado e enviado!")
                } else {
                    toast.showError("Corrija os campos destacados")
                }
            },
            modifier = Modifier.fillMaxWidth(),
        )
        MaigTextArea(
            field = form["bio"],
            label = "Observações (opcional)",
            placeholder = "Conte um pouco sobre seu projeto...",
            maxLength = 150,
            modifier = Modifier.fillMaxWidth(),
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(MaigTheme.spacing.sm),
        ) {
            MaigButton(
                text = "Limpar",
                onClick = {
                    form.reset()
                    toast.showInfo("Formulário resetado")
                },
                variant = MaigButtonVariant.Outline,
                modifier = Modifier.weight(1f),
            )
            MaigButton(
                text = "Validar & Enviar",
                onClick = {
                    if (form.validate()) {
                        toast.showSuccess("Tudo válido! Enviado com sucesso.")
                    } else {
                        toast.showError("Existem erros de validação no formulário.")
                    }
                },
                modifier = Modifier.weight(1f),
            )
        }

        MaigCard(variant = MaigCardVariant.Filled, modifier = Modifier.fillMaxWidth()) {
            BasicText(
                text = "form.values: ${form.values}",
                style = MaigTheme.typography.caption.copy(color = MaigTheme.colors.textSecondary),
            )
        }
    }
}
