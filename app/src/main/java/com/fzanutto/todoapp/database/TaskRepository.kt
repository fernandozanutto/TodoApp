package com.fzanutto.todoapp.database

import android.content.Context
import com.fzanutto.todoapp.GlobalVariables
import com.fzanutto.todoapp.database.room.TaskRepositoryRoom
import com.fzanutto.todoapp.models.Task

object TaskRepository {

    private lateinit var impl: TaskRepositoryInterface

    fun initialize(context: Context) {
        if (!::impl.isInitialized) {
            impl = if (GlobalVariables.isTestMode) {
                TaskRepositoryMock()
            } else {
                TaskRepositoryRoom(context)
            }
        }
    }

    fun getTaskById(position: Int): Task? {
        return impl.getTaskById(position)
    }

    fun getAllTasks(): List<Task> {
        return impl.getAllTasks()
    }

    fun saveTask(task: Task) {
        return impl.saveTask(task)
    }

    fun deleteTask(task: Task) {
        impl.deleteTask(task)
    }
}