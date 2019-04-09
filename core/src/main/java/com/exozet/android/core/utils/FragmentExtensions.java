package com.exozet.android.core.utils;

import android.os.Bundle;
import android.util.Log;
import androidx.annotation.AnimRes;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.exozet.android.core.BuildConfig;
import com.exozet.android.core.R;
import com.exozet.android.core.interfaces.ChainableCommand;
import com.exozet.android.core.interfaces.annotations.Transit;
import net.kibotu.ContextHelper;
import net.kibotu.logger.Logger;

import static net.kibotu.ContextHelper.getAppCompatActivity;

/**
 * Created by Jan Rabe on 29/07/15.
 */
final public class FragmentExtensions {

    public static final String TAG = FragmentExtensions.class.getSimpleName();

    // region setting fragment container id // TODO: 07/03/16 find a better solution to set this

    @IdRes
    public static int fragmentContainerId = R.id.fragment_container;

    @IdRes
    public static int getFragmentContainerId() {
        return fragmentContainerId;
    }

    public static void setFragmentContainerId(@IdRes final int fragmentContainerId) {
        FragmentExtensions.fragmentContainerId = fragmentContainerId;
    }

    // endregion

    // region logging

    public static boolean LOGGING_ENABLED = BuildConfig.DEBUG;

    public static boolean isLoggingEnabled() {
        return LOGGING_ENABLED;
    }

    public static void setLoggingEnabled(final boolean loggingEnabled) {
        LOGGING_ENABLED = loggingEnabled;
    }

    private FragmentExtensions() throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    // endregion

    // region misc

    public static boolean isInRoot() {
        return getAppCompatActivity().getSupportFragmentManager().getBackStackEntryCount() == 0;
    }

    public static Fragment currentFragment(@IdRes final int container) {
        return getAppCompatActivity().getSupportFragmentManager().findFragmentById(container);
    }

    public static Fragment currentFragment() {
        return getAppCompatActivity().getSupportFragmentManager().findFragmentById(getFragmentContainerId());
    }

    @NonNull
    public static <T extends Fragment> T newInstance(@NonNull final Class<T> type, @Nullable final Bundle args) {
        final T f = ClassExtensions.newInstance(type);
        if (args != null) {
            f.setArguments(args);
        }
        return f;
    }

    public static void printBackStack() {
        if (!LOGGING_ENABLED)
            return;

        final FragmentManager fm = getAppCompatActivity().getSupportFragmentManager();
        Log.v(TAG, "Current BackStack:  " + fm.getBackStackEntryCount());
        for (int entry = 0; entry < fm.getBackStackEntryCount(); entry++) {
            final FragmentManager.BackStackEntry stackEntry = fm.getBackStackEntryAt(entry);
            Log.v(TAG, "[" + stackEntry.getId() + "] " + stackEntry.getName());
        }
    }

    // endregion

    // region transaction type

    @NonNull
    public static <T extends Fragment> ChainableCommand<FragmentTransaction> add(@NonNull final T fragment, @NonNull final ChainableCommand<FragmentTransaction> command) {
        if (LOGGING_ENABLED) {
            final Fragment currentFragment = currentFragment();
            final String tag = currentFragment != null ? currentFragment.getClass().getSimpleName() : "[empty]";
            Logger.v(TAG, "[add] " + tag + " with " + fragment.getClass().getCanonicalName());
        }
        return t -> command.execute(t).add(getFragmentContainerId(), fragment, fragment.getClass().getCanonicalName());
    }


    @NonNull
    public static <T extends Fragment> ChainableCommand<FragmentTransaction> replace(@NonNull final T fragment, @NonNull final ChainableCommand<FragmentTransaction> command) {
        if (LOGGING_ENABLED) {
            final Fragment currentFragment = currentFragment();
            final String tag = currentFragment != null ? currentFragment.getClass().getSimpleName() : "[empty]";
            Logger.v(TAG, "[replace] " + tag + " with " + fragment.getClass().getCanonicalName());
        }
        return t -> command.execute(t).replace(getFragmentContainerId(), fragment, fragment.getClass().getCanonicalName());
    }

