package it.awlex.happyhandbreaks

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Log

/**
 * Created by Awlex on 26.07.2017. <br>
 * Simplifying some SharedPreference mechanics
 */

/**
 *
 */
inline fun SharedPreferences.editAndApply(func: SharedPreferences.Editor.() -> Unit) {
    val editor = edit()
    editor.func()
    editor.apply()
}

// cache this value
private var nextTriggerTime: Long = -1L

/**
 * Cache the new time when a notification will be fired and save it in the default shared preferences
 */
fun SharedPreferences.saveNextAlarmTriggerTime(time: Long) {
    nextTriggerTime = time
    editAndApply { putLong(Constants.NEXT_ALARM, time) }

    Log.d(Constants.ALARM_LOG, "Alarm saved. Next Alarm at $time")
}

/**
 * Load and cache the saved value from the default shared preferences
 */
fun SharedPreferences.loadNextAlarmTriggerTime(): Long {
    if (nextTriggerTime == -1L)
        nextTriggerTime = getLong(Constants.NEXT_ALARM, -1L)

    Log.d(Constants.ALARM_LOG, "Alarm loaded. Next Alarm at $nextTriggerTime")

    return nextTriggerTime
}

/**
 * Short way of writing `PreferenceManager.getDefaultSharedPreferences(context)`
 */
@Suppress("NOTHING_TO_INLINE")
inline fun prefs(context: Context) = PreferenceManager.getDefaultSharedPreferences(context)
