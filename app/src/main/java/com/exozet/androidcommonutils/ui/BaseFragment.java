package com.exozet.androidcommonutils.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.ColorRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.common.android.utils.ContextHelper;
import com.common.android.utils.interfaces.Backpress;
import com.common.android.utils.interfaces.DispatchTouchEvent;
import com.common.android.utils.interfaces.LogTag;
import com.common.android.utils.misc.UIDGenerator;
import com.exozet.basejava.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.common.android.utils.extensions.FragmentExtensions.currentFragment;
import static com.common.android.utils.extensions.ViewExtensions.changeStatusBarColorRes;
import static com.common.android.utils.extensions.ViewExtensions.hideOnLostFocus;

/**
 * Created by jan.rabe on 19/07/16.
 * <p>
 * <img src="https://raw.githubusercontent.com/Aracem/android-lifecycle/master/complete_android_fragment_lifecycle.png"/>
 */
public abstract class BaseFragment extends Fragment implements LogTag, DispatchTouchEvent, Backpress, FragmentManager.OnBackStackChangedListener {

    private Unbinder unbinder;
    /**
     * <a href="http://stackoverflow.com/a/15314508">Restoring instance state after fragment transactions.</a>
     */
    private Bundle savedState = null;

    protected final int uid = UIDGenerator.newUID();

    public BaseFragment() {
    }

    @LayoutRes
    protected abstract int getLayout();

    @ColorRes
    public int onEnterStatusBarColor() {
        return R.color.colorPrimary;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(getLayout(), container, false);
        unbinder = ButterKnife.bind(this, rootView);

        /**
         * If the Fragment was destroyed in between (screen rotation), we need to recover the savedState first.
         * However, if it was not, it stays in the instance from the last onDestroyView() and we don't want to overwrite it.
         */
        if (savedInstanceState != null && savedState == null) {
            savedState = savedInstanceState.getBundle(tag());
        }
        if (savedState != null) {
            onRestoreSavedState(savedState);
        }
        savedState = null;

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        /**
         * If onDestroyView() is called first, we can use the previously onSaveInstanceState but we can't call onSaveInstanceState() anymore
         * If onSaveInstanceState() is called first, we don't have onSaveInstanceState, so we need to call onSaveInstanceState()
         * => (?:) operator inevitable!
         */
        outState.putBundle(tag(), savedState != null
                ? savedState
                : onSaveInstanceState());

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getSupportFragmentManager().addOnBackStackChangedListener(this);
        changeStatusBarColorRes(onEnterStatusBarColor());
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().getSupportFragmentManager().removeOnBackStackChangedListener(this);
    }

    @Override
    public void onDestroyView() {
        savedState = onSaveInstanceState();
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * Called either from {@link #onDestroyView()} or {@link #onSaveInstanceState(Bundle)}
     **/
    @CallSuper
    protected Bundle onSaveInstanceState() {

        // save state

        return new Bundle();
    }

    /**
     * Called from {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
     *
     * @param savedInstanceState
     */
    protected void onRestoreSavedState(@NonNull final Bundle savedInstanceState) {
        // restore saved state
    }

    @Override
    public Context getContext() {
        return ContextHelper.getContext();
    }

    @Override
    public boolean dispatchTouchEvent(@NonNull final MotionEvent event) {
        hideOnLostFocus(event, viewsThatHideKeyboardWhenLostFocus());
        return false;
    }

    @Nullable
    protected View[] viewsThatHideKeyboardWhenLostFocus() {
        return null;
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @NonNull
    @Override
    public String tag() {
        return getClass().getSimpleName() + "[" + uid + "]";
    }

    public BaseFragment setArgument(Bundle bundle) {
        setArguments(bundle);
        return this;
    }

    /**
     * Returned to this fragment after BackStack changes.
     */
    protected void onActiveAfterBackStackChanged() {
        colorizeStatusBar();
    }

    protected void colorizeStatusBar() {
        changeStatusBarColorRes(onEnterStatusBarColor());
    }

    protected boolean isCurrentFragment() {
        final Fragment fragment = currentFragment();
        return fragment instanceof BaseFragment && ((BaseFragment) fragment).tag().equals(tag());
    }

    @Override
    public void onBackStackChanged() {
        if (isCurrentFragment())
            onActiveAfterBackStackChanged();
    }

}