@file:JvmName("MathExtensions")

package com.exozet.android.core.extensions

fun Short.clamp(min: Short, max: Short): Short {
    if (this < min) return min
    return if (this > max) max else this
}

fun Int.clamp(min: Int, max: Int): Int {
    if (this < min) return min
    return if (this > max) max else this
}

fun Long.clamp(min: Long, max: Long): Long {
    if (this < min) return min
    return if (this > max) max else this
}

fun Float.clamp(min: Float, max: Float): Float {
    if (this < min) return min
    return if (this > max) max else this
}

fun Double.clamp(min: Double, max: Double): Double {
    if (this < min) return min
    return if (this > max) max else this
}