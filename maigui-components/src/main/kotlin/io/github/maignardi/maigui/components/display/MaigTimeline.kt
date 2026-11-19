package io.github.maignardi.maigui.components.display

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.maignardi.maigui.core.theme.MaigTheme

/**
 * Status style for each [MaigTimelineItem].
 */
public enum class MaigTimelineStatus {
    Completed,
    Active,
    Error,
    Pending,
}

/**
 * Data item rendered inside [MaigTimeline].
 */
@Immutable
public class MaigTimelineItem(
    public val title: String,
    public val description: String? = null,
    public val timestamp: String? = null,
    public val status: MaigTimelineStatus = MaigTimelineStatus.Completed,
)

/**
 * Vertical activity/order timeline with semantic status nodes and connecting tracks.
 */
@Composable
public fun MaigTimeline(
    items: List<MaigTimelineItem>,
    modifier: Modifier = Modifier,
) {
    val c = MaigTheme.colors

    Column(modifier = modifier.fillMaxWidth()) {
        items.forEachIndexed { index, item ->
            val isLast = index == items.lastIndex
            val dotColor = when (item.status) {
                MaigTimelineStatus.Completed -> c.successDefault
                MaigTimelineStatus.Active -> c.primaryDefault
                MaigTimelineStatus.Error -> c.dangerDefault
                MaigTimelineStatus.Pending -> c.borderStrong
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
            ) {
                Column(
                    modifier = Modifier.width(24.dp).fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Box(
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .size(if (item.status == MaigTimelineStatus.Active) 14.dp else 11.dp)
                            .clip(CircleShape)
                            .background(dotColor)
                            .then(
                                if (item.status == MaigTimelineStatus.Active) {
                                    Modifier.border(3.dp, c.primarySubtle, CircleShape)
                                } else {
                                    Modifier
                                },
                            ),
                    )
                    if (!isLast) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .width(2.dp)
                                .padding(vertical = 4.dp)
                                .background(
                                    if (item.status == MaigTimelineStatus.Completed) {
                                        c.successDefault.copy(alpha = 0.4f)
                                    } else {
                                        c.divider
                                    },
                                ),
                        )
                    }
                }

                Spacer(modifier = Modifier.width(MaigTheme.spacing.md))

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(bottom = if (isLast) 0.dp else MaigTheme.spacing.lg),
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        BasicText(
                            text = item.title,
                            style = MaigTheme.typography.label.copy(
                                color = if (item.status == MaigTimelineStatus.Pending) c.textSecondary else c.textPrimary,
                            ),
                        )
                        if (item.timestamp != null) {
                            BasicText(
                                text = item.timestamp,
                                style = MaigTheme.typography.caption.copy(
                                    color = c.textTertiary,
                                    fontFeatureSettings = "tnum",
                                ),
                            )
                        }
                    }
                    if (item.description != null) {
                        BasicText(
                            text = item.description,
                            style = MaigTheme.typography.bodySmall.copy(color = c.textSecondary),
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigTimelineLightPreview() {
    MaigTheme(darkTheme = false) {
        Box(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(MaigTheme.spacing.lg),
        ) {
            MaigTimeline(
                items = listOf(
                    MaigTimelineItem("Pedido confirmado", "Pagamento via Pix aprovado", "10:15", MaigTimelineStatus.Completed),
                    MaigTimelineItem("Em separação", "Preparando pacote no centro de distribuição", "11:40", MaigTimelineStatus.Active),
                    MaigTimelineItem("Saiu para entrega", "Previsão: amanhã", null, MaigTimelineStatus.Pending),
                ),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigTimelineDarkPreview() {
    MaigTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(MaigTheme.spacing.lg),
        ) {
            MaigTimeline(
                items = listOf(
                    MaigTimelineItem("Build iniciado", "CI GitHub Actions", "00:40", MaigTimelineStatus.Completed),
                    MaigTimelineItem("Testes aprovados", "26 testes + Roborazzi", "00:42", MaigTimelineStatus.Active),
                ),
            )
        }
    }
}
