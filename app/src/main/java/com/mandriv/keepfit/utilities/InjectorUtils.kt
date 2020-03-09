package com.mandriv.keepfit.utilities

import android.content.Context
import com.mandriv.keepfit.data.AppDatabase
import com.mandriv.keepfit.data.goals.GoalRepository
import com.mandriv.keepfit.ui.goals.GoalsViewModelFactory

object InjectorUtils {

    private fun getGoalsRepository(context: Context): GoalRepository {
        return GoalRepository.getInstance(
            AppDatabase.getInstance(context.applicationContext).goalDao())
    }

    fun provideGoalsViewModelFactory(
        context: Context
    ): GoalsViewModelFactory {
        val repository = getGoalsRepository(context)
        return GoalsViewModelFactory(repository)
    }
}