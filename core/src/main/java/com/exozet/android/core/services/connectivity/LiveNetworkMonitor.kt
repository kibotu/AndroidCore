package com.exozet.android.core.services.connectivity

import android.content.Context
import android.net.ConnectivityManager
import net.kibotu.ContextHelper

object LiveNetworkMonitor {

    val isConnected: Boolean
        get() {
            val cm = ContextHelper.getApplication()?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            @Suppress("DEPRECATION")
            return cm.activeNetworkInfo?.isAvailable == true && cm.activeNetworkInfo?.isConnectedOrConnecting == true
        }
}