@file:JvmName("ApplicationExtensions")

package com.exozet.android.core.extensions

import android.app.Application
import android.content.Intent
import android.os.Build
import android.os.StrictMode
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