package com.mandriv.ctnotifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.Icon
import android.media.RingtoneManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat


class NotificationHelper constructor(context: Context) : ContextWrapper(context) {
    private val notificationManager: NotificationManager =
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    companion object {
        private const val MESSAGE_NOTIFICATION_TITLE = "Message Notification Channel"
        private const val COMMENT_NOTIFICATION_TITLE = "Comment Notification Channel"
        private const val DEFAULT_NOTIFICATION_TITLE = "Application Notification"
        const val MESSAGE_NOTIFICATION_CHANNEL = "your.package.name.message_channel"
        const val COMMENT_NOTIFICATION_CHANNEL = "your.package.name.comment_channel"
        private const val DEFAULT_NOTIFICATION_CHANNEL = "your.project.name.default_channel"
    }
    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            createChannels()
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun createChannels() {
        val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationChannels = mutableListOf<NotificationChannel>()
        // Building message notification channel

        val messageNotificationChannel = NotificationChannel(
            MESSAGE_NOTIFICATION_CHANNEL,
            MESSAGE_NOTIFICATION_TITLE, NotificationManager.IMPORTANCE_HIGH
        )
        messageNotificationChannel.enableLights(true)
        messageNotificationChannel.lightColor = Color.RED
        messageNotificationChannel.setShowBadge(true)
        messageNotificationChannel.setSound(uri, null)
        messageNotificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        notificationChannels.add(messageNotificationChannel)
        // Building comment notification channel

        val commentNotificationChannel = NotificationChannel(
            COMMENT_NOTIFICATION_CHANNEL,
            COMMENT_NOTIFICATION_TITLE, NotificationManager.IMPORTANCE_HIGH
        )
        commentNotificationChannel.enableLights(true)
        commentNotificationChannel.lightColor = Color.RED
        commentNotificationChannel.setShowBadge(true)
        commentNotificationChannel.setSound(uri, null)
        commentNotificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        notificationChannels.add(commentNotificationChannel)
        // Building default notification channel
        val defaultNotificationChannel = NotificationChannel(
            DEFAULT_NOTIFICATION_CHANNEL,
            DEFAULT_NOTIFICATION_TITLE, NotificationManager.IMPORTANCE_LOW
        )
        defaultNotificationChannel.setShowBadge(true)
        defaultNotificationChannel.setSound(uri, null)
        notificationChannels.add(defaultNotificationChannel)
        notificationManager.createNotificationChannels(notificationChannels)
    }
    fun createNotificationBuilder(
        title: String,
        body: String,
        cancelAble: Boolean = true,
        pendingIntent: PendingIntent? = null,
        channelId: String = DEFAULT_NOTIFICATION_CHANNEL
    ): Notification.Builder {
        val builder: Notification.Builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            Notification.Builder(applicationContext, channelId)
        else
            Notification.Builder(applicationContext)
        // get icon of a package
        builder.setSmallIcon(android.R.drawable.ic_dialog_info)
        if (pendingIntent != null)
            builder.setContentIntent(pendingIntent)
        builder.setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(cancelAble)
        return builder
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    fun deleteChannel(channelId: String) = apply {
        notificationManager.deleteNotificationChannel(channelId)
    }
    fun makeNotification(builder: Notification.Builder, notificationId: Int) = apply {
        notificationManager.notify(notificationId, builder.build())
    }
    fun cancelNotification(notificationId: Int) = apply {
        notificationManager.cancel(notificationId)
    }
}