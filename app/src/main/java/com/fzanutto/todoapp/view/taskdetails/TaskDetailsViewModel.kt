package com.fzanutto.todoapp.view.taskdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fzanutto.todoapp.database.TaskRepositoryManager
import com.fzanutto.todoapp.models.RepeatType
import com.fzanutto.todoapp.models.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date

class TaskDetailsViewModel: ViewModel() {

    private val _task = MutableLiveData<Task>()
    val task: LiveData<Task> = _task

    fun requestTask(id: Int) {
        if (id == -1) {
            _task.postValue(Task(0, "", "", RepeatType.DO_NOT_REPEAT, Date(), 0))
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            TaskRepositoryManager.getTaskById(id)?.let { task ->
                _task.postValue(task)
            }
        }
    }

    fun saveTask(title: String, description: String, repeatType: RepeatType?, interval: Int, date: Date) {
        val task = _task.value?.let {
            it.title = title
            it.description = description
            it.repeat = repeatType ?: RepeatType.DO_NOT_REPEAT
            it.interval = interval
            it.initialDate = date
            it
        } ?: Task(0, title, description, repeatType ?: RepeatType.DO_NOT_REPEAT, date, interval = interval)

        viewModelScope.launch(Dispatchers.IO) {
            TaskRepositoryManager.saveTask(task)
        }
    }

    fun deleteTask() {
        _task.value?.let {
            viewModelScope.launch(Dispatchers.IO) {
                TaskRepositoryManager.deleteTask(it)
            }
        }
    }
}