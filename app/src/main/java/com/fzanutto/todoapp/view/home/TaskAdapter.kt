package com.fzanutto.todoapp.view.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fzanutto.todoapp.R
import com.fzanutto.todoapp.databinding.TaskRowItemBinding
import com.fzanutto.todoapp.models.RepeatType
import com.fzanutto.todoapp.models.Task

class TaskAdapter(private val taskList: ArrayList<Task>, private val listener: TaskClickListener) : RecyclerView.Adapter<TaskAdapter.ViewHolder>() {

    private val selectedItems = mutableSetOf<Int>()

    class ViewHolder(val binding: TaskRowItemBinding) : RecyclerView.ViewHolder(binding.root)

    @SuppressLint("NotifyDataSetChanged")
    fun updateTaskList(newList: List<Task>) {
        taskList.clear()
        taskList.addAll(newList)
        notifyDataSetChanged()
    }

    private fun toggleItemSelected(taskId: Int) {
        if (selectedItems.contains(taskId)) {
            selectedItems.remove(taskId)
        } else {
            selectedItems.add(taskId)
        }

        listener.onToggleCheckAdapterItem(taskId)
        updateAll()
    }

    fun uncheckEverything() {
        selectedItems.clear()
        updateAll()
    }

    private fun updateAll() {
        notifyItemRangeChanged(0, itemCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = TaskRowItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = taskList[position]

        val context = holder.itemView.context

        holder.binding.apply {
            card.isChecked = selectedItems.contains(task.id)

            title.text = task.title

            if (task.getNextRun() != null) {
                nextReminder.visibility = View.VISIBLE
                timer.text = task.getNextRunEstimatedString(context)
                card.alpha = 1f
            } else {
                nextReminder.visibility = View.GONE
                timer.text = context.getString(R.string.task_done)
                card.alpha = 0.5f
            }

            if (task.repeat == RepeatType.DO_NOT_REPEAT) {
                type.text = context.getString(R.string.one_time_task)
            } else {
                type.text = context.getString(R.string.repeat_at, task.interval.toString(), task.repeat.getShortName())
            }
        }

        holder.itemView.setOnClickListener {
            if (selectedItems.size == 0) {
                listener.onClickAdapterItem(task.id)
            } else {
                toggleItemSelected(task.id)
            }
        }

        holder.itemView.setOnLongClickListener {
            toggleItemSelected(task.id)
            true
        }
    }

    override fun getItemCount() = taskList.size

    interface TaskClickListener {
        fun onClickAdapterItem(taskId: Int)
        fun onToggleCheckAdapterItem(taskId: Int)
    }
}