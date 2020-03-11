package com.mandriv.keepfit.data.steps

data class HistoryEntry(
    val id: Int,
    val stepCount: Int,
    val goalId: Int,
    val goalValue: Int,
    val percentageCompleted: Int
) {
    override fun toString(): String {
        return "id: ${id}, stepCount: ${stepCount}, goalId: ${goalId} goalValue: ${goalValue}, percentageCompleted: ${percentageCompleted}"
    }
}