@file:JvmName("ButtonExtensions")

package com.exozet.android.core.extensions

import android.view.View
import android.widget.CompoundButton

/**
 * Created by [Jan Rabe](https://about.me/janrabe).
 */


fun CompoundButton.setCheckedWithoutAnimation(checked: Boolean) {
    val beforeVisibility = visibility
    visibility = View.INVISIBLE
    isChecked = checked
    visibility = beforeVisibility
}
