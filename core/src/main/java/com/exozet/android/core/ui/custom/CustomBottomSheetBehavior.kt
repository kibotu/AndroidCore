package com.exozet.android.core.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HALF_EXPANDED
import net.kibotu.logger.Logger.i

/**
 * Custom behavior to inflate layout with [STATE_HALF_EXPANDED] state.
 */
class CustomBottomSheetBehavior<V : View> : BottomSheetBehavior<V> {

    constructor() : super()

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    init {
        isHideable = false
        // we force initialization to be half expanded, default seems to be expanded
        state = STATE_HALF_EXPANDED
    }

    override fun layoutDependsOn(parent: CoordinatorLayout, child: V, dependency: View): Boolean {
        i("layoutDependsOn parent=$parent child=$child dependency=$dependency")
        return super.layoutDependsOn(parent, child, dependency)
    }
}