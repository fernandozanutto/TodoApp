package com.fzanutto.todoapp.database

import android.content.Context
import com.fzanutto.todoapp.GlobalVariables
import com.fzanutto.todoapp.database.room.TaskRepositoryRoom
import com.fzanutto.todoapp.models.Task

object TaskRepositoryManager {

    private lateinit var impl: TaskRepository

    fun initialize(context: Context) {
        if (!::impl.isInitialized) {
            impl = if (GlobalVariables.isTestMode) {
                TaskRepositoryMock()
            } else {
                TaskRepositoryRoom(context)
            }
        }
    }

    suspend fun getTaskById(position: Int): Task? {
        return impl.getTaskById(position)
    }

    suspend fun getAllTasks(): List<Task> {
        return impl.getAllTasks()
    }

    suspend fun saveTask(task: Task) {
        return impl.saveTask(task)
    }

    suspend fun deleteTask(task: Task) {
        impl.deleteTask(task)
    }

    suspend fun deleteTasksById(tasksId: List<Int>) {
        impl.deleteTasksById(tasksId)
    }
}