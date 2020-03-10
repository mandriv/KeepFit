package com.mandriv.keepfit.data.goals

import androidx.lifecycle.LiveData

class GoalRepository private constructor(private val goalDao: GoalDao) {

    fun getInactiveGoals(): LiveData<List<Goal>> {
        return goalDao.getInactiveGoals()
    }

    fun getActiveGoal(): LiveData<Goal> {
        return goalDao.getActiveGoal()
    }

    fun getById(id: Int): LiveData<Goal> {
        return goalDao.getById(id)
    }

    suspend fun resetActiveGoals() {
        return goalDao.resetActiveGoals()
    }

    suspend fun insert(goal: Goal): Long {
        return goalDao.insert(goal)
    }

    suspend fun update(goal: Goal) {
        return goalDao.update(goal)
    }

    suspend fun delete(goal: Goal) {
        return goalDao.delete(goal)
    }

    companion object {
        // For Singleton instantiation
        @Volatile
        private var instance: GoalRepository? = null

        fun getInstance(goalDao: GoalDao) =
            instance ?: synchronized(this) {
                instance ?: GoalRepository(goalDao).also { instance = it }
            }
    }
}