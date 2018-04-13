package com.exozet.android.core.provider

import com.exozet.android.core.time.TimestampConvert
import com.google.gson.Gson
import com.google.gson.GsonBuilder

/**
 * Created by [Jan Rabe](https://about.me/janrabe).
 */

object GsonProvider {

    @JvmStatic
    val gson: Gson by lazy {
        GsonBuilder()
                .disableHtmlEscaping()
                .create()
    }

    @JvmStatic
    val gsonPrettyPrinting: Gson by lazy {
        GsonBuilder()
                .disableHtmlEscaping()
                .setPrettyPrinting()
                .create()
    }

    @JvmStatic
    val gsonWithDate: Gson by lazy {
        GsonBuilder()
                .disableHtmlEscaping()
                .setPrettyPrinting()
                .setDateFormat(TimestampConvert.iso8601Format())
                .create()
    }
}