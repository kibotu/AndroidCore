@file:JvmName("ApplicationExtensions")

package com.exozet.android.core.extensions

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.PowerManager
import android.os.StrictMode
import androidx.annotation.RequiresPermission
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.security.ProviderInstaller
import net.kibotu.logger.Logger

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

fun Application.initStrictMode() {

    StrictMode.setThreadPolicy(
        StrictMode.ThreadPolicy.Builder()
            .detectCustomSlowCalls()
            .detectNetwork()
            .penaltyLog()
            .penaltyDeath()
            .build()
    )

    StrictMode.setVmPolicy(
        StrictMode.VmPolicy.Builder()
            .apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                    detectLeakedRegistrationObjects()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    detectCleartextNetwork()
            }
            .detectActivityLeaks()
            .detectLeakedClosableObjects()
            .detectLeakedSqlLiteObjects()
            .penaltyLog()
            .penaltyDeath()
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