package com.mandriv.keepfit.data.goals

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface GoalDao {

    @Query("SELECT * from goals WHERE isActive = 0 AND isDeleted = 0 ORDER BY value DESC")
    fun getInactiveGoals(): LiveData<List<Goal>>

    @Query("SELECT * from goals WHERE isActive = 1 AND isDeleted = 0 LIMIT 1")
    fun getActiveGoal(): LiveData<Goal>

    @Query("SELECT * FROM goals WHERE id = :goalId LIMIT 1")
    fun getById(goalId: Int): LiveData<Goal>

    @Query("SELECT * FROM goals WHERE isDeleted = 0 ORDER BY value DESC")
    fun getNotDeleted(): LiveData<List<Goal>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(goal: Goal): Long

    @Update
    suspend fun update(goal: Goal)

    @Delete
    suspend fun delete(goal: Goal)

    @Query("UPDATE goals SET isActive = 0 WHERE isActive = 1")
    suspend fun resetActiveGoals()

}