package com.exozet.android.core.demo

import com.exozet.android.core.base.BaseApplication
import net.kibotu.logger.Logger

/**
 * Created by [Jan Rabe](https://about.me/janrabe).
 */

class App : BaseApplication() {

    override fun onCreate() {
        super.onCreate()
        Logger.with(this)
        initRealm()
    }
}