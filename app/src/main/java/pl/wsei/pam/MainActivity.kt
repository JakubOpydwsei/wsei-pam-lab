package pl.wsei.pam

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import pl.wsei.pam.lab01.Lab01Activity
import pl.wsei.pam.lab02.Lab02Activity
import pl.wsei.pam.lab06.Lab06

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Utwórz kanał notyfikacji
        NotificationUtils.createNotificationChannel(this)

        // Poproś o zgodę na notyfikacje, jeśli potrzebne
        requestNotificationPermission()

        // Zaplanuj alarm z powiadomieniem (testowo po 10 sek.)
        scheduleAlarm()
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1001
                )
            }
        }
    }

    private fun scheduleAlarm() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!alarmManager.canScheduleExactAlarms()) {
                // Nie mamy uprawnień – opcjonalnie poprowadź użytkownika do ustawień
                val intent = Intent(android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                startActivity(intent)
                return
            }
        }


        val intent = Intent(this, NotificationBroadcastReceiver::class.java).apply {
            putExtra("titleExtra", "Przypomnienie")
            putExtra("messageExtra", "Czas na zadanie!")
        }

        val pendingIntent = PendingIntent.getBroadcast(
            this,
            1,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val triggerTime = System.currentTimeMillis() + 10_000 // za 10 sekund
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
    }

    fun onClickMainBtnRunLab01(v: View) {
        Toast.makeText(this, "Opening Lab 01", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, Lab01Activity::class.java)
        startActivity(intent)
    }

    fun onClickMainBtnRunLab02(v: View) {
        Toast.makeText(this, "Opening Lab 02", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, Lab02Activity::class.java)
        startActivity(intent)
    }

    fun onClickMainBtnRunLab06(v: View) {
        Toast.makeText(this, "Opening Lab 6", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, Lab06::class.java)
        startActivity(intent)
    }
}
