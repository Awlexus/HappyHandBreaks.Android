package it.awlex.happyhandbreaks

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log

/**
 * Created by Awlex on 26.07.2017.
 */

/**
 * return the default Intent to start a Notification
 */
fun getNotificationIntent(context: Context) = Intent(context, AlarmReceiver::class.java)

fun getPendingIntent(context: Context, requestCode: Int = 0, intent: Intent, flags: Int = PendingIntent.FLAG_NO_CREATE) =
        PendingIntent.getBroadcast(context, requestCode, intent, flags)

fun scheduleNotification(context: Context, intent: Intent, startAt: Long, delay: Long) {
    Log.d("Alarm", "Scheduling Alarm")
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val pendingIntent = getPendingIntent(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, startAt, delay, pendingIntent)
    prefs(context).saveNextAlarmTriggerTime(startAt)
    Log.d("Alarm", "Alarm scheduled")
}

fun cancleAlarm(context: Context, intent: Intent) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    alarmManager.cancel(getPendingIntent(context, intent = intent, flags = PendingIntent.FLAG_UPDATE_CURRENT))
    prefs(context).saveNextAlarmTriggerTime(-1L)
    Log.d("Alarm", "Alarm cancled")
}