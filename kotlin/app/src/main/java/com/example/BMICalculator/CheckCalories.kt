package com.example.BMICalculator

import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.text.NumberFormat

class CheckCalories : AppCompatActivity() 
{
    private var radioGroup: RadioGroup? = null
    private var radioButton: RadioButton? = null
    private var btnDisplay: Button? = null
    private val age = 20
    override fun onCreate(savedInstanceState: Bundle?) 
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_calories)
        val intent = intent
        val message = intent.getStringExtra(MainActivity.BMI)
        val bmiValue = findViewById<TextView>(R.id.bmiValue)
        bmiValue.text = message
        findViewById<TextView>(R.id.caloriesValue)
        val bundle = getIntent().extras
        val height = bundle!!.getInt(MainActivity.Companion.HEIGHT)
        val weight = bundle.getDouble(MainActivity.Companion.WEIGHT)
        addListenerOnButton(height, weight, age)
    }

    fun addListenerOnButton(height: Int, weight: Double, age: Int) 
    {
        radioGroup = findViewById<View>(R.id.radioGroup) as RadioGroup
        btnDisplay = findViewById<View>(R.id.foodOfferButton) as Button
        btnDisplay!!.setOnClickListener 
        {
            val selectedId = radioGroup!!.checkedRadioButtonId
            radioButton = findViewById<View>(selectedId) as RadioButton
            calculateCalories(radioButton!!.text as String, age, weight, height)
        }
    }

    fun calculateCalories(radio: String, age: Int, weight: Double, height: Int) 
    {
        var age: Int
        val caloriesValue = findViewById<TextView>(R.id.caloriesValue)
        val ageValue = findViewById<View>(R.id.ageValue) as EditText
        val ageV = ageValue.text.toString()
        age = ageV.toInt()
        val calories: Double = if (radio == "Mężczyzna") 
        {
            66.47 + 13.7 * weight + 5.0 * height - 6.76 * age
        } 
        else 
        {
            655.1 + 9.567 * weight + 1.85 * height - 4.68 * age
        }
        caloriesValue.text = weightFormat.format(calories)
        showRecipe()
    }

    fun showRecipe() 
    {
        val recipeTextView = findViewById<TextView>(R.id.recipe)
        val intent = intent
        val bmi = intent.getStringExtra(MainActivity.BMI)
        val bmiValue = bmi!!.toDouble()
        if (bmiValue > 25.0) 
        {
            recipeTextView.text = Html.fromHtml(""""<h2 style=\"text-align:justify\">Przepis:</h2>\n" +" <p style=\"text-align:justify\">Filety z łososia posypujemy solą, pieprzem i posiekanym koperkiem. Skrapiamy sokiem z cytryny, obkładamy plastrami cytryny. Zawijamy dokładnie w folię aluminiową. Pieczemy w temperaturze 200 stopni przez około 20 minut.</p>""", Html.FROM_HTML_MODE_COMPACT)
        } 
        else 
        {
            recipeTextView.text = Html.fromHtml("""<h2 style=\"text-align:justify\">Przepis:</h2>\n" +" <p style=\"text-align:justify\">Skrzydełka myjemy, każde przesmarowujemy odrobiną miodu i posypujemy obficie przyprawą do skrzydełek. Marynujemy przynajmniej godzinę w lodówce.\n" +"Po przełożeniu do woreczka, wstawiamy je do piekarnika rozgrzanego do 180 st na 40-45 minut.\n" +"Na 10 minut przed końcem pieczenia, rozcinamy woreczek  i wysypujemy skrzydełka na naczynie żaroodporne, żeby skórka nieco się przypiekła.\n" +"Podane z ryżem i 'sosikiem' który wytworzył się podczas pieczenia.</p>""", Html.FROM_HTML_MODE_COMPACT)
        }
    }

    companion object 
    {
        private val weightFormat = NumberFormat.getNumberInstance()
        private val intFormat = NumberFormat.getIntegerInstance()
    }
}