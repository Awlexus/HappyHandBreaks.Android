package it.awlex.happyhandbreaks.utils

import android.content.Context
import android.util.Log
import it.awlex.happyhandbreaks.Constants

/**
 * Created by Awlex on 27.07.2017.
 *      Utils for the scheduled alarms
 */
// cached value indication when the alarm will trigger
private var nextTriggerTime: Long = -1L

/**
 * Set the new time when the notification will trigger.
 * This value will be saved to the shared preferences
 */
fun setNextAlarmTriggerTime(context: Context, time: Long) {
    // Set new TriggerTime
    nextTriggerTime = time

    // Save the new time to the shared preferences
    prefs(context).editAndApply { putLong(Constants.NEXT_ALARM, time) }

    Log.d(Constants.ALARM_LOG, "Alarm saved. Next Alarm at $time")
}

/**
 * Load and cache the saved value from the default shared preferences
 */
fun getNextAlarmTriggerTime(context: Context): Long {
    val prefs = prefs(context)

    // Load the time if not it's -1, to be secure whether it
    // was not cached yet or there's no alarm scheduled
    if (nextTriggerTime == -1L)
        nextTriggerTime = prefs.getLong(Constants.NEXT_ALARM, -1L)

    // Override the next trigger with -1 if it should have happened already
    if (nextTriggerTime < System.currentTimeMillis()) {
        Log.e(Constants.ALARM_LOG, "Saved value was before the current time")
        setNextAlarmTriggerTime(context, -1)
    }

    if (nextTriggerTime == -1L)
        Log.d(Constants.ALARM_LOG, "No Alarm planned")
    else
        Log.d(Constants.ALARM_LOG, "Alarm loaded. Next Alarm at ${nextTriggerTime}")

    return nextTriggerTime
}