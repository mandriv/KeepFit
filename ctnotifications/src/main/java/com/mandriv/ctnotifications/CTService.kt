package com.mandriv.ctnotifications

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.work.*
import com.mandriv.ctnotifications.data.ServiceDatabase
import com.mandriv.ctnotifications.workers.CheckContextTriggerWorker
import java.util.concurrent.TimeUnit

class CTService: Service() {
    private val binder = ICTServiceImpl()

    override fun onCreate() {
        super.onCreate()
        binder.database = ServiceDatabase.getInstance(this)

        val workManager = WorkManager.getInstance(this)

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .build()

        // remove this
        val workNow = OneTimeWorkRequestBuilder<CheckContextTriggerWorker>()
            .setConstraints(constraints)
            .setBackoffCriteria(
                BackoffPolicy.EXPONENTIAL,
                PeriodicWorkRequest.MIN_BACKOFF_MILLIS,
                TimeUnit.MILLISECONDS
            )
            .build()
        workManager.enqueue(workNow)

        // restore this
        /*
        val work = PeriodicWorkRequestBuilder<CheckContextTriggerWorker>(
            PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS, // 15 minutes
            TimeUnit.MILLISECONDS
        )
            // .setConstraints(constraints)
            .setBackoffCriteria(BackoffPolicy.LINEAR, PeriodicWorkRequest.MIN_BACKOFF_MILLIS, TimeUnit.MILLISECONDS)
            .build()
        workManager.enqueueUniquePeriodicWork(
            "CHECK_CONTEXT_TRIGGERS",
            ExistingPeriodicWorkPolicy.KEEP,
            work
            );
         */


    }

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("XDDD", "no i chuj")
    }

}