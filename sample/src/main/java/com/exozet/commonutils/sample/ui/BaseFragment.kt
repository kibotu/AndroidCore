package com.exozet.androidcommonutils.sample.ui

import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.annotation.ColorRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import butterknife.Unbinder
import com.common.android.utils.extensions.FragmentExtensions
import com.common.android.utils.extensions.ViewExtensions
import com.common.android.utils.interfaces.Backpress
import com.common.android.utils.interfaces.DispatchTouchEvent
import com.common.android.utils.interfaces.LogTag
import com.common.android.utils.misc.UIDGenerator
import com.exozet.commonutils.sample.R


/**
 * Created by jan.rabe on 19/07/16.
 *
 *
 *
 */
open abstract class BaseFragment : Fragment(), LogTag, DispatchTouchEvent, Backpress, FragmentManager.OnBackStackChangedListener {

    private var unbinder: Unbinder? = null
    /**
     * [Restoring instance state after fragment transactions.](http://stackoverflow.com/a/15314508)
     */
    private var savedState: Bundle? = null

    protected val uid = UIDGenerator.newUID()

    protected abstract val layout: Int

    @ColorRes
    open fun onEnterStatusBarColor(): Int {
        return R.color.colorPrimary
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(layout, container, false)
        unbinder = ButterKnife.bind(this, rootView)

        /**
         * If the Fragment was destroyed in between (screen rotation), we need to recover the savedState first.
         * However, if it was not, it stays in the instance from the last onDestroyView() and we don't want to overwrite it.
         */
        if (savedInstanceState != null && savedState == null) {
            savedState = savedInstanceState.getBundle(tag())
        }
        if (savedState != null) {
            onRestoreSavedState(savedState!!)
        }
        savedState = null

        return rootView
    }

    override fun onSaveInstanceState(outState: Bundle) {
        /**
         * If onDestroyView() is called first, we can use the previously onSaveInstanceState but we can't call onSaveInstanceState() anymore
         * If onSaveInstanceState() is called first, we don't have onSaveInstanceState, so we need to call onSaveInstanceState()
         * => (?:) operator inevitable!
         */
        outState!!.putBundle(tag(), if (savedState != null)
            savedState
        else
            onSaveInstanceState())

        super.onSaveInstanceState(outState)
    }

    override fun onResume() {
        super.onResume()
        activity!!.supportFragmentManager.addOnBackStackChangedListener(this)
        ViewExtensions.changeStatusBarColorRes(onEnterStatusBarColor())
    }

    override fun onPause() {
        super.onPause()
        activity!!.supportFragmentManager.removeOnBackStackChangedListener(this)
    }

    override fun onDestroyView() {
        savedState = onSaveInstanceState()
        super.onDestroyView()
        unbinder!!.unbind()
    }

    /**
     * Called either from [.onDestroyView] or [.onSaveInstanceState]
     */
    @CallSuper
    protected fun onSaveInstanceState(): Bundle {

        // save state

        return Bundle()
    }

    /**
     * Called from [.onCreateView]

     * @param savedInstanceState
     */
    protected fun onRestoreSavedState(savedInstanceState: Bundle) {
        // restore saved state
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        val views = viewsThatHideKeyboardWhenLostFocus()
        if (views != null)
            ViewExtensions.hideOnLostFocus(event, *views)
        return false
    }

    protected fun viewsThatHideKeyboardWhenLostFocus(): Array<View>? {
        return null
    }

    override fun onBackPressed(): Boolean {
        return false
    }

    override fun tag(): String {
        return javaClass.simpleName + "[" + uid + "]"
    }

    fun setArgument(bundle: Bundle): BaseFragment {
        arguments = bundle
        return this
    }

    /**
     * Returned to this fragment after BackStack changes.
     */
    protected fun onActiveAfterBackStackChanged() {
        colorizeStatusBar()
    }

    protected fun colorizeStatusBar() {
        ViewExtensions.changeStatusBarColorRes(onEnterStatusBarColor())
    }

    protected val isCurrentFragment: Boolean
        get() {
            val fragment = FragmentExtensions.currentFragment()
            return fragment is BaseFragment && fragment.tag() == tag()
        }

    override fun onBackStackChanged() {
        if (isCurrentFragment)
            onActiveAfterBackStackChanged()
    }

}