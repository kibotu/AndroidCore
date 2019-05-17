package com.exozet.android.core.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GestureDetectorCompat

open class SwipeDistanceView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    protected val gestureDetector: GestureDetectorCompat

    val scrollListener: ScrollListener = ScrollListener({ measuredWidth }, { measuredHeight })

    protected var onIsScrollingChanged: ((isScrolling: Boolean) -> Unit)? = null

    var isScrolling = false

    init {
        gestureDetector = GestureDetectorCompat(context, scrollListener)
        gestureDetector.setIsLongpressEnabled(false)
    }

    /**
     * @param percentX [-1,1]
     * @param percentY [-1,1]
     */
    fun onScroll(onScroll: ((percentX: Float, percentY: Float) -> Unit)?) {
        scrollListener.onScroll = onScroll
    }

    /**
     * Notifies when scrolling starts or ends.
     *
     * @param isScrolling true if user is scrolling
     */
    fun onIsScrollingChanged(onIsScrollingChanged: ((isScrolling: Boolean) -> Unit)?) {
        this.onIsScrollingChanged = onIsScrollingChanged
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean = when {
        event?.actionMasked == MotionEvent.ACTION_MOVE -> {
            startScrolling()
            gestureDetector.onTouchEvent(event)
        }
        else -> {
            stopScrolling()
            gestureDetector.onTouchEvent(event)
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