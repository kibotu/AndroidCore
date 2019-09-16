@file:JvmName("ResourceExtensions")

package com.exozet.android.core.extensions

import android.animation.Animator
import android.animation.AnimatorInflater
import android.annotation.SuppressLint
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
import androidx.annotation.*
import androidx.annotation.IntRange
import androidx.core.content.ContextCompat
import androidx.core.content.res.use
import com.exozet.android.core.R
import com.github.florent37.application.provider.application
import net.kibotu.ContextHelper
import net.kibotu.ContextHelper.getApplication
import net.kibotu.logger.Logger
import net.kibotu.resourceextension.*
import net.kibotu.resourceextension.dp
import net.kibotu.resourceextension.px
import net.kibotu.resourceextension.resBoolean
import net.kibotu.resourceextension.sp
import java.io.ByteArrayOutputStream
import java.util.*


/**
 * Created by [Jan Rabe](https://about.me/janrabe).
 */

/**
 * Converts dp to pixel.
 */
@Deprecated("use Float#dp", ReplaceWith("dp", "net.kibotu.resourceextension.dp"))
val Float.px: Float
    get() = this.dp

/**
 * Converts pixel to dp.
 */
@Deprecated("use Float#px", ReplaceWith("px", "net.kibotu.resourceextension.px"))
val Float.dp: Float
    get() = this.px

@Deprecated("use Int#dp", ReplaceWith("dp", "net.kibotu.resourceextension.dp"))
val Int.px: Int
    get() = this.dp

@Deprecated("use Int#px", ReplaceWith("px", "net.kibotu.resourceextension.px"))
val Int.dp: Int
    get() = this.px

@Deprecated("use Int#sp", ReplaceWith("sp", "net.kibotu.resourceextension.sp"))
val Int.sp: Float
    get() = this.sp

@Deprecated("use Float#sp", ReplaceWith("sp", "net.kibotu.resourceextension.sp"))
val Float.sp: Float
    get() = this.sp

@Deprecated("use TextView#sp", ReplaceWith("sp", "net.kibotu.resourceextension.sp"))
var TextView.sp: Float
    set(value) { this.sp = value }
    get() = this.sp

@Deprecated("use Int#resBoolean", ReplaceWith("resBoolean", "net.kibotu.resourceextension.resBoolean"))
val Int.resBoolean: Boolean
    get() = ContextHelper.getApplication()!!.resources!!.getBoolean(this)

@Deprecated("use Int#resInt", ReplaceWith("resInt", "net.kibotu.resourceextension.resInt"))
val Int.resInt: Int
    get() = ContextHelper.getApplication()!!.resources!!.getInteger(this)

@Deprecated("use Int#resLong", ReplaceWith("resLong", "net.kibotu.resourceextension.resLong"))
val Int.resLong: Long
    get() = ContextHelper.getApplication()!!.resources!!.getInteger(this).toLong()

@Deprecated("use Int#resDimension", ReplaceWith("resDimension", "net.kibotu.resourceextension.resDimension"))
val Int.resDimension: Float
    get() = ContextHelper.getApplication()!!.resources!!.getDimension(this)

@Deprecated("use Int#resString", ReplaceWith("resString", "net.kibotu.resourceextension.resString"))
val Int.resString: String
    get() = ContextHelper.getApplication()!!.resources!!.getString(this)

@Deprecated("use Int#resString", ReplaceWith("resString", "net.kibotu.resourceextension.resString"))
inline fun <reified T> Int.resString(formatArg: T): String = ContextHelper.getApplication()!!.resources!!.getString(this, formatArg)

@Deprecated("use Int#resColor", ReplaceWith("resColor", "net.kibotu.resourceextension.resColor"))
val Int.resColor: Int
    get() = ContextCompat.getColor(ContextHelper.getApplication()!!, this)

@Deprecated("use Int#html", ReplaceWith("html", "net.kibotu.resourceextension.html"))
val Int.html: Spanned
    get() = resString.html

@Deprecated("use String#html", ReplaceWith("html", "net.kibotu.resourceextension.html"))
val String.html: Spanned
    get() = if (SDK_INT >= N) {
        Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
    } else {
        @Suppress("DEPRECATION")
        Html.fromHtml(this)
    }

@Deprecated("use Int#resAnim", ReplaceWith("resAnim", "net.kibotu.resourceextension.resAnim"))
val Int.resAnim: Animation
    get() = AnimationUtils.loadAnimation(ContextHelper.getApplication(), this)

