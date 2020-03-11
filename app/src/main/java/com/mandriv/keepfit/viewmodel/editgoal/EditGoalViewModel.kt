package com.mandriv.keepfit.viewmodel.editgoal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mandriv.keepfit.data.goals.Goal
import com.mandriv.keepfit.data.goals.GoalRepository
import kotlinx.coroutines.launch


class EditGoalViewModel(
    private val goalsRepository: GoalRepository,
    private val goalId: Int
) : ViewModel() {

    val goal: LiveData<Goal> = goalsRepository.getById(goalId)

    val newGoalName = MutableLiveData<String>()
    val newGoalValue = MutableLiveData<String>()
    val newGoalActive = MutableLiveData<Boolean>()

    val edited = MutableLiveData<Boolean>(false)

    fun updateFields(updatedGoal: Goal) {
        newGoalName.value = updatedGoal.name
        newGoalValue.value = updatedGoal.value.toString()
        newGoalActive.value = updatedGoal.isActive
    }

    private fun update(goal: Goal) {
        viewModelScope.launch {
            goalsRepository.update(goal)
            edited.value = true
        }
    }

    fun onSave(): Boolean {
        if (newGoalValue.value != null && newGoalName.value != null && newGoalActive.value != null) {
            val value: Int = newGoalValue.value!!.toInt()
            val name: String = newGoalName.value!!
            val isActive: Boolean = newGoalActive.value ?: false
            val newGoal = Goal(goalId, value, name, isActive)
            update(newGoal)
        }
        return false
    }

}