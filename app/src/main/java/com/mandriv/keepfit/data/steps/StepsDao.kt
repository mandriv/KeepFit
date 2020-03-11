package com.mandriv.keepfit.data.steps

import android.util.Log
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

    @Query(
        """
        SELECT 
            steps.id AS id,
            steps.stepCount AS stepCount,
            goals.id AS goalId,
            goals.value AS goalValue,
            steps.stepCount * 100 / goals.value AS percentageCompleted
        FROM steps
        LEFT JOIN goals ON steps.goalId = goals.id
        """)
    fun getAllHistoryEntries(): LiveData<List<HistoryEntry>>

    /*
    @Query("UPDATE steps SET goalId = :goalId WHERE date(addedAt) = date('now')")
    fun updateTodayGoal(goalId: Int)
     */

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
