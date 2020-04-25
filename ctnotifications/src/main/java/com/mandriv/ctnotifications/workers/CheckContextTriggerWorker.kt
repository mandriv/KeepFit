package com.mandriv.ctnotifications.workers

import android.app.NotificationManager
import android.content.Context
import android.location.Location
import android.text.format.DateFormat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.mandriv.ctnotifications.contextproviders.location.LocationRepository
import com.mandriv.ctnotifications.contextproviders.weather.WeatherRepository
import com.mandriv.ctnotifications.contextproviders.weather.pojos.WeatherDataResponse
import com.mandriv.ctnotifications.data.NotificationLog
import com.mandriv.ctnotifications.data.ServiceDatabase
import com.mandriv.ctnotifications.data.TriggerPolicy
import com.mandriv.ctnotifications.utils.NotificationHelper
import com.mandriv.ctnotifications.utils.TriggerContext
import com.mandriv.ctnotifications.utils.TriggerContextChecker
import java.util.*

class CheckContextTriggerWorker(val context: Context, workerParameters: WorkerParameters) :
    CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {
        // get current triggers
        val db = ServiceDatabase.getInstance(context)
        val triggers = db.triggerDao().getAll()
        // get required contexts to collect data
        val requiredTriggerContexts = TriggerContextChecker.getRequiredContext(triggers)

        // collect relevant data
        var currentWeather: WeatherDataResponse? = null
        var timeNow: String? = null
        // weather
        if (requiredTriggerContexts.contains(TriggerContext.WEATHER)) {
            var currentLocation: Location? = null
            try {
                currentLocation = LocationRepository.getLastLocation(context)
            } catch (err: Exception) {
                err.printStackTrace()
            }
            if (currentLocation != null) {
                try {
                    currentWeather =
                        WeatherRepository.getCurrentWeather(currentLocation.latitude, currentLocation.longitude)
                } catch (err: Exception) {
                    return Result.retry()
                }
            }
        }
        // time
        if (requiredTriggerContexts.contains(TriggerContext.TIME)) {
            timeNow = DateFormat.format("hh:mm", Date()) as String
        }
        // all context data collected, check conditions
        for (trigger in triggers) {
            // passing last log (notification) to check against policy
            var lastLog: NotificationLog? = null
            if (trigger.policy != TriggerPolicy.NO_POLICY) {
                lastLog = db.notificationLogDao().getLastLogByTriggerId(trigger.id)
            }
            // check if notification should fire
            val shouldFire = TriggerContextChecker.checkConditions(
                trigger,
                currentWeather,
                timeNow,
                lastLog
            )
            if (shouldFire) {
                // create notification and fire!
                val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                nm.notify(trigger.id, NotificationHelper.getFromTrigger(context, trigger))
                // save log of notification
                db.notificationLogDao().add(NotificationLog(trigger.id))
            }
        }

        return Result.success()
    }
}