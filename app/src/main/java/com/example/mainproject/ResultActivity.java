package com.example.mainproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

public class ResultActivity extends AppCompatActivity implements View.OnClickListener {

    AppCompatTextView tvResult, tvTime, tvCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        int success = getIntent().getIntExtra("success", 0);
        int count = getIntent().getIntExtra("count", 0);
        String time = getIntent().getStringExtra("time");

        tvResult = findViewById(R.id.tvResult);
        tvTime = findViewById(R.id.tvTime);
        tvCount = findViewById(R.id.tvCount);

        tvResult.setText(success == 1 ? "Success" : "Failed");
        tvTime.setText("ElapsedTime: " + time);
        tvCount.setText("Count: " + count);

        findViewById(R.id.btnExit).setOnClickListener(this);
        findViewById(R.id.btnNewGame).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnNewGame) {
            startActivity(new Intent(this, GameActivity.class));
            finish();
        } else {
            finish();
        }
    }
}
