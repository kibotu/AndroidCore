package com.exozet.android.core.base

import io.reactivex.disposables.CompositeDisposable

interface CompositeDisposableHolder {

    var subscription: CompositeDisposable

    fun disposeCompositeDisposable()
}