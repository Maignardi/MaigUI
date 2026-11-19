package io.github.maignardi.maigui.components.display

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import io.github.maignardi.maigui.core.theme.MaigTheme

/**
 * Clean key-value summary row ideal for receipts, order details, profiles, and settings.
 */
@Composable
public fun MaigKeyValueRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    valueColor: Color = MaigTheme.colors.textPrimary,
    emphasizeValue: Boolean = false,
    showDivider: Boolean = false,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = MaigTheme.spacing.sm),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BasicText(
                text = label,
                modifier = Modifier.weight(1f),
                style = MaigTheme.typography.bodySmall.copy(color = MaigTheme.colors.textSecondary),
            )
            BasicText(
                text = value,
                style = (if (emphasizeValue) MaigTheme.typography.title else MaigTheme.typography.label).copy(
                    color = valueColor,
                    textAlign = TextAlign.End,
                    fontFeatureSettings = "tnum",
                ),
            )
        }
        if (showDivider) {
            MaigDivider()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigKeyValueRowLightPreview() {
    MaigTheme(darkTheme = false) {
        Box(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(MaigTheme.spacing.lg),
        ) {
            MaigCard(modifier = Modifier.fillMaxWidth()) {
                MaigKeyValueRow(label = "Subtotal", value = "R$ 149,90", showDivider = true)
                MaigKeyValueRow(label = "Desconto", value = "- R$ 20,00", valueColor = MaigTheme.colors.successDefault, showDivider = true)
                MaigKeyValueRow(label = "Total", value = "R$ 129,90", emphasizeValue = true)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigKeyValueRowDarkPreview() {
    MaigTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(MaigTheme.spacing.lg),
        ) {
            MaigCard(modifier = Modifier.fillMaxWidth()) {
                MaigKeyValueRow(label = "Plano", value = "Enterprise", showDivider = true)
                MaigKeyValueRow(label = "Renovação", value = "19/12/2026")
            }
        }
    }
}
