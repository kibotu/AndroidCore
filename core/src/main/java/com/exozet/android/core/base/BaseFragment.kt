package com.exozet.android.core.base

import android.content.Intent
import android.os.Bundle
import android.os.Process.killProcess
import android.os.Process.myPid
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.transition.TransitionInflater
import com.crashlytics.android.Crashlytics
import com.exozet.android.core.R
import com.exozet.android.core.extensions.*
import com.exozet.android.core.interfaces.BackPress
import com.exozet.android.core.interfaces.DispatchTouchEventHandler
import com.exozet.android.core.interfaces.annotations.ScreenOrientation
import com.exozet.android.core.misc.UIDGenerator
import com.exozet.android.core.services.notifications.PushNotificationPublisher
import io.reactivex.disposables.CompositeDisposable
import net.kibotu.logger.Logger.loge
import net.kibotu.logger.Logger.logv

@Deprecated("demo base fragment")
abstract class BaseFragment : Fragment(), BackPress, DispatchTouchEventHandler,
    FragmentManager.OnBackStackChangedListener, CompositeDisposableHolder {

    @get:LayoutRes
    protected abstract val layout: Int

    val uuid = UIDGenerator.newUID()

    @ScreenOrientation
    protected open val screenOrientation: Int? = null

    protected open val isSecure = false

    open val isFullScreen = false

    open val hasLightStatusBar = true

    override var subscription = CompositeDisposable()

    val onConnectionUpdate: Observer<in Boolean> = Observer {
        onConnectivityUpdate(it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleTransition()
        logv { "[$uuid-Lifecycle-onCreate]" }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        logv { "[$uuid-Lifecycle-onCreateView]" }
        try {
            return inflater.inflate(layout, container, false)
        } catch (e: Exception) {

            // on api 21, webview updates and hence it is not accessible during app usage, in this case we send the user to the play market and give him a hint in as push notification
            if (e.message?.contains("WebView") == true) {
                PushNotificationPublisher.sendNotification(
                    context!!,
                    "Outdated WebView",
                    "Please complete updating your WebView before continuing using ${R.string.app_name.resString}"
                )

                // tracking how often this actually happens
                Crashlytics.logException(e)

                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        "http://play.google.com/store/apps/details?id=com.google.android.webview".toUri()
                    )
                )

                activity?.finish()
                killProcess(myPid())

            } else
                e.printStackTrace()
        }

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    protected fun handleTransition() {
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(R.transition.move).apply {
            duration = android.R.integer.config_shortAnimTime.resLong
            startDelay = 0
        }
        sharedElementReturnTransition = TransitionInflater.from(context).inflateTransition(R.transition.move).apply {
            duration = android.R.integer.config_shortAnimTime.resLong
            startDelay = 0
        }
        // postponeEnterTransition()
    }

    protected open fun updateMainLayout() {
        screenOrientation?.let { activity?.requestedOrientation }

        activity?.isLightStatusBar = hasLightStatusBar

        if (isFullScreen)
            activity?.hideSystemUI()
        else
            activity?.showSystemUI()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logv { "[$uuid-Lifecycle-onViewCreated]" }

        updateMainLayout()

        subscription = CompositeDisposable()
        subscribeUi()
    }

    @CallSuper
    open fun subscribeUi() {
        logv { "[$uuid-Lifecycle-subscribeUi]" }
    }

    @CallSuper
    open fun unsubscribeUi() {
        logv { "[$uuid-Lifecycle-unsubscribeUi]" }
    }

    override fun onStart() {
        super.onStart()
        logv { "[$uuid-Lifecycle-onStart]" }
    }

    override fun onResume() {
        super.onResume()
        logv { "[$uuid-Lifecycle-onResume]" }

        if (isSecure)
            activity?.addSecureFlag()

        hideKeyboard()
    }

    override fun onPause() {
        super.onPause()
        logv { "[$uuid-Lifecycle-onPause]" }

        if (isSecure)
            activity?.clearSecureFlag()

        hideKeyboard()
    }

    override fun onDestroyView() {
        unsubscribeUi()
        super.onDestroyView()
        logv { "[$uuid-Lifecycle-onDestroyView]" }
    }

    override fun onDestroy() {

        disposeCompositeDisposable()

        super.onDestroy()
        logv { "[$uuid-Lifecycle-onDestroy]" }
    }

    override fun consumeBackPress(): Boolean = false

    open fun onBackPressed() {
        activity?.onBackPressed()
    }

    open fun onConnectivityUpdate(isConnected: Boolean) {
        logv { "[$uuid-onConnectivityUpdate] isConnected=$isConnected" }
    }

    open fun onError(exception: Throwable) {
        logv { "[$uuid-onError] ${exception::class.java.simpleName} ${exception.message}" }
    }

    override val viewsHideKeyboardOnFocusLoss: Array<View?>? = null

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        try {
//            logv {"[dispatchTouchEvent] $uuid views=${viewsHideKeyboardOnFocusLoss?.map { it.toString() }}")
            viewsHideKeyboardOnFocusLoss.hideOnLostFocus(event)
        } catch (e: Exception) {
            loge { "[$uuid-dispatchTouchEvent] ${e.message}" }
        }
        return false
    }

    // region FragmentManager.OnBackStackChangedListener

    @CallSuper
    override fun onBackStackChanged() {
        logv { "[$uuid-Lifecycle-onBackStackChanged]" }
        updateMainLayout()
    }

    // endregion

    // region CompositeDisposableHolder

    override fun disposeCompositeDisposable() {
        if (!subscription.isDisposed)
            subscription.dispose()
    }

    // endregion
}