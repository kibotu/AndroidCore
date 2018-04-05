@file:JvmName("URLExtensions")

package com.exozet.android.core.extensions

import java.net.HttpURLConnection
import java.net.URL
import java.net.URLConnection

/**
 * Created by [Jan Rabe](https://about.me/janrabe).
 */

inline fun <R> URL.use(block: (URLConnection) -> R): R {
    var connection: URLConnection? = null
    try {
        connection = openConnection()
        return block(connection)
    } catch (e: Exception) {
        throw e
    } finally {
        (connection as? HttpURLConnection)?.disconnect()
    }
}