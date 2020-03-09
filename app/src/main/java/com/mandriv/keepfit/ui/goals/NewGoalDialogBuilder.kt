package com.mandriv.keepfit.ui.goals

import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mandriv.keepfit.R

class NewGoalDialogBuilder: MaterialAlertDialogBuilder {

    @FunctionalInterface
    interface NewGoalDialogListener {
        fun onOk()
        fun onCancel()
    }

    constructor(context: Context, listener: NewGoalDialogListener) : super(context, R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Centered) {
        val inflater = LayoutInflater.from(context)

        setTitle(R.string.add_new_goal_title)
        setIcon(R.drawable.ic_check_box_blue)
        setView(inflater.inflate(R.layout.new_goal_dialog_view, null))
        setPositiveButton(R.string.save) { _,_ -> listener.onOk()}
        setNegativeButton(R.string.cancel) {_,_ -> listener.onCancel()}
    }

}

