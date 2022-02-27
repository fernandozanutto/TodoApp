package com.fzanutto.todoapp.view.taskdetails

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import com.fzanutto.todoapp.R
import com.fzanutto.todoapp.databinding.ActivityTaskDetailsBinding
import com.fzanutto.todoapp.models.RepeatType
import com.fzanutto.todoapp.models.Task
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class TaskDetailsActivity : AppCompatActivity() {

    companion object {
        const val TASK_ID = "TASK_ID"
    }

    private lateinit var binding: ActivityTaskDetailsBinding
    private var taskID = -1

    private val viewModel: TaskDetailsViewModel by viewModels()
    private var datePickerDate = Date()
    private lateinit var task: Task

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
                binding.titleLabel.editText?.text.toString(),
                binding.descriptionLabel.editText?.text.toString(),
                task.repeat,
                binding.interval.text.toString().toInt(),
                datePickerDate
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

        val repeatValues = RepeatType.values().map { it.getName(this) }
        val repeatTypeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, repeatValues)

        binding.repeatType.setAdapter(repeatTypeAdapter)

        binding.repeatType.setOnItemClickListener { _, _, i, _ ->
            val repeatType = RepeatType.fromInt(i + 1) ?: RepeatType.DO_NOT_REPEAT
            task.repeat = repeatType
            if (repeatType == RepeatType.DO_NOT_REPEAT) {
                binding.interval.visibility = View.GONE
                binding.intervalLabel.visibility = View.GONE
            } else {
                binding.interval.visibility = View.VISIBLE
                binding.intervalLabel.visibility = View.VISIBLE
            }
        }

        viewModel.task.observe(this) {
            task = it
            updateUI(it)
        }

        viewModel.requestTask(taskID)
    }

    private fun updateUI(task: Task) {
        binding.apply {
            title.setText(task.title)
            description.setText(task.description)
            repeatType.setText(repeatType.adapter.getItem(task.repeat.type - 1).toString(), false);
            interval.setText(task.interval.toString())
        }

        setupDatePickers(task.initialDate)
    }

    private fun setupDatePickers(initialDate: Date) {
        datePickerDate = initialDate
        val fromDateCalendar: Calendar = Calendar.getInstance()
        fromDateCalendar.time = initialDate

        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        binding.date.text = dateFormat.format(initialDate)
        binding.time.text = timeFormat.format(initialDate)

        val datePickerListenerFrom = DatePickerDialog.OnDateSetListener { picker, _, _, _ ->
            fromDateCalendar.set(Calendar.MONTH, picker.month)
            fromDateCalendar.set(Calendar.DAY_OF_MONTH, picker.dayOfMonth)
            fromDateCalendar.set(Calendar.YEAR, picker.year)
            datePickerDate.time = fromDateCalendar.timeInMillis
            binding.date.text = dateFormat.format(datePickerDate)
        }

        val timePickerListener = TimePickerDialog.OnTimeSetListener { timePicker, _, _ ->
            fromDateCalendar.set(Calendar.HOUR_OF_DAY, timePicker.hour)
            fromDateCalendar.set(Calendar.MINUTE, timePicker.minute)
            datePickerDate.time = fromDateCalendar.timeInMillis
            binding.time.text = timeFormat.format(datePickerDate)
        }

        binding.date.setOnClickListener {
            val auxCal: Calendar = Calendar.getInstance()
            auxCal.time = datePickerDate

            val day: Int = auxCal.get(Calendar.DAY_OF_MONTH)
            val month: Int = auxCal.get(Calendar.MONTH)
            val year: Int = auxCal.get(Calendar.YEAR)
            val datePickerFrom = DatePickerDialog(
                this,
                datePickerListenerFrom,
                year,
                month,
                day
            )

            datePickerFrom.show()
        }

        binding.time.setOnClickListener {
            val auxCal: Calendar = Calendar.getInstance()
            auxCal.time = datePickerDate

            val hour: Int = auxCal.get(Calendar.HOUR_OF_DAY)
            val minutes: Int = auxCal.get(Calendar.MINUTE)

            val timePickerFrom = TimePickerDialog(
                this,
                timePickerListener,
                hour,
                minutes,
                true
            )

            timePickerFrom.show()
        }
    }
}