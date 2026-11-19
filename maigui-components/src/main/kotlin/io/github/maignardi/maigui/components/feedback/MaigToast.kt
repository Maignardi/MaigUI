package io.github.maignardi.maigui.components.feedback

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.maignardi.maigui.components.internal.MaigCloseIcon
import io.github.maignardi.maigui.components.internal.MaigGlyphType
import io.github.maignardi.maigui.components.internal.MaigStatusGlyphIcon
import io.github.maignardi.maigui.core.theme.MaigTheme
import kotlinx.coroutines.delay

/**
 * Semantic toast notification categories.
 */
public enum class MaigToastType {
    Info,
    Success,
    Warning,
    Error,
}

/**
 * Duration presets for toast messages.
 */
public enum class MaigToastDuration(public val millis: Long) {
    Short(2500L),
    Long(4500L),
    Indefinite(kotlin.Long.MAX_VALUE),
}

/**
 * Immutable data payload describing a queued toast notification.
 */
@Immutable
public class MaigToastData(
    public val message: String,
    public val type: MaigToastType = MaigToastType.Info,
    public val actionLabel: String? = null,
    public val onAction: (() -> Unit)? = null,
    public val duration: MaigToastDuration = MaigToastDuration.Short,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is MaigToastData) return false
        return message == other.message &&
            type == other.type &&
            actionLabel == other.actionLabel &&
            duration == other.duration
    }

    override fun hashCode(): Int {
        var result = message.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + (actionLabel?.hashCode() ?: 0)
        result = 31 * result + duration.hashCode()
        return result
    }
}

/**
 * State holder managing queued toast notifications for [MaigToastHost].
 */
@Stable
public class MaigToastState {
    public var currentToast: MaigToastData? by mutableStateOf(null)
        private set

    private val queue = ArrayDeque<MaigToastData>()

    /**
     * Enqueues or displays a toast notification immediately if none is active.
     */
    public fun show(
        message: String,
        type: MaigToastType = MaigToastType.Info,
        actionLabel: String? = null,
        onAction: (() -> Unit)? = null,
        duration: MaigToastDuration = MaigToastDuration.Short,
    ) {
        val data = MaigToastData(
            message = message,
            type = type,
            actionLabel = actionLabel,
            onAction = onAction,
            duration = duration,
        )
        if (currentToast == null) {
            currentToast = data
        } else {
            queue.addLast(data)
        }
    }

    /** Convenience method for showing a success toast. */
    public fun showSuccess(message: String): Unit = show(message = message, type = MaigToastType.Success)

    /** Convenience method for showing an error toast. */
    public fun showError(message: String): Unit = show(message = message, type = MaigToastType.Error)

    /** Convenience method for showing a warning toast. */
    public fun showWarning(message: String): Unit = show(message = message, type = MaigToastType.Warning)

    /** Convenience method for showing an informational toast. */
    public fun showInfo(message: String): Unit = show(message = message, type = MaigToastType.Info)

    /** Dismisses the currently visible toast and advances the queue if non-empty. */
    public fun dismiss() {
        currentToast = if (queue.isNotEmpty()) queue.removeFirst() else null
    }
}

/**
 * CompositionLocal exposing the nearest [MaigToastState] provided by `MaigScaffold` or [MaigToastHost].
 */
public val LocalMaigToastState: ProvidableCompositionLocal<MaigToastState> = staticCompositionLocalOf {
    MaigToastState()
}

/**
 * Remembers a [MaigToastState] instance across recompositions.
 */
@Composable
public fun rememberMaigToastState(): MaigToastState = remember { MaigToastState() }

/**
 * Defaults for [MaigToastHost] and [MaigToastBanner].
 */
public object MaigToastDefaults {
    @Composable
    internal fun colorsFor(type: MaigToastType): Triple<Color, Color, Color> {
        val c = MaigTheme.colors
        return when (type) {
            MaigToastType.Info -> Triple(c.surfaceElevated, c.textPrimary, c.infoDefault)
            MaigToastType.Success -> Triple(c.successSubtle, c.onSuccessSubtle, c.successDefault)
            MaigToastType.Warning -> Triple(c.warningSubtle, c.onWarningSubtle, c.warningDefault)
            MaigToastType.Error -> Triple(c.dangerSubtle, c.onDangerSubtle, c.dangerDefault)
        }
    }
}

/**
 * Animated toast host that renders active notifications from [hostState] and auto-dismisses them.
 */
