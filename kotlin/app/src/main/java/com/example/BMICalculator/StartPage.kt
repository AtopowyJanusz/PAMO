package com.example.BMICalculator

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class StartPage : AppCompatActivity() {
    var switchToSecondActivity: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_page)
        switchToSecondActivity = findViewById(R.id.startButton)
        switchToSecondActivity?.setOnClickListener(View.OnClickListener { switchActivities() })
    }

    private fun switchActivities() {
        val switchActivityIntent = Intent(this, MainActivity::class.java)startActivity(switchActivityIntent)
    }
    fun goToChart(view: View?) {
        val intent = Intent(this, Chart::class.java)
        startActivity(intent)
    }

    fun goToQuiz(view: View?) {
        val intent = Intent(this, Quiz::class.java)
        startActivity(intent)
    }
}