package com.exozet.android.core.extensions


inline fun <reified T> List<T>.circularIndex(index: Int): Int =
    if (index < 0) (index % size + size) % size
    else index % size

inline fun <reified T> List<T>.circularAt(index: Int): T = this[circularIndex(index)]