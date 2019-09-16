@file:JvmName("IntExtensions")

package com.exozet.android.core.extensions

import kotlin.random.Random


fun Int.sleep() = this.toLong().sleep()

fun Long.sleep() = Thread.sleep(this)

fun Int.randomStrings(): String {
    val randomStringBuilder = StringBuilder()
    var tempChar: Char
    for (i in 0 until this) {
        tempChar = (Random.nextInt(96) + 32).toChar()
        randomStringBuilder.append(tempChar)
    }
    return randomStringBuilder.toString()
}

inline fun <reified T> Int.repeat(factory: (index: Int) -> T) = arrayListOf<T>().apply { for (i in 0..this@repeat) add(factory(i)) }