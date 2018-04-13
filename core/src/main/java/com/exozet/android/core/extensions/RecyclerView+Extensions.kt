@file:JvmName("RecyclerViewExtensions")

package com.exozet.android.core.extensions

import android.support.v7.widget.RecyclerView
import net.kibotu.android.recyclerviewpresenter.PresenterAdapter

/**
 * Created by [Jan Rabe](https://about.me/janrabe).
 */

fun <VH> RecyclerView.Adapter<VH>.isEmpty(): Boolean where VH : RecyclerView.ViewHolder {
    return itemCount == 0
}

fun <T> PresenterAdapter<T>.forEach(block: (T) -> Unit) {
    (0 until itemCount).forEach { block(get(it)) }
}

fun <T> PresenterAdapter<T>.forEachIndexed(block: (Int, T) -> Unit) {
    (0 until itemCount).forEachIndexed { index, t -> block(index, get(t)) }
}
