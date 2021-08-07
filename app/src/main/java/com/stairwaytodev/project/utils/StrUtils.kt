package com.stairwaytodev.project.utils

import android.graphics.Paint
import android.widget.TextView

object StrUtils {

    fun strikeText(view: TextView, isToBeStrikedThrough: Boolean) {
        if (isToBeStrikedThrough) {
            view.paintFlags = view.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }else {
            view.paintFlags = 0
        }
    }

    fun fillInZeroInFront(number: Int): String {
        if (number < 10) {
            return "0$number"
        }
        return "$number"
    }
}