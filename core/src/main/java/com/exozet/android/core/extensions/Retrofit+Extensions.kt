package com.exozet.android.core.extensions

import net.kibotu.logger.Logger.loge
import okhttp3.Request
import okhttp3.Response

inline fun <reified T> retrofit2.Response<T>.log() {
    val message = "${code()} ${message()} ${errorBody()}"
    loge { message }
}

fun Response.setCustomHeader(key: String, value: String) = request.setAuthHeader(key, value)

fun Request.setCustomHeader(key: String, value: String) = newBuilder()
    .removeHeader(key)
    .addHeader(key, value)
    .build()

fun Response.setAuthHeader(authorization: String = "JWTAuthorization", token: String) = request.setAuthHeader(authorization, token)

fun Request.setAuthHeader(authorization: String = "JWTAuthorization", token: String) = newBuilder()
    .removeHeader(authorization)
    .addHeader(authorization, "Bearer $token")
    .build()

fun Response.setCacheKey(cacheKey: String = "JWTCacheKey", token: String) = request.setAuthHeader(cacheKey, token)

fun Request.setCacheKey(cacheKey: String = "JWTCacheKey", token: String) = newBuilder()
    .removeHeader(cacheKey)
    .addHeader(cacheKey, token)
    .build()