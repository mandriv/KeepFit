package com.mandriv.keepfit.utilities

import android.content.Context
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.mandriv.keepfit.R

class NewStepsEntryAlertBuilder(context: Context) : MaterialAlertDialogBuilder(context) {

    fun create(view: View): AlertDialog {
        setTitle(context.resources.getString(R.string.add_steps))
        setView(view)
        setPositiveButton(context.resources.getString(R.string.add)) { _, _ -> }
        setNegativeButton(context.resources.getString(R.string.cancel)) { _, _ -> }
        return super.create()
    }

}