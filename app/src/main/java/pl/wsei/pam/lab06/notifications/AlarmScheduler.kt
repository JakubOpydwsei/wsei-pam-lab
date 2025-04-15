package pl.wsei.pam.lab06.notifications

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import pl.wsei.pam.lab06.Lab06Activity
import pl.wsei.pam.lab06.TodoTask
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

object AlarmScheduler {

    private val formatter = DateTimeFormatter.ofPattern("d/M/yyyy") // Obsługa dat typu 15/4/2025

    @SuppressLint("ShortAlarm")
    fun scheduleRepeatingAlarm(context: Context, task: TodoTask) {
        cancelAlarm(context)

        try {
            val date = LocalDate.parse(task.deadline, formatter).minusDays(1)

            val triggerTime = date
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()

            val intent = Intent(context, NotificationBroadcastReceiver::class.java).apply {
                putExtra(Lab06Activity.titleExtra, "Deadline!")
                putExtra(Lab06Activity.messageExtra, "Task: ${task.title} is waiting for you !")
            }

            val pendingIntent = PendingIntent.getBroadcast(
                context,
                Lab06Activity.notificationID,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                triggerTime,
                20_000L,
                pendingIntent
            )

        } catch (e: DateTimeParseException) {
            Log.e("Ararm error", "Date error", e)
        }
    }

    fun cancelAlarm(context: Context) {
        val intent = Intent(context, NotificationBroadcastReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            Lab06Activity.notificationID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_NO_CREATE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        if (pendingIntent != null) alarmManager.cancel(pendingIntent)
    }
}
