@file:JvmName("SharedPreferenceDelegate")

package com.exozet.android.core.delegates

/**
 * Created by [Jan Rabe](https://about.me/janrabe).
 */

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.exozet.android.core.R
import com.exozet.android.core.extensions.resString
import com.exozet.android.core.provider.GsonProvider
import net.kibotu.ContextHelper
import net.kibotu.ContextHelper.getApplication
import net.kibotu.logger.Logger
import kotlin.jvm.internal.CallableReference
import kotlin.reflect.KClass
import kotlin.reflect.KDeclarationContainer
import kotlin.reflect.KProperty

/*
 * Android Shared Preferences Delegate for Kotlin
 *
 * Usage:
 *
 * var accessToken by stringPreference("token")
 * will be stored as
 *   "my.package.myclass::accessToken": "token"
 *
 */

abstract class PrefDelegate<T>(val prefName: String? = R.string.app_name.resString, val prefKey: String? = null) {

    protected val prefs: SharedPreferences by lazy {
        Logger.v("SharedPreferences", "initializing with $prefName in private mode")
        if (ContextHelper.getApplication() != null)
            if (prefName != null) ContextHelper.getApplication()!!.getSharedPreferences(prefName, Context.MODE_PRIVATE) else PreferenceManager.getDefaultSharedPreferences(ContextHelper.getApplication()!!)
        else
            throw IllegalStateException("Context was not initialized. Call Logger#with")
    }

    abstract operator fun getValue(thisRef: Any?, property: KProperty<*>): T
    abstract operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T)
}

fun stringPreference(defaultValue: String? = null) = StringPrefDelegate(defaultValue = defaultValue)

fun stringPreference(prefKey: String? = null, defaultValue: String? = null) = StringPrefDelegate(prefKey, defaultValue)

class StringPrefDelegate(prefKey: String? = null, val defaultValue: String?) : PrefDelegate<String?>(prefKey = prefKey) {
    override fun getValue(thisRef: Any?, property: KProperty<*>) = prefs.getString(prefKey
            ?: property.defaultDelegateName(), defaultValue)

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: String?) = prefs.edit().putString(prefKey
            ?: property.defaultDelegateName(), value).apply()
}

fun intPreference(defaultValue: Int = 0) = IntPrefDelegate(defaultValue = defaultValue)

fun intPreference(prefKey: String? = null, defaultValue: Int = 0) = IntPrefDelegate(prefKey, defaultValue)

class IntPrefDelegate(prefKey: String? = null, val defaultValue: Int = 0) : PrefDelegate<Int>(prefKey = prefKey) {
    override fun getValue(thisRef: Any?, property: KProperty<*>) = prefs.getInt(prefKey
            ?: property.defaultDelegateName(), defaultValue)

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Int) = prefs.edit().putInt(prefKey
            ?: property.defaultDelegateName(), value).apply()
}

fun floatPreference(defaultValue: Float = 0f) = FloatPrefDelegate(defaultValue = defaultValue)

fun floatPreference(prefKey: String? = null, defaultValue: Float = 0f) = FloatPrefDelegate(prefKey, defaultValue)

class FloatPrefDelegate(prefKey: String? = null, val defaultValue: Float = 0f) : PrefDelegate<Float>(prefKey = prefKey) {
    override fun getValue(thisRef: Any?, property: KProperty<*>) = prefs.getFloat(prefKey
            ?: property.defaultDelegateName(), defaultValue)

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Float) = prefs.edit().putFloat(prefKey
            ?: property.defaultDelegateName(), value).apply()
}

fun booleanPreference(defaultValue: Boolean = false) = BooleanPrefDelegate(defaultValue = defaultValue)

fun booleanPreference(prefKey: String? = null, defaultValue: Boolean = false) = BooleanPrefDelegate(prefKey, defaultValue)

class BooleanPrefDelegate(prefKey: String? = null, val defaultValue: Boolean = false) : PrefDelegate<Boolean>(prefKey = prefKey) {
    override fun getValue(thisRef: Any?, property: KProperty<*>) = prefs.getBoolean(prefKey
            ?: property.defaultDelegateName(), defaultValue)

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Boolean) = prefs.edit().putBoolean(prefKey
            ?: property.defaultDelegateName(), value).apply()
}

fun LongPreference(defaultValue: Long = 0) = LongPrefDelegate(defaultValue = defaultValue)

