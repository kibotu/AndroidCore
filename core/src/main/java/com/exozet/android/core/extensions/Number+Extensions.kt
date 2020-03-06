package com.exozet.android.core.extensions

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract


/**
 * Returns `true` if this not null or zero.
 */
@UseExperimental(ExperimentalContracts::class)
inline fun <reified T : Number> T?.onNotNullOrZero(block: T.() -> Unit): Boolean {
    contract {
        returns(true) implies (this@onNotNullOrZero != null)
    }

    return if (this != null && this.isNotZero()) {
        block(this)
        true
    } else {
        false
    }
}

inline fun <reified T : Number> T.format(digits: Int) = "%.${digits}f".format(this)