package com.mandriv.keepfit.data.goals

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "goals")
data class Goal(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val value: Int,
    val name: String,
    val isActive: Boolean,
    val isDeleted: Boolean = false
) {
    override fun toString() = name
    fun getValueString() = value.toString()
}