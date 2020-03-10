package com.mandriv.keepfit.view.goals

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.mandriv.keepfit.R


abstract class SwipeToDeleteCallback internal constructor(context: Context) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
    private var deleteIcon: Drawable = ContextCompat.getDrawable(context, R.drawable.ic_delete_black_24dp)!!
    private var colorDrawableBackground: ColorDrawable = ColorDrawable(Color.parseColor("#cc0000"))

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val itemView = viewHolder.itemView
        val iconMarginVertical = (viewHolder.itemView.height - deleteIcon.intrinsicHeight) / 2

        if (dX > 0) {
            colorDrawableBackground.setBounds(itemView.left, itemView.top, dX.toInt(), itemView.bottom)
            deleteIcon.setBounds(
                itemView.left + iconMarginVertical, itemView.top + iconMarginVertical,
                itemView.left + iconMarginVertical + deleteIcon.intrinsicWidth, itemView.bottom - iconMarginVertical
            )
        } else {
            colorDrawableBackground.setBounds(
                itemView.right + dX.toInt(),
                itemView.top,
                itemView.right,
                itemView.bottom
            )
            deleteIcon.setBounds(
                itemView.right - iconMarginVertical - deleteIcon.intrinsicWidth, itemView.top + iconMarginVertical,
                itemView.right - iconMarginVertical, itemView.bottom - iconMarginVertical
            )
            deleteIcon.level = 0
        }

        colorDrawableBackground.draw(c)

        c.save()

        if (dX > 0)
            c.clipRect(itemView.left, itemView.top, dX.toInt(), itemView.bottom)
        else
            c.clipRect(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)

        deleteIcon.draw(c)

        c.restore()

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    /*
    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        return 0.7f
    }

     */
}