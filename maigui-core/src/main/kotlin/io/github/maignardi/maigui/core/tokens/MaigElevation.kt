package io.github.maignardi.maigui.core.tokens

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Elevation depth tokens for surfaces, cards, popovers, and dialogs.
 */
@Immutable
public class MaigElevation(
    public val none: Dp = 0.dp,
    public val xs: Dp = 1.dp,
    public val sm: Dp = 2.dp,
    public val md: Dp = 4.dp,
    public val lg: Dp = 8.dp,
    public val xl: Dp = 16.dp,
) {
    /**
     * Creates a copy of this [MaigElevation] with optionally overridden elevations.
     */
    public fun copy(
        none: Dp = this.none,
        xs: Dp = this.xs,
        sm: Dp = this.sm,
        md: Dp = this.md,
        lg: Dp = this.lg,
        xl: Dp = this.xl,
    ): MaigElevation = MaigElevation(
        none = none,
        xs = xs,
        sm = sm,
        md = md,
        lg = lg,
        xl = xl,
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is MaigElevation) return false
        return none == other.none &&
            xs == other.xs &&
            sm == other.sm &&
            md == other.md &&
            lg == other.lg &&
            xl == other.xl
    }

    override fun hashCode(): Int {
        var result = none.hashCode()
        result = 31 * result + xs.hashCode()
        result = 31 * result + sm.hashCode()
        result = 31 * result + md.hashCode()
        result = 31 * result + lg.hashCode()
        result = 31 * result + xl.hashCode()
        return result
    }

    override fun toString(): String =
        "MaigElevation(none=$none, xs=$xs, sm=$sm, md=$md, lg=$lg, xl=$xl)"
}
