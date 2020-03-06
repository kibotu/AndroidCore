@file:JvmName("ApplicationExtensions")

package com.exozet.android.core.extensions

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.PowerManager
import android.os.StrictMode
import androidx.annotation.RequiresPermission
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.security.ProviderInstaller
import com.google.firebase.messaging.FirebaseMessaging
import com.jakewharton.processphoenix.ProcessPhoenix
import net.kibotu.logger.Logger
import java.util.*

fun Application.installServiceProviderIfNeeded() = try {
    ProviderInstaller.installIfNeededAsync(this, object : ProviderInstaller.ProviderInstallListener {
        override fun onProviderInstallFailed(errorCode: Int, recoveryIntent: Intent?) {
            val apiGoogleInstance = GoogleApiAvailability.getInstance()

            if (apiGoogleInstance.isUserResolvableError(errorCode)) {
                // Recoverable error. Show a dialog prompting the user to
                // install/update/enable Google Play services.
                Logger.e("onProviderInstallFailed=$errorCode")
            }
        }

        override fun onProviderInstalled() {
        }
    })
} catch (e: GooglePlayServicesRepairableException) {
    Logger.e(e)
} catch (e: GooglePlayServicesNotAvailableException) {
    Logger.e(e)
}

/**
 * https://developer.android.com/reference/android/os/StrictMode
 */
fun Application.initStrictMode() {

    StrictMode.setThreadPolicy(
        StrictMode.ThreadPolicy.Builder()
            .detectCustomSlowCalls()
            .detectNetwork()
            .penaltyDeathOnNetwork()
            .apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    detectResourceMismatches()
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    detectUnbufferedIo()
                }
            }
            //.detectDiskReads() // realm native libs
            //.detectDiskWrites() // realm native libs
            .penaltyLog()
//            .penaltyDeath()
            .build()
    )

    StrictMode.setVmPolicy(
        StrictMode.VmPolicy.Builder()
            .detectActivityLeaks()
            .detectLeakedClosableObjects()
            .detectLeakedSqlLiteObjects()
            .apply {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    detectFileUriExposure()
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    detectCleartextNetwork()
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    detectContentUriWithoutPermission()
                    // detectUntaggedSockets() // exoplayer
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    // detectNonSdkApiUsage() // exoplayer
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    detectCredentialProtectedWhileLocked()
                    detectImplicitDirectBoot()
                }
            }
            .penaltyLog()
//            .penaltyDeath()
            .build()
    )
}

@RequiresPermission(Manifest.permission.DISABLE_KEYGUARD)
fun Application.unlockScreen() {
    val km = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
    @Suppress("DEPRECATION") val kl = km.newKeyguardLock("MyKeyguardLock")
    kl.disableKeyguard()

    val pm = getSystemService(Context.POWER_SERVICE) as PowerManager

    @Suppress("DEPRECATION")
    @SuppressLint("InvalidWakeLockTag") val wakeLock = pm.newWakeLock(
        PowerManager.FULL_WAKE_LOCK
                or PowerManager.ACQUIRE_CAUSES_WAKEUP
                or PowerManager.ON_AFTER_RELEASE, "MyWakeLock"
    )
    wakeLock.acquire(10 * 60 * 1000L /*10 minutes*/)
}

fun Application.switchPushNotificationTopic(from: Locale, to: Locale) {
    val fromTopic = "${packageName}.${from.language}"
    val toTopic = "${packageName}.${to.language}"
    with(FirebaseMessaging.getInstance()) {
        Logger.i("unsubscribe topic from=$fromTopic")
        unsubscribeFromTopic(fromTopic)
        unsubscribeFromTopic(from.language)
        Logger.i("subscribing topic to=$toTopic")
        subscribeToTopic(toTopic)
        subscribeToTopic(to.language)
    }
}

enum class VSC(val commitUrl: String, val filesUrl: String) {
    Github("/commits", "/src"),
    Gitlab("/commits", "/tree")
}

/**
 * [determine-if-the-device-is-a-smartphone-or-tablet](http://stackoverflow.com/a/18387977)
 */
val Application.isTablet: Boolean get() = resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_LARGE

fun Application.restart() = ProcessPhoenix.triggerRebirth(this)