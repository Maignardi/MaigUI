package io.github.maignardi.maigui.components.display

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.maignardi.maigui.components.internal.MaigPlusIcon
import io.github.maignardi.maigui.core.theme.LocalMaigContentColor
import io.github.maignardi.maigui.core.theme.MaigTheme

/**
 * Trend direction for [MaigStatCard].
 */
public enum class MaigStatTrend {
    Positive,
    Negative,
    Neutral,
}

/**
 * Modern SaaS/fintech KPI metric card displaying a [label], prominent tabular [value],
 * optional [delta] trend pill, [subtitle], and leading/trailing [icon].
 */
@Composable
public fun MaigStatCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    delta: String? = null,
    trend: MaigStatTrend = MaigStatTrend.Neutral,
    subtitle: String? = null,
    icon: @Composable (() -> Unit)? = null,
    variant: MaigCardVariant = MaigCardVariant.Elevated,
    onClick: (() -> Unit)? = null,
) {
    val c = MaigTheme.colors
    val trendColor = when (trend) {
        MaigStatTrend.Positive -> MaigTagColor.Success
        MaigStatTrend.Negative -> MaigTagColor.Danger
        MaigStatTrend.Neutral -> MaigTagColor.Neutral
    }

    val body: @Composable () -> Unit = {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top,
        ) {
            BasicText(
                text = label,
                style = MaigTheme.typography.label.copy(color = c.textSecondary),
            )
            if (icon != null) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(c.primarySubtle),
                    contentAlignment = Alignment.Center,
                ) {
                    CompositionLocalProvider(LocalMaigContentColor provides c.onPrimarySubtle) {
                        icon()
                    }
                }
            }
        }

        Row(
            modifier = Modifier.padding(top = MaigTheme.spacing.xs),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MaigTheme.spacing.sm),
        ) {
            BasicText(
                text = value,
                style = MaigTheme.typography.displayMedium.copy(
                    color = c.textPrimary,
                    fontFeatureSettings = "tnum",
                ),
            )
            if (delta != null) {
                MaigTag(
                    text = delta,
                    color = trendColor,
                    variant = MaigTagVariant.Soft,
                    showDot = true,
                )
            }
        }

        if (subtitle != null) {
            BasicText(
                text = subtitle,
                modifier = Modifier.padding(top = MaigTheme.spacing.xs),
                style = MaigTheme.typography.caption.copy(color = c.textTertiary),
            )
        }
    }

    if (onClick != null) {
        MaigCard(onClick = onClick, modifier = modifier, variant = variant) { body() }
    } else {
        MaigCard(modifier = modifier, variant = variant) { body() }
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigStatCardLightPreview() {
    MaigTheme(darkTheme = false) {
        Box(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(MaigTheme.spacing.lg),
        ) {
            MaigStatCard(
                label = "Receita Mensal",
                value = "R$ 48.920",
                delta = "+18,4%",
                trend = MaigStatTrend.Positive,
                subtitle = "Comparado aos últimos 30 dias",
                icon = { MaigPlusIcon() },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigStatCardDarkPreview() {
    MaigTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(MaigTheme.spacing.lg),
        ) {
            MaigStatCard(
                label = "Taxa de Churn",
                value = "1,8%",
                delta = "-0,4%",
                trend = MaigStatTrend.Negative,
                subtitle = "Última semana",
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
