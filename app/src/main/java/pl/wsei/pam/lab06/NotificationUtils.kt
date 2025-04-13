package pl.wsei.pam

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

object NotificationUtils {
    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "todoChannel",
                "Todo Notifications",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Kanał przypomnień o zadaniach"
            }

            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }
}
