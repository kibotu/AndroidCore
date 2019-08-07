@file:JvmName("StringExtensions")

package com.exozet.android.core.extensions

import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import android.text.TextUtils.isEmpty
import com.crashlytics.android.Crashlytics
import com.exozet.android.core.utils.ByteExtensions.validUTF8
import io.fabric.sdk.android.services.network.HttpRequest
import net.kibotu.ContextHelper
import net.kibotu.logger.Logger
import okio.ByteString
import okio.ByteString.Companion.decodeBase64
import okio.ByteString.Companion.encodeUtf8
import java.io.UnsupportedEncodingException
import java.math.BigInteger
import java.nio.charset.Charset
import java.security.MessageDigest
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

/**
 * Created by [Jan Rabe](https://about.me/janrabe).
 */

fun String.sha256(charset: Charset = Charsets.UTF_8): String = "%064x".format(BigInteger(1, with(MessageDigest.getInstance("SHA-256")) {
    update(toByteArray(charset))
    digest()
}))

fun String.sha256_HMAC(): String = with(MessageDigest.getInstance("SHA-256")) {
    update(this@sha256_HMAC.toByteArray())
    HttpRequest.Base64.encodeBytes(digest())
}

fun String.capitalize() = when {
    length < 2 -> toUpperCase()
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
        val byteString = decodeBase64()
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
        val utf8 = encodeUtf8().base64()
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
    ContextHelper.getActivity()?.startActivity(
        Intent.createChooser(
            Intent()
                .apply {
                    action = Intent.ACTION_SEND
                    type = "text/plain"
                    putExtra(Intent.EXTRA_SUBJECT, "")
                    putExtra(Intent.EXTRA_TEXT, this)
                }, "Save Data with:"
        )
    )
}

/**
 * Phone Number to be opened in dialpad.
 */
fun String.openDialPad() {
    if (!android.util.Patterns.PHONE.matcher(this).matches()) {
        Crashlytics.logException(Throwable("$this is no valid phone number."))
    }

    val uri = Uri.parse("tel:$this")
    Logger.v("[openDialPad] $this -> $uri")

    Intent(Intent.ACTION_DIAL)
        .apply {
            data = uri
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        .also { intent ->
            intent.resolveActivity(
                ContextHelper.getApplication()?.packageManager
                    ?: return
            )?.let {
                ContextHelper.getActivity()?.startActivity(Intent.createChooser(intent, this))
            }
        }
}

fun clamp(s: String?, maxLength: Int): String {
    return if (isEmpty(s)) "" else s!!.substring(0, s.length.clamp(0, maxLength))
}

/**
 * http://stackoverflow.com/a/1447720
 */
fun fixEncoding(latin1: String): String {
    try {
        val bytes = latin1.toByteArray(charset("ISO-8859-1"))
        return if (!validUTF8(bytes)) latin1 else String(bytes, Charsets.UTF_8)
    } catch (e: UnsupportedEncodingException) {
        // Impossible, throw unchecked
        throw IllegalStateException("No Latin1 or UTF-8: " + e.message)
    }

}

fun surroundWithQuotes(msg: String) = String.format("\"%s\"", "" + msg)

fun escape(s: String): String {
    return if (isEmpty(s))
        ""
    else
        s.replace("\n", "\\n").replace("\r", "\\r")
}

fun length(string: String?) = if (isEmpty(string)) 0 else string!!.length

/**
 * Returns `true` if this not null or empty.
 */
@UseExperimental(ExperimentalContracts::class)
inline fun CharSequence?.isNotNullOrEmpty(): Boolean {
    contract {
        returns(true) implies (this@isNotNullOrEmpty != null)
    }

    return this != null && this.isNotEmpty()
}