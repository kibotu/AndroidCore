package com.exozet.android.core.extensions

import android.content.Context
import net.kibotu.ContextHelper

/**
 * Created by [Jan Rabe](https://about.me/janrabe).
 */

fun Int.asCsv(context: Context = ContextHelper.getContext()!!): List<String> = context.resources.getString(this).split(",").map(String::trim).toList()