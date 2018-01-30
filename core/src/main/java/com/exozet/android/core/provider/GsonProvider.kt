package com.exozet.android.core.provider

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

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
}