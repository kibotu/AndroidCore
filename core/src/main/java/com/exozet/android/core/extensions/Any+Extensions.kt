@file:JvmName("AnyExtensions")

package com.exozet.android.core.extensions

import net.kibotu.logger.Logger

val Any.TAG: String
    get () = this::class.java.simpleName

fun Any.logv(message: String) = Logger.v(TAG, message)

fun Any.logd(message: String) = Logger.d(TAG, message)

fun Any.logi(message: String) = Logger.i(TAG, message)

fun Any.logw(message: String) = Logger.w(TAG, message)

fun Any.loge(message: String) = Logger.e(TAG, message)

inline infix fun Any.paul(message: String?) = logv("PAUL $message")