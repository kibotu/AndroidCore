package com.exozet.android.core.extensions

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat
import com.github.florent37.application.provider.application


fun Application.openNotificationsSettings() {
    val intent = Intent().apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> intent.setOpenSettingsForApiLarger25()
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> intent.setOpenSettingsForApiBetween21And25()
        else -> intent.setOpenSettingsForApiLess21()
    }
    startActivity(intent)
}

@RequiresApi(Build.VERSION_CODES.O)
private fun Intent.setOpenSettingsForApiLarger25() {
    action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
    putExtra("android.provider.extra.APP_PACKAGE", application!!.packageName)
}

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
private fun Intent.setOpenSettingsForApiBetween21And25() {
    action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
    putExtra("app_package", application!!.packageName)
    putExtra("app_uid", application!!.applicationInfo?.uid)
}

private fun Intent.setOpenSettingsForApiLess21() {
    action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
    addCategory(Intent.CATEGORY_DEFAULT)
    data = Uri.parse("package:" + application!!.packageName)
}

/**
 * https://developer.android.com/reference/android/app/NotificationChannel#getImportance()
 */
val NotificationChannel.isEnabled
    @RequiresApi(Build.VERSION_CODES.O)
    get() = importance != NotificationManager.IMPORTANCE_NONE

fun Application.notificationChannelIsEnabled(channel: String): Boolean = with(NotificationManagerCompat.from(this)) {
    val notificationsEnabled = areNotificationsEnabled()
    when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> notificationsEnabled && (notificationChannels.isEmpty() || notificationChannels.firstOrNull { it.id == channel }?.isEnabled == true)
        else -> notificationsEnabled
    }
}