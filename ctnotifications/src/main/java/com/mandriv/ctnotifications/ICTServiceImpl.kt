package com.mandriv.ctnotifications

import android.content.Context

class ICTServiceImpl(private val ctx: Context): ICTService.Stub() {
    override fun addTrigger(config: String) {
        val notificationHelper = NotificationHelper(ctx)
        val builder = notificationHelper.createNotificationBuilder(
            "Test notification",
            "Notification description."
        )
        notificationHelper.makeNotification(builder, 123)
    }
}