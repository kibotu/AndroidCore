package com.exozet.android.core.services.notifications

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.O
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.exozet.android.core.R
import com.exozet.android.core.extensions.resString
import net.kibotu.ContextHelper

/**
 * Created by [Jan Rabe](https://about.me/janrabe).
 */
object PushNotificationPublisher {

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    fun sendNotification(context: Context = ContextHelper.getApplication()!!, title: String, messageBody: String) {
        val intent = Intent(context, Activity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            context, NOTIFICATION_REQUEST_CODE /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        val channelId = R.string.default_notification_channel_id.resString
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher)
            .setContentTitle(title)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (SDK_INT >= O) {
            addNotificationChannel(channelId, notificationManager)
        }

        notificationManager.notify(NOTIFICATION_REQUEST_CODE /* ID of notification */, notificationBuilder.build())
    }

    @RequiresApi(O)
    private fun addNotificationChannel(channelId: String?, notificationManager: NotificationManager) {
        val channel = NotificationChannel(channelId, R.string.channel_human_readable_title.resString, NotificationManager.IMPORTANCE_DEFAULT)
        notificationManager.createNotificationChannel(channel)
    }

    private const val NOTIFICATION_REQUEST_CODE = 101
}