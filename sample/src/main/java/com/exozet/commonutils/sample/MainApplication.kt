package com.exozet.commonutils.sample

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.provider.Settings
import android.support.multidex.MultiDex
import android.support.multidex.MultiDexApplication
import com.common.android.utils.ContextHelper
import com.common.android.utils.extensions.FragmentExtensions
import com.common.android.utils.extensions.JUnitExtensions
import com.common.android.utils.logging.LogcatLogger
import com.common.android.utils.logging.Logger
import com.crashlytics.android.Crashlytics
import com.exozet.commonutils.sample.BuildConfig.*
import com.exozet.commonutils.sample.misc.ConnectivityChangeListenerRx
import com.exozet.commonutils.sample.misc.DefaultUserAgent
import com.exozet.commonutils.sample.storage.LocalUser
import com.zplesac.connectionbuddy.ConnectionBuddy
import com.zplesac.connectionbuddy.ConnectionBuddyConfiguration
import io.fabric.sdk.android.Fabric
import net.danlew.android.joda.JodaTimeAndroid
import net.kibotu.android.deviceinfo.library.Device
import java.util.*


/**
 * Created by [Jan Rabe](https://about.me/janrabe).
 */

class MainApplication : MultiDexApplication() {


    override fun onCreate() {
        //MultiDex.install(applicationContext)//when multidex takes time to load a class
        super.onCreate()

        JodaTimeAndroid.init(this)
        ContextHelper.with(this)
        Device.with(this)
        LocalUser.with(this)
        initLogger()

        if (JUnitExtensions.isJUnitTest())
            return

        logBuildConfig()


        initFabric()


        initConnectivityChangeListener()


    }



    private fun initLogger() {

        Logger.addLogger(LogcatLogger())
        Logger.setLogLevel(if (resources.getBoolean(R.bool.enable_logging))
            Logger.Level.VERBOSE
        else
            Logger.Level.SILENT)

        FragmentExtensions.setLoggingEnabled(resources.getBoolean(R.bool.enable_logging))
    }

    private fun initConnectivityChangeListener() {
        ConnectionBuddy.getInstance().init(ConnectionBuddyConfiguration.Builder(this).build())
        ConnectivityChangeListenerRx.with(this)
        ConnectivityChangeListenerRx.observable
                .subscribe({ connectivityEvent ->
                    Logger.v(TAG, "[connectivityEvent] " + connectivityEvent)
                }, { it.printStackTrace() })
    }

    private fun initFabric() {
        Fabric.with(this, Crashlytics())
        for ((key, value) in createInfo(this))
            Crashlytics.setString(key, value)
    }

    private fun logBuildConfig() {
        for ((key, value) in createInfo(this)) {
            Logger.i(TAG, key + " : " + value)
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Logger.v(TAG, "[onConfigurationChanged] " + newConfig)
        LocalUser.defaultLocale = getLocaleFrom(newConfig)
    }

    override fun onTerminate() {
        ContextHelper.onTerminate()
        Device.onTerminate()
        ConnectivityChangeListenerRx.onTerminate(this)
        super.onTerminate()
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    companion object {

        private val TAG = MainApplication::class.java.simpleName

        fun createInfo(context: Context): Map<String, String> {
            val info = createAppBuildInfo(context)
            info.putAll(createDeviceBuild(context))
            return info
        }

        fun createAppBuildInfo(context: Context): MutableMap<String, String> {
            val info = LinkedHashMap<String, String>()
            info.put("DEVICE_ID", "" + Build.SERIAL)
            info.put("ANDROID ID", Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID))
            info.put("CANONICAL_VERSION_NAME", CANONICAL_VERSION_NAME)
            info.put("SIMPLE_VERSION_NAME", SIMPLE_VERSION_NAME)
            info.put("VERSION_NAME", "" + VERSION_NAME)
            info.put("VERSION_CODE", "" + VERSION_CODE)
            info.put("BUILD_TYPE", BUILD_TYPE)
            info.put("FLAVOR", FLAVOR)
            val d = Calendar.getInstance()
            d.timeInMillis = java.lang.Long.parseLong(BUILD_DATE)
            info.put("BUILD_DATE", "" + d.time)
            info.put("BRANCH", BRANCH)
            info.put("COMMIT_HASH", COMMIT_HASH)
            info.put("COMMIT_URL", VSC + "commits/" + COMMIT_HASH)
            info.put("TREE_URL", VSC + "src/" + COMMIT_HASH)
            return info
        }

        fun createDeviceBuild(context: Context): Map<String, String> {
            val info = LinkedHashMap<String, String>()
            // http://developer.android.com/reference/android/os/Build.html

            info.put("Model", Build.MODEL)
            info.put("Manufacturer", Build.MANUFACTURER)
            info.put("Release", Build.VERSION.RELEASE)
            info.put("SDK_INT", Build.VERSION.SDK_INT.toString())
            info.put("TIME", Date(Build.TIME).toString())

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                info.put("SUPPORTED_ABIS", Arrays.toString(Build.SUPPORTED_ABIS))
            else {
                info.put("CPU_ABI", Build.CPU_ABI)
                info.put("CPU_ABI2", Build.CPU_ABI2)
            }

            info.put("Board", Build.BOARD)
            info.put("Bootloader", Build.BOOTLOADER)
            info.put("Brand", Build.BRAND)
            info.put("Device", Build.DEVICE)
            info.put("Display", Build.DISPLAY)
            info.put("Fingerprint", Build.FINGERPRINT)
            info.put("Hardware", Build.HARDWARE)
            info.put("Host", Build.HOST)
            info.put("Id", Build.ID)
            info.put("Product", Build.PRODUCT)
            info.put("Serial", Build.SERIAL)
            info.put("Tags", Build.TAGS)
            info.put("Type", Build.TYPE)
            info.put("User", Build.USER)

            // http://developer.android.com/reference/android/os/Build.VERSION.html
            info.put("Codename", Build.VERSION.CODENAME)
            info.put("Incremental", Build.VERSION.INCREMENTAL)
            info.put("User Agent", DefaultUserAgent.getDefaultUserAgent(context))
            info.put("HTTP Agent", System.getProperty("http.agent"))

            return info
        }

        @SuppressLint("NewApi")
        fun getLocaleFrom(configuration: Configuration): Locale {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                configuration.locales.get(0)
            else
                configuration.locale
        }
    }
}
