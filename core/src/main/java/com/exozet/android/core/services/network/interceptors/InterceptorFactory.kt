@file:JvmName("InterceptorFactory")

package com.exozet.android.core.services.network.interceptors

import com.exozet.android.core.services.network.SslUtils
import com.github.simonpercic.oklog3.OkLogInterceptor
import net.kibotu.ContextHelper
import net.kibotu.logger.Logger
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

fun createHttpLoggingInterceptor(enableLogging: () -> Boolean): HttpLoggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { Logger.v(it) }).apply {
    level = if (enableLogging())
        HttpLoggingInterceptor.Level.BODY
    else
        HttpLoggingInterceptor.Level.NONE
}

/**
 * Show all charite certificates
 *
 *      openssl s_client -connect s-c15-sps-app01.charite.de:443 -showcerts
 *
 * Download certificate
 *
 *      openssl s_client -showcerts -connect s-c15-sps-app01.charite.de:443 </dev/null 2>/dev/null|openssl x509 -outform PEM > app/main/assets/charite.pem
 */
fun OkHttpClient.Builder.addCertificates(certificate: String? = null, dangerouslyTrustingAllHosts: Boolean = false): OkHttpClient.Builder = when {
    dangerouslyTrustingAllHosts -> {
        @Suppress("DEPRECATION")
        sslSocketFactory(SslUtils.getTrustAllHostsSSLSocketFactory()!!)
    }
    certificate != null -> {
        @Suppress("DEPRECATION")
        sslSocketFactory(SslUtils.getSslContextForCertificateFile(ContextHelper.getApplication()!!, certificate).socketFactory)
    }
    else -> this
}

fun createOKLogInterceptor() = OkLogInterceptor.builder()
    .withRequestHeaders(true)
    .withResponseHeaders(true)
    .build()