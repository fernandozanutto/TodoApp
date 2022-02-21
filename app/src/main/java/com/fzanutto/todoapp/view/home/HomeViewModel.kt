package com.fzanutto.todoapp.view.home

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fzanutto.todoapp.database.TaskRepository
import com.fzanutto.todoapp.models.Task
import com.fzanutto.todoapp.notification.Notification
import com.fzanutto.todoapp.notification.channelID
import com.fzanutto.todoapp.notification.messageExtra
import com.fzanutto.todoapp.notification.notificationID
import com.fzanutto.todoapp.notification.titleExtra
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

class HomeViewModel : ViewModel() {

    private val _taskList = MutableLiveData<List<Task>>()
    val taskList: LiveData<List<Task>> = _taskList


    fun requestTaskList() {
        viewModelScope.launch(Dispatchers.IO) {
            val list = TaskRepository.getAllTasks()
            _taskList.postValue(list)
        }
    }

    fun createNotificationChannel(context: Context) {
        val name = "Notif Channel"
        val desc = "aiosdjasoihasd"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelID, name, importance)
        channel.description = desc

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    fun scheduleNotification(context: Context) {
        val intent = Intent(context, Notification::class.java)
        val title = "Notificação de teste"
        val message = "Descrição"
        intent.putExtra(titleExtra, title)
        intent.putExtra(messageExtra, message)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            notificationID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val time = Calendar.getInstance()
        time.timeInMillis = Date().time
        time.add(Calendar.SECOND, 2)

        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time.timeInMillis, pendingIntent)
    }

}