package com.mandriv.keepfit.data.goals

import android.util.Log
import androidx.lifecycle.LiveData

class GoalRepository private constructor(private val goalDao: GoalDao) {

    fun getAllGoals(): LiveData<List<Goal>> {
        return goalDao.getAllGoals();
    }

    fun getActiveGoal(): LiveData<Goal> {
        return goalDao.getActiveGoal();
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