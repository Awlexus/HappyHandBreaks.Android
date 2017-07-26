package it.awlex.happyhandbreaks

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_countdown.view.*

/**
 * Created by Awlex on 26.07.2017.
 */
class CountDownFragment : Fragment() {

    private lateinit var v: View
    private var triggerAt: Long = -1
    private lateinit var thread: Thread

    companion object {
        fun newInstance() = CountDownFragment()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater!!.inflate(R.layout.fragment_countdown, container, false)
        return v
    }

    private fun updaterThread(): Thread {
        return object : Thread() {
            @SuppressLint("SetTextI18n")
            override fun run() {
                try {
                    while (!isInterrupted) {
                        activity.runOnUiThread {
                            val remaining = triggerAt - System.currentTimeMillis()

                            if (remaining > 0) {
                                v.next_break.text = "${remaining / 60000}:${remaining / 1000 % 60}:${remaining % 1000}"
                            } else {
                                stopCountDown()
                                interrupt()
                                startActivity(Intent(context, ExerciseActivity::class.java))
                            }
                        }
                        sleep(50)
                    }

                } catch (e: InterruptedException) {
                } catch (e: NullPointerException) {
                }

            }
        }
    }

    override fun onStop() {
        stopCountDown()
        super.onStop()
    }

    override fun onResume() {
        super.onResume()

        triggerAt = prefs(context).loadNextAlarmTriggerTime()
        thread = updaterThread()
        thread.start()
    }

    fun stopCountDown() = thread.interrupt()
}