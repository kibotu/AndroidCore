@file:JvmName("ContextExtensions")

package com.exozet.android.core.extensions

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils.isEmpty
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import net.kibotu.ContextHelper
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
    Handler(Looper.getMainLooper()).post {
        Toast.makeText(this ?: return@post, message, Toast.LENGTH_SHORT).show()
    }
}

fun <T> lazyFast(operation: () -> T): Lazy<T> = lazy(LazyThreadSafetyMode.NONE) {
    operation()
}

fun Context.safeContext(): Context =
    takeUnless {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> isDeviceProtectedStorage
            else -> true
        }
    }?.run {
        applicationContext.let {
            ContextCompat.createDeviceProtectedStorageContext(it) ?: it
        }
    } ?: this


fun String.openExternally() {

    var result = this

    if (!result.startsWith("http://") && !result.startsWith("https://")) {
        result = "http://$this"
    }

    ContextHelper.getApplication()
        ?.startActivity(Intent(Intent.ACTION_VIEW)
            .apply {
                addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT)
                }
                data = Uri.parse(result)
            })
}

fun Activity.addSecureFlag() {
    window?.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
}

fun Activity.clearSecureFlag() {
    window?.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
}

fun sendEmail(address: String, subject: String = "", body: Spanned = SpannableString(""), requestCode: Int = 5001, popupTitle: String = "") = try {
    ContextHelper.getActivity()?.startActivityForResult(
        Intent.createChooser(
            Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", address, null))
                .apply {
                    putExtra(Intent.EXTRA_SUBJECT, subject)
                    putExtra(Intent.EXTRA_TEXT, body)
                }, popupTitle
        ), requestCode
    )
} catch (ex: ActivityNotFoundException) {
    Logger.snackbar("There are no email clients installed.")
}
