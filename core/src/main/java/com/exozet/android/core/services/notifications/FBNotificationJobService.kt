package com.exozet.android.core.services.notifications

import android.util.Log
import com.firebase.jobdispatcher.JobParameters
import com.firebase.jobdispatcher.JobService
import net.kibotu.logger.Logger


/**
 * Created by [Jan Rabe](https://about.me/janrabe).
 */
class FBNotificationJobService : JobService() {

    override fun onStartJob(job: JobParameters?): Boolean {
        Logger.d(TAG, "[onStartJob] Performing long running task in scheduled job")
        // TODO(developer): add long running task here.
        return false
    }

    override fun onStopJob(job: JobParameters?): Boolean {
        Logger.d(TAG, "[onStopJob] Performing long running task in scheduled job")
        return false
    }

    companion object {
        val TAG: String = FBNotificationJobService::class.java.simpleName
    }
}