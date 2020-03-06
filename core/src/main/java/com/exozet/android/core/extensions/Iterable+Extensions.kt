package com.exozet.android.core.extensions

import java.text.Collator
import java.util.*
import kotlin.math.absoluteValue


inline fun <reified T, reified K> Iterable<T>.mostFrequent(crossinline keySelector: (T) -> K) =
    groupingBy(keySelector).eachCount().maxBy { it.value }?.key

fun Iterable<Int>.closestValue(value: Int) = minBy { value.minus(it).absoluteValue }

inline fun <reified T> Iterable<T>.sortedWithLocale(locale: Locale = Locale.GERMAN, crossinline selector: (T) -> Comparable<*>?): List<T> {

    // ensure locale
    val comparator: Comparator<in T> = compareBy(Collator.getInstance(locale), selector)

    if (this is Collection) {
        if (size <= 1) return this.toList()
        @Suppress("UNCHECKED_CAST")
        return (toTypedArray<Any?>() as Array<T>).apply { sortWith(comparator) }.asList()
    }
    return toMutableList().apply { sortWith(comparator) }
}