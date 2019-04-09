package com.exozet.android.core.demo

import com.exozet.android.core.base.BaseApplication

/**
 * Created by [Jan Rabe](https://about.me/janrabe).
 */

class App : BaseApplication() {

    override fun onCreate() {
        super.onCreate()
        initRealm()
    }
}