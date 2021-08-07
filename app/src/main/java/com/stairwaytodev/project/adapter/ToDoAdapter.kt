package com.stairwaytodev.project.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.stairwaytodev.project.R
import com.stairwaytodev.project.data.todo.ToDoDatabase.Companion.getInstance
import com.stairwaytodev.project.data.todo.ToDoModel
import com.stairwaytodev.project.data.todo.ToDoRepository
import com.stairwaytodev.project.ui.todo.todo_list.ToDoListFragmentDirections
import com.stairwaytodev.project.utils.StrUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class ToDoAdapter : RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder>() {

    private var todosList: List<ToDoModel>
    private var context: Context? = null

    init {
        todosList = ArrayList()
    }

    fun getToDoList(): List<ToDoModel> {
        return todosList
    }

    fun setTodosList(todosList: List<ToDoModel>) {
        this.todosList = todosList
        notifyDataSetChanged()
    }

    fun getToDoAtPosition(position: Int): ToDoModel {
        return todosList[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        context = parent.context
        val view = LayoutInflater.from(parent.context).inflate(R.layout.todo_view, parent, false)
        return ToDoViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        holder.textViewToDo.text = todosList[position].todo
        holder.textViewDeadline.text = todosList[position].deadlineTimeStamp
        holder.checkBoxDone.isChecked = todosList[position].isDone
        if (holder.textViewDeadline.text.isNotEmpty()){
            holder.textViewDeadline.visibility = View.VISIBLE
            holder.textViewDeadlineLabel.visibility = View.VISIBLE
        }else{
            holder.textViewDeadline.visibility = View.GONE
            holder.textViewDeadlineLabel.visibility = View.GONE
        }
        var isDone = holder.checkBoxDone.isChecked
        strikeThroughToDoTextIfDone(holder, isDone)
        val database = getInstance(context!!)
        val dao = database.ToDoDao()
        val repository = ToDoRepository(dao)
        holder.checkBoxDone.setOnClickListener {
            try {
                GlobalScope.launch(Dispatchers.IO) {
                    isDone = todosList[position].isDone
                    repository.editToDo(todosList[position].id, !isDone)
                }
                strikeThroughToDoTextIfDone(holder, !isDone)

            } catch (e: IndexOutOfBoundsException) {
                e.printStackTrace()
            }
        }
        holder.itemView.setOnClickListener {
            val navAction = ToDoListFragmentDirections.actionToDoListFragmentToEditToDoFragment(
                todosList[position]
            )
            Navigation.findNavController(it).navigate(navAction)
        }
    }

    override fun getItemCount(): Int {
        return todosList.size
    }

    private fun strikeThroughToDoTextIfDone(holder: ToDoViewHolder, isDone: Boolean) {
        StrUtils.strikeText(holder.textViewToDo, isDone)
        StrUtils.strikeText(holder.textViewDeadline, isDone)
        StrUtils.strikeText(holder.textViewDeadlineLabel, isDone)
    }

    class ToDoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textViewToDo: TextView = itemView.findViewById(R.id.textViewTodo)
        var textViewDeadline: TextView = itemView.findViewById(R.id.textViewDeadline)
        var textViewDeadlineLabel: TextView = itemView.findViewById(R.id.textViewDeadlineLabel)
        var checkBoxDone: CheckBox = itemView.findViewById(R.id.checkBoxDone)
    }
}