package it.awlex.happyhandbreaks

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log

/**
 * Created by Awlex on 26.07.2017. A collection of functions for building the notifications
 */

/**
 * return the default Intent to start a Notification
 */
fun getNotificationIntent(context: Context) = Intent(context, AlarmReceiver::class.java)


fun getPendingIntent(context: Context, requestCode: Int = 0, intent: Intent, flags: Int = PendingIntent.FLAG_NO_CREATE) =
        PendingIntent.getBroadcast(context, requestCode, intent, flags)

/**
 * Schedule a Notification starting at [startAt] and repeat doing so every [delay]ms
 *
 * @param intent Intent for launching the Notification. Preferably [getNotificationIntent] with added
 * extra data
 */
fun scheduleNotification(context: Context, intent: Intent, startAt: Long, delay: Long) {
    Log.d(Constants.ALARM_LOG, "Scheduling Alarm.")

    // Initialize alarmmanager and PendingIntent
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val pendingIntent = getPendingIntent(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

    // Start the repeated cycle of Notifications
    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, startAt, delay, pendingIntent)

    // Save the time (ms) when the Notification will be triggered
    prefs(context).saveNextAlarmTriggerTime(startAt)

    Log.d(Constants.ALARM_LOG, "Alarm scheduled. Next Alarm: $startAt")
}

/**
 * Cancel the schedule to [intent]
 *
 * @param intent the Intent that the schedule fires every interwall
 */
fun cancelAlarm(context: Context, intent: Intent) {
    // Cancel schedule
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    alarmManager.cancel(getPendingIntent(context, intent = intent, flags = PendingIntent.FLAG_UPDATE_CURRENT))

    // Update the trigger time
    prefs(context).saveNextAlarmTriggerTime(-1L)

    Log.d(Constants.ALARM_LOG, "Alarm cancled")
}