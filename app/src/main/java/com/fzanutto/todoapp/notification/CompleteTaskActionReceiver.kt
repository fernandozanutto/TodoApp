package com.fzanutto.todoapp.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import com.fzanutto.todoapp.database.TaskRepository
import com.fzanutto.todoapp.notification.NotificationManager.taskIdTag

class CompleteTaskActionReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val taskId = intent.getIntExtra(taskIdTag, -1)
        Log.d("notification", "notificaton to cancel ${taskId.taskIdToNotificationId()}")
        if (taskId == -1) return

        TaskRepository.initialize(context)

        val task = TaskRepository.getTaskById(taskId) ?: return

        with(NotificationManagerCompat.from(context)) {
            cancel(taskId.taskIdToNotificationId())
        }
    }
}