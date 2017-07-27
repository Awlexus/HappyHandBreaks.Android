package it.awlex.happyhandbreaks

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import it.awlex.happyhandbreaks.utils.getCurrentTheme

class ExerciseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(getCurrentTheme(this))
        setContentView(R.layout.activity_exercise)
    }
}
