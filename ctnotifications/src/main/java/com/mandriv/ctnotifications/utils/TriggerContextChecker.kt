package com.mandriv.ctnotifications.utils

import com.mandriv.ctnotifications.contextproviders.weather.pojos.WeatherDataResponse
import com.mandriv.ctnotifications.data.NotificationLog
import com.mandriv.ctnotifications.data.Trigger
import com.mandriv.ctnotifications.data.TriggerPolicy
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object TriggerContextChecker {

    private fun daysFrom(date: Calendar): Int {
        val start = Calendar.getInstance().apply {
            timeInMillis = 0
            set(Calendar.DAY_OF_YEAR, date.get(Calendar.DAY_OF_YEAR))
            set(Calendar.YEAR, date.get(Calendar.YEAR))
        }.timeInMillis
        val now = Calendar.getInstance()
        val end = Calendar.getInstance().apply {
            timeInMillis = 0
            set(Calendar.DAY_OF_YEAR, now.get(Calendar.DAY_OF_YEAR))
            set(Calendar.YEAR, now.get(Calendar.YEAR))
        }.timeInMillis
        val differenceMillis = end - start
        return TimeUnit.MILLISECONDS.toDays(differenceMillis).toInt()
    }

    private val hasTimeField: (Trigger) -> Boolean = { !it.cTimeFrom.isBlank() || !it.cTimeTo.isBlank() }
    private val hasWeatherField: (Trigger) -> Boolean =
        { !it.weatherDesc.isBlank() || it.weatherTempMax != null || it.weatherTempMin != null }
    private val hasCustomNumericData: (Trigger) -> Boolean = { it.customNumeric.isNotEmpty() }

    private fun getRequiredContext(t: Trigger): List<TriggerContext> {
        val requiredContext = mutableListOf<TriggerContext>()
        // check for time triggers
        if (hasTimeField(t)) {
            requiredContext.add(TriggerContext.TIME)
        }
        // check for weather trigger
        if (hasWeatherField(t)) {
            requiredContext.add(TriggerContext.WEATHER)
        }
        // check for custom data trigger
        if (hasCustomNumericData(t)) {
            requiredContext.add(TriggerContext.CUSTOM_DATA)
        }
        return requiredContext
    }

    fun getRequiredContext(triggers: List<Trigger>): List<TriggerContext> {
        val requiredContext = mutableListOf<TriggerContext>()
        // check for time triggers
        if (triggers.any(hasTimeField)) {
            requiredContext.add(TriggerContext.TIME)
        }
        // check for weather trigger
        if (triggers.any(hasWeatherField)) {
            requiredContext.add(TriggerContext.WEATHER)
        }
        // check for custom data trigger
        if (triggers.any(hasCustomNumericData)) {
            requiredContext.add(TriggerContext.CUSTOM_DATA)
        }

        return requiredContext
    }

    fun checkConditions(
        t: Trigger,
        weatherData: WeatherDataResponse?,
        timeNow: String?,
        lastLog: NotificationLog?
    ): Boolean {
        if (hasWeatherField(t)) {
            if (weatherData == null) return false
            val currentTemp = weatherData.main.temp
            val currentDesc = weatherData.weather[0].description
            if (t.weatherTempMin != null && currentTemp < t.weatherTempMin) return false
            if (t.weatherTempMax != null && currentTemp > t.weatherTempMax) return false
            if (t.weatherDesc.isNotEmpty() && !t.weatherDesc.equals(currentDesc, true)) return false
        }
        if (hasTimeField(t)) {
            if (timeNow.isNullOrEmpty()) return false
            val sdf = SimpleDateFormat("hh:mm")

            val dateNow: Date = sdf.parse(timeNow)
            if (t.cTimeFrom.isNotEmpty()) {
                val dateFrom = sdf.parse(t.cTimeFrom)
                if (dateNow.before(dateFrom)) return false
            }
            if (t.cTimeTo.isNotEmpty()) {
                val dateTo = sdf.parse(t.cTimeTo)
                if (dateNow.after(dateTo)) return false
            }
        }
        if (hasCustomNumericData(t)) {
            for (cnt in t.customNumeric) {
                if (cnt.currentValue == null && (cnt.min != null || cnt.max != null)) return false
                if (cnt.currentValue != null) {
                    if (cnt.min != null && cnt.currentValue < cnt.min) return false
                    if (cnt.max != null && cnt.currentValue > cnt.max) return false
                }
            }
        }
        if (lastLog != null) {
            val daysFrom = daysFrom(lastLog.date)
            if (t.policy == TriggerPolicy.DAILY && daysFrom == 0) return false
            if (t.policy == TriggerPolicy.WEEKLY && daysFrom < 7) return false
        }
        return true
    }

}