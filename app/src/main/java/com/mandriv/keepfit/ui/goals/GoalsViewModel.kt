package com.mandriv.keepfit.ui.goals

import androidx.lifecycle.*
import com.mandriv.keepfit.data.goals.Goal
import com.mandriv.keepfit.data.goals.GoalRepository

// Class extends AndroidViewModel and requires application as a parameter.
class GoalsViewModel internal constructor(goalsRepository: GoalRepository) : ViewModel() {
    val allGoals: LiveData<List<Goal>> = goalsRepository.getAllGoals()
    val activeGoal: LiveData<Goal> = goalsRepository.getActiveGoal()

    val test = "x"

}