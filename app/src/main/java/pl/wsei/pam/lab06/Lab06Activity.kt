package pl.wsei.pam.lab06

import android.Manifest
import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import pl.wsei.pam.lab06.data.TodoDatabase
import pl.wsei.pam.lab06.data.TodoRepository
import pl.wsei.pam.lab06.notifications.NotificationBroadcastReceiver
import pl.wsei.pam.lab06.ui.theme.Lab06Theme
import pl.wsei.pam.lab06.viewmodel.TodoViewModel
import pl.wsei.pam.lab06.viewmodel.TodoViewModelFactory

class Lab06Activity : ComponentActivity() {

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                println("Granted")
            } else {
                println("Not granted")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        createNotificationChannel()


        scheduleAlarm(System.currentTimeMillis() + 2000)
        scheduleInitialTaskNotification()

        val dao = TodoDatabase.getDatabase(application).taskDao()
        val repository = TodoRepository(dao)
        val todoViewModel = ViewModelProvider(this, TodoViewModelFactory(repository, application))[TodoViewModel::class.java]

        setContent {
            Lab06Theme {
                Surface(
                    modifier = androidx.compose.ui.Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(todoViewModel = todoViewModel)
                }
            }
        }
    }

    @SuppressLint("ShortAlarm")
    private fun scheduleAlarm(time: Long) {
        val intent = Intent(applicationContext, NotificationBroadcastReceiver::class.java).apply {
            putExtra(titleExtra, "Task!")
            putExtra(messageExtra, "Your deadline is comming soon !")
        }

        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            notificationID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            time,
            20_000L,
            pendingIntent
        )
    }

    private fun scheduleInitialTaskNotification() {
        val intent = Intent(applicationContext, NotificationBroadcastReceiver::class.java).apply {
            putExtra(titleExtra, "Welcome")
            putExtra(messageExtra, "First testing alarm")
        }

        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            notificationID + 1,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis() + 2000,
            pendingIntent
        )
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Lab06 channel"
            val descriptionText = "Lab06 is channel for notifications for approaching tasks."
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        const val notificationID = 121
        const val channelID = "Lab06 channel"
        const val titleExtra = "title"
        const val messageExtra = "message"
    }
}
