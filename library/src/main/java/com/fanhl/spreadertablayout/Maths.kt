package com.fanhl.spreadertablayout

import kotlin.math.roundToInt

internal fun Float.floor() = Math.floor(this.toDouble()).roundToInt()
internal fun Float.ceil() = Math.ceil(this.toDouble()).roundToInt()