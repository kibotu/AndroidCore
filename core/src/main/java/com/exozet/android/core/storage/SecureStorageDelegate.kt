package com.exozet.android.core.storage

import de.adorsys.android.securestoragelibrary.SecurePreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


/**
 * https://github.com/adorsys/secure-storage-android?utm_source=android-arsenal.com&utm_medium=referral&utm_campaign=5648
 */
private class SecureStorageDelegate<T>(
    private val key: String,
    private val defaultValue: T
) : ReadWriteProperty<Any, T> {

    @Suppress("IMPLICIT_CAST_TO_ANY", "UNCHECKED_CAST")
    override fun getValue(thisRef: Any, property: KProperty<*>): T = when (defaultValue) {
        is Boolean -> SecurePreferences.getBooleanValue(key, defaultValue)
        is Int -> SecurePreferences.getIntValue(key, defaultValue)
        is Long -> SecurePreferences.getLongValue(key, defaultValue)
        is Float -> SecurePreferences.getFloatValue(key, defaultValue)
        is String -> SecurePreferencesLongString.getLongStringValue(key) ?: defaultValue
        else -> throw IllegalArgumentException()
    } as T

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) = when (defaultValue) {
        is Boolean -> SecurePreferences.setValue(key, value as Boolean)
        is Int -> SecurePreferences.setValue(key, value as Int)
        is Long -> SecurePreferences.setValue(key, value as Long)
        is Float -> SecurePreferences.setValue(key, value as Float)
        is String -> SecurePreferencesLongString.setLongStringValue(key, value as String)
        else -> throw IllegalArgumentException()
    }
}

fun <T> secureStorage(key: String, defaultValue: T): ReadWriteProperty<Any, T> = SecureStorageDelegate(key = key, defaultValue = defaultValue)