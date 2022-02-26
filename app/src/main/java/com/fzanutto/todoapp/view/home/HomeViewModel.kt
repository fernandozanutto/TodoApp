package com.fzanutto.todoapp.view.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fzanutto.todoapp.database.TaskRepository
import com.fzanutto.todoapp.models.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _taskList = MutableLiveData<List<Task>>()
    val taskList: LiveData<List<Task>> = _taskList


    fun requestTaskList() {
        viewModelScope.launch(Dispatchers.IO) {
            val list = TaskRepository.getAllTasks()
            _taskList.postValue(list)
        }
    }
}