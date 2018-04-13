@file:JvmName("StringExtensions")

package com.exozet.android.core.extensions

import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import com.exozet.android.core.provider.GsonProvider
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import net.kibotu.ContextHelper
import net.kibotu.logger.Logger
import okio.ByteString
import java.io.UnsupportedEncodingException
import java.math.BigInteger
import java.nio.charset.Charset
import java.security.MessageDigest

/**
 * Created by [Jan Rabe](https://about.me/janrabe).
 */

// region json

fun Any.toJson(): String = GsonProvider.gson.toJson(this)

fun Any.toJsonPrettyPrinting(): String = GsonProvider.gsonPrettyPrinting.toJson(this)

inline fun <reified T> Gson.fromJson(json: String): T = this.fromJson<T>(json, object : TypeToken<T>() {}.type)

// endregion

fun String.sha256(charset: Charset = Charsets.UTF_8): String = "%064x".format(BigInteger(1, with(MessageDigest.getInstance("SHA-256")) {
    update(toByteArray(charset))
    digest()
}))

fun String.capitalize() = when { length < 2 -> toUpperCase()
    else -> Character.toUpperCase(toCharArray()[0]) + substring(1).toLowerCase()
}

fun CharSequence?.ifIsEmpty(t: CharSequence): CharSequence = if (this == null || this.isBlank()) t else this

fun String?.ifIsEmpty(t: String): String = if (this == null || this.isBlank()) t else this

fun String.isValidEmail(): Boolean = !isNullOrEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String.saveAt(pathName: String): Boolean = toByteArray().saveAt(pathName)


fun String.decodeBase64(): String {
    return if (TextUtils.isEmpty(this)) {
        ""
    } else {
        val byteString = ByteString.decodeBase64(this)
        if (byteString == null) {
            "INVALID_UTF-8"
        } else {
            val utf8 = byteString.utf8()
            if (TextUtils.isEmpty(utf8)) "INVALID_UTF-8" else utf8
        }
    }
}

fun Char.toHex(): String {
    return Integer.toHexString(this.toInt())
}

fun String.toHex(charset: Charset = Charset.forName("UTF-8")): String {
    return try {
        String.format("%040x", BigInteger(1, toByteArray(charset)))
    } catch (e: UnsupportedEncodingException) {
        ""
    }
}

private val hexArray = "0123456789ABCDEF".toCharArray()

fun ByteArray.bytesToHex(): String {
    val hexChars = CharArray(size * 2)
    for (j in indices) {
        val v = this[j].toInt() and 0xFF
        hexChars[j * 2] = hexArray[v.ushr(4)]
        hexChars[j * 2 + 1] = hexArray[v and 0x0F]
    }
    return String(hexChars)
}

fun String.fromHexString(): ByteArray {
    if (length % 2 != 0)
        throw IllegalArgumentException("Input string must contain an even number of characters")

    val result = ByteArray(length / 2)
    val enc = toCharArray()
    var i = 0
    while (i < enc.size) {
        result[i / 2] = Integer.parseInt(enc[i].toString() + enc[i + 1], 16).toByte()
        i += 2
    }
    return result
}


fun String.encodeBase64(): String {
    return if (TextUtils.isEmpty(this)) {
        ""
    } else {
        val utf8 = ByteString.encodeUtf8(this).base64()
        if (TextUtils.isEmpty(utf8)) "INVALID_UTF-8" else utf8
    }
}

val arabicChars = charArrayOf('٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩')

fun String.asArabicNumbers(): String {
    if (TextUtils.isEmpty(this))
        return ""

    val builder = StringBuilder()
    for (i in 0 until this.length) {
        if (Character.isDigit(this[i])) {
            builder.append(arabicChars[this[i].toInt() - 48])
        } else {
            builder.append(this[i])
        }
    }
    return "" + builder.toString()
}

fun String.share() {
    Logger.v("[share] $this")
    ContextHelper.getActivity()?.startActivity(
            Intent.createChooser(Intent()
                    .apply {
                        action = Intent.ACTION_SEND
                        type = "text/plain"
                        putExtra(Intent.EXTRA_SUBJECT, "")
                        putExtra(Intent.EXTRA_TEXT, this)
                    }, "Save Data with:"))
}

/**
 * Phone Number to be opened in dialpad.
 */
fun String.openDialPad() {
    val callIntent = Intent(Intent.ACTION_VIEW)
    callIntent.data = Uri.parse("tel:$this")
    ContextHelper.getApplication()?.startActivity(callIntent)
}
