package com.mandriv.ctnotifications.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mandriv.ctnotifications.utils.DATABASE_NAME


@Database(entities = [Trigger::class], version = 2, exportSchema = false)
@TypeConverters(Conventers::class)
abstract class ServiceDatabase: RoomDatabase() {
    abstract fun triggerDao(): TriggerDao

    companion object {

        // For Singleton instantiation
        @Volatile private var instance: ServiceDatabase? = null

        fun getInstance(context: Context): ServiceDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): ServiceDatabase {
            return Room.databaseBuilder(
                context,
                ServiceDatabase::class.java,
                DATABASE_NAME
            )
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build()
        }
    }
}