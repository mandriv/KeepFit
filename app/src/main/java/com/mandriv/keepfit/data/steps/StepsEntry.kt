package com.mandriv.keepfit.data.steps

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.mandriv.keepfit.data.goals.Goal
import java.util.*


@Entity(
    tableName = "steps",
    foreignKeys = [ForeignKey(
        entity = Goal::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("goalId"),
        onDelete = ForeignKey.SET_NULL
    )]
)
data class StepsEntry(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val stepCount: Int,
    val addedAt: Calendar = Calendar.getInstance(),
    val goalId: Int?
)