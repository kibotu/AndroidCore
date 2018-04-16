@file:JvmName("RxJavaExtensions")
package com.exozet.android.core.extensions

import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by armando.shkurti on 31.01.18.
 */
fun <T> Flowable<T>.applySchedulersIO(): Flowable<T> {
    return subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}

fun <T> Flowable<T>.applySchedulersComputation(): Flowable<T> {
    return subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
}

fun <T> Flowable<T>.subscribeUIToIO(onNext: (T) -> Unit, onError: (Throwable) -> Unit): Disposable {
    return applySchedulersIO().subscribe({ next ->
        onNext(next)
    }, { error ->
        onError(error)
    })
}

fun <T> Flowable<T>.subscribeUIToComputation(onNext: (T) -> Unit, onError: (Throwable) -> Unit): Disposable {
    return applySchedulersComputation().subscribe({ next ->
        onNext(next)
    }, { error ->
        onError(error)
    })
}