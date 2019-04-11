@file:JvmName("ResourceExtensions")

package com.exozet.android.core.extensions

import android.animation.Animator
import android.animation.AnimatorInflater
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.JELLY_BEAN_MR1
import android.os.Build.VERSION_CODES.N
import android.text.Html
import android.text.Spanned
import android.util.TypedValue
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.exozet.android.core.R
import net.kibotu.ContextHelper
import net.kibotu.ContextHelper.getApplication
import net.kibotu.logger.Logger
import java.util.*


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
val Float.px: Float get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, ContextHelper.getContext()!!.resources.displayMetrics)

/**
 * Converts pixel to dp.
 */
val Float.dp: Float get() = this / Resources.getSystem().displayMetrics.density

val Int.px: Int get() = toFloat().px.toInt()

val Int.dp: Int get() = toFloat().dp.toInt()

val Int.sp: Float
    get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this.toFloat(), ContextHelper.getContext()!!.resources.displayMetrics)

val Float.sp: Float
    get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this, ContextHelper.getContext()!!.resources.displayMetrics)

var TextView.sp: Float
    set(value) = setTextSize(TypedValue.COMPLEX_UNIT_SP, value)
    get() = textSize.sp

val Int.resBoolean: Boolean
    get() = ContextHelper.getApplication()!!.resources!!.getBoolean(this)

val Int.resInt: Int
    get() = ContextHelper.getApplication()!!.resources!!.getInteger(this)

val Int.resLong: Long
    get() = ContextHelper.getApplication()!!.resources!!.getInteger(this).toLong()

val Int.resDimension: Float
    get() = ContextHelper.getApplication()!!.resources!!.getDimension(this)

val Int.resString: String
    get() = ContextHelper.getApplication()!!.resources!!.getString(this)

inline fun <reified T> Int.resString(formatArg: T): String = ContextHelper.getApplication()!!.resources!!.getString(this, formatArg)

val Int.resColor: Int
    get() = ContextCompat.getColor(ContextHelper.getApplication()!!, this)

val Int.html: Spanned
    get() = resString.html

val String.html: Spanned
    get() = if (SDK_INT >= N) {
        Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
    } else {
        @Suppress("DEPRECATION")
        Html.fromHtml(this)
    }

val Int.resAnim: Animation
    get() = AnimationUtils.loadAnimation(ContextHelper.getApplication(), this)

val Int.resAnimator: Animator
    get() = AnimatorInflater.loadAnimator(ContextHelper.getApplication(), this)

val Int.resName: String
    get() = ContextHelper.getContext()!!.resources.getResourceEntryName(this)

val String.resId: Int
    get() = ContextHelper.getContext()!!.resources.getIdentifier(this, "id", ContextHelper.getContext()!!.packageName)

val Int.resDrawable: Drawable
    get() = ContextCompat.getDrawable(ContextHelper.getContext()!!, this)!!

val Int.resStringArray: Array<String>
    get() = ContextHelper.getApplication()!!.resources!!.getStringArray(this)

val Int.resIntArray: IntArray
    get() = ContextHelper.getApplication()!!.resources!!.getIntArray(this)

val Int.resTextArray: Array<CharSequence>
    get() = ContextHelper.getApplication()!!.resources!!.getTextArray(this)

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

val Uri.isTelephoneLink: Boolean
    get() = toString().startsWith("tel:")

val Uri.isMailToLink: Boolean
    get() = toString().startsWith("mailto:")


/**
 * https://stackoverflow.com/a/9475663/1006741
 *
 * @param id     string resource id
 * @param locale locale
 * @return localized string
 */
fun getLocalizedString(@StringRes id: Int, locale: Locale): String {

    if (SDK_INT > JELLY_BEAN_MR1) {
        return getLocalizedResources(getApplication()!!, locale).getString(id)
    }

    val res = getApplication()!!.resources
    val conf = res.configuration
    val savedLocale = conf.locale
    conf.locale = locale // whatever you want here
    res.updateConfiguration(conf, null) // second arg null means don't change

    // retrieve resources from desired locale
    val text = res.getString(id)

    // restore original locale
    conf.locale = savedLocale
    res.updateConfiguration(conf, null)

    return text
}

@RequiresApi(api = JELLY_BEAN_MR1)
fun getLocalizedResources(context: Context, desiredLocale: Locale): Resources {
    var conf = context.resources.configuration
    conf = Configuration(conf)
    conf.setLocale(desiredLocale)
    val localizedContext = context.createConfigurationContext(conf)
    return localizedContext.resources
}
