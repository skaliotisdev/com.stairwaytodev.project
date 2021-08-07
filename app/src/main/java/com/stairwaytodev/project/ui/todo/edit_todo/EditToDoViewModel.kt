package com.stairwaytodev.project.ui.todo.edit_todo

import android.app.Application
import androidx.lifecycle.*
import com.stairwaytodev.project.data.todo.ToDoDatabase
import com.stairwaytodev.project.data.todo.ToDoModel
import com.stairwaytodev.project.data.todo.ToDoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditToDoViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ToDoRepository

    init {
        val dao = ToDoDatabase.getInstance(application.applicationContext).ToDoDao()
        repository = ToDoRepository(dao)
    }

    fun saveToDo(todo: ToDoModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.editToDo(todo)
        }
    }

    fun deleteToDo(todo: ToDoModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteToDo(todo)
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