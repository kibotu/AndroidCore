package com.exozet.android.core.utils;

import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;

import java.util.Collection;

import static com.exozet.android.core.utils.ViewExtensions.setColor;

/**
 * Created by jan.rabe on 17/12/15.
 */
final public class FontExtensions {

    private FontExtensions() throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    public static void applyFont(@NonNull final Typeface font, @NonNull final TextView... views) {
        for (final TextView view : views)
            view.setTypeface(font);
    }

    public static void applyColor(@ColorRes final int resourceId, @NonNull final Collection<View> views) {
        for (final View v : views)
            setColor(v, resourceId);
    }

    public static void applyColor(@ColorRes final int resourceId, @NonNull final View... views) {
        for (final View v : views)
            setColor(v, resourceId);
    }
}
