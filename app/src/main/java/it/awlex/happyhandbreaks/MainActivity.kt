package it.awlex.happyhandbreaks

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var currentFragment: Fragment
    private lateinit var notificationIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        notificationIntent = getNotificationIntent(this)
    }

    override fun onResume() {
        super.onResume()
        val alarm = prefs(this).loadNextAlarmTriggerTime()
        if (alarm == -1L)
            Log.d(Constants.ALARM_LOG, "No alarm scheduled")
        else
            Log.d(Constants.ALARM_LOG, "next Alarm in ${alarm - System.currentTimeMillis()}ms")

        // Add either the MainFragment or the CountDownFragment depending on
        // whether there is an alarm scheduled
        supportFragmentManager.inTransaction {
            if (alarm != -1L) {
                currentFragment = CountDownFragment.newInstance()
            } else {
                currentFragment = MainFragment.newInstance()
            }
            add(R.id.container, currentFragment)
        }

        // Setup the bottom button depending on whether there is an alarm scheduled
        prepareButton(alarm != -1L)
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
        cancelAlarm(this, notificationIntent)
        if (currentFragment is CountDownFragment) {
            (currentFragment as CountDownFragment).stopCountDown()
            replace(MainFragment.newInstance())
        }
        prefs(this).saveNextAlarmTriggerTime(-1L)
        prepareButton(false)
    }

    private fun startAlarm() {
        Log.d(Constants.ALARM_LOG, "Prepare Alarm")

        // TODO: Replace with real values
        val duration = 10000L // mainFragment.getDuration()
        val between = 60000L  // mainFragment.getBetween()

        notificationIntent.putExtra(Constants.DURATION, duration)
        notificationIntent.putExtra(Constants.BETWEEN, between)

        val triggerAt = System.currentTimeMillis() + between
        val delay = duration + between

        scheduleNotification(this, notificationIntent, triggerAt, delay)
        replace(CountDownFragment.newInstance())
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