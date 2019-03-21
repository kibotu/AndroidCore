package com.exozet.android.core.services.network.interceptors

import okhttp3.Interceptor
import okhttp3.Response

class AuthorizationInterceptor(private val tokenProvider: () -> String?) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        // add jwt token
        if (request.header("Authorization") == "Bearer") {
            request = request.newBuilder().removeHeader("Authorization")
                .addHeader("Authorization", "Bearer ${tokenProvider()}")
                .build()
        }

        return chain.proceed(request)
    }
}