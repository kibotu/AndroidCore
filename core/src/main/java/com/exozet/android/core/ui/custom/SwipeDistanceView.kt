package com.exozet.android.core.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View

open class SwipeDistanceView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    protected val gestureDetector: GestureDetector

    protected val scrollListener: ScrollListener

    protected var onScroll: ((percentX: Float, percentY: Float) -> Unit)? = null

    protected var onIsScrollingChanged: ((isScrolling: Boolean) -> Unit)? = null

    var isScrolling = false

    init {
        scrollListener = ScrollListener({ measuredWidth }, { measuredHeight }) { percentX, percentY ->
            onScroll?.invoke(percentX, percentY)
        }
        gestureDetector = GestureDetector(context, scrollListener)
    }

    /**
     * @param percentX [-1,1]
     * @param percentY [-1,1]
     */
    fun onScroll(onScroll: ((percentX: Float, percentY: Float) -> Unit)? = null) {
        this.onScroll = onScroll
    }

    /**
     * Notifies when scrolling starts or ends.
     *
     * @param isScrolling true if user is scrolling
     */
    fun onIsScrollingChanged(onIsScrollingChanged: ((isScrolling: Boolean) -> Unit)? = null) {
        this.onIsScrollingChanged = onIsScrollingChanged
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        parent.requestDisallowInterceptTouchEvent(true)
        return when {
            event?.actionMasked == MotionEvent.ACTION_DOWN && event.pointerCount <= 1 -> {
                gestureDetector.onTouchEvent(event)
            }
            event?.actionMasked == MotionEvent.ACTION_MOVE && event.pointerCount <= 1 -> {
                startScrolling()
                gestureDetector.onTouchEvent(event)
            }
            else -> {
                stopScrolling()
                super.onTouchEvent(event)
            }
        }
    }

    protected fun startScrolling() {
        if (isScrolling)
            return

        isScrolling = true

        onIsScrollingChanged?.invoke(isScrolling)
    }

    protected fun stopScrolling() {
        if (!isScrolling)
            return

        isScrolling = false

        onIsScrollingChanged?.invoke(isScrolling)
    }
}