package com.mandriv.keepfit.data.steps

import androidx.lifecycle.LiveData

class StepsRepository private constructor(private val stepsDao: StepsDao) {

    fun getHistoryEntries(): LiveData<List<HistoryEntry>> {
        return stepsDao.getAllHistoryEntries()
    }

    fun getTodayStepEntry(): LiveData<StepsEntry> {
        return stepsDao.getTodayStepEntry()
    }

    suspend fun addTodaySteps(steps: Int, goalId: Int) {
        return stepsDao.addTodaySteps(steps, goalId)
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
