package com.mandriv.keepfit.data.steps

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "steps")
data class StepsEntry(
    @PrimaryKey (autoGenerate = true) val id: Int,
    val stepCount: Int,
    val addedAt: Calendar = Calendar.getInstance()
)