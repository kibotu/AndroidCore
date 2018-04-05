package com.exozet.android.core.extensions

import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar

/**
 * Created by [Jan Rabe](https://about.me/janrabe).
 */

fun View.setMargins(
        left: Int? = null,
        top: Int? = null,
        right: Int? = null,
        bottom: Int? = null
) {
    val lp = layoutParams as? ViewGroup.MarginLayoutParams
            ?: return

    lp.setMargins(
            left ?: lp.leftMargin,
            top ?: lp.topMargin,
            right ?: lp.rightMargin,
            bottom ?: lp.bottomMargin
    )

    layoutParams = lp
}


fun View.setDimension(
        width: Int? = null,
        height: Int? = null
) {
    val params = layoutParams
    params.width = width ?: params.width
    params.height = height ?: params.height
    layoutParams = params
}

fun View?.show(isShowing: Boolean = true) {
    this?.visibility = if (isShowing) View.VISIBLE else View.INVISIBLE
}

fun View?.hide(isHiding: Boolean = true) {
    this?.visibility = if (isHiding) View.INVISIBLE else View.VISIBLE
}

fun View?.gone(isGone: Boolean = true) {
    this?.visibility = if (isGone) View.GONE else View.VISIBLE
}

infix fun View.onClick(function: () -> Unit) {
    setOnClickListener { function() }
}

fun ProgressBar.indeterminateDrawableColor(@ColorRes color: Int) {
    indeterminateDrawable.setColorFilter(
            ContextCompat.getColor(context, color), android.graphics.PorterDuff.Mode.SRC_IN)
}