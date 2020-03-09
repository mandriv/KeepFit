package com.mandriv.keepfit.data.steps

class StepsRepository private constructor(private val stepsDao: StepsDao) {

    fun getTodaySteps() = stepsDao.getTodayStepEntries()

    fun getAllSteps() = stepsDao.getStepEntries()

    companion object {
        // For Singleton instantiation
        @Volatile private var instance: StepsRepository? = null

        fun getInstance(stepsDao: StepsDao) =
            instance ?: synchronized(this) {
                instance ?: StepsRepository(stepsDao).also { instance = it }
            }
    }
}
