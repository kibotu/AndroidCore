package com.exozet.android.core.interfaces;

import android.view.MotionEvent;

import androidx.annotation.NonNull;

/**
 * Created by Jan Rabe on 28/09/15.
 */
public interface DispatchTouchEvent {

    boolean dispatchTouchEvent(@NonNull final MotionEvent event);
}

