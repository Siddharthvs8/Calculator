package com.example.calculatorapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView display;
    private double firstValue = 0;
    private double secondValue = 0;
    private String operator = "";
    private boolean isNewOp = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        display = findViewById(R.id.display);
        if (display == null) {
            throw new RuntimeException("Display TextView is not found. Check activity_main.xml for the correct ID.");
        }

        // Initialize button listeners
        setNumberClickListeners();
        setOperatorClickListeners();
    }

    private void setNumberClickListeners() {
        int[] numberButtonIds = {
                R.id.btn_0, R.id.btn_1, R.id.btn_2, R.id.btn_3,
                R.id.btn_4, R.id.btn_5, R.id.btn_6, R.id.btn_7,
                R.id.btn_8, R.id.btn_9, R.id.btn_decimal
        };

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button button = (Button) v;
                String buttonText = button.getText().toString();

                if (isNewOp) {
                    display.setText(buttonText.equals(".") ? "0." : buttonText);
                    isNewOp = false;
                } else {
                    display.append(buttonText);
                }
            }
        };

        for (int id : numberButtonIds) {
            Button button = findViewById(id);
            if (button != null) {
                button.setOnClickListener(listener);
            }
        }
    }

    private void setOperatorClickListeners() {
        Button clearButton = findViewById(R.id.btn_clear);
        Button plusMinusButton = findViewById(R.id.btn_plusMinus);
        Button percentButton = findViewById(R.id.btn_percent);
        Button addButton = findViewById(R.id.btn_add);
        Button subtractButton = findViewById(R.id.btn_subtract);
        Button multiplyButton = findViewById(R.id.btn_multiply);
        Button divideButton = findViewById(R.id.btn_divide);
        Button equalsButton = findViewById(R.id.btn_equals);

        if (clearButton != null) {
            clearButton.setOnClickListener(v -> {
                display.setText("0");
                firstValue = 0;
                secondValue = 0;
                operator = "";
                isNewOp = true;
            });
        }

        if (plusMinusButton != null) {
            plusMinusButton.setOnClickListener(v -> {
                String value = display.getText().toString();
                if (!value.equals("0")) {
                    if (value.startsWith("-")) {
                        display.setText(value.substring(1));
                    } else {
                        display.setText("-" + value);
                    }
                }
            });
        }

        if (percentButton != null) {
            percentButton.setOnClickListener(v -> {
                double value = Double.parseDouble(display.getText().toString()) / 100;
                display.setText(String.valueOf(value));
                isNewOp = true;
            });
        }

        if (addButton != null) addButton.setOnClickListener(v -> setOperator("+"));
        if (subtractButton != null) subtractButton.setOnClickListener(v -> setOperator("-"));
        if (multiplyButton != null) multiplyButton.setOnClickListener(v -> setOperator("×"));
        if (divideButton != null) divideButton.setOnClickListener(v -> setOperator("÷"));
        if (equalsButton != null) equalsButton.setOnClickListener(v -> calculateResult());
    }

    private void setOperator(String op) {
        try {
            firstValue = Double.parseDouble(display.getText().toString());
            operator = op;
            isNewOp = true;
        } catch (NumberFormatException e) {
            display.setText("Error");
        }
    }

    private void calculateResult() {
        if (operator.isEmpty()) return;

        try {
            secondValue = Double.parseDouble(display.getText().toString());
            double result = 0;

            switch (operator) {
                case "+":
                    result = firstValue + secondValue;
                    break;
                case "-":
                    result = firstValue - secondValue;
                    break;
                case "×":
                    result = firstValue * secondValue;
                    break;
                case "÷":
                    if (secondValue != 0) {
                        result = firstValue / secondValue;
                    } else {
                        display.setText("Error");
                        operator = "";
                        isNewOp = true;
                        return;
                    }
                    break;
            }

            display.setText(String.valueOf(result));
            operator = "";
            isNewOp = true;
        } catch (NumberFormatException e) {
            display.setText("Error");
        }
    }
}
