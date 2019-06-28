package com.exozet.android.core.interfaces.annotations

import android.annotation.TargetApi
import android.os.Build
import android.view.HapticFeedbackConstants
import androidx.annotation.IntDef

@TargetApi(Build.VERSION_CODES.O_MR1)
@IntDef(
    value = [
        HapticFeedbackConstants.CLOCK_TICK,
        HapticFeedbackConstants.CONTEXT_CLICK,
        HapticFeedbackConstants.KEYBOARD_PRESS, // same as HapticFeedbackConstants.KEYBOARD_TAP
        HapticFeedbackConstants.KEYBOARD_RELEASE,
        HapticFeedbackConstants.LONG_PRESS,
        HapticFeedbackConstants.TEXT_HANDLE_MOVE,
        HapticFeedbackConstants.VIRTUAL_KEY,
        HapticFeedbackConstants.VIRTUAL_KEY_RELEASE]
)
@Retention(AnnotationRetention.SOURCE)
annotation class HapticFeedback