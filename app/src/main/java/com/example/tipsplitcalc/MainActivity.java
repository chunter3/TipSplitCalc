package com.example.tipsplitcalc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    private EditText inputtedBillAmnt;
    private EditText numOfPeople;
    RadioGroup selectTipPercent;
    RadioButton tipPercent;
    TextView displayTipAmnt;
    TextView displayTotalWithTip;
    TextView displayTotalPerPerson;
    private static final DecimalFormat df = new DecimalFormat( "#.00" ); // helps achieve appropriate monetary format
    private static final String TAG = "MainActivity";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: Successfully created!");
        inputtedBillAmnt = findViewById(R.id.DecimalBillTotal);
        numOfPeople = findViewById(R.id.IntNumOfPeople);
        displayTipAmnt = findViewById(R.id.DecimalTipAmount);
        displayTotalWithTip = findViewById(R.id.DecimalTotalWithTip);
        displayTotalPerPerson = findViewById(R.id.DecimalTotalPerPerson);
    }

    public double calcTip(View v) { // calculates the tip
        // Locate the tip percentage that was selected (lines 42-45)
        selectTipPercent = findViewById(R.id.SelectTipPercent);
        int selectedTipPercent = selectTipPercent.getCheckedRadioButtonId();
        tipPercent = findViewById(selectedTipPercent);

        String billAmntStr = inputtedBillAmnt.getText().toString();
        String tipPercentStr = tipPercent.getTag().toString();
        double billAmntDbl = Double.parseDouble(billAmntStr);
        double tipPercentDbl = Double.parseDouble(tipPercentStr);
         /* BigDecimal
         - Ensures decimal values are rounded to 2 decimal places
         - Is better for monetary calculation
         */
        if ((BigDecimal.valueOf(billAmntDbl).scale() > 2) || (billAmntDbl < 0)) {  // Checks whether the inputted bill amount is greater than two decimal places or negative
            return -1;                                                            // if so, then return -1
        }
        BigDecimal tipAmnt = new BigDecimal(billAmntDbl * tipPercentDbl).setScale(2, RoundingMode.UP);
        return tipAmnt.doubleValue();
    }

    public double calcTotalWithTip(View v) { // calculates the total w/ the tip included
        double tipAmnt = calcTip(v);
        if (tipAmnt == -1) {
            return -1;
        }
        String billAmntStr = inputtedBillAmnt.getText().toString();
        double billAmntDbl = Double.parseDouble(billAmntStr);

        BigDecimal totalWithTip = new BigDecimal(billAmntDbl + tipAmnt);
        return totalWithTip.doubleValue();
    }

    public double calcTotalPerPerson(View v) { // calculates the total per person
        if (numOfPeople.getText().length() == 0){
            return -1;
        }
        String numOfPeopleStr = numOfPeople.getText().toString();
        int numOfPeopleInt = Integer.parseInt(numOfPeopleStr);
        double totalWithTip = calcTotalWithTip(v);
        if ((totalWithTip == -1) || (numOfPeopleInt < 1)) {
            return -1;
        }
        BigDecimal totalPerPerson = new BigDecimal(totalWithTip / numOfPeopleInt).setScale(2, RoundingMode.UP);
        return totalPerPerson.doubleValue();
    }

    public void displayTipAndTotal(View v) { // displays tip and total amounts
        inputtedBillAmnt = findViewById(R.id.DecimalBillTotal);
        if (inputtedBillAmnt.getText().length() == 0) {
            selectTipPercent = findViewById(R.id.SelectTipPercent);
            selectTipPercent.clearCheck();
            return;
        }
        double tipAmnt = calcTip(v);
        if (tipAmnt == -1) {
            return;
        }
        double totalWithTip = calcTotalWithTip(v);
        Log.d(TAG, "Tip Amount: " + tipAmnt);
        Log.d(TAG, "Total With Tip: "  + totalWithTip);

        displayTipAmnt.setText(String.format("$" + "%s", df.format(tipAmnt)));
        displayTotalWithTip.setText(String.format("$" + "%s", df.format(totalWithTip)));
    }

    public void displayTotalPerPerson(View v) { // displays the total per person amount
        inputtedBillAmnt = findViewById(R.id.DecimalBillTotal);
        selectTipPercent = findViewById(R.id.SelectTipPercent);
        if ((inputtedBillAmnt.getText().length() == 0)) {
            return;
        }
        String billAmntStr = inputtedBillAmnt.getText().toString();
        double billAmntDbl = Double.parseDouble(billAmntStr);
        if ((BigDecimal.valueOf(billAmntDbl).scale() > 2) || (selectTipPercent.getCheckedRadioButtonId() == -1)) {
            return;
        }
        double totalPerPerson = calcTotalPerPerson(v);
        if (totalPerPerson == -1)  {
            return;
        }
        Log.d(TAG, "Total Per Person: " + totalPerPerson);

        displayTotalPerPerson.setText(String.format("$" + "%s", df.format(totalPerPerson)));
    }

    public void clear(View v) {
        inputtedBillAmnt.getText().clear();
        numOfPeople.getText().clear();

        selectTipPercent = findViewById(R.id.SelectTipPercent);
        selectTipPercent.clearCheck();

        displayTipAmnt.setText("");
        displayTotalWithTip.setText("");
        displayTotalPerPerson.setText("");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: Start application");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: Resume application");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: Application paused");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: Application stopped");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: Application terminated");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: Application restarted");
    }
}