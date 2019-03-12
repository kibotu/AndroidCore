@file:JvmName("ActivityExtensions")

package com.exozet.android.core.extensions

import android.view.View
import net.kibotu.ContextHelper.getActivity

val contentRootView: View
    get() = getActivity()!!
        .window
        .decorView
        .findViewById(android.R.id.content)