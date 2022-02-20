package com.fzanutto.todoapp.database

import android.content.Context
import com.fzanutto.todoapp.database.room.TaskRepositoryRoom
import com.fzanutto.todoapp.models.Task

object TaskRepository {

    private lateinit var impl: TaskRepositoryInterface

    fun initialize(useMock: Boolean, context: Context) {
        impl = if (useMock) {
            TaskRepositoryMock()
        } else {
            TaskRepositoryRoom(context)
        }
    }

    fun getTaskById(position: Int): Task? {
        return impl.getTaskById(position)
    }

    fun getAllTasks(): List<Task> {
        return impl.getAllTasks()
    }
}