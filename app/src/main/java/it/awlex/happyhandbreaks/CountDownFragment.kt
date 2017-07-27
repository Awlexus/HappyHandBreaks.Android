package it.awlex.happyhandbreaks

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import it.awlex.happyhandbreaks.utils.getNextAlarmTriggerTime
import it.awlex.happyhandbreaks.utils.prefs
import kotlinx.android.synthetic.main.fragment_countdown.view.*

/**
 * Created by Awlex on 26.07.2017. <br>
 * Fragment Counting down to the time the Notification will be triggered
 */
class CountDownFragment : Fragment() {

    private lateinit var v: View
    private var triggerAt: Long = -1
    private lateinit var thread: Thread

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater!!.inflate(R.layout.fragment_countdown, container, false)
        return v
    }

    /**
     * Returns a new Thread used to update the countdown
     */
    @SuppressLint("SetTextI18n")
    private fun updaterThread(): Thread {
        return object : Thread() {
            override fun run() {
                try {
                    while (!isInterrupted) {
                        activity.runOnUiThread {

                            // Time to next Alarm
                            val remaining = triggerAt - System.currentTimeMillis()

                            // Update the UI if countdown is not finished or launch the ExerciseActivity
                            if (remaining > 0) {
                                v.next_break.text = "${remaining / 60000}:${remaining / 1000 % 60}:${remaining % 1000}"
                            } else {
                                stopCountDown()
                                interrupt()
                                startActivity(Intent(context, ExerciseActivity::class.java))
                            }
                        }

                        // Don't flood the apps with updates
                        sleep(50)
                    }

                } catch (e: InterruptedException) {
                } catch (e: NullPointerException) {
                    // `activity` can sometimes be null
                }

            }
        }
    }

    override fun onStop() {
        super.onStop()
        stopCountDown()
    }

    override fun onResume() {
        super.onResume()

        triggerAt = getNextAlarmTriggerTime(context)
        thread = updaterThread()
        thread.start()
    }

    companion object {
        fun newInstance() = CountDownFragment()
    }

    fun stopCountDown() {
        if (!thread.isInterrupted)
            thread.interrupt()
    }
}