package com.exozet.commonutils.sample.storage

import android.app.Application
import com.common.android.utils.extensions.LocaleExtensions
import com.common.android.utils.logging.Logger
import com.exozet.commonutils.sample.BuildConfig
import com.orhanobut.hawk.Hawk
import com.orhanobut.hawk.HawkBuilder
import com.orhanobut.hawk.LogLevel
import java.util.*


/**
 * Created by [Jan Rabe](https://about.me/janrabe).
 */
class LocalUser {

    // region constants

    internal enum class HawkKeys {

        LOCALE_DEFAULT,
        LOCALE_DEBUG
    }

    // endregion

    companion object {

        private val TAG = LocalUser::class.java.simpleName

        @JvmStatic fun clear() {
            Hawk.clear()
        }

        @JvmStatic fun with(context: Application) {

            Hawk.init(context)
                    .setEncryptionMethod(HawkBuilder.EncryptionMethod.NO_ENCRYPTION)
                    .setStorage(HawkBuilder.newSharedPrefStorage(context))
                    .setLogLevel(if (BuildConfig.DEBUG)
                        LogLevel.FULL
                    else
                        LogLevel.NONE)
                    .build()
        }


        // region locale

        var defaultLocale: Locale
            get() = Hawk.get(HawkKeys.LOCALE_DEFAULT.name, Locale.ENGLISH)
            set(locale) {
                Hawk.put(HawkKeys.LOCALE_DEFAULT.name, locale)
            }

        val debugLocale: Locale
            get() = Hawk.get(HawkKeys.LOCALE_DEBUG.name, Locale.KOREA)

        @JvmStatic fun setDebugLocale(locale: Locale): Locale {
            Logger.v(TAG, "[setDebugLocale] " + locale)
            return Hawk.get(HawkKeys.LOCALE_DEBUG.name, locale)
        }

        @JvmStatic fun switchLocale(locale: Locale) {
            Logger.v(TAG, "[switchToDefault] " + locale)
            setDebugLocale(locale)
            LocaleExtensions.restartInLocale(locale)
        }

//        @JvmStatic fun switchToKorean() {
//            switchLocale(Locale.KOREA)
//        }

        @JvmStatic fun switchToDefault() {
            switchLocale(defaultLocale)
        }

        // endregion

    }
}