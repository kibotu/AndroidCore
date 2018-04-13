@file:JvmName("CollectionExtensions")

package com.exozet.android.core.extensions

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
