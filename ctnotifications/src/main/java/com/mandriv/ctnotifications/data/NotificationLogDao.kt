package com.mandriv.ctnotifications.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface NotificationLogDao {

    @Query("SELECT * FROM notification_logs WHERE triggerId = :triggerId ORDER BY datetime(date) DESC LIMIT 1")
    fun getLastLogByTriggerId(triggerId: Int): NotificationLog

    @Insert
    fun add(n: NotificationLog)

}