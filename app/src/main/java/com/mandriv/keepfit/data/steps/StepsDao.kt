package com.mandriv.keepfit.data.steps

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface StepsDao {

    @Query("SELECT * from steps WHERE date(addedAt) = date('now') LIMIT 1")
    fun getTodayStepEntry(): LiveData<StepsEntry>

    @Query("SELECT * from steps ORDER BY datetime(addedAt) ASC")
    fun getAllStepEntries(): LiveData<List<StepsEntry>>

    @Query("SELECT * from steps WHERE date(addedAt) = date('now') LIMIT 1")
    fun getTodayStepEntryRaw(): StepsEntry

    @Update
    fun update(stepsEntry: StepsEntry)

    @Insert
    fun insert(stepsEntry: StepsEntry)

    @Transaction
    suspend fun updateTodayGoal(goalId: Int) {
        val stepsEntry = getTodayStepEntryRaw()
        @Suppress("SENSELESS_COMPARISON")
        if (stepsEntry != null) {
            update(StepsEntry(stepsEntry.id, stepsEntry.stepCount, stepsEntry.addedAt, goalId))
        }
    }

    @Transaction
    suspend fun addTodaySteps(steps: Int, goalId: Int) {
        val stepsEntry = getTodayStepEntryRaw()
        @Suppress("SENSELESS_COMPARISON")
        if (stepsEntry != null) {
            val newStepCount: Int = stepsEntry.stepCount + steps
            update(StepsEntry(stepsEntry.id, newStepCount, stepsEntry.addedAt, stepsEntry.goalId))
        } else {
            insert(StepsEntry(0, steps, goalId = goalId))
        }
    }

}
