package com.mandriv.keepfit.viewmodel.today

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mandriv.keepfit.data.goals.GoalRepository
import com.mandriv.keepfit.data.steps.StepsRepository

class TodayViewModelFactory(
    private val stepsRepository: StepsRepository,
    private val goalRepository: GoalRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TodayViewModel(stepsRepository, goalRepository) as T
    }
}