package it.awlex.happyhandbreaks

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var currentFragment: Fragment
    private lateinit var notificationIntent: Intent

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        notificationIntent = getNotificationIntent(this)
    }

    override fun onResume() {
        super.onResume()
        val alarm = prefs(this).loadNextAlarmTriggerTime()
        if (alarm == -1L)
            Log.d("Alarm", "No alarm scheduled")
        else
            Log.d("Alarm", "next Alarm in ${alarm - System.currentTimeMillis()}ms")

        supportFragmentManager.inTransaction {
            if (alarm != -1L) {
                currentFragment = CountDownFragment.newInstance()
            } else {
                currentFragment = MainFragment.newInstance()
            }
            add(R.id.container, currentFragment)
        }

        prepareButton(alarm != -1L)
    }

    private fun prepareButton(IsAlarmRunning: Boolean) {
        if (IsAlarmRunning) {
            session_button.text = getString(R.string.cancel_alarm)
            session_button.setBackgroundColor(resources.getColor(android.R.color.holo_red_light))
            session_button.setOnClickListener { cancelAlarm() }
        } else {
            session_button.text = getString(R.string.start_alarm)
            session_button.setBackgroundColor(resources.getColor(android.R.color.holo_green_light))
            session_button.setOnClickListener { startSession() }
        }
    }

    fun cancelAlarm() {
        cancleAlarm(this, notificationIntent)
        if (currentFragment is CountDownFragment) {
            (currentFragment as CountDownFragment).stopCountDown()
            replace(MainFragment.newInstance())
        }
        prefs(this).saveNextAlarmTriggerTime(-1L)
        prepareButton(false)
    }

    private fun startSession() {
        Log.d("Alarm", "Prepare Repeating")
        // Cool oneliner nobody understands in kotlin
        // If currentFragment is an instance of Mainfragment allocate it, else exit this function

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

    private inline fun replace(newFragment: Fragment) {
        currentFragment = newFragment
        supportFragmentManager.inTransaction { replace(R.id.container, newFragment) }
    }


}