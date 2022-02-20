package com.fzanutto.todoapp.database

import com.fzanutto.todoapp.models.RepeatType
import com.fzanutto.todoapp.models.Task
import java.util.Date

object TaskRepositoryMock: TaskRepository {
    override fun getAllTasks(): List<Task> {
        return listOf(
            Task(1, "Beber água", "", RepeatType.MINUTES, Date(1645383091688), 30L),
            Task(2, "Beber água de 45 em 45", "", RepeatType.MINUTES, Date(), 45L),
            Task(3, "Eliminação BBB", "", RepeatType.WEEKLY, Date(1645555891688), 1L),
            Task(4, "Lavar Roupa", "", RepeatType.DO_NOT_REPEAT, Date(1645393891688), 0L),
        )
    }

}