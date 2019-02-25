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

        when (value) {
            is String -> args.putString(key, value)
            is Int -> args.putInt(key, value)
            is Short -> args.putShort(key, value)
            is Long -> args.putLong(key, value)
            is Byte -> args.putByte(key, value)
            is ByteArray -> args.putByteArray(key, value)
            is Char -> args.putChar(key, value)
            is CharArray -> args.putCharArray(key, value)
            is CharSequence -> args.putCharSequence(key, value)
            is Float -> args.putFloat(key, value)
            is Bundle -> args.putBundle(key, value)
            is Binder -> BundleCompat.putBinder(args, key, value)
            is android.os.Parcelable -> {
                try {
                    args.putParcelable(key, Parcels.wrap(value))
                } catch (e: Exception) {
                    e.printStackTrace()
                    args.putParcelable(key, value)
                }
            }
            is java.io.Serializable -> args.putSerializable(key, value)
            else -> throw IllegalStateException("Type ${value/*.javaClass.canonicalName*/} of property ${property.name} is not supported")
        }
    }
}

inline fun <reified T : Any?> Fragment.bundle(): ReadWriteProperty<Fragment, T?> = FragmentArgumentDelegate()