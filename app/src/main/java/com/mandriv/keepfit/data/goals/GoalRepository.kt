package com.mandriv.keepfit.data.goals

import androidx.lifecycle.LiveData
import com.mandriv.keepfit.data.steps.StepsDao
import kotlinx.coroutines.coroutineScope

class GoalRepository private constructor(private val goalDao: GoalDao, private val stepsDao: StepsDao) {

    fun getInactiveGoals(): LiveData<List<Goal>> {
        return goalDao.getInactiveGoals()
    }

    fun getActiveGoal(): LiveData<Goal> {
        return goalDao.getActiveGoal()
    }

    fun getNotDeleted(): LiveData<List<Goal>> {
        return goalDao.getNotDeleted()
    }

    fun getById(id: Int): LiveData<Goal> {
        return goalDao.getById(id)
    }

    suspend fun insert(goal: Goal) {
        coroutineScope {
            if (goal.isActive) {
                goalDao.resetActiveGoals()
                val insertedId = goalDao.insert(goal)
                stepsDao.updateTodayGoal(insertedId.toInt())
            } else {
                goalDao.insert(goal)
            }
        }
    }

    suspend fun update(goal: Goal) {
        coroutineScope {
            if (goal.isActive) {
                stepsDao.updateTodayGoal(goal.id)
                goalDao.resetActiveGoals()
            }
            goalDao.update(goal)

        }
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