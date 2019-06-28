@file:JvmName("BundleExtensions")


package com.exozet.android.core.extensions

import android.os.Bundle
import net.kibotu.logger.Logger.logv
import net.kibotu.logger.TAG


fun Bundle.printBundle() {
    for (key in keySet()) {
        val value = get(key)
        logv { message }
    }
}

val Bundle.string
    get() = with(StringBuilder()) {
        append("{")
        for (key in keySet()) {
            val value = get(key)
            append(key).append(":").append(value)
        }
        append("}")
    }.toString()