package io.github.maignardi.maigui.components.display

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.maignardi.maigui.core.theme.MaigTheme
import kotlin.math.absoluteValue

/**
 * Size presets for [MaigAvatar].
 */
public enum class MaigAvatarSize(
    public val dp: Dp,
    public val fontSize: TextUnit,
    public val statusSize: Dp,
) {
    Xs(24.dp, 10.sp, 7.dp),
    Sm(32.dp, 12.sp, 9.dp),
    Md(40.dp, 14.sp, 11.dp),
    Lg(52.dp, 18.sp, 13.dp),
    Xl(68.dp, 22.sp, 16.dp),
}

/**
 * Presence/status indicator dot options for [MaigAvatar].
 */
public enum class MaigAvatarStatus {
    Online,
    Away,
    Busy,
    Offline,
}

/**
 * Default values and initial-extraction helpers for [MaigAvatar].
 */
public object MaigAvatarDefaults {
    /** Extracts up to 2 uppercase initials from a display name. */
    public fun extractInitials(name: String): String {
        val parts = name.trim().split(Regex("\\s+")).filter { it.isNotEmpty() }
        return when {
            parts.isEmpty() -> "?"
            parts.size == 1 -> parts[0].take(2).uppercase()
            else -> "${parts.first().first()}${parts.last().first()}".uppercase()
        }
    }

    /** Picks a consistent pastel/accent background color based on [name]. */
    @Composable
    public fun containerColorFor(name: String): Color {
        val c = MaigTheme.colors
        val palette = listOf(
            c.primarySubtle to c.onPrimarySubtle,
            c.secondarySubtle to c.onSecondarySubtle,
            c.successSubtle to c.onSuccessSubtle,
            c.warningSubtle to c.onWarningSubtle,
            c.infoSubtle to c.onInfoSubtle,
        )
        val idx = (name.hashCode().absoluteValue) % palette.size
        return palette[idx].first
    }

    /** Resolves status indicator color. */
    @Composable
    public fun statusColor(status: MaigAvatarStatus): Color = when (status) {
        MaigAvatarStatus.Online -> MaigTheme.colors.successDefault
        MaigAvatarStatus.Away -> MaigTheme.colors.warningDefault
        MaigAvatarStatus.Busy -> MaigTheme.colors.dangerDefault
        MaigAvatarStatus.Offline -> MaigTheme.colors.neutral.shade400
    }
}

/**
 * Circular avatar supporting image [painter], [name] initials fallback, and optional [status] indicator.
 */
@Composable
public fun MaigAvatar(
    name: String,
    modifier: Modifier = Modifier,
    painter: Painter? = null,
    size: MaigAvatarSize = MaigAvatarSize.Md,
    status: MaigAvatarStatus? = null,
    containerColor: Color = MaigTheme.colors.primarySubtle,
    contentColor: Color = MaigTheme.colors.onPrimarySubtle,
    contentDescription: String? = name,
) {
    Box(
        modifier = modifier
            .size(size.dp)
            .semantics {
                if (contentDescription != null) {
                    this.contentDescription = contentDescription
                }
            },
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .clip(CircleShape)
                .background(containerColor),
            contentAlignment = Alignment.Center,
        ) {
            if (painter != null) {
                Image(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier.matchParentSize(),
                    contentScale = ContentScale.Crop,
                )
            } else {
                BasicText(
                    text = MaigAvatarDefaults.extractInitials(name),
                    style = MaigTheme.typography.label.copy(
                        color = contentColor,
                        fontSize = size.fontSize,
                    ),
                )
            }
        }

        if (status != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(size.statusSize)
                    .clip(CircleShape)
                    .background(MaigAvatarDefaults.statusColor(status))
                    .border(2.dp, MaigTheme.colors.surface, CircleShape),
            )
        }
    }
}

/**
 * Overlapping stacked group of user avatars with an optional `+N` overflow badge.
 */
@Composable
public fun MaigAvatarGroup(
    names: List<String>,
    modifier: Modifier = Modifier,
    maxVisible: Int = 4,
    size: MaigAvatarSize = MaigAvatarSize.Sm,
) {
    val visibleNames = names.take(maxVisible)
    val overflow = (names.size - maxVisible).coerceAtLeast(0)
    val overlap = size.dp * 0.28f

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        visibleNames.forEachIndexed { index, personName ->
            Box(
                modifier = Modifier
                    .offset(x = -overlap * index)
                    .border(2.dp, MaigTheme.colors.surface, CircleShape),
            ) {
                MaigAvatar(
                    name = personName,
                    size = size,
                    containerColor = MaigAvatarDefaults.containerColorFor(personName),
                    contentColor = MaigTheme.colors.textPrimary,
                )
            }
        }
        if (overflow > 0) {
            Box(
                modifier = Modifier
                    .offset(x = -overlap * visibleNames.size)
                    .size(size.dp)
                    .clip(CircleShape)
                    .background(MaigTheme.colors.surfaceVariant)
                    .border(2.dp, MaigTheme.colors.surface, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                BasicText(
                    text = "+$overflow",
                    style = MaigTheme.typography.caption.copy(
                        color = MaigTheme.colors.textPrimary,
                        fontSize = size.fontSize,
                    ),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigAvatarLightPreview() {
    MaigTheme(darkTheme = false) {
        Row(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(MaigTheme.spacing.lg),
            horizontalArrangement = Arrangement.spacedBy(MaigTheme.spacing.md),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            MaigAvatar(name = "Matheus Maignardi", status = MaigAvatarStatus.Online)
            MaigAvatar(name = "Ana Silva", size = MaigAvatarSize.Lg, status = MaigAvatarStatus.Busy)
            MaigAvatarGroup(names = listOf("Ana", "Bruno", "Carlos", "Diana", "Eduardo", "Fernanda"))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigAvatarDarkPreview() {
    MaigTheme(darkTheme = true) {
        Row(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(MaigTheme.spacing.lg),
            horizontalArrangement = Arrangement.spacedBy(MaigTheme.spacing.md),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            MaigAvatar(name = "Matheus Maignardi", status = MaigAvatarStatus.Online)
            MaigAvatarGroup(names = listOf("Ana", "Bruno", "Carlos", "Diana", "Eduardo"))
        }
    }
}
