package com.fzanutto.todoapp.database.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TaskDAO {

    @Query("SELECT * FROM task")
    fun getAll(): List<TaskDB>

    @Query("SELECT * FROM task WHERE id = :id")
    fun getById(id: Int): TaskDB?

    @Insert
    fun insertTask(task: TaskDB)

    @Delete
    fun deleteTask(task: TaskDB)
}