package it.awlex.happyhands

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

/**
 * Created by Awlex on 26.07.2017.
 */
inline fun SharedPreferences.editAndApply(func: SharedPreferences.Editor.() -> Unit) {
    val editor = edit()
    editor.func()
    editor.apply()
}

// cache this value
private var nextTriggerTime: Long = -1L

fun SharedPreferences.saveNextAlarmTriggerTime(time: Long) {
    nextTriggerTime = time
    editAndApply { putLong(Constants.NEXT_ALARM, time) }
}

fun SharedPreferences.loadNextAlarmTriggerTime(): Long {
    if (nextTriggerTime == -1L)
        nextTriggerTime = getLong(Constants.NEXT_ALARM, -1L)
    return nextTriggerTime
}

/**
 * Short way of writing `PreferenceManager.getDefaultSharedPreferences(context)`
 */
inline fun prefs(context: Context) = PreferenceManager.getDefaultSharedPreferences(context)
