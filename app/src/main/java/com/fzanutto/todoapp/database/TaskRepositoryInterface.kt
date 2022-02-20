package com.fzanutto.todoapp.database

import com.fzanutto.todoapp.models.Task

interface TaskRepositoryInterface {

    fun getAllTasks(): List<Task>
    fun getTaskById(id: Int): Task?
}