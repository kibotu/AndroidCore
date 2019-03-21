@file:JvmName("ActivityExtensions")

package com.exozet.android.core.extensions

import android.app.Activity
import android.view.ViewGroup

val Activity.contentRootView: ViewGroup
    get() = window
        .decorView
        .findViewById(android.R.id.content)