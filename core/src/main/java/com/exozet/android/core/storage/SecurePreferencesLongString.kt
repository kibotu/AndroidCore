package com.exozet.android.core.storage

import de.adorsys.android.securestoragelibrary.SecurePreferences

object SecurePreferencesLongString {

    private const val chunkSize = 100

    private fun getNumberOfChunksKey(key: String) = "${key}_numberOfChunks"

    fun setLongStringValue(key: String, value: String) {
        val chunks = value.chunked(chunkSize)

        SecurePreferences.setValue(getNumberOfChunksKey(key), chunks.size)

        chunks.forEachIndexed { index, chunk ->
            SecurePreferences.setValue("$key$index", chunk)
        }
    }

    fun getLongStringValue(key: String): String? {
        val numberOfChunks = SecurePreferences.getIntValue(getNumberOfChunksKey(key), 0)

        if (numberOfChunks == 0) {
            return null
        }

        return (0 until numberOfChunks)
            .map { index ->
                val string = SecurePreferences.getStringValue("$key$index", null) ?: run {
                    return null
                }

                string
            }.reduce { accumulator, chunk -> accumulator + chunk }
    }

    fun removeLongStringValue(key: String) {
        val numberOfChunks = SecurePreferences.getIntValue(getNumberOfChunksKey(key), 0)

        (0 until numberOfChunks).map { SecurePreferences.removeValue("$key$it") }
        SecurePreferences.removeValue(getNumberOfChunksKey(key))
    }

    fun containsLongStringValue(key: String): Boolean {
        return SecurePreferences.contains(getNumberOfChunksKey(key))
    }
}