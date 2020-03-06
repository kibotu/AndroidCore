package com.exozet.android.core.extensions


import android.os.Build
import android.webkit.WebView
import androidx.annotation.RequiresApi


@RequiresApi(Build.VERSION_CODES.KITKAT)
fun WebView.evaluateJavascriptCommandFetch(command: String, onSuccess: (String?) -> Unit) = evaluateJavascript("(function() { return $command; })();") { onSuccess(it) }

@RequiresApi(Build.VERSION_CODES.KITKAT)
fun WebView.evaluateJavascriptCommand(command: String, onSuccess: ((String?) -> Unit)? = null) = evaluateJavascript(""" (function() { $command })(); """.trimMargin()) { onSuccess?.invoke(it) }