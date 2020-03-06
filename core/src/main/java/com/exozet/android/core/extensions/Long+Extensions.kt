package com.exozet.android.core.extensions

import java.util.*
import kotlin.math.abs


/**
 * https://stackoverflow.com/a/3758880/1006741
 */
fun Long.humanReadableByteCountBinary(): String {
    val b = when (this) {
        Long.MIN_VALUE -> Long.MAX_VALUE
        else -> abs(this)
    }
    return when {
        b < 1024L -> "$this B"
        b <= 0xfffccccccccccccL shr 40 -> "%.1f KiB".format(Locale.UK, this / 1024.0)
        b <= 0xfffccccccccccccL shr 30 -> "%.1f MiB".format(Locale.UK, this / 1048576.0)
        b <= 0xfffccccccccccccL shr 20 -> "%.1f GiB".format(Locale.UK, this / 1.073741824E9)
        b <= 0xfffccccccccccccL shr 10 -> "%.1f TiB".format(Locale.UK, this / 1.099511627776E12)
        b <= 0xfffccccccccccccL -> "%.1f PiB".format(Locale.UK, (this shr 10) / 1.099511627776E12)
        else -> "%.1f EiB".format(Locale.UK, (this shr 20) / 1.099511627776E12)
    }
}