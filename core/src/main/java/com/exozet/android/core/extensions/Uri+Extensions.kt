package com.exozet.android.core.extensions

import android.net.Uri
import android.os.Environment
import androidx.browser.customtabs.CustomTabsIntent
import com.github.florent37.application.provider.ActivityProvider
import com.github.florent37.application.provider.application
import de.cketti.shareintentbuilder.ShareIntentBuilder
import net.kibotu.ContextHelper
import java.io.File

/**
 * Created by [Jan Rabe](https://about.me/janrabe).
 */

fun String.parseAssetFile(): Uri = Uri.parse("file:///android_asset/$this")

fun String.parseInternalStorageFile(): Uri = Uri.parse("${ContextHelper.getApplication()!!.filesDir.absolutePath}/$this")

fun String.parseExternalStorageFile(): Uri = Uri.parse("${Environment.getExternalStorageDirectory()}/$this")

fun String.parseFile(): Uri = Uri.fromFile(File(this))

val Uri.fileExists: Boolean
    get() = File(toString()).exists()

fun Uri.share() = ShareIntentBuilder.from(application!!)
    .text(this.toString())
    .share()

fun Uri.openChromeTab() = CustomTabsIntent.Builder()
    .build()
    .launchUrl(ActivityProvider.currentActivity!!, this)