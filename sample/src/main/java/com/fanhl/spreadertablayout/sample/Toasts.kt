package com.fanhl.spreadertablayout.sample

import android.app.Activity
import android.widget.Toast
import androidx.annotation.StringRes

fun Activity.toast(msg: String) = Toast.makeText(this, "test touch", Toast.LENGTH_SHORT).show()
fun Activity.toast(@StringRes stringRes: Int) = Toast.makeText(this, "test touch", Toast.LENGTH_SHORT).show()