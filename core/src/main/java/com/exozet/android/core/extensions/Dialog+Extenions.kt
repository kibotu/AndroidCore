package com.exozet.android.core.extensions

import android.app.Dialog
import android.os.Build
import android.view.Window
import android.view.WindowManager


fun Dialog.addFullScreenFlags(): Dialog {
    window?.addFullScreenFlags()
    return this
}

fun Window.addFullScreenFlags() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    requestFeature(Window.FEATURE_NO_TITLE)
    setBackgroundDrawableResource(android.R.color.transparent)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
        setClipToOutline(false)
    }
}