package com.exozet.android.core.services.notifications


import android.util.Log
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.apache.commons.io.IOUtils
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

/**
 * https://github.com/prashanthd/google-services/blob/master/android/gcm/gcmsender/src/main/java/gcm/play/android/samples/com/gcmsender/GcmSender.java
 */
object GcmSender {

    var TAG = GcmSender::class.java.simpleName

    var API_KEY = "API_KEY"

    fun sendAsync(message: String, recipient: String? = null) {
        Observable.fromCallable { send(message, recipient) }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({}, Throwable::printStackTrace)
    }

    fun send(message: String, recipient: String? = null) {
        try {
            // Prepare JSON containing the GCM message content. What to send and where to send.
            val jGcmData = JSONObject()
            val jData = JSONObject()
            jData.put("message", message.trim { it <= ' ' })
            // Where to send GCM message.
            if (recipient != null) {
                jGcmData.put("to", recipient)
            } else {
                jGcmData.put("to", "/topics/global")
            }
            // What to send in GCM message.
            jGcmData.put("data", jData)

            // Create connection to send GCM Message request.
            val url = URL("https://android.googleapis.com/gcm/send")
            val conn = url.openConnection() as HttpURLConnection
            conn.setRequestProperty("Authorization", "key=$API_KEY")
            conn.setRequestProperty("Content-Type", "application/json")
            conn.requestMethod = "POST"
            conn.doOutput = true

            // Send GCM message content.
            val outputStream = conn.outputStream
            outputStream.write(jGcmData.toString().toByteArray())

            // Read GCM response.
            val inputStream = conn.inputStream
            val resp = IOUtils.toString(inputStream)
            Log.v(TAG, resp)
            Log.v(TAG, "Check your device/emulator for notification or logcat for " + "confirmation of the receipt of the GCM message.")
        } catch (e: IOException) {
            Log.v(TAG, "Unable to send GCM message.")
            Log.v(TAG, "Please ensure that API_KEY has been replaced by the server " + "API key, and that the device's registration token is correct (if specified).")
            e.printStackTrace()
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
}