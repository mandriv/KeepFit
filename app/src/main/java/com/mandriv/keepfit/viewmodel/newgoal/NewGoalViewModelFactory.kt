package com.mandriv.keepfit.viewmodel.newgoal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mandriv.keepfit.data.goals.GoalRepository

class NewGoalViewModelFactory(
    private val goalsRepository: GoalRepository
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewGoalViewModel(goalsRepository) as T
    }
}