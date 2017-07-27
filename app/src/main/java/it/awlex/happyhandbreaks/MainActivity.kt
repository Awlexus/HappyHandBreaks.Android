package it.awlex.happyhandbreaks

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import it.awlex.happyhandbreaks.utils.*
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var currentFragment: Fragment? = null
    private lateinit var notificationIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTheme(getCurrentTheme(this))
        setContentView(R.layout.activity_main)

        notificationIntent = getNotificationIntent(this)
    }

    override fun onResume() {
        super.onResume()
        val alarm = getNextAlarmTriggerTime(this)
        if (alarm == -1L)
            Log.d(Constants.ALARM_LOG, "No alarm scheduled")
        else
            Log.d(Constants.ALARM_LOG, "next Alarm in ${alarm - System.currentTimeMillis()}ms")

        // When returning from another Activity, currentfragment won't be null,
        // meaning if currentfragment is not null adding a Fragment would stack it on top
        // of currentfragment and the fragments underneath would be unaddressable (Memory Leak?)

        // Add either the MainFragment or the CountDownFragment depending on
        // whether there is an alarm scheduled
        supportFragmentManager.inTransaction {
            val isNull = currentFragment == null
            if (alarm != -1L) {
                currentFragment = CountDownFragment.newInstance()
            } else {
                currentFragment = MainFragment.newInstance()
            }
            if (isNull)
                add(R.id.container, currentFragment)
            else replace(R.id.container, currentFragment)
        }

        // Setup the bottom button depending on whether there is an alarm scheduled
        prepareButton(alarm != -1L)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        menu ?: return true
        menu.findItem(R.id.debug).isChecked = isDebugMode(this)
        menu.findItem(R.id.set_theme).setTitle(getToggledThemeTitleId(this))

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item ?: return false

        when (item.itemId) {
            R.id.debug -> item.isChecked = toggleDebugMode(this)
            R.id.set_theme -> {
                toggleCurrentTheme(this)
                // item.setTitle(getToggledThemeTitleId(this))
            }
        }

        return true
    }

    @Suppress("DEPRECATION")
    private fun prepareButton(IsAlarmRunning: Boolean) {
        if (IsAlarmRunning) {
            session_button.text = getString(R.string.cancel_alarm)
            session_button.setBackgroundColor(resources.getColor(android.R.color.holo_red_light))
            session_button.setOnClickListener { cancelAlarm() }
        } else {
            session_button.text = getString(R.string.start_alarm)
            session_button.setBackgroundColor(resources.getColor(android.R.color.holo_green_light))
            session_button.setOnClickListener { startAlarm() }
        }
    }

    /**
     * Cancel the scheduled alarm and update UI
     */
    fun cancelAlarm() {

        // Cancel the alarm
        cancelNotification(this, notificationIntent)

        // CountdownFragment with Mainfragment
        if (currentFragment is CountDownFragment) {
            (currentFragment as CountDownFragment).stopCountDown()
            replace(MainFragment.newInstance())
        }

        // Save -1 to indicate that the schedule is canceled
        setNextAlarmTriggerTime(this, -1L)

        // Let Button start the schedule when clicked on
        prepareButton(false)
    }

    private fun startAlarm() {
        Log.d(Constants.ALARM_LOG, "Prepare Alarm")

        // TODO: Replace with real values
        val duration = 10000L // mainFragment.getDuration()
        val between = 60000L  // mainFragment.getBetween()

        // Add extras
        notificationIntent.putExtra(Constants.DURATION, duration)
        notificationIntent.putExtra(Constants.BETWEEN, between)

        // Calculate repeating times
        val triggerAt = System.currentTimeMillis() + between
        val delay = duration + between

        //Save new trigger time to indicate that the has been started
        setNextAlarmTriggerTime(this, triggerAt)

        // Schedule Alarm
        scheduleNotification(this, notificationIntent, triggerAt, delay)

        // Replace MainFragment with CountDownFragment
        replace(CountDownFragment.newInstance())

        // Let the Button cancel the schedule when clicked on
        prepareButton(true)
    }

    inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> Unit) {
        val transaction = beginTransaction()
        transaction.func()
        transaction.commit()
    }

    /**
     * Replace [currentFragment] with [newFragment]
     *
     * @param newFragment the fragment the will replace [currentFragment]
     */
    @Suppress("NOTHING_TO_INLINE")
    private inline fun replace(newFragment: Fragment) {
        currentFragment = newFragment
        supportFragmentManager.inTransaction { replace(R.id.container, newFragment) }
    }
}