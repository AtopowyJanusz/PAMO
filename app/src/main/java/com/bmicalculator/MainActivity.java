package com.kalkulatorbmi;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.NumberFormat;
public class MainActivity extends AppCompatActivity {
    public static final String BMI = "com.app.CheckCaloriesBMI";
    public static final String WEIGHT = "com.app.CheckCaloriesWEIGHT";
    public static final String HEIGHT = "com.app.CheckCaloriesHEIGHT";
    private static final NumberFormat weightFormat = NumberFormat.getNumberInstance();
    private static final NumberFormat intFormat = NumberFormat.getIntegerInstance();
    private double weightValue = 0.0;
    private int heightValue = 1;
    private double bmi = 0;
    private TextView weightText;
    private TextView bmiValueText;
    private final TextWatcher weightTextWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            try {
                weightValue = Double.parseDouble(s.toString());
            } catch (NumberFormatException e) {
                weightValue = 0.0;
            }
            calculate();
        }
        @Override
        public void afterTextChanged(Editable s) {
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
    };
    private TextView heightText;
    private final SeekBar.OnSeekBarChangeListener heightBarListener =
            new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    heightValue = progress;
                    heightText.setText(intFormat.format(heightValue));
                    calculate();
                }
                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }
                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        weightText = (TextView) findViewById(R.id.weightTextView);
        weightText.setText("0.0");
        heightText = (TextView) findViewById(R.id.heightTextView);
        heightText.setText("1");
        bmiValueText = (TextView) findViewById(R.id.bmiValueTextView);
        bmiValueText.setText("0");
        EditText weightText =
                (EditText) findViewById(R.id.weightTextView);
        weightText.addTextChangedListener(weightTextWatcher);
        SeekBar heightSeekBar =
                (SeekBar) findViewById(R.id.heightSeekBar);
        heightSeekBar.setOnSeekBarChangeListener(heightBarListener);
    }

    private void calculate() {
        double bmiValue = weightValue / ((heightValue * heightValue) * 0.0001);
        bmiValue = bmiValue * 100;
        bmiValue = Math.round(bmiValue);
        bmiValue = bmiValue / 100;
        bmiValueText.setText(weightFormat.format(bmiValue));
    }

    private void switchActivities() {
        Intent switchActivityIntent = new Intent(this, CheckCalories.class);
        startActivity(switchActivityIntent);
    }

    public void sendMessage(View view) {
        Intent intent = new Intent(this, CheckCalories.class);
        TextView textView = (TextView) findViewById(R.id.bmiValueTextView);
        String message = textView.getText().toString();
        Bundle bundle = new Bundle();
        bundle.putString(BMI, message);
        bundle.putInt(HEIGHT, heightValue);
        bundle.putDouble(WEIGHT, weightValue);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}