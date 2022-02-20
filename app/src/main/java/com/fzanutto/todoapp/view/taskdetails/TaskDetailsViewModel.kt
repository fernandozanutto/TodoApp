package com.fzanutto.todoapp.view.taskdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fzanutto.todoapp.database.TaskRepository
import com.fzanutto.todoapp.models.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
}