package com.exozet.android.core.extensions

import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.JELLY_BEAN
import android.support.annotation.ColorInt
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
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

fun gone(isGone: Boolean = true, vararg views: View) {
    views.forEach { it.gone(isGone) }
}

fun Array<View>.gone(isGone: Boolean = true) {
    forEach { it.gone(isGone) }
}

infix fun View.onClick(function: () -> Unit) {
    setOnClickListener { function() }
}

fun ProgressBar.indeterminateDrawableColor(@ColorRes color: Int) {
    indeterminateDrawable.setColorFilter(
            ContextCompat.getColor(context, color), android.graphics.PorterDuff.Mode.SRC_IN)
}

fun View.aspect(ratio: Float = 3 / 4f) =
        post {
            val params = layoutParams
            params.height = (width / ratio).toInt()
            layoutParams = params
        }


fun View.waitForLayout(onGlobalLayoutListener: ViewTreeObserver.OnGlobalLayoutListener) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                viewTreeObserver.removeOnGlobalLayoutListener(this)
            else {
                @Suppress("DEPRECATION")
                viewTreeObserver.removeGlobalOnLayoutListener(this)
            }

            onGlobalLayoutListener.onGlobalLayout()
        }
    })
}

fun View.setOnTouchActionUpListener(action: (v: View, event: MotionEvent) -> Boolean) {
    setOnTouchListener { v, event ->
        when (event.action) {
            MotionEvent.ACTION_DOWN -> true
            MotionEvent.ACTION_UP -> {
                action(v, event)
            }
            else -> false
        }
    }
}

/**
 * gradient(200f, 0x80C24641.toInt(), 0x80FFFFFF.toInt())
 */
fun View.gradient(radius: Float, vararg colors: Int) {
    if (SDK_INT >= JELLY_BEAN)
        background = GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors).apply { cornerRadius = radius }
    else
        throw UnsupportedOperationException("requires SDK >= 16 but was $SDK_INT")
}

fun View.locationInWindow(): Rect {
    val rect = Rect()
    val location = IntArray(2)

    getLocationInWindow(location)

    rect.left = location[0]
    rect.top = location[1]
    rect.right = location[0] + width
    rect.bottom = location[1] + height

    return rect
}

fun View.locationOnScreen(): Rect {
    val rect = Rect()
    val location = IntArray(2)

    getLocationOnScreen(location)

    rect.left = location[0]
    rect.top = location[1]
    rect.right = location[0] + width
    rect.bottom = location[1] + height

    return rect
}

@ColorInt
fun View.getCurrentColor(@ColorInt default: Int = Color.TRANSPARENT): Int = (background as? ColorDrawable)?.color
        ?: default

fun View.resize(width: Float? = null, height: Float? = null) {
    width?.let { layoutParams.width = it.toInt() }
    height?.let { layoutParams.height = it.toInt() }
}