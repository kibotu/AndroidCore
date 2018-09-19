package com.exozet.android.core.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;

import java.util.Locale;

import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.JELLY_BEAN_MR1;
import static android.os.Build.VERSION_CODES.LOLLIPOP_MR1;
import static net.kibotu.ContextHelper.getApplication;
import static net.kibotu.ContextHelper.getContext;

/**
 * Created by Jan Rabe on 27/10/15.
 */
final public class ResourceExtensions {

    private ResourceExtensions() throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    public static String getString(@StringRes final int id) {
        return getApplication().getString(id);
    }

    public static int dimension(@DimenRes final int resId) {
        return (int) getResources().getDimension(resId);
    }

    public static int color(@ColorRes final int color) {
        return ContextCompat.getColor(getApplication(), color);
    }

    @Nullable
    @TargetApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    public static Drawable drawable(@DrawableRes final int drawable) {
        return SDK_INT >= LOLLIPOP_MR1
                ? getResources().getDrawable(drawable, getContext().getTheme())
                : getResources().getDrawable(drawable);
    }

    public static Resources getResources() {
        return getApplication().getResources();
    }

    public static Configuration configuration() {
        return getResources().getConfiguration();
    }

    /**
     * Returns drawable resource id by name.
     *
     * @param drawable The name of the desired resource.
     * @return The associated resource identifier.  Returns 0 if no such
     * resource was found.  (0 is not a valid resource ID.)
     */
    @DrawableRes
    public static int getDrawableIdByName(@NonNull final String drawable) {
        return getResources().getIdentifier(drawable, "drawable", getApplication().getPackageName());
    }

    @Nullable
    public static Drawable getDrawableByName(@NonNull final Context context, @NonNull final String drawable) {
        return ContextCompat.getDrawable(context, getApplication().getResources().getIdentifier(drawable, "drawable", context.getPackageName()));
    }

    /**
     * https://stackoverflow.com/a/9475663/1006741
     *
     * @param id     string resource id
     * @param locale locale
     * @return localized string
     */
    public static String getLocalizedString(@StringRes int id, Locale locale) {

        if (SDK_INT > JELLY_BEAN_MR1) {
            return getLocalizedResources(getApplication(), locale).getString(id);
        }

        Resources res = getApplication().getResources();
        Configuration conf = res.getConfiguration();
        Locale savedLocale = conf.locale;
        conf.locale = locale; // whatever you want here
        res.updateConfiguration(conf, null); // second arg null means don't change

        // retrieve resources from desired locale
        String text = res.getString(id);

        // restore original locale
        conf.locale = savedLocale;
        res.updateConfiguration(conf, null);

        return text;
    }

    @RequiresApi(api = JELLY_BEAN_MR1)
    @NonNull
    public static Resources getLocalizedResources(Context context, Locale desiredLocale) {
        Configuration conf = context.getResources().getConfiguration();
        conf = new Configuration(conf);
        conf.setLocale(desiredLocale);
        Context localizedContext = context.createConfigurationContext(conf);
        return localizedContext.getResources();
    }

}
