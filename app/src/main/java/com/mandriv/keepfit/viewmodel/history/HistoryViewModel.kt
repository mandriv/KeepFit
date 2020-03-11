package com.mandriv.keepfit.viewmodel.history

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.mandriv.keepfit.data.steps.StepsRepository

class HistoryViewModel(private val stepsRepository: StepsRepository) : ViewModel() {

    val entries = stepsRepository.getHistoryEntries()

}