package com.stairwaytodev.project.ui.todo.edit_todo

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
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
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.stairwaytodev.project.R
import com.stairwaytodev.project.ui.todo.pickers.DatePickerDialogFragment
import com.stairwaytodev.project.ui.todo.pickers.TimePickerDialogFragment
import com.stairwaytodev.project.utils.RoomUtils
import com.stairwaytodev.project.utils.DateFormat
import com.stairwaytodev.project.utils.StrUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class EditToDoFragment : Fragment() {

    private lateinit var viewModel: EditToDoViewModel
    private val fragmentArgs by navArgs<EditToDoFragmentArgs>()
    private lateinit var todoText: EditText
    private lateinit var timeSet: TextView
    private lateinit var dateSet: TextView
    private lateinit var toolbar: MaterialToolbar
    private lateinit var fragmentViewRef: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_todo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentViewRef = view
        setupActionBar()
        setupActionBarOptionsMenu()
        viewModel = ViewModelProvider(this).get(EditToDoViewModel::class.java)

        todoText = view.findViewById(R.id.editTextToDoUpdating)
        timeSet = view.findViewById(R.id.textViewToDoTimeSetUpdating)
        dateSet = view.findViewById(R.id.textViewToDoDateSetUpdating)

        todoText.setText(fragmentArgs.existingToDo.todo)
        timeSet.text = fragmentArgs.existingToDo.deadlineTime
        dateSet.text = fragmentArgs.existingToDo.deadlineDate

        setTimeForToDo()
        setDateForToDo()

        viewModel.getToDoTime().observe(viewLifecycleOwner, {
            timeSet.text = it
        })

        viewModel.getToDoDate().observe(viewLifecycleOwner, {
            dateSet.text = it
        })

        val buttonSave = view.findViewById<FloatingActionButton>(R.id.fabUpdateToDo)
        buttonSave.setOnClickListener {
            saveToDo()
        }
    }

    private fun saveToDo() {
        if (TextUtils.isEmpty(todoText.text)) {
            Toast.makeText(requireContext(), R.string.toast_todo_empty, Toast.LENGTH_SHORT).show()
            return
        }
        val todo = RoomUtils.getUpdatedToDo(
            fragmentArgs.existingToDo.id,
            todoText.text.toString(),
            timeSet.text.toString(),
            dateSet.text.toString()
        )

        if (todo.deadlineTime!!.isNotEmpty() && todo.deadlineDate!!.isNotEmpty()) {
            todo.deadlineTimeStamp = DateFormat.convertTimeAndDateAsString(
                todo.deadlineTime!!, todo.deadlineDate!!
            )
        } else if (todo.deadlineTime!!.isNotEmpty() && todo.deadlineDate!!.isEmpty()) {
            todo.deadlineTimeStamp = DateFormat.convertTimeAsStringLocale(todo.deadlineTime!!)
        } else if (todo.deadlineTime!!.isEmpty() && todo.deadlineDate!!.isNotEmpty()){
            todo.deadlineTimeStamp = DateFormat.convertDateAsStringLocale(todo.deadlineDate!!)
        }
        viewModel.saveToDo(todo)
        Toast.makeText(requireContext(), R.string.toast_todo_saved, Toast.LENGTH_SHORT).show()
        Navigation.findNavController(fragmentViewRef)
            .navigate(R.id.action_editToDoFragment_to_toDoListFragment)
    }

    @SuppressLint("SetTextI18n")
    private fun setDateForToDo() {
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            @Suppress("NAME_SHADOWING") val month = month + 1
            viewModel.setToDoDate("$day/$month/$year")
        }
        val buttonShowDatePickerDialog = fragmentViewRef.findViewById<Button>(R.id.buttonShowDatePickerDialogUpdating)
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
        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            viewModel.setToDoTime(
                StrUtils.fillInZeroInFront(hourOfDay).plus(":").plus(StrUtils.fillInZeroInFront(minute))
            )
        }

        val buttonShowTimePickerDialog = fragmentViewRef.findViewById<Button>(R.id.buttonShowTimePickerDialogUpdating)
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
        toolbar = view?.findViewById(R.id.toolbar)!!
        try {
            val navController = Navigation.findNavController(requireView())
            val appBarConfiguration = AppBarConfiguration(navController.graph)
            toolbar.setupWithNavController(navController, appBarConfiguration)
        } catch (e: Exception) {
        }

        toolbar.title = resources.getString(R.string.fragment_edit_todo)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            toolbar.setTitleTextColor(resources.getColor(R.color.colorWhite, null))
        } else {
            toolbar.setTitleTextColor(Color.WHITE)
        }
    }

    private fun setupActionBarOptionsMenu() {
        toolbar.inflateMenu(R.menu.toolbar_menu)
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_menu_delete -> {
                    deleteToDo()
                    true
                }
                else -> false
            }
        }
    }

    private fun deleteToDo() {
        val builder = AlertDialog.Builder(requireContext(), R.style.AlertDialogTheme)
            .setTitle(fragmentArgs.existingToDo.todo)
            .setMessage(R.string.dialog_delete_todo)
            .setPositiveButton(R.string.delete) { _, _ ->
                GlobalScope.launch(Dispatchers.IO) {
                    viewModel.deleteToDo(
                        RoomUtils.getUpdatedToDo(
                            fragmentArgs.existingToDo.id,
                            todoText.text.toString(),
                            timeSet.text.toString(),
                            dateSet.text.toString()
                        )
                    )
                }
                Toast.makeText(requireContext(), R.string.toast_todo_deleted, Toast.LENGTH_SHORT).show()
                Navigation.findNavController(fragmentViewRef).navigate(
                    R.id.action_editToDoFragment_to_toDoListFragment
                )
            }
            .setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
        val dialog = builder.create()
        dialog.show()
    }
}