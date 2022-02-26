package com.fzanutto.todoapp.notification

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.fzanutto.todoapp.MainActivity
import com.fzanutto.todoapp.R
import com.fzanutto.todoapp.database.TaskRepository
import com.fzanutto.todoapp.notification.NotificationManager.channelID
import com.fzanutto.todoapp.notification.NotificationManager.messageExtra
import com.fzanutto.todoapp.notification.NotificationManager.taskIdTag
import com.fzanutto.todoapp.notification.NotificationManager.titleExtra


class TaskNotification : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notificationTitle = intent.getStringExtra(titleExtra)
        val notificationBody = intent.getStringExtra(messageExtra)
        val taskId = intent.getIntExtra(taskIdTag, -1)

        if (taskId == -1) return

        TaskRepository.initialize(context)
        val task = TaskRepository.getTaskById(taskId) ?: return

        val clickPendingIntent = getClickIntent(context, taskId)

        val action = getNotificationAction(context, taskId)

        val notification = NotificationCompat.Builder(context, channelID)
            .setSmallIcon(R.drawable.ic_baseline_edit_24)
            .setContentTitle(notificationTitle)
            .setContentText(notificationBody)
            .setContentIntent(clickPendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .addAction(action)
            .setAutoCancel(true)
            .setOngoing(true)
            .build()

        with(NotificationManagerCompat.from(context)) {
            notify(taskId.taskIdToNotificationId(), notification)
        }

        NotificationManager.scheduleTaskNotification(context, task)
        Log.d("notification", "${task.id} ${task.title}")
    }

    private fun getClickIntent(context: Context, taskId: Int): PendingIntent {
        val clickIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra(taskIdTag, taskId)
        }

        return PendingIntent.getActivity(context, 0, clickIntent, PendingIntent.FLAG_IMMUTABLE)
    }


    private fun getNotificationAction(context: Context, taskId: Int): NotificationCompat.Action {
        val actionIntent = Intent(context, CompleteTaskActionReceiver::class.java)
        actionIntent.putExtra(taskIdTag, taskId)

        Log.d("notification", "${taskId}")

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            taskId.taskIdToActionId(),
            actionIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Action.Builder(
            R.drawable.ic_launcher_background,
            "Concluído",
            pendingIntent
        ).build()
    }
}