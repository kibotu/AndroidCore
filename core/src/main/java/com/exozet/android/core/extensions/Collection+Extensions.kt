@file:JvmName("CollectionExtensions")

package com.exozet.android.core.extensions

import java.util.*

/**
 * Created by [Jan Rabe](https://about.me/janrabe).
 */


fun <E> Collection<E>.isLast(position: Int) = position == size - 1


inline fun <reified T> T.addToList(list: MutableList<T>): T = this.apply {
    list.add(this)
}

fun Collection<*>?.indexOutOfBounds(index: Int): Boolean {
    return this == null || index < 0 || index > this.size - 1
}

fun <E> java.util.Collection<E>.removeInRange(position: Int, count: Int) {
    this.removeAll(drop(position).take(count))
}

fun <E> java.util.List<E>.removeInRange(position: Int, count: Int) {
    this.removeAll(drop(position).take(count))
}

fun <E> java.util.ArrayList<E>.removeInRange(position: Int, count: Int) {
    this.removeAll(drop(position).take(count))
}

fun <T> Collection<T>.intersect(other: MutableCollection<out T>): Collection<T> {
    val result = ArrayList<T>()
    for (t in this) {
        if (other.remove(t)) result.add(t)
    }
    return result
}

fun Collection<*>?.isEmpty() = this == null || this.isEmpty()