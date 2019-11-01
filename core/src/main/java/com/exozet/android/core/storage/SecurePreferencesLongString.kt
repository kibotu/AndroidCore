package com.exozet.android.core.storage

import android.content.Context
import de.adorsys.android.securestoragelibrary.SecurePreferences


private const val chunkSize = 240

private fun getNumberOfChunksKey(key: String) = "${key}_numberOfChunks"

internal fun Context.setLongStringValue(key: String, value: String) {
    val chunks = value.chunked(chunkSize)

    SecurePreferences.setValue(this, getNumberOfChunksKey(key), chunks.size)

    chunks.forEachIndexed { index, chunk ->
        SecurePreferences.setValue(this, "$key$index", chunk)
    }
}

internal fun Context.getLongStringValue(key: String): String? {
    val numberOfChunks = SecurePreferences.getIntValue(this, getNumberOfChunksKey(key), 0)

    if (numberOfChunks == 0) {
        return null
    }

    return (0 until numberOfChunks)
        .map { index ->
            val string = SecurePreferences.getStringValue(this, "$key$index", null) ?: run {
                return null
            }

            string
        }.reduce { accumulator, chunk -> accumulator + chunk }
}

internal fun Context.removeLongStringValue(key: String) {
    val numberOfChunks = SecurePreferences.getIntValue(this, getNumberOfChunksKey(key), 0)

    (0 until numberOfChunks).map { SecurePreferences.removeValue(this, "$key$it") }
    SecurePreferences.removeValue(this, getNumberOfChunksKey(key))
}

internal fun Context.containsLongStringValue(key: String): Boolean = SecurePreferences.contains(this, getNumberOfChunksKey(key))
