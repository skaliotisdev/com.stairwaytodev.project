package com.stairwaytodev.project.ui.todo.pickers

import android.app.Dialog
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.os.Bundle
import androidx.annotation.Nullable
import androidx.fragment.app.DialogFragment

class TimePickerDialogFragment : DialogFragment() {

    private var listener: OnTimeSetListener? = null
    private var hourOfDay = 0
    private var minute = 0
    private var is24Hours = false

    fun setListener(listener: OnTimeSetListener?) {
        this.listener = listener
    }

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val fragmentArgs = arguments
        if (fragmentArgs != null) {
            hourOfDay = fragmentArgs.getInt("hourOfDay")
            minute = fragmentArgs.getInt("minute")
            is24Hours = fragmentArgs.getBoolean("is24hours")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return TimePickerDialog(context, listener, hourOfDay, minute, is24Hours)
    }

    companion object {
        fun getInstance(hourOfDay: Int, minute: Int, is24Hours: Boolean): TimePickerDialogFragment {
            val timePickerDialogFragment = TimePickerDialogFragment()
            val bundle = Bundle()
            bundle.putInt("hourOfDay", hourOfDay)
            bundle.putInt("minute", minute)
            bundle.putBoolean("is24hours", is24Hours)
            timePickerDialogFragment.arguments = bundle
            return timePickerDialogFragment
        }
    }
}