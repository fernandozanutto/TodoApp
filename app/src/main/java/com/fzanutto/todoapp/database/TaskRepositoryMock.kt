package com.fzanutto.todoapp.database

import com.fzanutto.todoapp.models.RepeatType
import com.fzanutto.todoapp.models.Task
import java.util.Date

class TaskRepositoryMock: TaskRepository {

    private val taskList = arrayListOf(
        Task(1, "Beber água", "bebe aguaaaaaaa", RepeatType.SECONDS, Date(1645383091688), 20),
        //Task(2, "Beber água de 45 em 45", "", RepeatType.MINUTES, Date(), 45),
        Task(3, "Eliminação BBB", "", RepeatType.WEEKLY, Date(1644951091688), 1),
        Task(4, "Lavar Roupa", "", RepeatType.DO_NOT_REPEAT, Date(1645393891688), 0),
    )

    override suspend fun getAllTasks(): List<Task> {
        return taskList
    }

    override suspend fun getTaskById(id: Int): Task? {
        return taskList.firstOrNull {
            it.id == id
        }
    }

    override suspend fun saveTask(task: Task) {
        val indexToUpdate = taskList.indexOfFirst { it.id == task.id }

        if (indexToUpdate == -1) {
            taskList.add(task)
        } else {
            taskList[indexToUpdate] = task
        }
    }

    override suspend fun deleteTask(task: Task) {
        taskList.removeAll {
            it.id == task.id
        }
    }

    override suspend fun deleteTasksById(tasksId: List<Int>) {
        taskList.removeAll {
            it.id in tasksId
        }
    }

}