@file:JvmName("RecyclerViewExtensions")

package com.exozet.android.core.extensions

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator

/**
 * Created by [Jan Rabe](https://about.me/janrabe).
 */

val <VH> RecyclerView.Adapter<VH>.isEmpty: Boolean where VH : RecyclerView.ViewHolder
    get() = itemCount == 0


var RecyclerView.supportsChangeAnimations: Boolean
    get() = (itemAnimator as SimpleItemAnimator).supportsChangeAnimations
    set(value) {
        (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = value
    }