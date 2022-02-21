package com.fzanutto.todoapp.database

import com.fzanutto.todoapp.models.RepeatType
import com.fzanutto.todoapp.models.Task
import java.util.Date

class TaskRepositoryMock: TaskRepositoryInterface {

    val taskList = arrayListOf(
        //Task(1, "Beber água", "", RepeatType.MINUTES, Date(1645383091688), 30),
        //Task(2, "Beber água de 45 em 45", "", RepeatType.MINUTES, Date(), 45),
        Task(3, "Eliminação BBB", "", RepeatType.WEEKLY, Date(1644951091688), 1),
        Task(4, "Lavar Roupa", "", RepeatType.DO_NOT_REPEAT, Date(1645393891688), 0),
    )

    override fun getAllTasks(): List<Task> {
        return taskList
    }

    override fun getTaskById(id: Int): Task? {
        return taskList.firstOrNull {
            it.id == id
        }
    }

    override fun saveTask(task: Task) {
        val indexToUpdate = taskList.indexOfFirst { it.id == task.id }

        if (indexToUpdate == -1) {
            taskList.add(task)
        } else {
            taskList[indexToUpdate] = task
        }
    }

    override fun deleteTask(task: Task) {
        taskList.removeAll {
            it.id == task.id
        }
    }

}