package com.exozet.android.core.extensions


inline fun <reified T> MutableList<T>.addNotNull(item: T?) = item?.let { add(it) }