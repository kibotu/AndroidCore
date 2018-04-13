package com.exozet.android.core.base

import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.annotation.ColorRes
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import butterknife.Unbinder
import com.exozet.android.core.R
import com.exozet.android.core.extensions.currentFragment
import com.exozet.android.core.interfaces.Backpress
import com.exozet.android.core.interfaces.DispatchTouchEvent
import com.exozet.android.core.misc.UIDGenerator
import com.exozet.android.core.utils.ViewExtensions.hideOnLostFocus
import net.kibotu.logger.LogTag
import net.kibotu.logger.Logger

/**
 * Created by jan.rabe on 19/07/16.
 *
 *
 *
 */
abstract class BaseFragment : Fragment(), LogTag, DispatchTouchEvent, Backpress, FragmentManager.OnBackStackChangedListener {

    private var unbinder: Unbinder? = null
    /**
     * [Restoring instance state after fragment transactions.](http://stackoverflow.com/a/15314508)
     */
    private var savedState: Bundle? = null

    protected val uid = UIDGenerator.newUID()

    protected abstract val layout: Int

    @ColorRes
    open fun onEnterStatusBarColor(): Int {
        return R.color.primary
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(layout, container, false)
        unbinder = ButterKnife.bind(this, rootView)

        Logger.v("${tag()} [onCreateView] $savedInstanceState")

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

        Logger.v("${tag()} [onViewCreated] $savedInstanceState")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        /**
         * If onDestroyView() is called first, we can use the previously onSaveInstanceState but we can't call onSaveInstanceState() anymore
         * If onSaveInstanceState() is called first, we don't have onSaveInstanceState, so we need to call onSaveInstanceState()
         * => (?:) operator inevitable!
         */
        outState.putBundle(javaClass.simpleName, if (savedState != null)
            savedState
        else
            onSaveInstanceState())

        super.onSaveInstanceState(outState)
    }

    override fun onResume() {
        super.onResume()
        Logger.v("${tag()} [onResume]")
        activity!!.supportFragmentManager.addOnBackStackChangedListener(this)
    }

    override fun onPause() {
        super.onPause()
        Logger.v("${tag()} [onPause]")
        activity!!.supportFragmentManager.removeOnBackStackChangedListener(this)
    }

    override fun onDestroyView() {
        Logger.v("${tag()} [onDestroyView]")
        savedState = onSaveInstanceState()
        super.onDestroyView()
        unbinder?.unbind()
    }

    /**
     * Called either from [.onDestroyView] or [.onSaveInstanceState]
     */
    @CallSuper
    protected open fun onSaveInstanceState(): Bundle {

        Logger.v("${tag()} [onSaveInstanceState]")

        // save state

        return Bundle()
    }

    /**
     * Called from [.onCreateView]

     * @param savedInstanceState
     */
    protected open fun onRestoreSavedState(savedInstanceState: Bundle) {
        // restore saved state
        Logger.v("${tag()} [onRestoreSavedState] $savedInstanceState")
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        val views = viewsThatHideKeyboardWhenLostFocus()
        if (views != null)
            hideOnLostFocus(event, *views)
        return false
    }

    protected fun viewsThatHideKeyboardWhenLostFocus(): Array<View>? {
        return null
    }

    override fun onBackPressed(): Boolean {
        // close menu
//        if (navigationDrawerMenu?.isDrawerOpen != false) {
//            navigationDrawerMenu?.closeDrawer()
//            return true
//        }

        return false
    }

    override fun tag() = javaClass.simpleName + "[" + uid + "]"

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
            return fragment is BaseFragment && fragment.tag() == tag()
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