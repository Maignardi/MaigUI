package io.github.maignardi.maigui.components.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.maignardi.maigui.components.internal.MaigCheckIcon
import io.github.maignardi.maigui.core.theme.MaigTheme

/**
 * Defaults for [MaigStepper].
 */
public object MaigStepperDefaults {
    @Composable
    public fun completedColor(): Color = MaigTheme.colors.successDefault

    @Composable
    public fun activeColor(): Color = MaigTheme.colors.primaryDefault

    @Composable
    public fun upcomingColor(): Color = MaigTheme.colors.surfaceVariant
}

/**
 * Horizontal multi-step progress indicator showing completed, active, and upcoming steps.
 */
@Composable
public fun MaigStepper(
    steps: List<String>,
    currentStep: Int,
    modifier: Modifier = Modifier,
    onStepClick: ((Int) -> Unit)? = null,
) {
    val c = MaigTheme.colors

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
    ) {
        steps.forEachIndexed { index, label ->
            val isCompleted = index < currentStep
            val isActive = index == currentStep

            val circleBg = when {
                isCompleted -> MaigStepperDefaults.completedColor()
                isActive -> MaigStepperDefaults.activeColor()
                else -> MaigStepperDefaults.upcomingColor()
            }
            val circleFg = when {
                isCompleted -> c.onSuccess
                isActive -> c.onPrimary
                else -> c.textSecondary
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .then(
                        if (onStepClick != null) {
                            Modifier.clickable { onStepClick(index) }
                        } else {
                            Modifier
                        },
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(MaigTheme.spacing.xs),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(2.dp)
                            .background(
                                when {
                                    index == 0 -> Color.Transparent
                                    index <= currentStep -> MaigStepperDefaults.completedColor()
                                    else -> c.border
                                },
                            ),
                    )
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(circleBg)
                            .then(
                                if (!isCompleted && !isActive) {
                                    Modifier.border(1.dp, c.border, CircleShape)
                                } else {
                                    Modifier
                                },
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        if (isCompleted) {
                            MaigCheckIcon(size = 14.dp, tint = circleFg)
                        } else {
                            BasicText(
                                text = "${index + 1}",
                                style = MaigTheme.typography.caption.copy(color = circleFg),
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(2.dp)
                            .background(
                                when {
                                    index == steps.lastIndex -> Color.Transparent
                                    index < currentStep -> MaigStepperDefaults.completedColor()
                                    else -> c.border
                                },
                            ),
                    )
                }

                BasicText(
                    text = label,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaigTheme.typography.caption.copy(
                        color = if (isActive || isCompleted) c.textPrimary else c.textSecondary,
                        textAlign = TextAlign.Center,
                    ),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigStepperLightPreview() {
    MaigTheme(darkTheme = false) {
        Box(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(MaigTheme.spacing.lg),
        ) {
            MaigStepper(
                steps = listOf("Dados", "Endereço", "Pagamento", "Revisão"),
                currentStep = 1,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigStepperDarkPreview() {
    MaigTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(MaigTheme.spacing.lg),
        ) {
            MaigStepper(
                steps = listOf("Conta", "Plano", "Pronto"),
                currentStep = 2,
            )
        }
    }
}
