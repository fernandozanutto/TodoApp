package com.fzanutto.todoapp.view.home

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import androidx.appcompat.view.ActionMode
import com.fzanutto.todoapp.R
import com.fzanutto.todoapp.database.TaskRepository
import com.fzanutto.todoapp.databinding.ActivityMainBinding
import com.fzanutto.todoapp.notification.NotificationManager
import com.fzanutto.todoapp.view.taskdetails.TaskDetailsActivity

class HomeActivity : AppCompatActivity(), TaskAdapter.TaskClickListener {

    private lateinit var binding: ActivityMainBinding

    private var _adapter: TaskAdapter? = null
    private val adapter get() = _adapter!!

    private val viewModel: HomeViewModel by viewModels()

    private val checkedTasksId = mutableSetOf<Int>()

    private val supportActionModeCallback: ActionMode.Callback = getActionModeCallback()

    private var actionMode: ActionMode? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        TaskRepository.initialize(this)
        NotificationManager.createNotificationChannel(this)

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
    }

    override fun onResume() {
        super.onResume()
        viewModel.requestTaskList()
    }

    override fun onDestroy() {
        super.onDestroy()
        _adapter = null
    }

    override fun onClickAdapterItem(taskId: Int) {
        startDetailsActivity(taskId)
    }

    override fun onToggleCheckAdapterItem(taskId: Int) {
        if (taskId in checkedTasksId) {
            checkedTasksId.remove(taskId)

            if (checkedTasksId.size == 0) {
                actionMode?.finish()
            }
        } else {
            checkedTasksId.add(taskId)

            if (checkedTasksId.size == 1) {
                actionMode = startSupportActionMode(supportActionModeCallback)
            }
        }

        actionMode?.title = "${checkedTasksId.size} selected"
    }

    private fun getActionModeCallback(): ActionMode.Callback {
        val callback = object : ActionMode.Callback {
            override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                menuInflater.inflate(R.menu.contextual_action_bar, menu)
                return true
            }

            override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                return false
            }

            override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
                return when (item?.itemId) {
                    R.id.delete -> {
                        viewModel.deleteTasks(checkedTasksId.toList())
                        checkedTasksId.clear()
                        mode?.finish()
                        true
                    }
                    else -> {
                        false
                    }
                }
            }

            override fun onDestroyActionMode(mode: ActionMode?) {
                adapter.uncheckEverything()
            }
        }

        return callback
    }

    private fun startDetailsActivity(taskId: Int = -1) {
        val intent = Intent(this, TaskDetailsActivity::class.java).apply {
            putExtra(TaskDetailsActivity.TASK_ID, taskId)
        }

        startActivity(intent)
    }
}