    @NonNull
    public static <T extends Fragment> ChainableCommand<FragmentTransaction> remove(@NonNull final T fragment, @NonNull final ChainableCommand<FragmentTransaction> command) {
        if (LOGGING_ENABLED) {
            final Fragment currentFragment = currentFragment();
            final String tag = currentFragment != null ? currentFragment.getClass().getSimpleName() : "[empty]";
            Logger.v(TAG, "[remove] " + tag + " with " + fragment.getClass().getCanonicalName());
        }
        return t -> command.execute(t).remove(fragment);
    }

    // endregion

    // region transaction lifecycle

    @NonNull
    public static ChainableCommand<FragmentTransaction> empty() {
        return t -> t;
    }

    public static void popBackStackImmediate() {
        if (LOGGING_ENABLED)
            Logger.v(TAG, "[popBackStackImmediate]");

        if (!ContextHelper.INSTANCE.isRunning().get())
            return;

        getAppCompatActivity().getSupportFragmentManager().popBackStackImmediate();
    }

    public static void popBackStack() {
        if (LOGGING_ENABLED)
            Logger.v(TAG, "[popBackStack]");

        if (!ContextHelper.INSTANCE.isRunning().get())
            return;

        getAppCompatActivity().getSupportFragmentManager().popBackStack();
    }

    public static void clearBackStack() {
        if (LOGGING_ENABLED)
            Logger.v(TAG, "[clearBackStack]");

        if (!ContextHelper.INSTANCE.isRunning().get())
            return;

        getAppCompatActivity().getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    @NonNull
    public static ChainableCommand<FragmentTransaction> beginTransaction() {
        FragmentExtensions.printBackStack();
        if (LOGGING_ENABLED)
            Logger.v(TAG, "[beginTransaction]");
        return t -> getAppCompatActivity().getSupportFragmentManager().beginTransaction();
    }

    @NonNull
    public static ChainableCommand<FragmentTransaction> addToBackStack(@NonNull final String tag, @NonNull final ChainableCommand<FragmentTransaction> command) {
        if (LOGGING_ENABLED)
            Logger.v(TAG, "[addToBackStack]");
        return t -> command.execute(t).addToBackStack(tag);
    }

    @NonNull
    public static void commit(@NonNull final ChainableCommand<FragmentTransaction> command, @Nullable final Runnable callback) {
        commit(command);
        if (callback != null)
            callback.run();
    }

    @NonNull
    public static void commit(@NonNull final ChainableCommand<FragmentTransaction> command) {
        if (LOGGING_ENABLED)
            Logger.v(TAG, "[commit]");

        // trying to fix:
        //
        // 'Fatal Exception: java.lang.IllegalStateExceptio
        // Can not perform this action after onSaveInstanceState'
        //
        // see http://www.androiddesignpatterns.com/2013/08/fragment-transaction-commit-state-loss.html
        if (!ContextHelper.INSTANCE.isRunning().get())
            return;

        command.execute(null).commit(); // intended 'null' so we can decorate in arbitrary order
        printBackStack();
    }

    // endregion

    // region transition 

    @NonNull
    public static ChainableCommand<FragmentTransaction> setTransition(@Transit final int transition, @NonNull final ChainableCommand<FragmentTransaction> command) {
        return t -> command.execute(t).setTransition(transition);
    }

    @NonNull
    public static ChainableCommand<FragmentTransaction> setTransitionOpen(@NonNull final ChainableCommand<FragmentTransaction> command) {
        return setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN, command);
    }

