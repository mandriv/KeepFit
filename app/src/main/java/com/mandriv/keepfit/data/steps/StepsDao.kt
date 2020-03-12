package com.mandriv.keepfit.data.steps

import androidx.lifecycle.LiveData
import androidx.room.*
import java.util.*

@Dao
interface StepsDao {

    @Query("SELECT * from steps WHERE date(addedAt) = date('now') LIMIT 1")
    fun getTodayStepEntry(): LiveData<StepsEntry>

    @Query("SELECT * from steps ORDER BY datetime(addedAt) ASC")
    fun getAllStepEntries(): LiveData<List<StepsEntry>>

    @Query("SELECT * from steps WHERE date(addedAt) = date('now') LIMIT 1")
    fun getTodayStepEntryRaw(): StepsEntry

    @Query("SELECT * FROM steps WHERE date(addedAt) = date(:date) LIMIT 1")
    suspend fun getStepsAtDate(date: Calendar): StepsEntry

    @Query(
        """
        SELECT 
            steps.id AS id,
            steps.addedAt AS date,
            steps.stepCount AS stepCount,
            goals.id AS goalId,
            goals.value AS goalValue,
            steps.stepCount * 100 / goals.value AS percentageCompleted
        FROM steps
        LEFT JOIN goals ON steps.goalId = goals.id
        ORDER BY datetime(addedAt) DESC
        """
    )
    fun getAllHistoryEntries(): LiveData<List<HistoryEntry>>

    @Delete
    suspend fun delete(stepsEntry: StepsEntry)

    @Update
    fun update(stepsEntry: StepsEntry)

    @Insert
    suspend fun insert(stepsEntry: StepsEntry)

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
