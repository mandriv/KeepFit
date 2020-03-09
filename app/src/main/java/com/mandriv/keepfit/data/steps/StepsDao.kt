package com.mandriv.keepfit.data.steps

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface StepsDao {

    @Query("SELECT * from steps WHERE date(addedAt) = date('now') ORDER BY datetime(addedAt) ASC")
    fun getTodayStepEntries(): LiveData<List<StepsEntry>>

    @Query("SELECT * from steps ORDER BY datetime(addedAt) ASC LIMIT :limit OFFSET :offset")
    fun getStepEntries(offset: Int = 0, limit: Int = 10): LiveData<List<StepsEntry>>

}
