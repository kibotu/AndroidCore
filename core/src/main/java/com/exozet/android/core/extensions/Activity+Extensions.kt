@file:JvmName("ActivityExtensions")

package com.exozet.android.core.extensions

import android.app.Activity
import android.view.View

val Activity.contentRootView: View
    get() = window
        .decorView
        .findViewById(android.R.id.content)