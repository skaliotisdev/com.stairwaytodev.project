package com.stairwaytodev.project.utils

import android.content.Context
import com.stairwaytodev.project.data.todo.ToDoDatabase
import com.stairwaytodev.project.data.todo.ToDoModel
import com.stairwaytodev.project.data.todo.ToDoRepository

object RoomUtils {

    fun getUpdatedToDo(id: Long, todoText: String, timeSet: String, dateSet: String): ToDoModel {
        val todo = ToDoModel()
        todo.id = id
        todo.todo = todoText
        todo.deadlineTime = timeSet
        todo.deadlineDate = dateSet
        return todo
    }

    fun getRepository(context: Context): ToDoRepository {
        val database = ToDoDatabase.getInstance(context)
        val dao = database.ToDoDao()
        return ToDoRepository(dao)
    }
}