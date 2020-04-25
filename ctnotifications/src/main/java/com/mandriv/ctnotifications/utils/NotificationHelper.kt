package com.mandriv.ctnotifications.utils

import android.app.Notification
import android.content.Context
import androidx.core.app.NotificationCompat
import com.mandriv.ctnotifications.data.Trigger

object NotificationHelper {
    fun getFromTrigger(context: Context, trigger: Trigger): Notification {
        // build notification
        val notificationBuilder = NotificationCompat.Builder(context, trigger.nChannelId)

        if (trigger.nBadgeType != null) {
            notificationBuilder.setBadgeIconType(trigger.nBadgeType)
        }
        if (trigger.nCategory.isNotEmpty()) {
            notificationBuilder.setCategory(trigger.nCategory)
        }
        if (trigger.nColor != null) {
            notificationBuilder.color = trigger.nColor
        }
        if (trigger.nContentInfo.isNotEmpty()) {
            notificationBuilder.setContentInfo(trigger.nContentInfo)
        }
        if (trigger.nDefaults != null) {
            notificationBuilder.setDefaults(trigger.nDefaults)
        }
        if (trigger.nPriority != null) {
            notificationBuilder.priority = trigger.nPriority
        }
        notificationBuilder.setContentTitle(trigger.nContentTitle)
        notificationBuilder.setContentText(trigger.nContentText)
        notificationBuilder.setSmallIcon(trigger.nSmallIcon)

        return notificationBuilder.build()
    }
}