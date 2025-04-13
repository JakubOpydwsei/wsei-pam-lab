package pl.wsei.pam

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class NotificationBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val title = intent?.getStringExtra("titleExtra") ?: "Uwaga"
        val message = intent?.getStringExtra("messageExtra") ?: "Zbliża się termin zadania"

        if (context == null) return

        // SPRAWDZENIE UPRAWNIENIA
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Brak uprawnień – nie pokazujemy powiadomienia
            return
        }

        val notification = NotificationCompat.Builder(context, "todoChannel")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        NotificationManagerCompat.from(context).notify(123, notification)
    }
}
