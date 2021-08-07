package com.stairwaytodev.project.ui.todo.todo_list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.stairwaytodev.project.data.todo.ToDoDatabase
import com.stairwaytodev.project.data.todo.ToDoModel
import com.stairwaytodev.project.data.todo.ToDoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ToDoListViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ToDoRepository

    init {
        val dao = ToDoDatabase.getInstance(application).ToDoDao()
        repository = ToDoRepository(dao)
    }

    fun getToDoList() : LiveData<List<ToDoModel>> {
        return repository.getToDoList
    }

    fun deleteToDoList(toDoList: List<ToDoModel>) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteToDoList(toDoList)
        }
    }
}