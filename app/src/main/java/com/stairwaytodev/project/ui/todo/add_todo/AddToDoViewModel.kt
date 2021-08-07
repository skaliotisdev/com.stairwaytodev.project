package com.stairwaytodev.project.ui.todo.add_todo

import android.app.Application
import androidx.lifecycle.*
import com.stairwaytodev.project.data.todo.ToDoDatabase
import com.stairwaytodev.project.data.todo.ToDoModel
import com.stairwaytodev.project.data.todo.ToDoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddToDoViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ToDoRepository

    init {
        val dao = ToDoDatabase.getInstance(application.applicationContext).ToDoDao()
        repository = ToDoRepository(dao)
    }

    fun addToDo(todo: ToDoModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addToDo(todo)
        }
    }

    private var todoTime: MutableLiveData<String> = MutableLiveData()
    fun getToDoTime(): LiveData<String> {
        return todoTime
    }

    fun setToDoTime(time: String) {
        todoTime.value = time
    }

    private val todoDate: MutableLiveData<String> = MutableLiveData()
    fun getToDoDate(): LiveData<String> {
        return todoDate
    }

    fun setToDoDate(date: String) {
        todoDate.value = date
    }
}