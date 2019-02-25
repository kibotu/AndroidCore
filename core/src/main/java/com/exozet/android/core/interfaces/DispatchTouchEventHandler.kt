package com.exozet.android.core.interfaces

import android.view.MotionEvent
import android.view.View

interface DispatchTouchEventHandler {

    val viewsHideKeyboardOnFocusLoss: Array<View?>?

    fun dispatchTouchEvent(event: MotionEvent): Boolean
}