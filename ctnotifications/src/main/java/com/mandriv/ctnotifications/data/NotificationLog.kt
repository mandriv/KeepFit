package com.mandriv.ctnotifications.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.*

@Entity(
    tableName = "notification_logs",
    foreignKeys = [ForeignKey(
        entity = Trigger::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("triggerId"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class NotificationLog(
    val triggerId: Int,
    val date: Calendar = Calendar.getInstance(),
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)