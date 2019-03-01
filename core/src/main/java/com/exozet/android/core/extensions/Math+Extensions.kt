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
/**
 * Calculates the percentage of a value in a given range.
 *
 * @param value Input value.
 * @param min   - Min Range Value.
 * @param max   - Max Range Value.
 * @return Percentage of a given value in a range. If within range, returned values are between [0f,1f]
 */
fun Float.valueToPercentOfRange(min: Float = 0f, max: Float) = (this - min) / (max - min)

/**
 * Calculates a value in a given range by percentage.
 *
 * @param percent - Percentage. Expecting a value [0f,1f] if expected to be in range.
 * @param min     - Min Range Value.
 * @param max     - Max Range Value.
 * @return Concrete value by a given percentage. If percentage value [0f, 1f] then the returned value will be in [min, max] range.
 */
fun Float.percentToValueOfRange(min: Float = 0f, max: Float) = min + this * (max - min)