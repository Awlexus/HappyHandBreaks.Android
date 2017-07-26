package it.awlex.happyhandbreaks

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import android.util.Log

/**
 * Receive a Broadcast to fire the Notification
 */
class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.d(Constants.ALARM_LOG, "Alarm received")

        if (!intent.hasExtra(Constants.DURATION) or !intent.hasExtra(Constants.BETWEEN))
            return

        // Get Extra data
        val duration = intent.getLongExtra(Constants.DURATION, -1L)
        val between = intent.getLongExtra(Constants.BETWEEN, -1L)

        // Calculate and save next trigger time
        prefs(context).saveNextAlarmTriggerTime(System.currentTimeMillis() + duration + between)

        // Create Intent, that fires when the Notification is clicked
        val notificationContent: Intent = Intent(context, ExerciseActivity::class.java)
        notificationContent.putExtra(Constants.DURATION, duration)
        notificationContent.putExtra(Constants.BETWEEN, between)

        // Build Notification
        val pendingIntent = PendingIntent.getActivity(context, 0, notificationContent, PendingIntent.FLAG_UPDATE_CURRENT)
        val notification = buildNotification(context, pendingIntent)

        // Send Notification
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(context.packageName, 0, notification)

        Log.d("Alarm", "Notification send")
    }

    private fun buildNotification(context: Context, pendingIntent: PendingIntent?): Notification? {
        return NotificationCompat.Builder(context, context.packageName)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(context.getString(R.string.notification_title))
                .setContentText(context.getText(R.string.notification_text))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()
    }
}
