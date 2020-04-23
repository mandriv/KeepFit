package com.mandriv.keepfit.data.steps

import androidx.lifecycle.LiveData

class StepsRepository private constructor(private val stepsDao: StepsDao) {

    fun getHistoryEntries(): LiveData<List<HistoryEntry>> {
        return stepsDao.getAllHistoryEntries()
    }

    fun getTodayStepEntry(): LiveData<StepsEntry> {
        return stepsDao.getTodayStepEntry()
    }

    fun getEntryById(id: Int): LiveData<StepsEntry> {
        return stepsDao.getStepEntryById(id)
    }

    suspend fun update(entry: StepsEntry) {
        return stepsDao.update(entry)
    }

    suspend fun delete(entry: StepsEntry) {
        return stepsDao.delete(entry)
    }

    suspend fun insert(entry: StepsEntry) {
        return stepsDao.insert(entry)
    }

    suspend fun addTodaySteps(steps: Int, goalId: Int) {
        return stepsDao.addTodaySteps(steps, goalId)
    }

    suspend fun insertIfNotPresent(entry: StepsEntry): Boolean {
        val entryAtDate = stepsDao.getStepsAtDate(entry.addedAt)
        @Suppress("SENSELESS_COMPARISON")
        if (entryAtDate != null) {
            return false
        }
        stepsDao.insert(entry)
        return true
    }

    companion object {
        // For Singleton instantiation
        @Volatile
        private var instance: StepsRepository? = null

        fun getInstance(stepsDao: StepsDao) =
            instance ?: synchronized(this) {
                instance ?: StepsRepository(stepsDao).also { instance = it }
            }
    }
}
