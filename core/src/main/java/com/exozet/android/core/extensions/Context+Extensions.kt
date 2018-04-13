@file:JvmName("ContextExtensions")

package com.exozet.android.core.extensions

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.text.TextUtils.isEmpty
import android.widget.Toast
import net.kibotu.logger.Logger


/**
 * Created by [Jan Rabe](https://about.me/janrabe).
 */

fun Context?.openMail(email: String? = null, subject: String? = null) {

    try {
        val intent = Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", email?.trim(), null))
        subject?.let { intent.putExtra(Intent.EXTRA_SUBJECT, subject.trim()) }
        this?.startActivity(Intent.createChooser(intent, null))
    } catch (e: Exception) {
        Logger.e(e)
    }
}

fun Context?.openGoogleMaps(address: String?) {
    if (isEmpty(address))
        return

    val gmmIntentUri = Uri.parse("geo:0,0?q=${address?.trim()}")
    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
    mapIntent.`package` = "com.google.android.apps.maps"
    this?.startActivity(mapIntent)
}

fun Context?.openCall(phoneNr: String) {
    this?.startActivity(Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNr.trim(), null)).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    })
}

infix fun Context?.toast(message: String) {
    Handler(Looper.getMainLooper()).post({
        Toast.makeText(this ?: return@post, message, Toast.LENGTH_SHORT).show()
    })
}