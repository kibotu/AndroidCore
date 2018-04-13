@file:JvmName("ParcelsExtensions")

package com.exozet.android.core.extensions

import android.os.Bundle
import android.os.Parcelable
import org.parceler.Parcels

/**
 * Created by [Jan Rabe](https://about.me/janrabe).
 */

inline fun <reified T> T.wrap(): Parcelable = Parcels.wrap(this)

inline fun <reified T> Parcelable?.unwrap(): T? = Parcels.unwrap(this)

inline fun <reified T> Bundle.unwrap(key: String?): T? = getParcelable<Parcelable>(key).unwrap()
