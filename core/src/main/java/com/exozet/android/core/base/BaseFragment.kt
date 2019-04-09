package com.exozet.android.core.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.ColorRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.exozet.android.core.R
import com.exozet.android.core.extensions.currentFragment
import com.exozet.android.core.extensions.hideOnLostFocus
import com.exozet.android.core.interfaces.BackPress
import com.exozet.android.core.interfaces.DispatchTouchEventHandler
import com.exozet.android.core.misc.UIDGenerator
import com.google.android.material.snackbar.Snackbar
import net.kibotu.logger.Logger
import net.kibotu.logger.Logger.loge
import net.kibotu.logger.Logger.logv
import net.kibotu.logger.TAG

/**
 * Created by jan.rabe on 19/07/16.
 *
 *
 *
 */
@Deprecated("just demo, don't use")
abstract class BaseFragment : Fragment(), DispatchTouchEventHandler, BackPress, FragmentManager.OnBackStackChangedListener {

    /**
     * [Restoring instance state after fragment transactions.](http://stackoverflow.com/a/15314508)
     */
    private var savedState: Bundle? = null

    protected val uuid = UIDGenerator.newUID()

    protected abstract val layout: Int

    @ColorRes
    open fun onEnterStatusBarColor() = R.color.primary

    override val viewsHideKeyboardOnFocusLoss: Array<View?>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(layout, container, false)

        logv("[onCreateView] $savedInstanceState")

        /**
         * If the Fragment was destroyed in between (screen rotation), we need to recover the savedState first.
         * However, if it was not, it stays in the instance from the last onDestroyView() and we don't want to overwrite it.
         */
        if (savedInstanceState != null && savedState == null) {
            savedState = savedInstanceState.getBundle(javaClass.simpleName)
        }
        if (savedState != null) {
            onRestoreSavedState(savedState!!)
        }
        savedState = null

        // navigationDrawerMenu = NavigationDrawerMenu()

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        logv("[onViewCreated] $savedInstanceState")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        /**
         * If onDestroyView() is called first, we can use the previously onSaveInstanceState but we can't call onSaveInstanceState() anymore
         * If onSaveInstanceState() is called first, we don't have onSaveInstanceState, so we need to call onSaveInstanceState()
         * => (?:) operator inevitable!
         */
        outState.putBundle(
            javaClass.simpleName, if (savedState != null)
                savedState
            else
                onSaveInstanceState()
        )

        super.onSaveInstanceState(outState)
    }

    override fun onResume() {
        super.onResume()
        logv("[onResume]")
        activity!!.supportFragmentManager.addOnBackStackChangedListener(this)
    }

    override fun onPause() {
        super.onPause()
        logv("[onPause]")
        activity!!.supportFragmentManager.removeOnBackStackChangedListener(this)
    }

    override fun onDestroyView() {
        logv("[onDestroyView]")
        savedState = onSaveInstanceState()
        super.onDestroyView()
    }

    /**
     * Called either from [.onDestroyView] or [.onSaveInstanceState]
     */
    @CallSuper
    protected open fun onSaveInstanceState(): Bundle {

        logv("[onSaveInstanceState]")

        // save state

        return Bundle()
    }

    /**
     * Called from [.onCreateView]

     * @param savedInstanceState
     */
    protected open fun onRestoreSavedState(savedInstanceState: Bundle) {
        // restore saved state
        logv("[onRestoreSavedState] $savedInstanceState")
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        try {
            viewsHideKeyboardOnFocusLoss.hideOnLostFocus(event)
        } catch (e: Exception) {
            loge("[$uuid-dispatchTouchEvent] ${e.message}")
        }
        return false
    }

    override fun consumeBackPress(): Boolean {

        // close menu
//        if (navigationDrawerMenu?.isDrawerOpen != false) {
//            navigationDrawerMenu?.closeDrawer()
//            return true
//        }

        return false
    }

    fun setArgument(bundle: Bundle): BaseFragment {
        arguments = bundle
        return this
    }

    /**
     * Returned to this fragment after BackStack changes.
     */
    protected fun onActiveAfterBackStackChanged() {
    }

    protected val isCurrentFragment: Boolean
        get() {
            val fragment = currentFragment()
            return fragment is BaseFragment && fragment.TAG == TAG
        }

    override fun onBackStackChanged() {
        if (isCurrentFragment)
            onActiveAfterBackStackChanged()
    }

    fun showSnackBar(message: String) {
        Logger.d(message)
        view?.let { Snackbar.make(it, message, Snackbar.LENGTH_SHORT).show() }
    }

}