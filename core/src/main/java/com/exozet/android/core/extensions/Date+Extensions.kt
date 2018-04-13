@file:JvmName("DateExtensions")

package com.exozet.android.core.extensions

import android.annotation.SuppressLint
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import net.kibotu.ContextHelper
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by [Jan Rabe](https://about.me/janrabe).
 */

/**
 * https://stackoverflow.com/a/43550290
 */
@SuppressLint("SimpleDateFormat")
fun Date?.localize(): String {
    if (this == null)
        return ""

    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2) {
        val pattern = android.text.format.DateFormat.getBestDateTimePattern(Locale.getDefault(), "EEEEMMMMdyyyy") //order on the String doesn't matter
        val formatter = SimpleDateFormat(pattern, Locale.getDefault())

        return formatter.format(this)
    }

    val format = Settings.System.getString(ContextHelper.getApplication()!!.contentResolver, Settings.System.DATE_FORMAT)
    val formatter = if (TextUtils.isEmpty(format))
        android.text.format.DateFormat.getMediumDateFormat(ContextHelper.getApplication())
    else
        SimpleDateFormat(format)

    return formatter.format(this)
}