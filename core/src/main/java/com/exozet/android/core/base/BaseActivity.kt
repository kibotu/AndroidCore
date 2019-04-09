package com.exozet.android.core.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.exozet.android.core.R
import com.exozet.android.core.debug.DebugMenu
import com.exozet.android.core.extensions.currentFragment
import com.exozet.android.core.extensions.printBackStack
import com.exozet.android.core.interfaces.BackPress
import com.google.android.gms.common.ConnectionResult.*
import com.google.android.gms.common.GoogleApiAvailability

/**
 * Created by armando.shkurti on 14/12/17.
 */
@Deprecated("just demo, don't use")
abstract class BaseActivity : AppCompatActivity() {

    var debugMenu: DebugMenu? = null

    @LayoutRes
    var activityLayoutId = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(activityLayoutId)

        debugMenu = DebugMenu()

        checkGooglePlayServices()
    }

    override fun onBackPressed() {

        // hide keyboard
        // DeviceExtensions.hideKeyboard()

        // close menu
        if (debugMenu?.isDrawerOpen == true) {
            debugMenu?.closeDrawer()
            return
        }

        // let fragments handle back press
        val fragment = currentFragment()
        if (fragment is BackPress && fragment.consumeBackPress())
            return

        // pop back stack
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
            printBackStack()
            return
        }

        super.onBackPressed()
    }

    private fun checkGooglePlayServices() {
        when (GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this)) {
            SERVICE_MISSING -> GoogleApiAvailability.getInstance().getErrorDialog(this, SERVICE_MISSING, 0).show()
            SERVICE_VERSION_UPDATE_REQUIRED -> GoogleApiAvailability.getInstance().getErrorDialog(this, SERVICE_VERSION_UPDATE_REQUIRED, 0).show()
            SERVICE_DISABLED -> GoogleApiAvailability.getInstance().getErrorDialog(this, SERVICE_DISABLED, 0).show()
        }
    }
}