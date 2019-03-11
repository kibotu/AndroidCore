package com.exozet.android.core.storage


import android.os.Binder
import android.os.Bundle
import android.os.Parcelable
import androidx.core.app.BundleCompat
import androidx.fragment.app.Fragment
import org.parceler.ParcelWrapper
import org.parceler.Parcels
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Eases the Fragment.newInstance ceremony by marking the fragment's args with this delegate
 * Just write the property in newInstance and read it like any other property after the fragment has been created
 *
 * Inspired by Adam Powell, he mentioned it during his IO/17 talk about Kotlin
 *
 * @source https://gist.github.com/yanngx/efdfbf777d21d6f0e73fab4efe47e924
 */
class FragmentArgumentDelegate<T : Any?> : ReadWriteProperty<Fragment, T?> {

    var value: T? = null

    override operator fun getValue(thisRef: Fragment, property: KProperty<*>): T? {
        if (value == null) {
            val args = thisRef.arguments

            @Suppress("UNCHECKED_CAST")
            val v = args?.get(property.name) as? T

            value = if (v is ParcelWrapper<*>) {
                Parcels.unwrap(v as Parcelable)
            } else {
                v
            }
        }

        return value
    }

    override operator fun setValue(thisRef: Fragment, property: KProperty<*>, value: T?) {
        var args = thisRef.arguments
        if (args == null) {
            args = Bundle()
            thisRef.arguments = args
        }

        val key = property.name

        with(args) {

            when (value) {
                is Boolean -> putBoolean(key, value)
                is Byte -> putByte(key, value)
                is Char -> putChar(key, value)
                is Short -> putShort(key, value)
                is Int -> putInt(key, value)
                is Long -> putLong(key, value)
                is Float -> putFloat(key, value)
                is Double -> putDouble(key, value)
                is String -> putString(key, value)
                is CharSequence -> putCharSequence(key, value)
                // is ArrayList<Int> -> putIntegerArrayList(key, value)
                // is ArrayList<String> -> putStringArrayList(key, value)
                // is ArrayList<CharSequence> -> putCharSequenceArrayList(key, value)
                is java.io.Serializable -> putSerializable(key, value)
                is BooleanArray -> putBooleanArray(key, value)
                is ByteArray -> putByteArray(key, value)
                is ShortArray -> putShortArray(key, value)
                is CharArray -> putCharArray(key, value)
                is IntArray -> putIntArray(key, value)
                is LongArray -> putLongArray(key, value)
                is FloatArray -> putFloatArray(key, value)
                is DoubleArray -> putDoubleArray(key, value)
                (value as? Array<*>)?.isArrayOf<String>() -> {
                    @Suppress("UNCHECKED_CAST")
                    putStringArray(key, (value as Array<String>))
                }
                (value as? Array<*>)?.isArrayOf<CharSequence>() -> {
                    @Suppress("UNCHECKED_CAST")
                    putCharSequenceArray(key, (value as Array<CharSequence>))
                }
                is Bundle -> putBundle(key, value)
                is Binder -> BundleCompat.putBinder(args, key, value)
                is android.os.Parcelable -> {
                    try {
                        putParcelable(key, Parcels.wrap(value))
                    } catch (e: Exception) {
                        e.printStackTrace()
                        putParcelable(key, value)
                    }
                }
                else -> throw IllegalStateException("Type ${value/*.javaClass.canonicalName*/} of property ${property.name} is not supported")
            }
        }
    }
}

inline fun <reified T : Any?> Fragment.bundle(): ReadWriteProperty<Fragment, T?> = FragmentArgumentDelegate()