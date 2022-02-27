package com.fzanutto.todoapp.database.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fzanutto.todoapp.models.RepeatType
import com.fzanutto.todoapp.models.Task
import java.util.Date

@Entity(tableName = "task")
class TaskDB(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val title: String,
    val description: String,
    val repeat: RepeatType,
    val initialDate: Date,
    val interval: Int,
    val done: Boolean,
    val active: Boolean
) {

    constructor(task: Task) : this(
        task.id,
        task.title,
        task.description,
        task.repeat,
        task.initialDate,
        task.interval,
        task.done,
        task.active
    )

    fun toModel(): Task {
        val modelTask = Task(
            id, title, description, repeat, initialDate, interval
        )

        modelTask.done = done
        modelTask.active = active

        return modelTask
    }
}