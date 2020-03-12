package com.mandriv.keepfit.viewmodel.history

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mandriv.keepfit.data.goals.GoalRepository
import com.mandriv.keepfit.data.steps.StepsEntry
import com.mandriv.keepfit.data.steps.StepsRepository
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.util.*

class NewHistoryViewModel(private val stepsRepository: StepsRepository, goalRepository: GoalRepository) :
    ViewModel() {

    val dateString = MutableLiveData<String>("")
    val goalName = MutableLiveData<String>("")
    val steps = MutableLiveData<String>("")

    val submitted = MutableLiveData<Boolean>(false)
    val badDateError = MutableLiveData<Boolean>(false)

    val close = MutableLiveData<Boolean>(false)

    private lateinit var calendarDate: Calendar

    val goals = goalRepository.getNotDeleted()
    val goalNames = Transformations.map(goals) { goals -> goals.map { goal -> "${goal.name} - ${goal.value} steps" } }

    fun onDate(epoch: Long) {
        val formattedDate = DateFormat.getDateInstance().format(epoch)
        dateString.value = formattedDate
        val cal = Calendar.getInstance()
        cal.timeInMillis = epoch
        calendarDate = cal
    }

    fun onSave(context: Context): Boolean {
        submitted.value = true
        if (dateString.value.isNullOrBlank() || goalName.value.isNullOrBlank() || steps.value.isNullOrBlank()) {
            return false
        }

        val steps = steps.value!!.toInt()
        val goalNameStr = goalName.value!!
        val goalParts = goalNameStr
            .substring(0, goalNameStr.length - 6)
            .split(" - ")
        val goalName: String = goalParts[0]
        val goalValue: Int = goalParts[1].toInt()
        val goalId = goals.value!!.find { g -> g.name == goalName && g.value == goalValue }?.id!!

        viewModelScope.launch {
            val entry = StepsEntry(0, steps, calendarDate, goalId)
            val success = stepsRepository.insertIfNotPresent(entry)
            if (success) close.value = true
            else {
                badDateError.value = true
                MaterialAlertDialogBuilder(context)
                    .setTitle("Invalid date provided")
                    .setMessage("There already exists a history entry on that date, please edit it or delete it from the list before adding new one.")
                    .setPositiveButton("ok") { _, _ -> }
                    .show()
            }
        }

        return false
    }
}