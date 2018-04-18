package com.exozet.android.core.demo

import android.os.Bundle
import com.exozet.android.core.base.BaseActivity
import com.exozet.android.core.extensions.replaceFragment

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Core)
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
//            replaceFragment(WidgetSampleFragment.newInstance())
            replaceFragment(ViewModelSampleFragment())
        }

    }

}