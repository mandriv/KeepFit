package com.mandriv.keepfit.viewmodel.goals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mandriv.keepfit.data.goals.GoalRepository

class EditGoalViewModelFactory(
    private val goalsRepository: GoalRepository,
    private val goalId: Int
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EditGoalViewModel(
            goalsRepository,
            goalId
        ) as T
    }
}