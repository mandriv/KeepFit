package com.mandriv.keepfit.data.goals

import androidx.lifecycle.LiveData
import com.mandriv.keepfit.data.steps.StepsDao

class GoalRepository private constructor(private val goalDao: GoalDao, private val stepsDao: StepsDao) {

    fun getInactiveGoals(): LiveData<List<Goal>> {
        return goalDao.getInactiveGoals()
    }

    fun getActiveGoal(): LiveData<Goal> {
        return goalDao.getActiveGoal()
    }

    fun getById(id: Int): LiveData<Goal> {
        return goalDao.getById(id)
    }

    suspend fun insert(goal: Goal): Long {
        if (goal.isActive) {
            goalDao.resetActiveGoals()
            val id = goalDao.insert(goal)
            stepsDao.updateTodayGoal(goal.id)
            return id
        }
        return goalDao.insert(goal)
    }

    suspend fun update(goal: Goal) {
        if (goal.isActive) {
            stepsDao.updateTodayGoal(goal.id)
            goalDao.updateAndResetActive(goal)
        }
        return goalDao.update(goal)
    }

    suspend fun delete(goal: Goal) {
        val deletedGoal = Goal(goal.id, goal.value, goal.name, false, true)
        return goalDao.update(deletedGoal)
    }

    companion object {
        // For Singleton instantiation
        @Volatile
        private var instance: GoalRepository? = null

        fun getInstance(goalDao: GoalDao, stepsDao: StepsDao) =
            instance ?: synchronized(this) {
                instance ?: GoalRepository(goalDao, stepsDao).also { instance = it }
            }
    }
}