@Composable
public fun MaigToastHost(
    hostState: MaigToastState,
    modifier: Modifier = Modifier,
) {
    val active = hostState.currentToast

    LaunchedEffect(active) {
        if (active != null && active.duration != MaigToastDuration.Indefinite) {
            delay(active.duration.millis)
            hostState.dismiss()
        }
    }

    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.BottomCenter,
    ) {
        AnimatedVisibility(
            visible = active != null,
            enter = fadeIn() + slideInVertically { it / 2 },
            exit = fadeOut() + slideOutVertically { it / 2 },
        ) {
            if (active != null) {
                MaigToastBanner(
                    data = active,
                    onDismiss = { hostState.dismiss() },
                    modifier = Modifier.padding(MaigTheme.spacing.lg),
                )
            }
        }
    }
}

/**
 * Visual toast card rendered inside [MaigToastHost] (also usable standalone in previews).
 */
@Composable
public fun MaigToastBanner(
    data: MaigToastData,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val (bg, fg, accent) = MaigToastDefaults.colorsFor(data.type)
    val glyph = when (data.type) {
        MaigToastType.Info -> MaigGlyphType.Info
        MaigToastType.Success -> MaigGlyphType.Success
        MaigToastType.Warning -> MaigGlyphType.Warning
        MaigToastType.Error -> MaigGlyphType.Error
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = if (MaigTheme.colors.isDark) 0.dp else MaigTheme.elevation.md,
                shape = MaigTheme.shapes.lg,
                ambientColor = MaigTheme.colors.shadow,
                spotColor = MaigTheme.colors.shadow,
            )
            .clip(MaigTheme.shapes.lg)
            .background(bg)
            .border(1.dp, accent.copy(alpha = 0.35f), MaigTheme.shapes.lg)
            .semantics {
                liveRegion = LiveRegionMode.Polite
                contentDescription = data.message
            }
            .padding(horizontal = MaigTheme.spacing.md, vertical = MaigTheme.spacing.md),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        MaigStatusGlyphIcon(type = glyph, tint = accent, size = 20.dp)
        Spacer(modifier = Modifier.width(MaigTheme.spacing.md))
        BasicText(
            text = data.message,
            modifier = Modifier.weight(1f),
            style = MaigTheme.typography.bodySmall.copy(color = fg),
        )
        if (data.actionLabel != null && data.onAction != null) {
            Spacer(modifier = Modifier.width(MaigTheme.spacing.sm))
            BasicText(
                text = data.actionLabel,
                modifier = Modifier
                    .clip(MaigTheme.shapes.sm)
                    .clickable {
                        data.onAction.invoke()
                        onDismiss()
                    }
                    .padding(horizontal = MaigTheme.spacing.sm, vertical = MaigTheme.spacing.xs),
                style = MaigTheme.typography.button.copy(color = accent),
            )
        }
        Spacer(modifier = Modifier.width(MaigTheme.spacing.xs))
        Box(
            modifier = Modifier
                .clip(MaigTheme.shapes.full)
                .clickable(onClick = onDismiss)
                .padding(MaigTheme.spacing.xs)
                .semantics { contentDescription = "Fechar notificação" },
        ) {
            MaigCloseIcon(size = 14.dp, tint = fg.copy(alpha = 0.7f))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigToastLightPreview() {
    MaigTheme(darkTheme = false) {
        Column(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(MaigTheme.spacing.md),
            verticalArrangement = Arrangement.spacedBy(MaigTheme.spacing.sm),
        ) {
            MaigToastBanner(
                data = MaigToastData("Alterações salvas com sucesso!", MaigToastType.Success),
                onDismiss = {},
            )
            MaigToastBanner(
                data = MaigToastData("Falha ao conectar", MaigToastType.Error, "Tentar", {}),
                onDismiss = {},
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MaigToastDarkPreview() {
    MaigTheme(darkTheme = true) {
        Column(
            modifier = Modifier
                .background(MaigTheme.colors.background)
                .padding(MaigTheme.spacing.md),
            verticalArrangement = Arrangement.spacedBy(MaigTheme.spacing.sm),
        ) {
            MaigToastBanner(
                data = MaigToastData("Sessão expirando em breve", MaigToastType.Warning),
                onDismiss = {},
            )
            MaigToastBanner(
                data = MaigToastData("Nova atualização disponível", MaigToastType.Info),
                onDismiss = {},
            )
        }
    }
}
