package com.exozet.android.core.services.notifications

import android.util.Log
import com.exozet.android.core.base.BaseApplication.Companion.isDebugMode
import com.exozet.android.core.extensions.toast
import com.firebase.jobdispatcher.FirebaseJobDispatcher
import com.firebase.jobdispatcher.GooglePlayDriver
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.gothaer.schadentracker.services.PushNotificationPublisher.sendNotification
import net.kibotu.ContextHelper


/**
 * Created by [Jan Rabe](https://about.me/janrabe).
 */
class FBMessagingService : FirebaseMessagingService() {

    var remoteMessage: RemoteMessage? = null

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        this.remoteMessage = remoteMessage
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage!!.from!!)

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: " + remoteMessage.data)
            Log.d(TAG, "Message data payload: " + Gson().toJson(remoteMessage.data))


//            if (/* Check if data needs to be processed by long running job */ true) {
//                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
//                scheduleJob()
//            } else {
            // Handle message within 10 seconds
            handleNow()
//            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.notification != null) {
            Log.d(TAG, "Message Notification Title: " + (remoteMessage.notification?.title
                    ?: "no title") + " Body: " + (remoteMessage.notification?.body ?: "no body"))
            handleNow()
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]


    /**
     * Schedule a job using FirebaseJobDispatcher.
     */
    private fun scheduleJob() {
        // [START dispatch_job]
        val dispatcher = FirebaseJobDispatcher(GooglePlayDriver(this))
        val myJob = dispatcher.newJobBuilder()
                .setService(FBNotificationJobService::class.java)
                .setTag("my-job-tag")
                .build()
        dispatcher.schedule(myJob)
        // [END dispatch_job]
    }

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private fun handleNow() {
        Log.d(TAG, "Short lived task is done.")

        if (isDebugMode()) {
            toast("Push: " + remoteMessage?.notification?.body)
        }

        if (!ContextHelper.isRunning.get()) {
            sendNotification(
                    messageBody = remoteMessage?.notification?.body ?: "",
                    title = remoteMessage?.notification?.title ?: "")
        }
    }

    companion object {
        val TAG: String = FBMessagingService::class.java.simpleName
    }
}