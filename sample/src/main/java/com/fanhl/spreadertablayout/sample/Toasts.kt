package com.fanhl.spreadertablayout.sample

import android.app.Activity
import android.widget.Toast
import androidx.annotation.StringRes

fun Activity.toast(text:CharSequence) = Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
fun Activity.toast(@StringRes resId: Int) = Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()