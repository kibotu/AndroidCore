package com.exozet.commonutils.sample.misc

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.common.android.utils.logging.Logger
import com.zplesac.connectionbuddy.ConnectionBuddy
import com.zplesac.connectionbuddy.interfaces.ConnectivityChangeListener
import com.zplesac.connectionbuddy.models.ConnectivityEvent
import rx.Observable
import rx.subjects.PublishSubject


/**
 * Created by [Jan Rabe](https://about.me/janrabe).
 */

enum class ConnectivityChangeListenerRx : ConnectivityChangeListener {

    /**
     * Singleton.
     */
    instance;

    private var activityLifecycleCallbacks: Application.ActivityLifecycleCallbacks? = null

    internal var subject = PublishSubject.create<ConnectivityEvent>()

    internal var connectivityEvent: ConnectivityEvent? = null

    override fun onConnectionChange(event: ConnectivityEvent?) {
        if (event == null)
            return

        Logger.v(TAG, "[connectivityEvent] type=" + event.type + " state=" + event.state)
        this.connectivityEvent = event
        subject.onNext(event)
    }

    private fun createActivityLifecycleCallbacks(): Application.ActivityLifecycleCallbacks {
        return object : Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                ConnectionBuddy.getInstance().registerForConnectivityEvents(instance, instance)
                if (savedInstanceState != null) {
                    ConnectionBuddy.getInstance().configuration.networkEventsCache.clearLastNetworkState(this)
                }
            }

            override fun onActivityStarted(activity: Activity) {
            }

            override fun onActivityResumed(activity: Activity) {
            }

            override fun onActivityPaused(activity: Activity) {
            }

            override fun onActivityStopped(activity: Activity) {
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

            }

            override fun onActivityDestroyed(activity: Activity) {
                ConnectionBuddy.getInstance().unregisterFromConnectivityEvents(instance)
            }
        }
    }

    companion object {

        private val TAG = ConnectivityChangeListenerRx::class.java.simpleName

        fun with(application: Application): ConnectivityChangeListenerRx {
            instance.activityLifecycleCallbacks = instance.createActivityLifecycleCallbacks()
            application.registerActivityLifecycleCallbacks(instance.activityLifecycleCallbacks)
            return instance
        }

        val observable: Observable<ConnectivityEvent>
            get() = instance.subject

        val current: ConnectivityEvent?
            get() = instance.connectivityEvent

        fun onTerminate(application: Application) {
            application.unregisterActivityLifecycleCallbacks(instance.activityLifecycleCallbacks)
        }
    }
}