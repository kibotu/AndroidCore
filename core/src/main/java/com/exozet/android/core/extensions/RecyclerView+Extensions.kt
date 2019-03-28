@file:JvmName("RecyclerViewExtensions")

package com.exozet.android.core.extensions

import androidx.recyclerview.widget.RecyclerView

/**
 * Created by [Jan Rabe](https://about.me/janrabe).
 */

fun <VH> RecyclerView.Adapter<VH>.isEmpty(): Boolean where VH : RecyclerView.ViewHolder {
    return itemCount == 0
}