package com.exozet.android.core.services.network.interceptors

import okhttp3.Interceptor
import okhttp3.Response

class ErrorInterceptor(val onError: (Response) -> Unit) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val response = chain.proceed(chain.request())

        return when {
            !response.isSuccessful -> {
                onError(response)
                Response.Builder().build()
            }
            else -> response
        }
    }
}