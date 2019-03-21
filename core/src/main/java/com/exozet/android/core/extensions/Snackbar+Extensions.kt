@file:JvmName("SnackbarExtensions")

package com.exozet.android.core.extensions

import android.widget.TextView
import com.google.android.material.snackbar.Snackbar

fun Snackbar.withTextColor(color: Int): Snackbar {
    view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).setTextColor(color)
    return this
}