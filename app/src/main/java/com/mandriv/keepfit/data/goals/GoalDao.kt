package com.mandriv.keepfit.data.goals

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface GoalDao {
    @Query("SELECT * from goals")
    fun getAllGoals(): LiveData<List<Goal>>

    @Query("SELECT * from goals WHERE isActive = 1 LIMIT 1")
    fun getActiveGoal(): LiveData<Goal>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(goal: Goal): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(plants: List<Goal>)
}