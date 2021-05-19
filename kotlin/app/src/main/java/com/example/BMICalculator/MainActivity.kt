package com.example.BMICalculator

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.NumberFormat
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() 
{
    private var weight = 0.0
    private var height = 1
    private var weightTextView: TextView? = null
    private var bmiValueTextView: TextView? = null
    private var heightTextView: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) 
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        weightTextView = findViewById<View>(R.id.weightTextView) as TextView
        weightTextView!!.text = "0.0"
        heightTextView = findViewById<View>(R.id.heightTextView) as TextView
        heightTextView!!.text = "1"
        bmiValueTextView = findViewById<View>(R.id.bmiValueTextView) as TextView
        bmiValueTextView!!.text = "0"
        val weightTextView = findViewById<View>(R.id.weightTextView) as EditText
        weightTextView.addTextChangedListener(weightTextWatcher)
        val heightSeekBar = findViewById<View>(R.id.heightSeekBar) as SeekBar
        heightSeekBar.setOnSeekBarChangeListener(heightBarListener)
    }

    private fun calculate() 
    {
        var bmi = weight / (height * height * 0.0001)
        bmi *= 100
        bmi = bmi.roundToInt().toDouble()
        bmi /= 100
        bmiValueTextView!!.text = weightFormat.format(bmi)
    }

    private val heightBarListener: OnSeekBarChangeListener = object : OnSeekBarChangeListener 
    {
        override fun onProgressChanged(seekBar: SeekBar, progress: Int,fromUser: Boolean) 
        {
            height = progress
            heightTextView!!.text = intFormat.format(height.toLong())
            calculate()
        }
        override fun onStartTrackingTouch(seekBar: SeekBar) {}
        override fun onStopTrackingTouch(seekBar: SeekBar) {}
    }
    private val weightTextWatcher: TextWatcher = object : TextWatcher 
    {
        override fun onTextChanged(s: CharSequence, start: Int,before: Int, count: Int) 
        {
            weight = try 
            {
                s.toString().toDouble()
            } catch (e: NumberFormatException) 
            {
                0.0
            }
            calculate()
        }

        override fun afterTextChanged(s: Editable) {}
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
    }

    fun sendMessage(view: View?) 
    {
        val intent = Intent(this, CheckCalories::class.java)
        val textView = findViewById<View>(R.id.bmiValueTextView) as TextView
        val message = textView.text.toString()
        val bun = Bundle()
        bun.putString(BMI, message)
        bun.putInt(HEIGHT, height)
        bun.putDouble(WEIGHT, weight)
        intent.putExtras(bun)
        startActivity(intent)
    }

    companion object 
    {
        private val weightFormat = NumberFormat.getNumberInstance()
        private val intFormat = NumberFormat.getIntegerInstance()
        const val BMI = "com.example.app.CheckCaloriesBMI"
        const val WEIGHT = "com.example.app.CheckCaloriesWEIGHT"
        const val HEIGHT = "com.example.app.CheckCaloriesHEIGHT"
    }
}