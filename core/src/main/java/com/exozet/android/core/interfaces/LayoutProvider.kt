package com.exozet.android.core.interfaces


import androidx.annotation.LayoutRes

/**
 * Created by jan.rabe on 02/02/16.
 */
interface LayoutProvider {

    @get:LayoutRes
    val layout: Int
}