@file:JvmName("AppCompatActivityExtensions")

package com.exozet.android.core.extensions

import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import com.exozet.android.core.R


/**
 * Created by armando.shkurti on 09/10/17.
 */

fun AppCompatActivity.replaceFragment(fragment: Fragment) {
    replaceFragment(fragment, R.id.fragment_container, fragment.tag())
}

fun AppCompatActivity.replaceWithStackFragment(fragment: Fragment) {
    replaceWithStackFragment(fragment, R.id.fragment_container, fragment.tag())
}

fun AppCompatActivity.replaceFragment(fragment: Fragment, @IdRes frameId: Int, tag: String) {
    supportFragmentManager.transact {
        replace(frameId, fragment, tag)
    }
}

fun AppCompatActivity.replaceWithStackFragment(fragment: Fragment, @IdRes frameId: Int, tag: String) {
    supportFragmentManager.transact {
        replace(frameId, fragment, tag)
        addToBackStack(null)
    }
}

/**
 * The `fragment` is added to the container view with tag. The operation is
 * performed by the `fragmentManager`.
 */
fun AppCompatActivity.addFragment(fragment: Fragment, tag: String) {
    supportFragmentManager.transact {
        add(fragment, tag)
    }
}

fun AppCompatActivity.addWithStackFragment(fragment: Fragment, tag: String) {
    supportFragmentManager.transact {
        add(fragment, tag)
        addToBackStack(null)
    }
}

fun AppCompatActivity.setupActionBar(@IdRes toolbarId: Int, action: ActionBar.() -> Unit) {
    setSupportActionBar(findViewById(toolbarId))
    supportActionBar?.run {
        action()
    }
}

/**
 * Runs a FragmentTransaction, then calls commit().
 */
private inline fun FragmentManager.transact(action: FragmentTransaction.() -> Unit) {
    beginTransaction().apply {
        action()
    }.commit()
}

/**
 * clear Fragment backstack in android
 */
fun AppCompatActivity.clearFragmentBackStack() {
    val manager = supportFragmentManager
    if (manager.backStackEntryCount > 0) {
        val first = manager.getBackStackEntryAt(0)
        manager.popBackStack(first.id, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }
}

fun Fragment.tag(): String {
    return javaClass.simpleName
}