@Deprecated("use Int#resAnimator", ReplaceWith("resAnimator", "net.kibotu.resourceextension.resAnimator"))
val Int.resAnimator: Animator
    get() = AnimatorInflater.loadAnimator(ContextHelper.getApplication(), this)

@Deprecated("use Int#resName", ReplaceWith("resName", "net.kibotu.resourceextension.resName"))
val Int.resName: String
    get() = ContextHelper.getContext()!!.resources.getResourceEntryName(this)

@Deprecated("use Int#resId", ReplaceWith("resId", "net.kibotu.resourceextension.resId"))
val String.resId: Int
    get() = ContextHelper.getContext()!!.resources.getIdentifier(this, "id", ContextHelper.getContext()!!.packageName)

@Deprecated("use Int#resDrawable", ReplaceWith("resDrawable", "net.kibotu.resourceextension.resDrawable"))
val Int.resDrawable: Drawable
    get() = ContextCompat.getDrawable(ContextHelper.getContext()!!, this)!!

@Deprecated("use String#resDrawableId", ReplaceWith("resDrawableId", "net.kibotu.resourceextension.resDrawableId"))
val String.resDrawableId: Int
    get() = ContextHelper.getContext()!!.resources.getIdentifier(this, "drawable", ContextHelper.getContext()!!.packageName)

@Deprecated("use Int#resStringArray", ReplaceWith("resStringArray", "net.kibotu.resourceextension.resStringArray"))
val Int.resStringArray: Array<String>
    get() = ContextHelper.getApplication()!!.resources!!.getStringArray(this)

@Deprecated("use Int#resIntArray", ReplaceWith("resIntArray", "net.kibotu.resourceextension.resIntArray"))
val Int.resIntArray: IntArray
    get() = ContextHelper.getApplication()!!.resources!!.getIntArray(this)

@Deprecated("use Int#resTextArray", ReplaceWith("resTextArray", "net.kibotu.resourceextension.resTextArray"))
val Int.resTextArray: Array<CharSequence>
    get() = ContextHelper.getApplication()!!.resources!!.getTextArray(this)

@Deprecated("use Int#csv", ReplaceWith("csv", "net.kibotu.resourceextension.csv"))
fun Int.asCsv(context: Context = ContextHelper.getContext()!!): List<String> = context.resources.getString(this).split(",").map(String::trim).toList()

/**
 * Returns -1 if not found
 */
@ColorRes
@Deprecated("use Int#resColorArray", ReplaceWith("csv", "net.kibotu.resourceextension.resColorArray"))
fun @receiver:ColorRes Int.resColorArray(@IntRange(from = 0) index: Int): Int {
    val array = ContextHelper.getApplication()!!.resources.obtainTypedArray(this)
    val resourceId = array.getResourceId(index, -1)
    array.recycle()
    return resourceId
}

/**
 * Returns -1 if not found
 */
@Deprecated("use Int#resDrawableIdArray", ReplaceWith("resDrawableIdArray", "net.kibotu.resourceextension.resDrawableIdArray"))
val @receiver:DrawableRes Int.resDrawableArray: List<Int>
    @SuppressLint("Recycle")
    get() = ContextHelper.getApplication()!!.resources.obtainTypedArray(this).use { array ->
        (0 until array.length()).map { array.getResourceId(it, -1) }
    }

/**
 * Returns -1 if not found
 */
@Deprecated("use Int#resColorIntArray", ReplaceWith("resColorIntArray", "net.kibotu.resourceextension.resColorIntArray"))
val @receiver:ColorRes Int.resColorArray: List<Int>
    @SuppressLint("Recycle")
    get() = ContextHelper.getApplication()!!.resources.obtainTypedArray(this).use { array ->
        (0 until array.length()).map { array.getResourceId(it, -1) }
    }

/**
 * Returns -1 if not found
 */
@Deprecated("use Int#resDrawableArray", ReplaceWith("resDrawableArray", "net.kibotu.resourceextension.resDrawableArray"))
fun Int.resDrawableArray(@IntRange(from = 0) index: Int): Int {
    val array = ContextHelper.getApplication()!!.resources.obtainTypedArray(this)
    val resourceId = array.getResourceId(index, -1)
    array.recycle()
    return resourceId
}

@Deprecated("use screenWidthDp", ReplaceWith("screenWidthDp", "net.kibotu.resourceextension.screenWidthDp"))
val screenWidthtDp
    get() = Resources.getSystem().configuration.screenWidthDp

@Deprecated("use screenHeightDp", ReplaceWith("screenHeightDp", "net.kibotu.resourceextension.screenHeightDp"))
val screenHeightDp
    get() = Resources.getSystem().configuration.screenHeightDp

