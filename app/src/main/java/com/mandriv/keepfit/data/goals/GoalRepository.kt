package com.mandriv.keepfit.data.goals

import androidx.lifecycle.LiveData

class GoalRepository private constructor(private val goalDao: GoalDao) {

    fun getAllGoals(): LiveData<List<Goal>> {
        return goalDao.getAllGoals()
    }

    fun getInactiveGoals(): LiveData<List<Goal>> {
        return goalDao.getInactiveGoals()
    }

    fun getActiveGoal(): LiveData<Goal> {
        return goalDao.getActiveGoal()
    }

    suspend fun resetActiveGoals() {
        return goalDao.resetActiveGoals()
    }

    suspend fun insert(goal: Goal): Long {
        return goalDao.insert(goal)
    }

    companion object {
        // For Singleton instantiation
        @Volatile private var instance: GoalRepository? = null

        fun getInstance(goalDao: GoalDao) =
            instance ?: synchronized(this) {
                instance ?: GoalRepository(goalDao).also { instance = it }
            }
    }
}