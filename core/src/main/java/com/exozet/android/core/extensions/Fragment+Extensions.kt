@file:JvmName("FragmentExtensions")

package com.exozet.android.core.extensions

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import com.exozet.android.core.R
import net.kibotu.ContextHelper.getAppCompatActivity
import net.kibotu.logger.Logger


/**
 * Created by [Jan Rabe](https://about.me/janrabe).
 */

@IdRes
var fragmentContainerId: Int = R.id.fragment_container

fun currentFragment(@IdRes container: Int = fragmentContainerId): Fragment? {
    return getAppCompatActivity()!!.supportFragmentManager.findFragmentById(container)
}

fun printBackStack() {
    val fm = getAppCompatActivity()!!.supportFragmentManager
    Logger.v("Fragment", "Current BackStack:  " + fm.backStackEntryCount)
    for (entry in 0 until fm.backStackEntryCount) {
        val stackEntry = fm.getBackStackEntryAt(entry)
        Logger.v("Fragment", "[" + stackEntry.id + "] " + stackEntry.name)
    }
}

