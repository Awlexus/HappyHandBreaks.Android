package it.awlex.happyhandbreaks.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

/**
 * Created by Awlex on 26.07.2017. <br>
 *      Simplifying some SharedPreference mechanics
 */

/**
 *
 */
inline fun SharedPreferences.editAndApply(func: SharedPreferences.Editor.() -> Unit) {
    val editor = edit()
    editor.func()
    editor.apply()
}


/**
 * Short way of writing `PreferenceManager.getDefaultSharedPreferences(context)`
 */
@Suppress("NOTHING_TO_INLINE")
inline fun prefs(context: Context) = PreferenceManager.getDefaultSharedPreferences(context)