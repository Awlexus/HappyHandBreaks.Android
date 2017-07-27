package it.awlex.happyhandbreaks.utils

import android.app.Activity
import android.content.Context
import it.awlex.happyhandbreaks.Constants
import it.awlex.happyhandbreaks.R

/**
 * Created by Awlex on 27.07.2017. <br> <br>
 *     Utils to manage the applications theme
 */

private var currentTheme: Int = -1

/**
 * @return the theme the user has currently selected
 */
fun getCurrentTheme(context: Context): Int {

    // Load the theme from the preferences if it's not cached
    if (currentTheme == -1)
        currentTheme = prefs(context).getInt(Constants.APP_THEME, R.style.AppTheme)

    return currentTheme
}

/**
 * Toggles between Light and Dark theme
 */
fun toggleCurrentTheme(activity: Activity) {

    // Load the theme from the preferences if it's not cached
    if (currentTheme == -1)
        getCurrentTheme(activity)

    // discover, which theme should be the new one
    if (currentTheme == R.style.AppTheme)
        currentTheme = R.style.AppTheme_Dark
    else
        currentTheme = R.style.AppTheme

    // apply the theme
    activity.recreate()
}

/**
 * @return the name of the current theme
 */
fun getCurrentThemeTitleId(context: Context): Int {

    // Load the theme from the preferences if it's not cached
    if (currentTheme == -1)
        getCurrentTheme(context)

    return if (currentTheme == R.style.AppTheme) R.string.light else R.string.dark
}

/**
 * @return the name of the opposite theme
 */
fun getToggledThemeTitleId(context: Context): Int {

    // Load the theme from the preferences if it's not cached
    if (currentTheme == -1)
        getCurrentTheme(context)

    return if (currentTheme == R.style.AppTheme) R.string.dark else R.string.light
}