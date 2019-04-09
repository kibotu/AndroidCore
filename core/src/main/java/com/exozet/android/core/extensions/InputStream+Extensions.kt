@file:JvmName("InputStreamExtensions")

package com.exozet.android.core.extensions

import java.io.InputStream
import java.nio.charset.Charset

/**
 * Created by [Jan Rabe](https://about.me/janrabe).
 */

fun InputStream.readTextAndClose(charset: Charset = Charsets.UTF_8): String = bufferedReader(charset).use { it.readText() }
