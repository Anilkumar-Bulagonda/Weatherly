package uk.ac.tees.mad.weatherly

import android.app.AlarmManager
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import dagger.hilt.android.HiltAndroidApp
import java.util.Calendar

@HiltAndroidApp
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        scheduleDailyNotification()
//        showTestNotificationNow(
//            context = this
//        )

    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "daily_channel",
                "Daily Weather Updates",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Sends a fun weather update every day at 8 PM"
            }
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }


    fun showTestNotificationNow(context: Context) {
        val notificationManager =
            context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val fakeWeatherUpdates = listOf(
            "Today's forecast: Sunny skies and warm vibes!",
            "Looks like some rain is on the way — grab your umbrella!",
            "Cloudy morning turning into a bright afternoon.",
            "Thunderstorms tonight — stay cozy indoors!",
            "Fog rolling in — drive safe out there!",
            "Light drizzle expected with cool winds.",
            "A chilly day ahead — perfect for a hot drink!"
        )

        val randomUpdate = fakeWeatherUpdates.random()

        val builder = NotificationCompat.Builder(context, "daily_channel")
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle("Weather Test Notification")
            .setContentText(randomUpdate)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        val notificationId = System.currentTimeMillis().toInt() // unique ID
        notificationManager.notify(notificationId, builder.build())
    }

    private fun scheduleDailyNotification() {
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 8)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)

            if (before(Calendar.getInstance())) {
                add(Calendar.DATE, 1)
            }
        }

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }



}


class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val fakeWeatherUpdates = listOf(
            " Today's forecast: Sunny skies and warm vibes!",
            " Looks like some rain is on the way — grab your umbrella!",
            " Cloudy morning turning into a bright afternoon.",
            " Thunderstorms tonight — stay cozy indoors!",
            " Fog rolling in — drive safe out there!",
            " Light drizzle expected with cool winds.",
            " A chilly day ahead — perfect for a hot drink!"
        )

        val randomUpdate = fakeWeatherUpdates.random()

        val builder = NotificationCompat.Builder(context, "daily_channel")
            .setSmallIcon(R.drawable.sp)
            .setContentTitle("Daily Weather Update")
            .setContentText(randomUpdate)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        notificationManager.notify(1001, builder.build())
    }
}


