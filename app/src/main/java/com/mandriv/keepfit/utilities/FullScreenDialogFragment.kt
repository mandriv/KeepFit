package com.mandriv.keepfit.utilities

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.mandriv.keepfit.R

open class FullScreenDialogFragment : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppTheme_FullScreenDialog)
    }

    override fun onStart() {
        super.onStart()
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.MATCH_PARENT
        // make it full screen
        dialog?.window?.setLayout(width, height)
        // make rounded corners visible
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        // add animation
        dialog?.window?.setWindowAnimations(R.style.AppTheme_Slide)
    }
}