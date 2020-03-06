package com.exozet.android.core.ui.custom

import android.content.Context
import android.content.res.Configuration
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

open class ZoomageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : com.jsibbold.zoomage.ZoomageView(context, attrs, defStyleAttr) {

    internal var view: View? = null

    override fun onTouchEvent(event: MotionEvent?): Boolean = when {
        event?.pointerCount ?: 0 <= 1 -> {
            view?.onTouchEvent(event)
            isTranslatable = false // disable translation while we have only one finger on screen
            super.onTouchEvent(event)
        }
        else -> {

            // consume touch event inside scroll container like view pagers
            parent.requestDisallowInterceptTouchEvent(true)

            isTranslatable = true
            super.onTouchEvent(event)
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        scaleType = ScaleType.FIT_CENTER
    }
}