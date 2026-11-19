package io.github.maignardi.maigui.core.tokens

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.dp

/**
 * Corner shape tokens ranging from sharp [none] to pill/circular [full].
 */
@Immutable
public class MaigShapes(
    public val none: CornerBasedShape = RoundedCornerShape(0.dp),
    public val xs: CornerBasedShape = RoundedCornerShape(6.dp),
    public val sm: CornerBasedShape = RoundedCornerShape(10.dp),
    public val md: CornerBasedShape = RoundedCornerShape(14.dp),
    public val lg: CornerBasedShape = RoundedCornerShape(18.dp),
    public val xl: CornerBasedShape = RoundedCornerShape(24.dp),
    public val full: CornerBasedShape = CircleShape,
) {
    /**
     * Creates a copy of this [MaigShapes] with optionally overridden corner shapes.
     */
    public fun copy(
        none: CornerBasedShape = this.none,
        xs: CornerBasedShape = this.xs,
        sm: CornerBasedShape = this.sm,
        md: CornerBasedShape = this.md,
        lg: CornerBasedShape = this.lg,
        xl: CornerBasedShape = this.xl,
        full: CornerBasedShape = this.full,
    ): MaigShapes = MaigShapes(
        none = none,
        xs = xs,
        sm = sm,
        md = md,
        lg = lg,
        xl = xl,
        full = full,
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is MaigShapes) return false
        return none == other.none &&
            xs == other.xs &&
            sm == other.sm &&
            md == other.md &&
            lg == other.lg &&
            xl == other.xl &&
            full == other.full
    }

    override fun hashCode(): Int {
        var result = none.hashCode()
        result = 31 * result + xs.hashCode()
        result = 31 * result + sm.hashCode()
        result = 31 * result + md.hashCode()
        result = 31 * result + lg.hashCode()
        result = 31 * result + xl.hashCode()
        result = 31 * result + full.hashCode()
        return result
    }

    override fun toString(): String =
        "MaigShapes(none=$none, xs=$xs, sm=$sm, md=$md, lg=$lg, xl=$xl, full=$full)"
}
