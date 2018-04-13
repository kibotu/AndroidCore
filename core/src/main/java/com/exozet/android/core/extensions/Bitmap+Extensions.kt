@file:JvmName("BitmapExtensions")

package com.exozet.android.core.extensions

import android.graphics.Bitmap
import net.kibotu.logger.Logger

/**
 * Created by [Jan Rabe](https://about.me/janrabe).
 */

fun Bitmap?.freeBitmap() {
    if (this == null || isRecycled) {
        return
    }

    try {
        recycle()
    } catch (e: Exception) {
        Logger.e(e)
    }
}