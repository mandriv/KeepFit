package com.mandriv.keepfit.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.mandriv.keepfit.data.AppDatabase
import com.mandriv.keepfit.data.goals.Goal
import com.mandriv.keepfit.utilities.SEED_FILENAME
import kotlinx.coroutines.coroutineScope

class SeedDatabaseWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result = coroutineScope {
        try {
            applicationContext.assets.open(SEED_FILENAME).use { inputStream ->
                JsonReader(inputStream.reader()).use { jsonReader ->
                    val goalType = object : TypeToken<List<Goal>>() {}.type
                    val goalList: List<Goal> = Gson().fromJson(jsonReader, goalType)

                    val database = AppDatabase.getInstance(applicationContext)
                    database.goalDao().insertAll(goalList)

                    Result.success()
                }
            }
        } catch (ex: Exception) {
            Log.e(TAG, "Error seeding database", ex)
            Result.failure()
        }
    }

    companion object {
        private val TAG = SeedDatabaseWorker::class.java.simpleName
    }
}