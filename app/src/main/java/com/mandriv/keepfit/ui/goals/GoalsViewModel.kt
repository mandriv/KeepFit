package com.mandriv.keepfit.ui.goals

import android.app.Application
import android.content.Context
import android.content.DialogInterface
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.*
import com.mandriv.keepfit.data.goals.Goal
import com.mandriv.keepfit.data.AppDatabase
import com.mandriv.keepfit.data.goals.GoalRepository
import kotlinx.coroutines.launch

// Class extends AndroidViewModel and requires application as a parameter.
class GoalsViewModel internal constructor(goalsRepository: GoalRepository) : ViewModel() {
    val allGoals: LiveData<List<Goal>> = goalsRepository.getAllGoals()
    val activeGoal: LiveData<Goal> = goalsRepository.getActiveGoal()

    val test = "x"

    val newGoalName = MutableLiveData<String>()
    val newGoalValue = MutableLiveData<String>()
    val newGoalActive = MutableLiveData<Boolean>()

    fun onNewGoalPress(context: Context) {
        val dialogBuilder = NewGoalDialogBuilder(context, object : NewGoalDialogBuilder.NewGoalDialogListener {
            override fun onOk() {
                Log.i("DIA", newGoalName.value.toString())
                Log.i("DIA", newGoalValue.value.toString())
                Log.i("DIA", newGoalActive.value.toString())
            }

            override fun onCancel() {
                Log.i("DIA", "Cancel")
            }
        })

        val dialog = dialogBuilder.create()
        dialog.show()
    }
}