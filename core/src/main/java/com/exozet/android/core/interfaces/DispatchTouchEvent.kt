package com.exozet.android.core.interfaces

import android.view.MotionEvent

/**
 * Created by Jan Rabe on 28/09/15.
 */
@Deprecated("use DispatchTouchEventHandler", ReplaceWith("DispatchTouchEventHandler"))
interface DispatchTouchEvent {

    fun dispatchTouchEvent(event: MotionEvent): Boolean
}

