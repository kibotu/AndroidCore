package com.exozet.android.core.extensions

import android.view.View
import io.reactivex.disposables.Disposable


fun Disposable.clearOnDetach(view: View) = view.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {

    override fun onViewAttachedToWindow(v: View?) {
    }

    override fun onViewDetachedFromWindow(v: View?) {
        if (!isDisposed)
            dispose()

        view.removeOnAttachStateChangeListener(this)
    }
})