@Deprecated("use screenWidthPixels", ReplaceWith("screenWidthPixels", "net.kibotu.resourceextension.screenWidthPixels"))
val screenWidthPixels
    get() = Resources.getSystem().displayMetrics.widthPixels

@Deprecated("use screenHeightPixels", ReplaceWith("screenHeightPixels", "net.kibotu.resourceextension.screenHeightPixels"))
val screenHeightPixels
    get() = Resources.getSystem().displayMetrics.heightPixels

@Deprecated("use isRightToLeft", ReplaceWith("isRightToLeft", "net.kibotu.resourceextension.isRightToLeft"))
fun isRightToLeft(): Boolean = R.bool.rtl.resBoolean

@StringRes
@Deprecated("use fromStringResource", ReplaceWith("fromStringResource", "net.kibotu.resourceextension.fromStringResource"))
fun String.fromStringResource(onError: ((Exception) -> Unit)? = null): Int {
    try {
        return ContextHelper.getApplication()!!.resources.getIdentifier(this, "string", ContextHelper.getApplication()!!.packageName)
    } catch (e: Exception) {
        onError?.invoke(e)
    }
    return 0
}

@Deprecated("use stringFromAssets", ReplaceWith("stringFromAssets", "net.kibotu.resourceextension.stringFromAssets"))
fun String.stringFromAssets(): String = try {
    ContextHelper.getApplication()!!.assets.open(this).bufferedReader().use { it.readText() }
} catch (e: Exception) {
    Logger.e(e)
    ""
}

@DrawableRes
@Deprecated("use fromDrawableResource", ReplaceWith("fromDrawableResource", "net.kibotu.resourceextension.fromDrawableResource"))
fun String.fromDrawableResource(onError: ((Exception) -> Unit)? = null): Int {
    try {
        return ContextHelper.getApplication()!!.resources.getIdentifier(this, "drawable", ContextHelper.getApplication()!!.packageName)
    } catch (e: Exception) {
        onError?.invoke(e)
    }
    return 0
}
@Deprecated("use BUFFER_SIZE", ReplaceWith("BUFFER_SIZE", "net.kibotu.resourceextension.BUFFER_SIZE"))

private val BUFFER_SIZE by lazy { 16 * 1024 }

@Deprecated("use copyBuffer", ReplaceWith("copyBuffer", "net.kibotu.resourceextension.copyBuffer"))
private val copyBuffer by lazy { ThreadLocal<ByteArray>() }

/**
 * Thread-Safe
 */
@Deprecated("use bytesFromAssets", ReplaceWith("bytesFromAssets", "net.kibotu.resourceextension.bytesFromAssets"))
fun String.bytesFromAssets(context: Context? = application, onError: ((Exception) -> Unit)? = null): ByteArray? = try {

    context?.assets?.open(this)?.use { inputStream ->

        ByteArrayOutputStream().use { buffer ->

            var byteBuffer = copyBuffer.get()
            if (byteBuffer == null) {
                byteBuffer = ByteArray(BUFFER_SIZE)
                copyBuffer.set(byteBuffer)
            }

            var readBytes: Int
            do {
                readBytes = inputStream.read(byteBuffer, 0, byteBuffer.size)
                if (readBytes != -1)
                    buffer.write(byteBuffer, 0, readBytes)
            } while (readBytes != -1)

            buffer.flush()

            buffer.toByteArray()
        }
    }

} catch (e: Exception) {
    onError?.invoke(e)
    null
}

@Deprecated("use isTelephoneLink", ReplaceWith("isTelephoneLink", "net.kibotu.resourceextension.isTelephoneLink"))
val Uri.isTelephoneLink: Boolean
    get() = toString().startsWith("tel:")

@Deprecated("use isMailToLink", ReplaceWith("isMailToLink", "net.kibotu.resourceextension.isMailToLink"))
val Uri.isMailToLink: Boolean
    get() = toString().startsWith("mailto:")

/**
 * https://stackoverflow.com/a/9475663/1006741
 *
 * @param id     string resource id
 * @param locale locale
 * @return localized string
 */
@Deprecated("use localizedString", ReplaceWith("localizedString", "net.kibotu.resourceextension.localizedString"))
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
@Deprecated("use localizedResources", ReplaceWith("localizedResources", "net.kibotu.resourceextension.localizedResources"))
fun getLocalizedResources(context: Context, desiredLocale: Locale): Resources {
    var conf = context.resources.configuration
    conf = Configuration(conf)
    conf.setLocale(desiredLocale)
    val localizedContext = context.createConfigurationContext(conf)
    return localizedContext.resources
}
