package com.mandriv.keepfit.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mandriv.keepfit.data.goals.Goal
import com.mandriv.keepfit.data.goals.GoalDao
import com.mandriv.keepfit.data.steps.StepsDao
import com.mandriv.keepfit.data.steps.StepsEntry
import com.mandriv.keepfit.utilities.DATABASE_FILE
import com.mandriv.keepfit.utilities.DATABASE_NAME


@Database(entities = [Goal::class, StepsEntry::class], version = 1, exportSchema = false)
@TypeConverters(Conventers::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun goalDao(): GoalDao
    abstract fun stepsDao(): StepsDao

    companion object {

        // For Singleton instantiation
        @Volatile private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                .createFromAsset(DATABASE_FILE)
                .build()
        }
    }
}