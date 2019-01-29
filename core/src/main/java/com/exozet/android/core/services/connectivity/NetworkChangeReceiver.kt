package com.exozet.android.core.services.connectivity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager

class NetworkChangeReceiver(var onConnectivityUpdate: ((Boolean) -> Unit)? = null) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        onConnectivityUpdate?.invoke(LiveNetworkMonitor.isConnected)
    }

    fun unregister(context: Context) {
        context.unregisterReceiver(this)
    }

    companion object {

        fun register(context: Context, onConnectivityUpdate: ((Boolean) -> Unit)? = null): NetworkChangeReceiver = NetworkChangeReceiver(onConnectivityUpdate)
            .also {
                // https://developer.android.com/training/monitoring-device-state/connectivity-monitoring#MonitorChanges
                @Suppress("DEPRECATION")
                context.registerReceiver(it, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
            }
    }
}