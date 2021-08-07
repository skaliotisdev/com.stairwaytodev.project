package com.stairwaytodev.project.data.todo

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.stairwaytodev.project.data.DB_NAME
import com.stairwaytodev.project.data.DB_VERSION

@Database(entities = [ToDoModel::class], version = DB_VERSION, exportSchema = false)

abstract class ToDoDatabase : RoomDatabase() {

    abstract fun ToDoDao(): ToDoDao

    companion object {
        @Volatile
        private var INSTANCE: ToDoDatabase? = null
        fun getInstance(context: Context): ToDoDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(context.applicationContext,
                        ToDoDatabase::class.java, DB_NAME
                    ).build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}