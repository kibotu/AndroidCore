package com.exozet.commonutils.sample.ui

import android.Manifest
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.WindowManager
import com.common.android.utils.extensions.DeviceExtensions
import com.common.android.utils.extensions.FragmentExtensions
import com.common.android.utils.extensions.JUnitExtensions
import com.common.android.utils.extensions.KeyGuardExtensions
import com.common.android.utils.interfaces.Backpress
import com.common.android.utils.interfaces.DispatchTouchEvent
import com.common.android.utils.logging.Logger
import com.exozet.androidcommonutils.clearFragmentBackStack
import com.exozet.androidcommonutils.replaceFragment
import com.exozet.commonutils.sample.R
import com.exozet.commonutils.sample.debugMenu.DebugMenu
import com.exozet.commonutils.sample.misc.MainActivityListener
import permissions.dispatcher.*

@RuntimePermissions
class MainActivity : AppCompatActivity(), MainActivityListener {


    internal var debugMenu: DebugMenu? = null
    private var newIntent: Intent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        if (JUnitExtensions.isJUnitTest())
            return

        newIntent = intent
        Logger.v(TAG, "[onCreate] savedInstanceState=$savedInstanceState intent=$newIntent")

        // Keep the screen always on
        if (resources.getBoolean(R.bool.flag_keep_screen_on))
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        // unlock screen
        if (resources.getBoolean(R.bool.unlock_screen_on_start))
            KeyGuardExtensions.unlockScreen(this)

        setContentView(R.layout.activity_main)

        debugMenu = DebugMenu()

        if (!consumeIntent()) {
            openFragment()
        }
    }

    override fun onBackPressed() {

        // hide keyboard
        DeviceExtensions.hideKeyboard()//Fixme change to lib method

        // close menu
        if (debugMenu!!.isDrawerOpen) {
            debugMenu!!.closeDrawer()
            return
        }

        // let fragments handle back press
        val fragment = FragmentExtensions.currentFragment()
        if (fragment is Backpress && fragment.onBackPressed())
            return

        // pop back stack
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
            FragmentExtensions.printBackStack()
            return
        }

        // quit app
        finish()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        Logger.v(TAG, "[onConfigurationChanged] " + newConfig)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        Logger.v(TAG, "[onActivityResult] requestCode=$requestCode resultCode=$resultCode data=$data")

    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        this.intent = intent

        Log.e(TAG, "[onNewIntent] " + intent)

        this.newIntent = intent

        consumeIntent()
    }

    override fun openWelcomeFragment() {
        clearFragmentBackStack()
        supportFragmentManager.findFragmentByTag(WelcomeFragment.TAG) as WelcomeFragment? ?:
                WelcomeFragment.newInstance().also {
                    replaceFragment(it, R.id.fragment_container, WelcomeFragment.TAG)
                }
    }


    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {

        val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (fragment is DispatchTouchEvent)
            return fragment.dispatchTouchEvent(ev) || super.dispatchTouchEvent(ev)

        return super.dispatchTouchEvent(ev)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun consumeIntent(): Boolean {
        if (newIntent == null)
            return false

        val dataString = newIntent!!.dataString
        if (TextUtils.isEmpty(dataString))
            return false

        Log.e(TAG, "[consumeIntent] " + dataString)

        openFragment()

        newIntent = null

        return true
    }

    private fun openFragment() {

        openWelcomeFragment()

    }

    @NeedsPermission(Manifest.permission.CAMERA)
    fun openCameraFragment() {
//        supportFragmentManager.beginTransaction()
//                .replace(R.id.fragment_container, CameraFragment.newInstance())
//                .addToBackStack(null)
//                .commit()

        //FragmentExtensions.replaceToBackStackByFading(CameraFragment.newInstance())
    }

    @OnShowRationale(Manifest.permission.CAMERA)
    internal fun showRationaleForCamera(request: PermissionRequest) {
        //FIXME show message for camera
        AlertDialog.Builder(this)
                .setMessage(R.string.permission_location_rationale)
                .setPositiveButton(R.string.button_allow) { dialog, button -> request.proceed() }
                .setNegativeButton(R.string.button_deny) { dialog, button -> request.cancel() }
                .show()
    }

    @OnPermissionDenied(Manifest.permission.CAMERA)
    internal fun showCameraDenied() {
        //FIXME show message for camera
    }

    @OnNeverAskAgain(Manifest.permission.CAMERA)
    internal fun showNeverAskForCamera() {
        //FIXME show message for camera
    }


    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }
}
