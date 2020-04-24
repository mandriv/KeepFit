package com.mandriv.ctnotifications.data

import androidx.room.*

@Dao
interface TriggerDao {

    @Query("SELECT * from triggers")
    fun getAll(): List<Trigger>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(trigger: Trigger): Long

}