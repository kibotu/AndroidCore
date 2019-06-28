package com.exozet.android.core.ui.custom

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout
import kotlin.math.max
import kotlin.math.min


/**
 * Created by zhuleiyue on 2017/3/7.
 */

class AppBarLayoutOverScrollViewBehavior(context: Context?, attrs: AttributeSet?) : AppBarLayout.Behavior(context, attrs) {

    private var targetHeight = 500f

    private var targetView: View? = null

    private var parentHeight = 0

    private var targetViewHeight = 0

    private var totalDy = 0f

    private var lastScale = 0f

    private var lastBottom = 0

    private var isAnimate = false

    init {
        attrs?.let {
            // todo get overscroll view by id
            // todo get height multiplier
        }
    }

    override fun onLayoutChild(parent: CoordinatorLayout, abl: AppBarLayout, layoutDirection: Int): Boolean {
        val handled = super.onLayoutChild(parent, abl, layoutDirection)
        // 需要在调用过super.onLayoutChild()方法之后获取
        if (targetView == null) {
            targetView = parent.findViewWithTag(TAG)
            if (targetView != null) {
                initial(abl)
            }
        }

        if (targetView == null) {
            throw NullPointerException("No target view defined, please set tag to 'overscroll'")
        }

        targetHeight = targetView!!.height.toFloat() * 1.1f

        return handled
    }

    override fun onStartNestedScroll(parent: CoordinatorLayout, child: AppBarLayout, directTargetChild: View, target: View, nestedScrollAxes: Int, type: Int): Boolean {
        isAnimate = true
        return super.onStartNestedScroll(parent, child, directTargetChild, target, nestedScrollAxes, type)
    }

    override fun onNestedPreScroll(coordinatorLayout: CoordinatorLayout, child: AppBarLayout, target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        if (targetView != null && (dy < 0 && child.bottom >= parentHeight || dy > 0 && child.bottom > parentHeight)) {
            scale(child, target, dy)
        } else {
            super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)
        }
    }

    override fun onNestedPreFling(coordinatorLayout: CoordinatorLayout, child: AppBarLayout, target: View, velocityX: Float, velocityY: Float): Boolean {
        if (velocityY > 100) {
            isAnimate = false
        }
        return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY)
    }

    override fun onStopNestedScroll(coordinatorLayout: CoordinatorLayout, abl: AppBarLayout, target: View, type: Int) {
        recovery(abl)
        super.onStopNestedScroll(coordinatorLayout, abl, target, type)
    }

    private fun initial(abl: AppBarLayout) {
        abl.clipChildren = false
        parentHeight = abl.height
        targetViewHeight = targetView!!.height
    }

    private fun scale(abl: AppBarLayout, target: View, dy: Int) {
        totalDy += (-dy).toFloat()
        totalDy = min(totalDy, targetHeight)
        lastScale = max(1f, 1f + totalDy / targetHeight)
        targetView!!.scaleX = lastScale
        targetView!!.scaleY = lastScale
        lastBottom = parentHeight + (targetViewHeight / 2 * (lastScale - 1)).toInt()
        abl.bottom = lastBottom
        target.scrollY = 0
    }

    private fun recovery(abl: AppBarLayout) {
        if (totalDy > 0) {
            totalDy = 0f
            if (isAnimate) {
                val anim = ValueAnimator.ofFloat(lastScale, 1f).setDuration(200)
                anim.addUpdateListener { animation ->
                    val value = animation.animatedValue as Float
                    targetView!!.scaleX = value
                    targetView!!.scaleY = value
                    abl.bottom = (lastBottom - (lastBottom - parentHeight) * animation.animatedFraction).toInt()
                }
                anim.start()
            } else {
                targetView!!.scaleX = 1f
                targetView!!.scaleY = 1f
                abl.bottom = parentHeight
            }
        }
    }

    companion object {
        private const val TAG = "overScroll"
    }
}
