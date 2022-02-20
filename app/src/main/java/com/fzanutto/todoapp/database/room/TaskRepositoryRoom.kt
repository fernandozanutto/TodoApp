package com.fzanutto.todoapp.database.room

import android.content.Context
import androidx.room.Room
import com.fzanutto.todoapp.database.TaskRepositoryInterface
import com.fzanutto.todoapp.models.Task

class TaskRepositoryRoom(context: Context) : TaskRepositoryInterface {

    private val taskDAO: TaskDAO

    init {
        val db = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "app_database"
        ).build()

        taskDAO = db.taskDAO()
    }


    override fun getAllTasks(): List<Task> {
        return taskDAO.getAll().map { it.toModel() }
    }

    override fun getTaskById(id: Int): Task? {
        return taskDAO.getById(id)?.toModel()
    }

    override fun saveTask(task: Task) {
        taskDAO.insertTask(TaskDB(task))
    }
}