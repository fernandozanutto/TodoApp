package com.fzanutto.todoapp.view.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fzanutto.todoapp.databinding.TaskRowItemBinding
import com.fzanutto.todoapp.models.Task

class TaskAdapter(private val taskList: ArrayList<Task>) : RecyclerView.Adapter<TaskAdapter.ViewHolder>() {

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

        val nextRun = task.getNextRun()
        holder.binding.apply {
            title.text = task.title
            timer.text = task.getNextRun().toString()

            if (nextRun == task.initialDate) {
                type.text = "Tarefa única"
            } else {
                type.text = task.repeat.toString()
            }
        }
    }

    override fun getItemCount() = taskList.size
}