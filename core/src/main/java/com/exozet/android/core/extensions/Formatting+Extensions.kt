@file:JvmName("FormattingExtensions")


package com.exozet.android.core.extensions

/**
 * Created by [Jan Rabe](https://about.me/janrabe).
 */

const val BYTES_TO_KB: Long = 1024
const val BYTES_TO_MB = BYTES_TO_KB * 1024
const val BYTES_TO_GB = BYTES_TO_MB * 1024
const val BYTES_TO_TB = BYTES_TO_GB * 1024

/**
 * alternative to Formatter.formatFileSize which doesn't show bytes and rounds to int
 */
fun Long.formatBytes(): String {
    if (this <= 0)
        return "0 bytes"

    return if (this / BYTES_TO_TB > 0)
        String.format("%.2f TB", this / BYTES_TO_TB.toFloat())
    else if (this / BYTES_TO_GB > 0)
        String.format("%.2f GB", this / BYTES_TO_GB.toFloat())
    else if (this / BYTES_TO_MB > 0)
        String.format("%.2f MB", this / BYTES_TO_MB.toFloat())
    else if (this / BYTES_TO_KB > 0) String.format("%.2f KB", this / BYTES_TO_KB.toFloat()) else this.toString() + " bytes"
}
