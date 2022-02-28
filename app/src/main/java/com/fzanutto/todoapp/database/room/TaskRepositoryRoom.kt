package com.fzanutto.todoapp.database.room

import android.content.Context
import androidx.room.Room
import com.fzanutto.todoapp.database.TaskRepository
import com.fzanutto.todoapp.models.Task

class TaskRepositoryRoom(context: Context) : TaskRepository {

    private val taskDAO: TaskDAO

    init {
        val db = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "app_database"
        ).build()

        taskDAO = db.taskDAO()
    }


    override suspend fun getAllTasks(): List<Task> {
        return taskDAO.getAll().map { it.toModel() }
    }

    override suspend fun getTaskById(id: Int): Task? {
        return taskDAO.getById(id)?.toModel()
    }

    override suspend fun saveTask(task: Task) {
        taskDAO.insertTask(TaskDB(task))
    }

    override suspend fun deleteTask(task: Task) {
        taskDAO.deleteTask(TaskDB(task))
    }

    override suspend fun deleteTasksById(tasksId: List<Int>) {
        taskDAO.deleteTasksById(tasksId)
    }
}