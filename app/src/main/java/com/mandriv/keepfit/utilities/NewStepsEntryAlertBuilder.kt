package com.mandriv.keepfit.utilities

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.mandriv.keepfit.R

class NewStepsEntryAlertBuilder(context: Context, private val onAddStepsListener: AddStepsListener) :
    MaterialAlertDialogBuilder(context) {

    @FunctionalInterface
    interface AddStepsListener {
        fun onAddSteps(steps: Int)
    }

    @SuppressLint("InflateParams")
    override fun create(): AlertDialog {
        setTitle(context.resources.getString(R.string.add_steps))
        val content = LayoutInflater.from(context).inflate(R.layout.add_steps_form, null)
        setView(content)
        setPositiveButton(context.resources.getString(R.string.add)) { _, _ ->
            val textInputEditText: TextInputEditText = content.findViewById(R.id.addStepsInput)
            val value: Int = textInputEditText.text.toString().toInt()
            onAddStepsListener.onAddSteps(value)
        }
        setNegativeButton(context.resources.getString(R.string.cancel)) { _, _ ->

        }
        return super.create()
    }

}