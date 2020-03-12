package com.mandriv.keepfit.viewmodel.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mandriv.keepfit.data.goals.GoalRepository
import com.mandriv.keepfit.data.steps.StepsRepository

class NewHistoryViewModelFactory(
    private val stepRepository: StepsRepository,
    private val goalRepository: GoalRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewHistoryViewModel(stepRepository, goalRepository) as T
    }
}