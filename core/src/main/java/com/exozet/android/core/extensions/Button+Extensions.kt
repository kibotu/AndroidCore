@file:JvmName("ButtonExtensions")

package com.exozet.android.core.extensions

import android.R
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import android.view.View
import android.widget.CompoundButton
import androidx.annotation.ColorRes
import androidx.appcompat.widget.AppCompatButton
import net.kibotu.resourceextension.resColor

/**
 * Created by [Jan Rabe](https://about.me/janrabe).
 */


fun CompoundButton.setCheckedWithoutAnimation(checked: Boolean) {
    val beforeVisibility = visibility
    visibility = View.INVISIBLE
    isChecked = checked
    visibility = beforeVisibility
}

fun AppCompatButton.iconWith(defaultIcon: Drawable, selectedIcon: Drawable): StateListDrawable = StateListDrawable().apply {
    val iconSelected = selectedIcon
    iconSelected.setBounds(0, 0, 21, 21)
    val defaultIcon = defaultIcon
    defaultIcon.setBounds(0, 0, 21, 21)

    addState(intArrayOf(R.attr.state_selected), iconSelected)
    addState(intArrayOf(-R.attr.state_selected), defaultIcon)
    addState(intArrayOf(), defaultIcon)
}

fun AppCompatButton.textColorsWith(@ColorRes color: Int, @ColorRes selectedColor: Int): ColorStateList {
    return ColorStateList(
        arrayOf(
            intArrayOf(R.attr.state_selected),
            intArrayOf(-R.attr.state_selected),
            intArrayOf()
        ),
        intArrayOf(
            selectedColor.resColor,
            color.resColor,
            color.resColor
        )
    )
}

