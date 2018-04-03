package com.exozet.android.core.base

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.os.Build.*
import android.os.Build.VERSION.*
import android.os.Build.VERSION_CODES.LOLLIPOP
import android.os.Build.VERSION_CODES.N
import android.provider.Settings
import android.support.multidex.MultiDexApplication
import com.crashlytics.android.Crashlytics
import com.exozet.android.core.R
import com.exozet.android.core.extensions.resBoolean
import com.exozet.android.core.extensions.resString
import com.exozet.android.core.misc.DefaultUserAgent
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.security.ProviderInstaller
import com.zplesac.connectionbuddy.ConnectionBuddy
import com.zplesac.connectionbuddy.ConnectionBuddyConfiguration
import com.zplesac.connectionbuddy.models.ConnectivityEvent
import com.zplesac.connectionbuddy.models.ConnectivityState
import io.fabric.sdk.android.Fabric
import net.danlew.android.joda.JodaTimeAndroid
import net.kibotu.android.deviceinfo.library.Device
import net.kibotu.logger.LogcatLogger
import net.kibotu.logger.Logger
import java.util.*

/**
 * Created by [Jan Rabe](https://about.me/janrabe).
 */

open class BaseApplication : MultiDexApplication() {

    private val connectivityListener: (ConnectivityEvent) -> Unit
        get() {
            return {
                when (it.state.value) {
                    ConnectivityState.CONNECTED -> {
                        Logger.snackbar(R.string.device_connected.resString)
                    }
                    ConnectivityState.DISCONNECTED -> Logger.snackbar(R.string.device_not_connected.resString)
                }
            }
        }

    override fun onCreate() {
        super.onCreate()

        Device.with(this)
        Logger.with(this)

        initLogger()
        JodaTimeAndroid.init(this)
        logBuildConfig()

        initRealm()

        initFabric()

        installServiceProviderIfNeeded(this)

        initConnectivityChangeListener(this)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        Logger.e("\nWarning!!! \nApplication onLowMemory called\n\n")
    }

    fun installServiceProviderIfNeeded(context: Context) {
        try {
            ProviderInstaller.installIfNeeded(context)
        } catch (e: GooglePlayServicesRepairableException) {
            Logger.e(e)
        } catch (e: GooglePlayServicesNotAvailableException) {
            Logger.e(e)
        }
    }

    private fun initRealm() {
//        Realm.init(this)
//        Realm.setDefaultConfiguration(LocalDataSource.realmConfiguration)
    }

    private fun initLogger() {

        Logger.addLogger(LogcatLogger(), if (R.bool.enable_logging.resBoolean)
            Logger.Level.VERBOSE
        else
            Logger.Level.SILENT)
    }

    private fun initConnectivityChangeListener(context: Context) {
        ConnectionBuddy.getInstance().apply {
            init(ConnectionBuddyConfiguration.Builder(context).build())
            registerForConnectivityEvents(connectivityListener, false, connectivityListener)
        }
    }

    private fun initFabric() {
        Fabric.with(this, Crashlytics())
        for ((key, value) in createInfo(this))
            Crashlytics.setString(key, value)
    }

    private fun logBuildConfig() {
        for ((key, value) in createInfo(this)) {
            Logger.i(TAG, "$key : $value")
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Logger.v(TAG, "[onConfigurationChanged] $newConfig")
    }

    override fun onTerminate() {
        Device.onTerminate()
        Logger.onTerminate()
        super.onTerminate()
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
    }

    companion object {

        private val TAG = BaseApplication::class.java.simpleName

        enum class VersionControlSystem(val commitUrl: String, val filesUrl: String) {
            Github("commits", "src"),
            Gitlab("commits", "tree")
        }

        fun isDebugMode() = R.bool.enable_debug_menu.resBoolean

        fun createInfo(context: Context): Map<String, String> {
            val info = createAppBuildInfo(context)
            info.putAll(createDeviceBuild(context))
            return info
        }

        fun createAppBuildInfo(context: Context): MutableMap<String, String> {
            val info = LinkedHashMap<String, String>()
            // if (SDK_INT >= O)
            // info.put("DEVICE_ID", "" + Build.getSerial())
            @Suppress("DEPRECATION")
            info["DEVICE_ID"] = "" + SERIAL

            info["ANDROID ID"] = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
//            info["CANONICAL_VERSION_NAME"] = CANONICAL_VERSION_NAME
//            info["SIMPLE_VERSION_NAME"] = SIMPLE_VERSION_NAME
//            info["VERSION_NAME"] = "" + VERSION_NAME
//            info["VERSION_CODE"] = "" + VERSION_CODE
//            info["BUILD_TYPE"] = BUILD_TYPE
//            info["FLAVOR"] = FLAVOR
            val d = Calendar.getInstance()
//            d.timeInMillis = java.lang.Long.parseLong(BUILD_DATE)
            info["BUILD_DATE"] = "" + d.time
//            info["BRANCH"] = BRANCH
//            info["COMMIT_HASH"] = COMMIT_HASH
//            info["COMMIT_URL"] = VSC + VersionControlSystem.Gitlab.commitUrl + "/" + COMMIT_HASH
//            info["TREE_URL"] = VSC + VersionControlSystem.Gitlab.filesUrl + "/" + COMMIT_HASH
            return info
        }

        fun createDeviceBuild(context: Context): Map<String, String> {
            val info = LinkedHashMap<String, String>()
            // http://developer.android.com/reference/android/os/Build.html

            info["Model"] = MODEL
            info["Manufacturer"] = MANUFACTURER
            info["Release"] = RELEASE
            info["SDK_INT"] = SDK_INT.toString()
            info["TIME"] = Date(TIME).toString()

            if (SDK_INT >= LOLLIPOP)
                info["SUPPORTED_ABIS"] = Arrays.toString(SUPPORTED_ABIS)
            else {

                @Suppress("DEPRECATION")
                info["CPU_ABI"] = CPU_ABI
                @Suppress("DEPRECATION")
                info["CPU_ABI2"] = CPU_ABI2
            }

            info["Board"] = BOARD
            info["Bootloader"] = BOOTLOADER
            info["Brand"] = BRAND
            info["Device"] = DEVICE
            info["Display"] = DISPLAY
            info["Fingerprint"] = FINGERPRINT
            info["Hardware"] = HARDWARE
            info["Host"] = HOST
            info["Id"] = ID
            info["Product"] = PRODUCT
            info["Tags"] = TAGS
            info["Type"] = TYPE
            info["User"] = USER

            // http://developer.android.com/reference/android/os/Build.VERSION.html
            info["Codename"] = CODENAME
            info["Incremental"] = INCREMENTAL
            info["User Agent"] = DefaultUserAgent.getDefaultUserAgent(context)
            info["HTTP Agent"] = System.getProperty("http.agent")

            return info
        }

        @SuppressLint("NewApi")
        fun getLocaleFrom(configuration: Configuration): Locale {
            return if (SDK_INT >= N)
                configuration.locales.get(0)
            else {
                @Suppress("DEPRECATION")
                configuration.locale
            }
        }
    }
}
