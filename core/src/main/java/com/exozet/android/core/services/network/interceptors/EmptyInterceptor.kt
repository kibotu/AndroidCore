package com.exozet.android.core.services.network.interceptors

import okhttp3.Interceptor
import okhttp3.Response

class EmptyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = chain.proceed(chain.request())
}