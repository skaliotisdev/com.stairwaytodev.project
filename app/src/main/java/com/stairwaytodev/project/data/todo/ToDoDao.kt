package com.stairwaytodev.project.data.todo

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ToDoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToDo(todo: ToDoModel)

    @Query("SELECT * FROM TABLE_TODO")
    fun getToDoList(): LiveData<List<ToDoModel>>

    @Query("UPDATE TABLE_TODO SET TABLE_TODO_DONE = :isDone WHERE id = :id")
    suspend fun updateToDo(id: Long, isDone: Boolean)

    @Update
    suspend fun updateToDo(todo: ToDoModel)

    @Delete
    suspend fun deleteToDo(todo: ToDoModel)

    @Delete
    suspend fun deleteToDoList(todos: List<ToDoModel>)
}