@file:JvmName("StringExtensions")

package com.exozet.android.core.extensions

import com.exozet.android.core.provider.GsonProvider
import java.math.BigInteger
import java.nio.charset.Charset
import java.security.MessageDigest

/**
 * Created by [Jan Rabe](https://about.me/janrabe).
 */

fun Any.toJson(): String = GsonProvider.gson.toJson(this)

fun Any.toJsonPrettyPrinting(): String = GsonProvider.gsonPrettyPrinting.toJson(this)

fun String.sha256(charset: Charset = Charsets.UTF_8): String = "%064x".format(BigInteger(1, with(MessageDigest.getInstance("SHA-256")) {
    update(toByteArray(charset))
    digest()
}))

fun String.capitalize() = when { length < 2 -> toUpperCase()
    else -> Character.toUpperCase(toCharArray()[0]) + substring(1).toLowerCase()
}