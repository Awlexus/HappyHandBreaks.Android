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
        fun newInstance(): MainFragment = MainFragment()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        v = inflater!!.inflate(R.layout.fragment_main, container, false)

        // Temporary number
        v.numberPicker.maxValue = 10
        v.numberPicker.displayedValues = Array(v.numberPicker.maxValue + 1, { i -> (i * 10 + 30).toString() })

        // Temporary number
        v.numberPicker2.maxValue = 40
        v.numberPicker2.displayedValues = Array(v.numberPicker2.maxValue + 1, { i -> (i * 5 + 5).toString() })

        return v
    }


    fun NumberPicker.currentDisplayValueInMillis() = (displayedValues[value].toInt() * 60000).toLong()

    /**
     * returns the preferred duration for a break in ms
     */
    fun getDuration(): Long = v.numberPicker.currentDisplayValueInMillis()

    /**
     * returns the preferred time between breaks in ms
     */
    fun getBetween(): Long = v.numberPicker2.currentDisplayValueInMillis()
}