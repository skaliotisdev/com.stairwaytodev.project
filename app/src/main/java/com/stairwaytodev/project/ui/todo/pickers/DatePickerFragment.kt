package com.stairwaytodev.project.ui.todo.pickers

import android.app.Dialog
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import androidx.annotation.Nullable
import androidx.fragment.app.DialogFragment

class DatePickerDialogFragment : DialogFragment() {

    private var listener: OnDateSetListener? = null
    private var year = 0
    private var month = 0
    private var dayOfMonth = 0

    fun setListener(listener: OnDateSetListener?) {
        this.listener = listener
    }

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val fragmentArgs = arguments
        if (fragmentArgs != null) {
            year = fragmentArgs.getInt("year")
            month = fragmentArgs.getInt("month")
            dayOfMonth = fragmentArgs.getInt("dayOfMonth")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return DatePickerDialog(requireContext(), listener, year, month, dayOfMonth)
    }

    companion object {
        fun getInstance(year: Int, month: Int, dayOfMonth: Int): DatePickerDialogFragment {
            val datePickerDialogFragment = DatePickerDialogFragment()
            val bundle = Bundle()
            bundle.putInt("year", year)
            bundle.putInt("month", month)
            bundle.putInt("dayOfMonth", dayOfMonth)
            datePickerDialogFragment.arguments = bundle
            return datePickerDialogFragment
        }
    }
}