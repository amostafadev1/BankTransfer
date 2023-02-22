package com.example.banktransfer.utils

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

fun Context.toast(@StringRes resId: Int) =
    Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()

fun Context.toast(text: String) =
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()