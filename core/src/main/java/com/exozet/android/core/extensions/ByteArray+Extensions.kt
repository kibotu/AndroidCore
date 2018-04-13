@file:JvmName("ByteArrayExtensions")

package com.exozet.android.core.extensions

import net.kibotu.logger.Logger
import java.io.File
import java.io.FileOutputStream

/**
 * Created by [Jan Rabe](https://about.me/janrabe).
 */

fun ByteArray.saveAt(pathName: String): Boolean = try {
    FileOutputStream(File(pathName)).use { it.write(this) }
    true
} catch (e: Exception) {
    Logger.e(e)
    false
}
