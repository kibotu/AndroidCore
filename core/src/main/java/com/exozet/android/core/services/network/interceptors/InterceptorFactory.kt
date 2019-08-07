@file:JvmName("InterceptorFactory")

package com.exozet.android.core.services.network.interceptors

import com.github.simonpercic.oklog3.OkLogInterceptor
import net.kibotu.logger.Logger
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

fun createHttpLoggingInterceptor(enableLogging: () -> Boolean): HttpLoggingInterceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
    override fun log(message: String) {
        Logger.v(message)
    }
}).apply {
    level = if (enableLogging())
        HttpLoggingInterceptor.Level.BODY
    else
        HttpLoggingInterceptor.Level.NONE
}

@Deprecated("removed, use app/res/xml/network-config.xml for certificates", replaceWith = ReplaceWith(""))
fun OkHttpClient.Builder.addCertificates(certificate: String? = null, dangerouslyTrustingAllHosts: Boolean = false): OkHttpClient.Builder = this

fun createOKLogInterceptor() = OkLogInterceptor.builder()
    .withRequestHeaders(true)
    .withResponseHeaders(true)
    .build()