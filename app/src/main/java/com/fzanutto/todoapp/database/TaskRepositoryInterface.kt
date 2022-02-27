package com.fzanutto.todoapp.database

import com.fzanutto.todoapp.models.Task

interface TaskRepositoryInterface {

    suspend fun getAllTasks(): List<Task>
    suspend fun getTaskById(id: Int): Task?
    suspend fun saveTask(task: Task)
    suspend fun deleteTask(task: Task)
    suspend fun deleteTasksById(tasksId: List<Int>)
}