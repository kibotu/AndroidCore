@file:JvmName("BooleanExtensions")

package com.exozet.android.core.extensions

/**
 * Created by [Jan Rabe](https://about.me/janrabe).
 */

inline fun <reified T> Boolean.whether(yes: () -> T, no: () -> T): T = if (this) yes() else no()

inline fun <reified T> Boolean.either(t: T): Pair<Boolean, T> = Pair(this, t)

inline infix fun <reified T> Pair<Boolean, T>.or(t: T): T = if (first) second else t