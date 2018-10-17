package com.exozet.android.core.extensions

import android.net.Uri
import android.os.Environment
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