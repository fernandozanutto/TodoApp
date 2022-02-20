package com.fzanutto.todoapp.view.taskdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fzanutto.todoapp.database.TaskRepository
import com.fzanutto.todoapp.models.RepeatType
import com.fzanutto.todoapp.models.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date

class TaskDetailsViewModel: ViewModel() {

    private val _task = MutableLiveData<Task>()
    val task: LiveData<Task> = _task


    fun requestTask(id: Int) {
        viewModelScope.launch(Dispatchers.IO){
            TaskRepository.getTaskById(id)?.let { task ->
                _task.postValue(task)
            }
        }
    }

    fun saveTask(title: String, description: String, repeatType: RepeatType?, interval: Int) {
        val task = _task.value?.let {
            it.title = title
            it.description = description
            it.repeat = repeatType ?: RepeatType.DO_NOT_REPEAT
            it.interval = interval
            it
        } ?: run {
            Task(0, title, description, repeatType ?: RepeatType.DO_NOT_REPEAT, Date(), interval = interval)
        }

        TaskRepository.saveTask(task)
    }
}