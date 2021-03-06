package com.kalkulatorbmi;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.NumberFormat;
public class CheckCalories extends AppCompatActivity {
    private static final NumberFormat weightFormat = NumberFormat.getNumberInstance();
    private static final NumberFormat intFormat = NumberFormat.getIntegerInstance();
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private Button btnDisplay;
    private int age = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_calories);
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.BMI);
        TextView bmiValue = findViewById(R.id.bmiValue);
        bmiValue.setText(message);
        TextView caloriesValue = findViewById(R.id.caloriesValue);
        Bundle bundle = getIntent().getExtras();
        int height = bundle.getInt(MainActivity.HEIGHT);
        double weight = bundle.getDouble(MainActivity.WEIGHT);
        addListenerOnButton(height, weight, age);
    }

    public void addListenerOnButton(int height, double weight, int age) {
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        btnDisplay = (Button) findViewById(R.id.foodOfferButton);
        btnDisplay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                radioButton = (RadioButton) findViewById(selectedId);
                calculateKcal((String) radioButton.getText(), age, weight, height);
            }
        });
    }

    public void calculateKcal(String radio, int age, double weight, int height) {
        TextView kcalValue = findViewById(R.id.caloriesValue);
        double kcal;
        EditText ageValue =
                (EditText) findViewById(R.id.ageValue);
        String ageValueInString = ageValue.getText().toString();
        age = Integer.parseInt(ageValueInString);
        if (radio.equals("M????czyzna")) {
            kcal = (66.47 + (13.7 * weight) + (5.0 * height) - (6.76 * age));
        } else {
            kcal = (655.1 + (9.567 * weight) + (1.85 * height) - (4.68 * age));
        }
        kcalValue.setText(weightFormat.format(kcal));
        showRecipe();

    }

    public void showRecipe() {
        TextView recipeTextView = findViewById(R.id.recipe);
        Intent intent = getIntent();
        String bmi = intent.getStringExtra(MainActivity.BMI);
        double bmiValue = Double.parseDouble(bmi);
        if (25.0 < bmiValue) {
            recipeTextView.setText(Html.fromHtml("<h2 style=\"text-align:justify\">Przepis:</h2>\n" +
                    " <p style=\"text-align:justify\">Filety z ??ososia posypujemy sol??, pieprzem i posiekanym koperkiem. Skrapiamy sokiem z cytryny, obk??adamy plastrami cytryny. Zawijamy dok??adnie w foli?? aluminiow??. Pieczemy w temperaturze 200 stopni przez oko??o 20 minut.</p>", Html.FROM_HTML_MODE_COMPACT));
        } else {
            recipeTextView.setText(Html.fromHtml("<h2 style=\"text-align:justify\">Przepis:</h2>\n" +
                    " <p style=\"text-align:justify\">Skrzyde??ka myjemy, ka??de przesmarowujemy odrobin?? miodu i posypujemy obficie przypraw?? do skrzyde??ek. Marynujemy przynajmniej godzin?? w lod??wce.\n" +
                    "Po prze??o??eniu do woreczka, wstawiamy je do piekarnika rozgrzanego do 180 st na 40-45 minut.\n" +
                    "Na 10 minut przed ko??cem pieczenia, rozcinamy woreczek  i wysypujemy skrzyde??ka na naczynie ??aroodporne, ??eby sk??rka nieco si?? przypiek??a.\n" +
                    "Podane z ry??em i 'sosikiem' kt??ry wytworzy?? si?? podczas pieczenia.</p>", Html.FROM_HTML_MODE_COMPACT));
        }
    }
}