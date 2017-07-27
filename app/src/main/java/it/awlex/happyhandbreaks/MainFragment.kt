package it.awlex.happyhandbreaks

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import kotlinx.android.synthetic.main.fragment_main.view.*


class MainFragment : Fragment() {

    private lateinit var v: View

    companion object {
        /**
         * Following the Android guide for making Fragments.
         * Mistakes were made
         */
        fun newInstance(): MainFragment = MainFragment()
        private val BETWEENKEY = "between_key"
        private val DURATIONKEY = "duration_key"
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        var betweenValue = 3
        var durationValue = 1

        if (savedInstanceState != null) {
            betweenValue = savedInstanceState.getInt(BETWEENKEY)
            durationValue = savedInstanceState.getInt(DURATIONKEY)
        }
        // Inflate the layout for this fragment
        v = inflater!!.inflate(R.layout.fragment_main, container, false)

        // Temporary number
        v.between_numberpicker.maxValue = 9
        v.between_numberpicker.displayedValues = Array(v.between_numberpicker.maxValue + 1, { i -> (i * 10 + 30).toString() })
        v.between_numberpicker.value = betweenValue

        // Temporary number
        v.duration_numberpicker.maxValue = 39
        v.duration_numberpicker.displayedValues = Array(v.duration_numberpicker.maxValue + 1, { i -> (i * 5 + 5).toString() })
        v.duration_numberpicker.value = durationValue

        return v
    }

    /**
     * Return the currently selected value of the [NumberPicker],
     * cast it into an Long and multiply it by 60000
     */
    private fun NumberPicker.currentDisplayValueInMillis() = displayedValues[value].toLong() * 60000

    /**
     * * returns the preferred duration for a break in ms
     */
    fun getDuration(): Long = v.between_numberpicker.currentDisplayValueInMillis()

    /**
     * returns the preferred time between breaks in ms
     */
    fun getBetween(): Long = v.duration_numberpicker.currentDisplayValueInMillis()

    override fun onSaveInstanceState(outState: Bundle?) {
        outState ?: return super.onSaveInstanceState(outState)

        outState.putInt(BETWEENKEY, v.between_numberpicker.value)
        outState.putInt(DURATIONKEY, v.duration_numberpicker.value)
        super.onSaveInstanceState(outState)
    }

}