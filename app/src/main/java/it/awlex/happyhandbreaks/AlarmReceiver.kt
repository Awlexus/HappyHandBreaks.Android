package it.awlex.happyhands

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import android.util.Log

class AlarmReceiver : BroadcastReceiver() {


    override fun onReceive(context: Context, intent: Intent) {
        Log.d("Alarm", "Alarm received")
        if (!intent.hasExtra(Constants.DURATION) or !intent.hasExtra(Constants.BETWEEN))
            return

        val duration = intent.getLongExtra(Constants.DURATION, -1L)
        val between = intent.getLongExtra(Constants.BETWEEN, -1L)

        prefs(context).saveNextAlarmTriggerTime(System.currentTimeMillis() + duration + between)

        val notificationContent: Intent = Intent(context, ExerciseActivity::class.java)
        notificationContent.putExtra(Constants.DURATION, duration)
        notificationContent.putExtra(Constants.BETWEEN, between)

        val pendingIntent = PendingIntent.getActivity(context, 0, notificationContent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = NotificationCompat.Builder(context, context.packageName)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(context.getString(R.string.notification_title))
                .setContentText(context.getText(R.string.notification_text))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(context.packageName, 0, notification)
        Log.d("Alarm", "Notification send")
    }
}
