package com.example.whatdotodo.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.whatdotodo.R

class NotificationHelper(private val context: Context) {

    private val channelId = "todoChannel"

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Todo Notifications"
            val descriptionText = "Notifications for top ToDo task"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showNotification(taskTitle: String) {
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // ✅ Use a proper white-on-transparent icon
            .setContentTitle("Top To-Do Task")
            .setContentText(taskTitle)
            .setPriority(NotificationCompat.PRIORITY_HIGH)   // ✅ High priority = heads-up & persistent
            .setOngoing(true)                                // ✅ Prevents swipe-dismiss (most devices)
            .setAutoCancel(false)                            // ✅ Prevents dismiss on click
            .setOnlyAlertOnce(true)                          // ✅ Don’t re-alert if updated
            .setDefaults(0)                                  // ✅ Disable vibration, sound


        val notificationManager = NotificationManagerCompat.from(context)

        // ✅ Check permission before notifying
        if (ActivityCompat.checkSelfPermission(
                context,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            notificationManager.notify(1, builder.build())
        }
    }


    fun cancelNotification() {
        with(NotificationManagerCompat.from(context)) {
            cancel(1)
        }
    }
}
