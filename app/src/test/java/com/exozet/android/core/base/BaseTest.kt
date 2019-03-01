package com.exozet.android.core.base

import android.app.Activity
import android.app.Application
import androidx.test.core.app.ApplicationProvider
import net.kibotu.ContextHelper
import net.kibotu.logger.Logger
import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config


/**
 * Created by [Jan Rabe](https://about.me/janrabe).
 */

@RunWith(RobolectricTestRunner::class)
@Config(application = AppStub::class, sdk = [28], manifest = Config.NONE)
abstract class BaseTest {

    private val application: Application
        get() = ApplicationProvider.getApplicationContext()

    private val cacheDir
        get() = application.cacheDir

    /**
     * Runs just once before all tests
     */
    @Before
    @Throws(Exception::class)
    open fun setup() {

        val app: Application = ApplicationProvider.getApplicationContext()
        Logger.with(app)
        val activity = Robolectric.buildActivity(ActivityStub::class.java).create().start().get()
        ContextHelper.setContext(activity)
    }

    fun String.stringFromAssets(): String = try {
        application.assets.open(this).bufferedReader().use { it.readText() }
    } catch (e: Exception) {
        Logger.e(e)
        ""
    }

    /**
     * Runs after every test
     */
    @After
    fun breakdown() {
    }

    companion object {

        /**
         * Runs just once before all tests
         */
        @BeforeClass
        fun beforeEverything() {
        }

        /**
         * Runs just once after all the tests
         */
        @AfterClass
        fun afterEverything() {
        }
    }
}

internal class AppStub : Application()
internal class ActivityStub : Activity()