package com.stairwaytodev.project.data.todo

import androidx.lifecycle.LiveData

class ToDoRepository(private val dao: ToDoDao) {

    suspend fun addToDo(toDoModel: ToDoModel) {
        dao.addToDo(toDoModel)
    }

    val getToDoList: LiveData<List<ToDoModel>> = dao.getToDoList()

    suspend fun editToDo(id: Long, isDone: Boolean) {
        dao.updateToDo(id, isDone)
    }

    suspend fun editToDo(toDoModel: ToDoModel) {
        dao.updateToDo(toDoModel)
    }

    suspend fun deleteToDo(toDoModel: ToDoModel) {
        dao.deleteToDo(toDoModel)
    }

    suspend fun deleteToDoList(toDoModelList: List<ToDoModel>) {
        dao.deleteToDoList(toDoModelList)
    }
}