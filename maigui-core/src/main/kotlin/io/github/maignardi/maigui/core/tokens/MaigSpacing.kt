package io.github.maignardi.maigui.core.tokens

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Consistent spacing scale tokens in density-independent pixels ([Dp]).
 */
@Immutable
public class MaigSpacing(
    public val xxs: Dp = 2.dp,
    public val xs: Dp = 4.dp,
    public val sm: Dp = 8.dp,
    public val md: Dp = 12.dp,
    public val lg: Dp = 16.dp,
    public val xl: Dp = 24.dp,
    public val xxl: Dp = 32.dp,
    public val xxxl: Dp = 48.dp,
) {
    /**
     * Creates a copy of this [MaigSpacing] with optionally overridden values.
     */
    public fun copy(
        xxs: Dp = this.xxs,
        xs: Dp = this.xs,
        sm: Dp = this.sm,
        md: Dp = this.md,
        lg: Dp = this.lg,
        xl: Dp = this.xl,
        xxl: Dp = this.xxl,
        xxxl: Dp = this.xxxl,
    ): MaigSpacing = MaigSpacing(
        xxs = xxs,
        xs = xs,
        sm = sm,
        md = md,
        lg = lg,
        xl = xl,
        xxl = xxl,
        xxxl = xxxl,
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is MaigSpacing) return false
        return xxs == other.xxs &&
            xs == other.xs &&
            sm == other.sm &&
            md == other.md &&
            lg == other.lg &&
            xl == other.xl &&
            xxl == other.xxl &&
            xxxl == other.xxxl
    }

    override fun hashCode(): Int {
        var result = xxs.hashCode()
        result = 31 * result + xs.hashCode()
        result = 31 * result + sm.hashCode()
        result = 31 * result + md.hashCode()
        result = 31 * result + lg.hashCode()
        result = 31 * result + xl.hashCode()
        result = 31 * result + xxl.hashCode()
        result = 31 * result + xxxl.hashCode()
        return result
    }

    override fun toString(): String =
        "MaigSpacing(xxs=$xxs, xs=$xs, sm=$sm, md=$md, lg=$lg, xl=$xl, xxl=$xxl, xxxl=$xxxl)"
}
