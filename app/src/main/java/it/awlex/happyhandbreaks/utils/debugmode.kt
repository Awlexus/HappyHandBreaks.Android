package it.awlex.happyhandbreaks.utils

import android.content.Context
import it.awlex.happyhandbreaks.Constants

/**
 * Created by Awlex on 27.07.2017.
 *      Utils for Debug mode
 */

// Cached value that tells whether debugmode is on or off
private var debug: Boolean? = null

/**
 * @return whether debug mode is activated
 */
fun isDebugMode(context: Context): Boolean {
    if (debug == null)
        debug = prefs(context).getBoolean(Constants.DEBUG, false)
    return debug!!
}

/**
 * Toggle debug mode on or off.
 * @return if debug mode is activated
 */
fun toggleDebugMode(context: Context): Boolean {
    if (debug == null)
        debug = !isDebugMode(context)
    else
        debug = !debug!!

    prefs(context).editAndApply {
        putBoolean(Constants.DEBUG, debug!!)
    }

    return debug!!
}