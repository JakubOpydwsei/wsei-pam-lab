package pl.wsei.pam.lab06.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import pl.wsei.pam.lab01.R
import pl.wsei.pam.lab06.Lab06Activity

class NotificationBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val title = intent?.getStringExtra(Lab06Activity.titleExtra) ?: "Task"
        val message = intent?.getStringExtra(Lab06Activity.messageExtra) ?: "Deadline is comming!"

        val notification = NotificationCompat.Builder(context, Lab06Activity.channelID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        if (NotificationManagerCompat.from(context)
                .areNotificationsEnabled()
        ) {
            NotificationManagerCompat.from(context)
                .notify(Lab06Activity.notificationID, notification)
        }
    }
}
