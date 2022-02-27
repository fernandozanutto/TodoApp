package com.fzanutto.todoapp.view.taskdetails

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import com.fzanutto.todoapp.R
import com.fzanutto.todoapp.databinding.ActivityTaskDetailsBinding
import com.fzanutto.todoapp.models.RepeatType
import com.fzanutto.todoapp.models.Task

class TaskDetailsActivity : AppCompatActivity() {

    companion object {
        const val TASK_ID = "TASK_ID"
    }

    private lateinit var binding: ActivityTaskDetailsBinding
    private var taskID = -1

    private val viewModel: TaskDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        taskID = intent.getIntExtra(TASK_ID, -1)

        if (taskID == -1) {
            supportActionBar?.title = getString(R.string.create_task)
        }

        binding.buttonSave.setOnClickListener {
            viewModel.saveTask(
                binding.title.text.toString(),
                binding.description.text.toString(),
                RepeatType.fromInt(binding.repeatType.selectedItemPosition + 1),
                binding.interval.text.toString().toInt()
            )
            finish()
        }

        binding.buttonCancel.setOnClickListener {
            finish()
        }

        binding.buttonDelete.setOnClickListener {
            viewModel.deleteTask()
            finish()
        }

        viewModel.task.observe(this) {
            updateUI(it)
        }

        val spinnerValues = RepeatType.values().map { it.getName(this) }
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerValues)
        binding.repeatType.adapter = spinnerAdapter
    }

    override fun onResume() {
        super.onResume()
        viewModel.requestTask(taskID)
    }

    private fun updateUI(task: Task) {
        binding.apply {
            title.setText(task.title)
            description.setText(task.description)
            repeatType.setSelection(task.repeat.type - 1)
            interval.setText(task.interval.toString())
            date.text = task.initialDate.toString()
        }
    }
}