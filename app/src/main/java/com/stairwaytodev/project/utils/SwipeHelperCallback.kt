package com.stairwaytodev.project.utils

import android.app.AlertDialog
import android.content.Context
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.stairwaytodev.project.R
import com.stairwaytodev.project.data.todo.ToDoModel
import com.stairwaytodev.project.adapter.ToDoAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SwipeHelperCallback(dragDirs: Int, swipeDirs: Int, private var adapter: ToDoAdapter, var context: Context

) : ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val todo: ToDoModel = adapter.getToDoAtPosition(viewHolder.bindingAdapterPosition)
        val builder = AlertDialog.Builder(context, R.style.AlertDialogTheme)
            .setTitle(todo.todo)
            .setMessage(R.string.dialog_delete_todo)
            .setPositiveButton(R.string.delete) { _, _ ->
                GlobalScope.launch(Dispatchers.IO) {
                    RoomUtils.getRepository(context).deleteToDo(todo)
                }
                Toast.makeText(context, R.string.toast_todo_deleted, Toast.LENGTH_SHORT).show()
                adapter.notifyItemChanged(viewHolder.bindingAdapterPosition)
            }
            .setNegativeButton(R.string.cancel) { dialog, _ ->
                adapter.notifyItemChanged(viewHolder.bindingAdapterPosition)
                dialog.dismiss()
            }
        val dialog = builder.create()
        dialog.show()
    }
}