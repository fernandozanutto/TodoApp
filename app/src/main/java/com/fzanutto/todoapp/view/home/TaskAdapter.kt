package com.fzanutto.todoapp.view.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fzanutto.todoapp.R
import com.fzanutto.todoapp.databinding.TaskRowItemBinding
import com.fzanutto.todoapp.models.RepeatType
import com.fzanutto.todoapp.models.Task

class TaskAdapter(private val taskList: ArrayList<Task>, private val listener: ClickListener) : RecyclerView.Adapter<TaskAdapter.ViewHolder>() {

    class ViewHolder(val binding: TaskRowItemBinding) : RecyclerView.ViewHolder(binding.root)

    @SuppressLint("NotifyDataSetChanged")
    fun updateTaskList(newList: List<Task>) {
        taskList.clear()
        taskList.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = TaskRowItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = taskList[position]

        val context = holder.itemView.context

        holder.binding.apply {
            title.text = task.title
            timer.text = task.getNextRunEstimatedString()

            if (task.repeat == RepeatType.DO_NOT_REPEAT) {
                type.text = context.getString(R.string.one_time_task)
            } else {
                type.text = context.getString(R.string.repeat_at, task.interval.toString(), task.repeat.getShortName())
            }
        }

        holder.itemView.setOnClickListener {
            listener.onClick(task.id)
        }
    }

    override fun getItemCount() = taskList.size

    interface ClickListener {
        fun onClick(taskId: Int)
    }
}