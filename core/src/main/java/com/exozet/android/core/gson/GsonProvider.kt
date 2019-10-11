@file:JvmName("GsonProvider")

package com.exozet.android.core.gson

import com.exozet.android.core.time.TimestampConvert
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Created by [Jan Rabe](https://about.me/janrabe).
 */


val gsonWithDate by lazy {
    with(GsonExclusionStrategy()) {
        com.google.gson.GsonBuilder()
            .addSerializationExclusionStrategy(this)
            .addDeserializationExclusionStrategy(this)
            .disableHtmlEscaping()
            .setDateFormat(TimestampConvert.iso8601Format())
            .create()
    }
}

val gson by lazy {
    with(GsonExclusionStrategy()) {
        com.google.gson.GsonBuilder()
            .addSerializationExclusionStrategy(this)
            .addDeserializationExclusionStrategy(this)
            .disableHtmlEscaping()
            .create()
    }
}

val gsonPrettyPrinting by lazy {
    with(GsonExclusionStrategy()) {
        com.google.gson.GsonBuilder()
            .addSerializationExclusionStrategy(this)
            .addDeserializationExclusionStrategy(this)
            .disableHtmlEscaping()
            .setPrettyPrinting()
            .create()
    }
}

inline fun <reified T> T.toJson(): String = gson.toJson(this)

inline fun <reified T> T.toJsonPrettyPrinting(): String = gsonPrettyPrinting.toJson(this)

inline fun <reified T> String.fromJson(): T = gson.fromJson(this)

inline fun <reified T> Gson.fromJson(json: String): T = this.fromJson<T>(json, object : TypeToken<T>() {}.type)