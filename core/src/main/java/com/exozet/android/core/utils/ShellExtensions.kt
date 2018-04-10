package com.exozet.android.core.utils

import android.text.TextUtils
import android.util.Log
import net.kibotu.logger.Logger
import org.apache.commons.io.IOUtils

/**
 * Created by [Jan Rabe](https://about.me/janrabe).
 */


fun executeShellCommand(command: String): Boolean {
    var process: Process? = null
    try {
        Logger.v("Command", "> $command")
        process = Runtime.getRuntime().exec(command)

        val outputStreamResult = IOUtils.toString(process.errorStream)
        if (!TextUtils.isEmpty(outputStreamResult)) {
            Logger.e("Command", "> outputStreamResult")
            Logger.snackbar(outputStreamResult)
            return false
        }

        val inputStreamResult = IOUtils.toString(process.inputStream)
        if (!TextUtils.isEmpty(inputStreamResult)) {
            Logger.v("Command", "> $inputStreamResult")
            Logger.snackbar(inputStreamResult)
        }

        return true
    } catch (e: Exception) {
        Log.e("Command", "" + e.message)
        return false
    } finally {
        if (process != null) {
            try {
                process.destroy()
            } catch (e: Exception) {
                Log.e("Command", "" + e.message)
            }

        }
    }
}