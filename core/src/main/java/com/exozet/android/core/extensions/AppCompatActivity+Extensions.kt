@file:JvmName("AppCompatActivityExtensions")

package com.exozet.android.core.extensions

import android.content.pm.PackageManager
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.N
import android.view.View
import androidx.annotation.IdRes
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.exozet.android.core.R
import net.kibotu.ContextHelper.getActivity
import net.kibotu.ContextHelper.getApplication


/**
 * Created by armando.shkurti on 09/10/17.
 */

fun AppCompatActivity.replaceFragment(fragment: Fragment, transitionSharedElement: View? = null, transitionName: String? = null) {
    replaceFragment(fragment, R.id.fragment_container, fragment.tag(), transitionSharedElement, transitionName)
}

fun AppCompatActivity.replaceWithStackFragment(fragment: Fragment, transitionSharedElement: View? = null, transitionName: String? = null) {
    replaceWithStackFragment(fragment, R.id.fragment_container, fragment.tag(), transitionSharedElement, transitionName)
}

fun AppCompatActivity.replaceFragment(fragment: Fragment, @IdRes frameId: Int, tag: String, transitionSharedElement: View? = null, transitionName: String? = null) {
    supportFragmentManager.transact {
        replace(frameId, fragment, tag)
        if(transitionSharedElement != null && transitionName != null) {
            addSharedElement(transitionSharedElement, transitionName)
        }
    }
}

fun AppCompatActivity.replaceWithStackFragment(fragment: Fragment, @IdRes frameId: Int, tag: String, transitionSharedElement: View? = null, transitionName: String? = null) {
    supportFragmentManager.transact {
        replace(frameId, fragment, tag)
        addToBackStack(null)
        if(transitionSharedElement != null && transitionName != null) {
            addSharedElement(transitionSharedElement, transitionName)
        }
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

@Suppress("DEPRECATION")
fun enterPictureInPictureMode() {
    if (supportsPictureInPicture()) {
        getActivity()!!.enterPictureInPictureMode()
    }
}

fun supportsPictureInPicture(): Boolean {
    return SDK_INT >= N && getApplication()!!.packageManager.hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE)
}