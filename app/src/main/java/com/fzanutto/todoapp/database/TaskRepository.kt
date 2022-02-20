package com.fzanutto.todoapp.database

import com.fzanutto.todoapp.models.Task

interface TaskRepository {

    fun getAllTasks(): List<Task>
}