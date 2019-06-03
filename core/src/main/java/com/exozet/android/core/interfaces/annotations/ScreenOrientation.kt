package com.exozet.android.core.interfaces.annotations

import android.content.pm.ActivityInfo.*
import androidx.annotation.IntDef


@IntDef(
    value = [
        SCREEN_ORIENTATION_UNSPECIFIED,
        SCREEN_ORIENTATION_LANDSCAPE,
        SCREEN_ORIENTATION_PORTRAIT,
        SCREEN_ORIENTATION_USER,
        SCREEN_ORIENTATION_BEHIND,
        SCREEN_ORIENTATION_SENSOR,
        SCREEN_ORIENTATION_NOSENSOR,
        SCREEN_ORIENTATION_SENSOR_LANDSCAPE,
        SCREEN_ORIENTATION_SENSOR_PORTRAIT,
        SCREEN_ORIENTATION_REVERSE_LANDSCAPE,
        SCREEN_ORIENTATION_REVERSE_PORTRAIT,
        SCREEN_ORIENTATION_FULL_SENSOR,
        SCREEN_ORIENTATION_USER_LANDSCAPE,
        SCREEN_ORIENTATION_USER_PORTRAIT,
        SCREEN_ORIENTATION_FULL_USER,
        SCREEN_ORIENTATION_LOCKED]
)
@Retention(AnnotationRetention.SOURCE)
annotation class ScreenOrientation