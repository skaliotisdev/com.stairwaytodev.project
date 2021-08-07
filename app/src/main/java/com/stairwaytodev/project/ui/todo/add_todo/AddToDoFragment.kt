package com.stairwaytodev.project.ui.todo.add_todo

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.stairwaytodev.project.R
import com.stairwaytodev.project.data.todo.ToDoModel
import com.stairwaytodev.project.ui.todo.pickers.DatePickerDialogFragment
import com.stairwaytodev.project.ui.todo.pickers.TimePickerDialogFragment
import com.stairwaytodev.project.utils.DateFormat
import com.stairwaytodev.project.utils.StrUtils
import java.util.*

class AddToDoFragment : Fragment() {

    private lateinit var viewModel: AddToDoViewModel
    private lateinit var textViewToDoTimeSet: TextView
    private lateinit var textViewToDoDateSet: TextView
    private lateinit var fragmentViewRef: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_todo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentViewRef = view
        setupActionBar()
        viewModel = ViewModelProvider(this).get(AddToDoViewModel::class.java)
        setTimeForToDo()
        setDateForToDo()

        textViewToDoTimeSet = view.findViewById(R.id.textViewToDoTimeSet)
        viewModel.getToDoTime().observe(viewLifecycleOwner, {
            textViewToDoTimeSet.text = it
        })

        textViewToDoDateSet = view.findViewById(R.id.textViewToDoDateSet)
        viewModel.getToDoDate().observe(viewLifecycleOwner, {
            textViewToDoDateSet.text = it
        })

        val buttonSave = view.findViewById<FloatingActionButton>(R.id.fabSaveToDo)
        buttonSave.setOnClickListener {
            saveToDo()
        }
    }

    private fun saveToDo() {
        val todoText = fragmentViewRef.findViewById<EditText>(R.id.editTextToDo)
        if (TextUtils.isEmpty(todoText.text)) {
            Toast.makeText(requireContext(), R.string.toast_todo_empty, Toast.LENGTH_SHORT).show()
            return
        }

        val todo = ToDoModel()
        todo.todo = todoText.text.toString()
        todo.deadlineTime = textViewToDoTimeSet.text.toString()
        todo.deadlineDate = textViewToDoDateSet.text.toString()
        if (todo.deadlineTime!!.isNotEmpty() && todo.deadlineDate!!.isNotEmpty()) {
            todo.deadlineTimeStamp = DateFormat.convertTimeAndDateAsString(
                todo.deadlineTime!!, todo.deadlineDate!!
            )
        } else if (todo.deadlineTime!!.isNotEmpty() && todo.deadlineDate!!.isEmpty()) {
            todo.deadlineTimeStamp = DateFormat.convertTimeAsStringLocale(todo.deadlineTime!!)
        } else if (todo.deadlineTime!!.isEmpty() && todo.deadlineDate!!.isNotEmpty()) {
            todo.deadlineTimeStamp = DateFormat.convertDateAsStringLocale(todo.deadlineDate!!)
        }
        viewModel.addToDo(todo)
        Toast.makeText(requireContext(), R.string.toast_todo_saved, Toast.LENGTH_SHORT).show()
        Navigation.findNavController(fragmentViewRef)
            .navigate(R.id.action_addToDoFragment_to_toDoListFragment)
    }

    @SuppressLint("SetTextI18n")
    private fun setDateForToDo() {
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            @Suppress("NAME_SHADOWING") val month = month + 1
            viewModel.setToDoDate("$day/$month/$year")
        }
        val buttonShowDatePickerDialog =
            fragmentViewRef.findViewById<Button>(R.id.buttonShowDatePickerDialog)
        buttonShowDatePickerDialog.setOnClickListener {
            val datePicker: DatePickerDialogFragment = DatePickerDialogFragment.getInstance(
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            )
            datePicker.setListener(dateSetListener)
            datePicker.showNow(childFragmentManager, null)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setTimeForToDo() {
        val timeSetListener = OnTimeSetListener { _, hourOfDay, minute ->
            viewModel.setToDoTime(
                StrUtils.fillInZeroInFront(hourOfDay).plus(":").plus(StrUtils.fillInZeroInFront(minute))
            )
        }
        val buttonShowTimePickerDialog = fragmentViewRef.findViewById<Button>(R.id.buttonShowTimePickerDialog)
        buttonShowTimePickerDialog.setOnClickListener {
            val timePicker: TimePickerDialogFragment = TimePickerDialogFragment.getInstance(
                Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                Calendar.getInstance().get(Calendar.MINUTE),
                true
            )
            timePicker.setListener(timeSetListener)
            timePicker.showNow(childFragmentManager, null)
        }
    }

    private fun setupActionBar() {
        val toolbar = view?.findViewById<MaterialToolbar>(R.id.toolbar)
        if (toolbar != null) {
            try {
                val navController = Navigation.findNavController(requireView())
                val appBarConfiguration = AppBarConfiguration(navController.graph)
                toolbar.setupWithNavController(navController, appBarConfiguration)
            } catch (e: Exception) {
            }

            toolbar.title = resources.getString(R.string.fragment_add_todo)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                toolbar.setTitleTextColor(resources.getColor(R.color.colorWhite, null))
            } else {
                toolbar.setTitleTextColor(Color.WHITE)
            }
        }
    }
}