@file:JvmName("ActivityExtensions")

package com.exozet.android.core.extensions

import android.app.Activity
import android.content.Intent
import android.net.wifi.WifiManager
import android.view.ViewGroup

val Activity.contentRootView: ViewGroup
    get() = window
        .decorView
        .findViewById(android.R.id.content)

fun Activity.chooseWifiNetwork() = startActivity(Intent(WifiManager.ACTION_PICK_WIFI_NETWORK))