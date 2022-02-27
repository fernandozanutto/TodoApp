package com.fzanutto.todoapp.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import com.fzanutto.todoapp.database.TaskRepository
import com.fzanutto.todoapp.notification.NotificationManager.taskIdTag
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CompleteTaskActionReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val taskId = intent.getIntExtra(taskIdTag, -1)
        Log.d("notification", "notificaton to cancel ${taskId.taskIdToNotificationId()}")
        if (taskId == -1) return

        TaskRepository.initialize(context)

        with(NotificationManagerCompat.from(context)) {
            cancel(taskId.taskIdToNotificationId())
        }

        CoroutineScope(Dispatchers.Default).launch {
            val task = TaskRepository.getTaskById(taskId) ?: return@launch

            // TODO registrar que a task foi concluida
        }
    }
}