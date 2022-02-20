package com.fzanutto.todoapp.database

import com.fzanutto.todoapp.models.RepeatType
import com.fzanutto.todoapp.models.Task
import java.util.Date

object TaskRepositoryMock: TaskRepository {
    override fun getAllTasks(): List<Task> {
        return listOf(
            Task("Beber água", "", RepeatType.MINUTES, Date(1645383091688), 30L),
            Task("Beber água de 45 em 45", "", RepeatType.MINUTES, Date(), 45L),
        )
    }

}