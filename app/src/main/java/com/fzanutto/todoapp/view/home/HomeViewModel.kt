package com.fzanutto.todoapp.view.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fzanutto.todoapp.database.TaskRepositoryManager
import com.fzanutto.todoapp.models.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _taskList = MutableLiveData<List<Task>>()
    val taskList: LiveData<List<Task>> = _taskList

    fun requestTaskList() {
        viewModelScope.launch(Dispatchers.IO) {
            val list = TaskRepositoryManager.getAllTasks()
            _taskList.postValue(list.sortedByDescending { it.getNextRun() != null })
        }
    }

    fun deleteTasks(tasksId: List<Int>) {
        viewModelScope.launch(Dispatchers.IO) {
            TaskRepositoryManager.deleteTasksById(tasksId)
            _taskList.postValue(_taskList.value?.filter { it.id !in tasksId })
        }
    }
}