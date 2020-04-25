package com.mandriv.ctnotifications.data

import androidx.room.*

@Dao
interface TriggerDao {

    @Query("SELECT * from triggers")
    fun getAll(): List<Trigger>

    @Query("SELECT * from triggers WHERE appPackage = :appPackage")
    fun getAllByPackageName(appPackage: String): List<Trigger>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(trigger: Trigger): Long

    @Query("DELETE FROM triggers WHERE appPackage = :appPackage")
    fun clearAppTriggers(appPackage: String)

    @Query("DELETE FROM triggers WHERE id = :triggerId")
    fun deleteTrigger(triggerId: Int)

    @Update
    fun update(trigger: Trigger)

}