package com.fanhl.spreadertablayout

import kotlin.math.roundToInt

fun Float.floor() = Math.floor(this.toDouble()).roundToInt()
fun Float.ceil() = Math.ceil(this.toDouble()).roundToInt()