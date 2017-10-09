package com.exozet.androidcommonutils.storage;

import android.app.Application;

import com.common.android.utils.logging.Logger;
import com.exozet.basejava.BuildConfig;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.HawkBuilder;
import com.orhanobut.hawk.LogLevel;

import java.util.Locale;

import static com.common.android.utils.extensions.LocaleExtensions.restartInLocale;
import static com.exozet.basejava.storage.LocalUser.HawkKeys.LOCALE_DEBUG;
import static com.exozet.basejava.storage.LocalUser.HawkKeys.LOCALE_DEFAULT;


/**
 * Created by <a href="https://about.me/janrabe">Jan Rabe</a>.
 */
public class LocalUser {

    private static final String TAG = LocalUser.class.getSimpleName();

    // region constants

    enum HawkKeys {

        LOCALE_DEFAULT,
        LOCALE_DEBUG
    }

    // endregion

    public static void clear() {
        Hawk.clear();
    }

    public static void with(Application context) {

        Hawk.init(context)
                .setEncryptionMethod(HawkBuilder.EncryptionMethod.NO_ENCRYPTION)
                .setStorage(HawkBuilder.newSharedPrefStorage(context))
                .setLogLevel(BuildConfig.DEBUG
                        ? LogLevel.FULL
                        : LogLevel.NONE)
                .build();
    }


    // region locale

    public static Locale getDefaultLocale() {
        return Hawk.get(LOCALE_DEFAULT.name(), Locale.ENGLISH);
    }

    public static void setDefaultLocale(Locale locale) {
        Hawk.put(LOCALE_DEFAULT.name(), locale);
    }

    public static Locale getDebugLocale() {
        return Hawk.get(LOCALE_DEBUG.name(), Locale.KOREA);
    }

    public static Locale setDebugLocale(Locale locale) {
        Logger.v(TAG, "[setDebugLocale] " + locale);
        return Hawk.get(LOCALE_DEBUG.name(), locale);
    }

    public static void switchLocale(Locale locale) {
        Logger.v(TAG, "[switchToDefault] " + locale);
        LocalUser.setDebugLocale(locale);
        restartInLocale(locale);
    }

    public static void switchToKorean() {
        switchLocale(Locale.KOREA);
    }

    public static void switchToDefault() {
        switchLocale(LocalUser.getDefaultLocale());
    }

    // endregion

}
