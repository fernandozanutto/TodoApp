package com.fzanutto.todoapp.view.home

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import androidx.activity.viewModels
import com.fzanutto.todoapp.database.TaskRepository
import com.fzanutto.todoapp.databinding.ActivityMainBinding
import com.fzanutto.todoapp.notification.NotificationManager
import com.fzanutto.todoapp.view.taskdetails.TaskDetailsActivity

class HomeActivity : AppCompatActivity(), TaskAdapter.ClickListener {

    private lateinit var binding: ActivityMainBinding

    private var _adapter: TaskAdapter? = null
    private val adapter get() = _adapter!!

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        TaskRepository.initialize(this)

        _adapter = TaskAdapter(arrayListOf(), this)

        binding.tasks.adapter = adapter

        viewModel.taskList.observe(this) { tasks ->
            adapter.updateTaskList(tasks)

            tasks.forEach {
                NotificationManager.scheduleTaskNotification(this, it)
            }
        }

        binding.fab.setOnClickListener {
            startDetailsActivity()
        }

        NotificationManager.createNotificationChannel(this)
    }

    override fun onResume() {
        super.onResume()
        viewModel.requestTaskList()
    }

    override fun onDestroy() {
        super.onDestroy()
        _adapter = null
    }

    override fun onClick(taskId: Int) {
        startDetailsActivity(taskId)
    }

    private fun startDetailsActivity(taskId: Int = -1) {
        val intent = Intent(this, TaskDetailsActivity::class.java).apply {
            putExtra(TaskDetailsActivity.TASK_ID, taskId)
        }

        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
    }
}