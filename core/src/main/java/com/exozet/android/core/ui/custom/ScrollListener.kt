package com.exozet.android.core.ui.custom

import android.view.GestureDetector
import android.view.MotionEvent
import com.exozet.android.core.extensions.px
import net.kibotu.resourceextension.dp

open class ScrollListener(val width: () -> Int, val height: () -> Int) : GestureDetector.SimpleOnGestureListener() {

    var onScroll: ((percentX: Float, percentY: Float) -> Unit)? = null
    /**
     * Min Swipe X-Distance
     */
    var thresholdX = 3.dp

    /**
     * Min Swipe Y-Distance
     */
    var thresholdY = 3.dp

    /**
     * Starting X-Position
     */
    protected var startX = 0f

    /**
     * Starting Y-Position
     */
    protected var startY = 0f

    override fun onDown(e: MotionEvent?): Boolean {
        startX = e?.x ?: 0f
        startY = e?.y ?: 0f
        return true
    }

    override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
        val dX = startX - (e2?.x ?: 0f)
        val dY = startY - (e2?.y ?: 0f)

        return when {
            Math.abs(dX) <= thresholdX && Math.abs(dY) <= thresholdY -> false
            else -> {

                val percentX = (dX) / width()
                val percentY = (dY) / height()

                onScroll?.invoke(percentX, percentY)

                true
            }
        }
    }
}