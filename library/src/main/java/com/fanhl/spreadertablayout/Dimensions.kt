package com.fanhl.spreadertablayout

import android.content.res.Resources
import androidx.annotation.Dimension

/** px2dp */
val @receiver:Dimension(unit = 1) Int.dp: Int
    @Dimension(unit = 0)
    get() = Math.round(this / Resources.getSystem().displayMetrics.density)

/** dp2px */
val @receiver:Dimension(unit = 0) Int.px
    @Dimension(unit = 1)
    get() = Math.round(Resources.getSystem().displayMetrics.density * this)

/** dp to px float */
val @receiver:Dimension(unit = 0) Int.pxf
    @Dimension(unit = 1)
    get() = Resources.getSystem().displayMetrics.density * this
