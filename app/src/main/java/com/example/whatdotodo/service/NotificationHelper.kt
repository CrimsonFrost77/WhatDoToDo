// service/NotificationHelper.kt

package com.example.whatdotodo.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.whatdotodo.R

class NotificationHelper(private val context: Context) {

    private val CHANNEL_ID = "todo_channel"

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Todo Notifications"
            val descriptionText = "Notifications for top ToDo task"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showNotification(taskTitle: String) {
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Replace with your own icon if needed
            .setContentTitle("Top To-Do Task")
            .setContentText(taskTitle)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setOngoing(true) // Make it sticky (can't swipe away)

        with(NotificationManagerCompat.from(context)) {
            notify(1, builder.build())
        }
    }

    fun cancelNotification() {
        with(NotificationManagerCompat.from(context)) {
            cancel(1)
        }
    }
}
