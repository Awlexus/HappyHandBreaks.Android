package it.awlex.happyhandbreaks

import android.app.Application

/**
 * Created by Awlex on 27.07.2017. <br> <br>
 * Load the theme the user wants at start
 */
class ExtendedApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val theme = prefs(this).getInt(Constants.APP_THEME, R.style.AppTheme)
        setTheme(theme)
    }
}