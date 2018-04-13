@file:JvmName("ResourceExtensions")

package com.exozet.android.core.extensions

import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.support.v4.content.ContextCompat
import android.text.Html
import android.text.Spanned
import com.exozet.android.core.R
import net.kibotu.ContextHelper
import net.kibotu.logger.Logger


/**
 * Created by [Jan Rabe](https://about.me/janrabe).
 */

@Deprecated("use Int#resBoolean", ReplaceWith("resBoolean"))
fun Int.asBoolean(default: Boolean = false): Boolean = ContextHelper.getApplication()!!.resources?.getBoolean(this)
        ?: default

@Deprecated("use Int#resInt", ReplaceWith("resInt"))
fun Int.asInteger(): Int = ContextHelper.getApplication()!!.resources.getInteger(this)

@Deprecated("use Int#resInt.toLong()", ReplaceWith("resInt.toLong()"))
fun Int.asLong(): Long = resInt.toLong()

fun Int.asDimens(): Float = ContextHelper.getApplication()!!.resources.getDimension(this)

@Deprecated("use Int#resString", ReplaceWith("resString"))
fun Int.asString(): String = ContextHelper.getApplication()!!.resources.getString(this)

@Deprecated("use Int#resColor", ReplaceWith("resColor"))
fun Int.asColor(): Int = ContextCompat.getColor(ContextHelper.getApplication()!!, this)

/**
 * Converts dp to pixel.
 */
val Int.px: Int get() = (this * Resources.getSystem().displayMetrics.density).toInt()

/**
 * Converts pixel to dp.
 */
val Int.dp: Int get() = (this / Resources.getSystem().displayMetrics.density).toInt()

val Int.resBoolean: Boolean
    get() {
        return ContextHelper.getApplication()!!.resources!!.getBoolean(this)
    }

val Int.resInt: Int
    get() {
        return ContextHelper.getApplication()!!.resources!!.getInteger(this)
    }

val Int.resLong: Long
    get() {
        return ContextHelper.getApplication()!!.resources!!.getInteger(this).toLong()
    }

val Int.resDimension: Float
    get() {
        return ContextHelper.getApplication()!!.resources!!.getDimension(this)
    }

val Int.resString: String
    get() {
        return ContextHelper.getApplication()!!.resources!!.getString(this)
    }

val Int.resColor: Int
    get() {
        return ContextCompat.getColor(ContextHelper.getApplication()!!, this)
    }

val Int.html: Spanned
    get() {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(resString, Html.FROM_HTML_MODE_LEGACY)
        } else {
            @Suppress("DEPRECATION")
            Html.fromHtml(resString)
        }
    }

fun Int.asCsv(context: Context = ContextHelper.getContext()!!): List<String> = context.resources.getString(this).split(",").map(String::trim).toList()

fun isRightToLeft(): Boolean = R.bool.rtl.resBoolean

inline fun <reified T> Int.times(factory: () -> T) = arrayListOf<T>().apply { for (i in 0..this@times) add(factory()) }

@StringRes
fun String.fromStringResource(): Int {
    try {
        return ContextHelper.getApplication()!!.resources.getIdentifier(this, "string", ContextHelper.getApplication()!!.packageName)
    } catch (e: Exception) {
        Logger.e(e)
    }
    return 0
}

fun String.stringFromAssets(): String = try {
    ContextHelper.getApplication()!!.assets.open(this).bufferedReader().use { it.readText() }
} catch (e: Exception) {
    Logger.e(e)
    ""
}

@DrawableRes
fun String.fromDrawableResource(): Int {
    try {
        return ContextHelper.getApplication()!!.resources.getIdentifier(this, "drawable", ContextHelper.getApplication()!!.packageName)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return 0
}

fun String.bytesFromAssets(): ByteArray? = try {
    ContextHelper.getApplication()!!.assets.open(this).use { ByteArray(it.available()).apply { it.read(this) } }
} catch (e: Exception) {
    Logger.e(e)
    null
}
