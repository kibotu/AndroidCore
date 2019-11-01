package com.exozet.android.core.storage

import android.content.Context
import com.exozet.android.core.extensions.lazyFast
import com.exozet.android.core.extensions.safeContext
import com.github.florent37.application.provider.application
import de.adorsys.android.securestoragelibrary.SecurePreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


/**
 * https://github.com/adorsys/secure-storage-android?utm_source=android-arsenal.com&utm_medium=referral&utm_campaign=5648
 */
private class SecureStorageDelegate<T>(
    private val context: Context,
    private val key: String,
    private val defaultValue: T
) : ReadWriteProperty<Any, T> {

    private val safeContext: Context by lazyFast { context.safeContext() }

    @Suppress("IMPLICIT_CAST_TO_ANY", "UNCHECKED_CAST")
    override fun getValue(thisRef: Any, property: KProperty<*>): T = when (defaultValue) {
        is Boolean -> SecurePreferences.getBooleanValue(context, key, defaultValue)
        is Int -> SecurePreferences.getIntValue(context, key, defaultValue)
        is Long -> SecurePreferences.getLongValue(context, key, defaultValue)
        is Float -> SecurePreferences.getFloatValue(context, key, defaultValue)
        is String -> safeContext.getLongStringValue(key) ?: defaultValue
        else -> throw IllegalArgumentException()
    } as T

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) = when (defaultValue) {
        is Boolean -> SecurePreferences.setValue(safeContext, key, value as Boolean)
        is Int -> SecurePreferences.setValue(safeContext, key, value as Int)
        is Long -> SecurePreferences.setValue(safeContext, key, value as Long)
        is Float -> SecurePreferences.setValue(safeContext, key, value as Float)
        is String -> safeContext.setLongStringValue(key, value as String)
        else -> throw IllegalArgumentException()
    }
}

fun <T> secureStorage(key: String, defaultValue: T): ReadWriteProperty<Any, T> = SecureStorageDelegate(application!!, key = key, defaultValue = defaultValue)

fun <T> secureStorage(context: Context = application!!, key: String, defaultValue: T): ReadWriteProperty<Any, T> = SecureStorageDelegate(context, key = key, defaultValue = defaultValue)