    @NonNull
    public static ChainableCommand<FragmentTransaction> setTransitionClose(@NonNull final ChainableCommand<FragmentTransaction> command) {
        return setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE, command);
    }

    @NonNull
    public static ChainableCommand<FragmentTransaction> setTransitionFade(@NonNull final ChainableCommand<FragmentTransaction> command) {
        return setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE, command);
    }

    // endregion

    // region animations

    @NonNull
    public static ChainableCommand<FragmentTransaction> setCustomAnimations(@AnimRes final int enter, @AnimRes final int exit, @AnimRes final int popEnter, @AnimRes final int popExit, @NonNull final ChainableCommand<FragmentTransaction> command) {
        return t -> command.execute(t).setCustomAnimations(enter, exit, popEnter, popExit);
    }

    @NonNull
    public static ChainableCommand<FragmentTransaction> setCustomAnimations(@AnimRes final int enter, @AnimRes final int exit, @NonNull final ChainableCommand<FragmentTransaction> command) {
        return t -> command.execute(t).setCustomAnimations(enter, exit);
    }

    @NonNull
    public static ChainableCommand<FragmentTransaction> fade(@NonNull final ChainableCommand<FragmentTransaction> command) {
        if (LOGGING_ENABLED)
            Logger.v(TAG, "[fade]");
        return setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out, command);
    }

    @NonNull
    public static ChainableCommand<FragmentTransaction> grow(@NonNull final ChainableCommand<FragmentTransaction> command) {
        if (LOGGING_ENABLED)
            Logger.v(TAG, "[grow]");
        return setCustomAnimations(R.anim.grow, R.anim.shrink, command);
    }

    @NonNull
    private static ChainableCommand<FragmentTransaction> slideVertically(@NonNull final ChainableCommand<FragmentTransaction> command) {
        if (LOGGING_ENABLED)
            Logger.v(TAG, "[slideVertically]");
        return setCustomAnimations(R.anim.slide_in_top, R.anim.slide_out_bottom, R.anim.slide_in_top, R.anim.slide_out_bottom, command);
    }

    @NonNull
    private static ChainableCommand<FragmentTransaction> slideHorizontally(@NonNull final ChainableCommand<FragmentTransaction> command) {
        if (LOGGING_ENABLED)
            Logger.v(TAG, "[slideHorizontally]");
        return setCustomAnimations(R.anim.slide_enter, R.anim.slide_exit, R.anim.slide_pop_enter, R.anim.slide_pop_exit, command);
    }

    // endregion

    // region convenient methods for add

    public static <T extends Fragment> void addByFading(@NonNull final T fragment) {
        commit(add(fragment, fade(beginTransaction())));
    }

    public static <T extends Fragment> void add(@NonNull final T fragment) {
        commit(add(fragment, beginTransaction()));
    }

    public static <T extends Fragment> void addToBackStackByFading(@NonNull final T fragment) {
        commit(addToBackStack(fragment.getClass().getCanonicalName(), add(fragment, fade(beginTransaction()))));
    }

    // endregion

    // region convenient methods for replace

    public static <T extends Fragment> void replace(@NonNull final T fragment) {
        commit(replace(fragment, beginTransaction()));
    }

    public static <T extends Fragment> void replaceByFading(@NonNull final T fragment) {
        commit(replace(fragment, fade(beginTransaction())));
    }

    public static <T extends Fragment> void replaceToBackStackByFading(@NonNull final T fragment) {
        commit(addToBackStack(fragment.getClass().getCanonicalName(), replace(fragment, fade(beginTransaction()))));
    }

    // endregion

    // region convenient methods for remove

    public static <T extends Fragment> void remove(@NonNull final T fragment) {
        commit(remove(fragment, beginTransaction()));
    }

    public static <T extends Fragment> void removeByFading(@NonNull final T fragment) {
        commit(remove(fragment, fade(beginTransaction())));
    }

    public static <T extends Fragment> void removeToBackStackByFading(@NonNull final T fragment) {
        commit(addToBackStack(fragment.getClass().getCanonicalName(), remove(fragment, fade(beginTransaction()))));
    }

    // endregion

}