fun LongPreference(prefKey: String? = null, defaultValue: Long = 0) = LongPrefDelegate(prefKey, defaultValue)

class LongPrefDelegate(prefKey: String? = null, val defaultValue: Long = 0) : PrefDelegate<Long>(prefKey = prefKey) {
    override fun getValue(thisRef: Any?, property: KProperty<*>) = prefs.getLong(prefKey
            ?: property.defaultDelegateName(), defaultValue)

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Long) = prefs.edit().putLong(prefKey
            ?: property.defaultDelegateName(), value).apply()
}

fun StringSetPreference(defaultValue: Set<String>? = null) = StringSetPreferenceDelegate(defaultValue = defaultValue)

fun StringSetPreference(prefKey: String? = null, defaultValue: Set<String>? = null) = StringSetPreferenceDelegate(prefKey, defaultValue)

class StringSetPreferenceDelegate(prefKey: String? = null, val defaultValue: Set<String>?) : PrefDelegate<Set<String>?>(prefKey = prefKey) {
    override fun getValue(thisRef: Any?, property: KProperty<*>) = prefs.getStringSet(prefKey
            ?: property.defaultDelegateName(), defaultValue)

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Set<String>?) = prefs.edit().putStringSet(prefKey
            ?: property.defaultDelegateName(), value).apply()
}

inline fun KProperty<*>.defaultDelegateName(customPrefix: String? = null, separator: String = "::") =
        (customPrefix ?: ownerCanonicalName)?.let { it + separator + name } ?: name

inline val KProperty<*>.ownerCanonicalName: String? get() = owner?.canonicalName

inline val KProperty<*>.owner: KDeclarationContainer? get() = if (this is CallableReference) owner else null

inline val KDeclarationContainer.canonicalName: String? get() = if (this is KClass<*>) this.java.canonicalName else null

interface DelegateProvider<out T> {
    operator fun provideDelegate(thisRef: Any?, property: KProperty<*>): T
}

/**
 * A property delegate that can read and write from a receiver of type [This].
 */
interface PropertyDelegate<in This, T> : DelegateProvider<PropertyDelegate<This, T>> {
    operator fun getValue(thisRef: This, property: KProperty<*>): T
    operator fun setValue(thisRef: This, property: KProperty<*>, value: T)
}

/**
 * Helper to create a [PropertyDelegate].
 *
 * @param R The raw type that will be read/written directly to the receiver of type [This].
 * @param T The transformed type (from [R]) that will be read/written from the property.
 * @param typeReader Transforms the raw type [R] in the property type [T] when reading from the receiver of type [This].
 * @param typeWriter Transforms the property type [T] in the raw type [R] when writing to the receiver of type [This].
 * @param extraReader Reads the value from the receiver of type [This].
 * @param extraWriter Writes the value to the receiver of type [This].
 * @param name An optional name for the property. If missing, a compile-time constant will be used equal to the qualified name of the class
 * in which the property is declared plus the real name of the property itself.
 * @param customPrefix An optional prefix for the property name, to be used before the real name of the property.
 * Note that this is ignored if [name] is present.
 */
@PublishedApi
internal inline fun <This, T, R> PropertyDelegate(
        crossinline extraReader: ExtraReader<This, R>,
        crossinline extraWriter: ExtraWriter<This, R>,
        crossinline typeReader: TypeReader<T, R>,
        crossinline typeWriter: TypeWriter<T, R>,
        name: String? = null,
        customPrefix: String? = null
) = object : PropertyDelegate<This, T> {

    private lateinit var name: String private set

    override operator fun provideDelegate(thisRef: Any?, property: KProperty<*>) = apply {
        this.name = name ?: property.defaultDelegateName(customPrefix)
    }

    override fun getValue(thisRef: This, property: KProperty<*>): T =
            typeReader(extraReader(thisRef, this.name))

    override fun setValue(thisRef: This, property: KProperty<*>, value: T) {
        value?.let { extraWriter(thisRef, this.name, typeWriter(it)) }
    }
}

typealias TypeReader<T, R> = (R) -> T
typealias TypeWriter<T, R> = (T) -> R

typealias ExtraReader<This, R> = This.(name: String) -> R
typealias ExtraWriter<This, R> = This.(name: String, value: R) -> Any?

fun sharedPreferencesToJson(): String {
    return GsonProvider.gsonPrettyPrinting.toJson(getApplication()?.getSharedPreferences(R.string.app_name.resString, Context.MODE_PRIVATE)?.all
            ?: Object())
}