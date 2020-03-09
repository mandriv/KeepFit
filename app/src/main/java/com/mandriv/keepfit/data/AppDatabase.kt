package com.mandriv.keepfit.data

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.mandriv.keepfit.data.goals.Goal
import com.mandriv.keepfit.data.goals.GoalDao
import com.mandriv.keepfit.data.steps.StepsDao
import com.mandriv.keepfit.data.steps.StepsEntry
import com.mandriv.keepfit.utilities.DATABASE_NAME
import com.mandriv.keepfit.workers.SeedDatabaseWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

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

        // Create and pre-populate the database. See this article for more details:
        // https://medium.com/google-developers/7-pro-tips-for-room-fbadea4bfbd1#4785
        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        val request = OneTimeWorkRequestBuilder<SeedDatabaseWorker>().build()
                        WorkManager.getInstance(context).enqueue(request)
                    }
                })
                .build()
        }
    }
}