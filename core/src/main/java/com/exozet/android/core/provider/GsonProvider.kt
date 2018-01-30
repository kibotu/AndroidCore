package com.exozet.android.core.provider

import com.google.gson.Gson
import com.google.gson.GsonBuilder

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

fun Any.toJson(): String = GsonProvider.gson.toJson(this)

fun Any.toJsonPrettyPrinting(): String = GsonProvider.gsonPrettyPrinting.toJson(this)