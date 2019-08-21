package com.exozet.android.core.base

import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import io.reactivex.disposables.Disposable

abstract class ViewHolder(var itemView: ViewGroup?) : Disposable {

    @get:LayoutRes
    protected abstract val layout: Int

    protected var disposed = false

    override fun isDisposed() = disposed

    @CallSuper
    override fun dispose() {
        itemView = null
        disposed = true
    }
}