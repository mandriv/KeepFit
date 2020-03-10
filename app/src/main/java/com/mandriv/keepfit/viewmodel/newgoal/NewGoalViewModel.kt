package com.mandriv.keepfit.viewmodel.newgoal

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mandriv.keepfit.data.goals.Goal
import com.mandriv.keepfit.data.goals.GoalRepository
import kotlinx.coroutines.launch

class NewGoalViewModel(
    private val goalsRepository: GoalRepository
) : ViewModel() {

    val newGoalName = MutableLiveData<String>()
    val newGoalValue = MutableLiveData<String>()
    val newGoalActive = MutableLiveData<Boolean>()

    private fun insert(goal: Goal) {
        viewModelScope.launch {
            goalsRepository.insert(goal)
        }
    }

    private fun resetActiveAndInsert(goal: Goal) {
        viewModelScope.launch {
            goalsRepository.resetActiveGoals()
            goalsRepository.insert(goal)
        }
    }

    fun onSave(): Boolean {
        if (newGoalValue.value != null && newGoalName.value != null) {
            val value: Int = newGoalValue.value!!.toInt()
            val name: String = newGoalName.value!!
            val isActive: Boolean = newGoalActive.value ?: false
            val newGoal = Goal(0, value, name, isActive)
            if (isActive) {
                resetActiveAndInsert(newGoal)
            } else {
                insert(newGoal)
            }
            return true
        }
        return false
    }

}