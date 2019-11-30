package com.example.mainproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.AppCompatSpinner;

import java.util.ArrayList;
import java.util.List;


public class SettingsActivity extends AppCompatActivity implements OnItemSelectedListener, CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private AppCompatSpinner spWait;
    private AppCompatEditText etRangeStart, etRangeEnd;
    private RadioGroup rgSize, rgTimer;
    private AppCompatRadioButton rbOn, rbOff, rb4, rb6;

    private int type = 0; //0 means 6 x 6 and 1 = 4 x 4  so default 6 x 6
    private int timer = 0;
    private int rangeStart = 0;
    private int rangeEnd = 100;

    private int initialDelay = 10000;
    private int lookupDelay = 3000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        type = getIntent().getIntExtra("type", 0);

        rangeStart = getIntent().getIntExtra("rangeStart", 0);
        rangeEnd = getIntent().getIntExtra("rangeEnd", 100);

        initializeText();
        initializeSpinner();
        initializeSize();
        initializeTimer();

        findViewById(R.id.btnBack).setOnClickListener(this);
        findViewById(R.id.btnExit).setOnClickListener(this);
    }

    private void registerRadioButton() {

    }

    private void initializeText() {
        etRangeStart = findViewById(R.id.etRangeStart);
        etRangeEnd = findViewById(R.id.etRangeEnd);

        etRangeStart.setText(String.format("%d", rangeStart));
        etRangeEnd.setText(String.format("%d", rangeEnd));
    }

    private void initializeSize() {
        rb4 = findViewById(R.id.rb4);
        rb6 = findViewById(R.id.rb6);

        rb4.setOnCheckedChangeListener(this);
        rb6.setOnCheckedChangeListener(this);

        rb4.setChecked(type == 1);
        rb6.setChecked(type == 1);
    }

    private void initializeTimer() {
        rbOn = findViewById(R.id.rbOn);
        rbOff = findViewById(R.id.rbOff);

        rbOff.setOnCheckedChangeListener(this);
        rbOn.setOnCheckedChangeListener(this);

        rbOff.setChecked(timer == 1);
        rbOn.setChecked(timer == 1);
    }

    private void initializeSpinner() {
        // Spinner element
        spWait = findViewById(R.id.spWait);

        // Spinner click listener
        spWait.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("5 seconds");
        categories.add("10 seconds");
        categories.add("15 seconds");
        categories.add("20 seconds");
        categories.add("25 seconds");
        categories.add("30 seconds");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching value adapter to spinner
        spWait.setAdapter(dataAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        initialDelay = (position + 1) * 5;
        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnBack) {
            AppCompatEditText etStart = findViewById(R.id.etRangeStart);
            AppCompatEditText etEnd = findViewById(R.id.etRangeEnd);

            rangeStart = Integer.parseInt(etStart.getText().toString());
            rangeEnd = Integer.parseInt(etEnd.getText().toString());

            type = rb4.isChecked() ? 1 : 0;
            timer = rbOn.isChecked() ? 1 : 0;

            Intent intent = new Intent();
            intent.putExtra("type", type);
            intent.putExtra("timer", timer);
            intent.putExtra("rangeStart", rangeStart);
            intent.putExtra("rangeEnd", rangeEnd);
            intent.putExtra("initialDelay", initialDelay);

            setResult(Activity.RESULT_OK, intent);
            finish();
        } else {
            finish();
        }
    }
}
