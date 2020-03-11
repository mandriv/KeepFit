package com.mandriv.keepfit.utilities

import android.content.Context
import com.mandriv.keepfit.data.AppDatabase
import com.mandriv.keepfit.data.goals.GoalRepository
import com.mandriv.keepfit.data.steps.StepsRepository
import com.mandriv.keepfit.viewmodel.editgoal.EditGoalViewModelFactory
import com.mandriv.keepfit.viewmodel.goals.GoalsViewModelFactory
import com.mandriv.keepfit.viewmodel.newgoal.NewGoalViewModelFactory
import com.mandriv.keepfit.viewmodel.today.TodayViewModelFactory

object InjectorUtils {

    private fun getGoalsRepository(context: Context): GoalRepository {
        return GoalRepository.getInstance(
            AppDatabase.getInstance(context.applicationContext).goalDao()
        )
    }

    private fun getStepsRepository(context: Context): StepsRepository {
        return StepsRepository.getInstance(
            AppDatabase.getInstance(context.applicationContext).stepsDao()
        )
    }

    fun provideGoalsViewModelFactory(
        context: Context
    ): GoalsViewModelFactory {
        val repository = getGoalsRepository(context)
        return GoalsViewModelFactory(repository)
    }

    fun provideNewGoalViewModelFactory(
        context: Context
    ): NewGoalViewModelFactory {
        val repository = getGoalsRepository(context)
        return NewGoalViewModelFactory(repository)
    }

    fun provideEditGoalViewModelFactory(
        context: Context,
        goalId: Int
    ): EditGoalViewModelFactory {
        val repository = getGoalsRepository(context)
        return EditGoalViewModelFactory(repository, goalId)
    }

    fun provideTodayViewModelFactory(
        context: Context
    ): TodayViewModelFactory {
        val stepsRepository = getStepsRepository(context)
        val goalRepository = getGoalsRepository(context)
        return TodayViewModelFactory(stepsRepository, goalRepository)
    }

}