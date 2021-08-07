package com.stairwaytodev.project.ui.todo.todo_list

import android.app.AlertDialog
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.stairwaytodev.project.R
import com.stairwaytodev.project.adapter.ToDoAdapter
import com.stairwaytodev.project.utils.SwipeHelperCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ToDoListFragment : Fragment(R.layout.fragment_todo_list) {

    private lateinit var viewModel: ToDoListViewModel
    private lateinit var toolbar: MaterialToolbar
    private lateinit var adapter: ToDoAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupActionBar()
        setupActionBarOptionsMenu()
        viewModel = ViewModelProvider(this).get(ToDoListViewModel::class.java)
        val fabAddToDo = view.findViewById<FloatingActionButton>(R.id.fabAddToDo)
        fabAddToDo.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_toDoListFragment_to_addToDoFragment)
        }
        adapter = ToDoAdapter()
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerViewToDo)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val swipeHelperCallback: ItemTouchHelper.Callback =
            SwipeHelperCallback(
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT,
                adapter,
                requireContext()
            )
        val itemTouchHelper = ItemTouchHelper(swipeHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
        viewModel.getToDoList().observe(viewLifecycleOwner, {
            adapter.setTodosList(it)
        })
    }

    private fun setupActionBar() {
        toolbar = view?.findViewById(R.id.toolbar)!!
        toolbar.title = resources.getString(R.string.fragment_todo_list_title)

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
                    deleteToDoList()
                    true
                }
                else -> false
            }
        }
    }

    private fun deleteToDoList() {
        val todoList = adapter.getToDoList()
        if (todoList.isNotEmpty()) {
            val builder = AlertDialog.Builder(requireContext(), R.style.AlertDialogTheme)
                .setTitle(R.string.dialog_delete_attention)
                .setMessage(R.string.dialog_delete_todos)
                .setPositiveButton(R.string.delete) { _, _ ->
                    GlobalScope.launch(Dispatchers.IO) {
                        viewModel.deleteToDoList(todoList)
                    }
                    Toast.makeText(
                        requireContext(),
                        R.string.toast_todos_deleted,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .setNegativeButton(R.string.cancel) { dialog, _ ->
                    dialog.dismiss()
                }
            val dialog = builder.create()
            dialog.show()
        }
    }
}