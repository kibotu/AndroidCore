package com.exozet.android.core.extensions


inline fun <reified T> tryCatch(function: () -> T) = try {
    function()
} catch (e: Exception) {
    null
}