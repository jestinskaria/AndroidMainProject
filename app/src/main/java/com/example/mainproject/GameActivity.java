package com.example.mainproject;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.widget.Chronometer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.Group;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    private int type = 1; //0 means 6 x 6 and 1 = 4 x 4
    private int timer = 1;
    private int rangeStart = 100;
    private int rangeEnd = 1000;

    private int initialDelay = 10000;
    private int lookupDelay = 3000;

    private Map<Integer, PositionData> positionMap = new HashMap();
    private Map<Integer, Integer> positionMemory = new HashMap();
    private List<Integer> found = new ArrayList<>();

    private AppCompatButton
            btn00, btn01, btn02, btn03, btn04, btn05,
            btn10, btn11, btn12, btn13, btn14, btn15,
            btn20, btn21, btn22, btn23, btn24, btn25,
            btn30, btn31, btn32, btn33, btn34, btn35,
            btn40, btn41, btn42, btn43, btn44, btn45,
            btn50, btn51, btn52, btn53, btn54, btn55;

    //Configuration buttons
    private AppCompatButton btnStart,
            btnEnd,
            btnSettings,
            btnExit;

    //row 1x and 4x column x1 and x4 is grouped
    private Group groupLeft;

    private AppCompatButton[] buttons;
    private Chronometer cmTimer;

    private int searchingPosition = -1;
    private int currentPosition = -1;
    private int errorPosition = -1;
    private boolean isStarted = false;
    private boolean isShowingError = false;

    private Timer lookupTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        initialize();
    }

    private void initialize() {
        initializeUi();
        initializeListeners();

        setUpUI();

        setButtonVisibility(false);
        btnEnd.setEnabled(false);
    }

    private void initializeUi() {
        cmTimer = findViewById(R.id.tvTimer);

        groupLeft = findViewById(R.id.grpLeft);

        btnStart = findViewById(R.id.btnStart);
        btnEnd = findViewById(R.id.btnEnd);
        btnSettings = findViewById(R.id.btnSettings);
        btnExit = findViewById(R.id.btnExit);

        btn00 = findViewById(R.id.btn00);
        btn01 = findViewById(R.id.btn01);
        btn02 = findViewById(R.id.btn02);
        btn03 = findViewById(R.id.btn03);
        btn04 = findViewById(R.id.btn04);
        btn05 = findViewById(R.id.btn05);
        btn10 = findViewById(R.id.btn10);
        btn11 = findViewById(R.id.btn11);
        btn12 = findViewById(R.id.btn12);
        btn13 = findViewById(R.id.btn13);
        btn14 = findViewById(R.id.btn14);
        btn15 = findViewById(R.id.btn15);
        btn20 = findViewById(R.id.btn20);
        btn21 = findViewById(R.id.btn21);
        btn22 = findViewById(R.id.btn22);
        btn23 = findViewById(R.id.btn23);
        btn24 = findViewById(R.id.btn24);
        btn25 = findViewById(R.id.btn25);
        btn30 = findViewById(R.id.btn30);
        btn31 = findViewById(R.id.btn31);
        btn32 = findViewById(R.id.btn32);
        btn33 = findViewById(R.id.btn33);
        btn34 = findViewById(R.id.btn34);
        btn35 = findViewById(R.id.btn35);
        btn40 = findViewById(R.id.btn40);
        btn41 = findViewById(R.id.btn41);
        btn42 = findViewById(R.id.btn42);
        btn43 = findViewById(R.id.btn43);
        btn44 = findViewById(R.id.btn44);
        btn45 = findViewById(R.id.btn45);
        btn50 = findViewById(R.id.btn50);
        btn51 = findViewById(R.id.btn51);
        btn52 = findViewById(R.id.btn52);
        btn53 = findViewById(R.id.btn53);
        btn54 = findViewById(R.id.btn54);
        btn55 = findViewById(R.id.btn55);


        buttons = new AppCompatButton[]{
                btn00, btn01, btn02, btn03, btn04, btn05,
                btn10, btn11, btn12, btn13, btn14, btn15,
                btn20, btn21, btn22, btn23, btn24, btn25,
                btn30, btn31, btn32, btn33, btn34, btn35,
                btn40, btn41, btn42, btn43, btn44, btn45,
                btn50, btn51, btn52, btn53, btn54, btn55};

        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setTag(i);
        }
    }


    private void initializeListeners() {
      if(timer !=0)
        cmTimer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {

            }
        });
        btnStart.setOnClickListener(this);
        btnExit.setOnClickListener(this);
        btnEnd.setOnClickListener(this);
        btnSettings.setOnClickListener(this);

        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setOnClickListener(this);
        }
    }

    private void setUpUI() {
        groupLeft.setVisibility(type == 1 ? View.GONE : View.VISIBLE);
        Random rand = new Random();

        for (int i = 0; i < buttons.length; i++) {

            int randomPartner = buttons.length - 1 - i;
            if (positionMap.containsKey(randomPartner)) {
                PositionData data1 = positionMap.get(randomPartner);

                PositionData data = new PositionData();
                data.position = i;
                data.value = data1.value;
                data.associationPosition = randomPartner;

                positionMap.put(i, data);
                continue;
            }
            if (buttons[i].getVisibility() == View.VISIBLE) {
                int randomValue = rangeStart + rand.nextInt(rangeEnd - rangeStart); //finding range from random function
                buttons[i].setText(String.format("%d", randomValue));
                buttons[randomPartner].setText(String.format("%d", randomValue));

                PositionData data = new PositionData();
                data.position = i;
                data.value = randomValue;
                data.associationPosition = randomPartner;

                positionMap.put(i, data);
            }
        }
    }

    private void setButtonVisibility(boolean isVisible) {
        if (!isVisible)
            positionMemory.clear();

        for (int i = 0; i < buttons.length; i++) {
            if (found.contains(i))
                continue;
            if (!isVisible) {
                String value = (String) buttons[i].getText();
                if (!TextUtils.isEmpty(value)) {
                    positionMemory.put(i, Integer.parseInt(value));
                    buttons[i].setText("");
                }
            } else {
                Integer value = positionMemory.get(i);
                if (null != value) {
                    buttons[i].setText(String.format("%d", value));
                }
            }
        }
    }

    private void startTimerForLookup() {
        if ((searchingPosition < 0 && searchingPosition > buttons.length - 1)
                || (currentPosition < 0 && currentPosition > buttons.length - 1)) {
            searchingPosition = -1;
            currentPosition = -1;
            errorPosition = -1;
            return;
        }

        int val1 = positionMemory.get(errorPosition);
        int val2 = positionMemory.get(currentPosition);

        buttons[errorPosition].setText(String.format("%d", val1));
        buttons[currentPosition].setText(String.format("%d", val2));

        buttons[errorPosition].setBackgroundTintList(ColorStateList.valueOf(Color.RED));
        buttons[currentPosition].setBackgroundTintList(ColorStateList.valueOf(Color.RED));

        isShowingError = true;
        if (null != lookupTimer) {
            lookupTimer.cancel();
            lookupTimer = null;
        }

        lookupTimer = new Timer();
        lookupTimer.schedule(new TimerTask() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        isShowingError = false;
                        if ((searchingPosition < 0 && searchingPosition > buttons.length - 1)
                                || (currentPosition < 0 && currentPosition > buttons.length - 1)) {
                            searchingPosition = -1;
                            currentPosition = -1;
                            errorPosition = -1;
                            return;
                        }
                        buttons[errorPosition].setText("");
                        buttons[currentPosition].setText("");

                        buttons[errorPosition].setBackgroundTintList(ColorStateList.valueOf(Color.LTGRAY));
                        buttons[currentPosition].setBackgroundTintList(ColorStateList.valueOf(Color.LTGRAY));

                        searchingPosition = -1;
                        currentPosition = -1;
                        errorPosition = -1;
                    }
                });
            }
        }, lookupDelay);
    }

    private void startTimerForInitialVisibility() {
        setButtonVisibility(true);

        Timer times = new Timer();
        times.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        isStarted = true;
                        setButtonVisibility(false);
                        notifyStarted();
                        cmTimer.setBase(SystemClock.elapsedRealtime());
                        cmTimer.start();
                    }
                });
            }
        }, initialDelay);
    }

    private void checkForAssociatedItem(View view) {

        if (searchingPosition == -1 && view instanceof AppCompatButton) {
            errorPosition = (int) view.getTag();
            PositionData data = positionMap.get(view.getTag());
            if (data != null) {
                ((AppCompatButton) view).setText("" + data.value);
                searchingPosition = data.associationPosition;
                found.add(data.position);
            }
            return;
        }

        if (searchingPosition == (Integer) view.getTag()) {

            PositionData data = positionMap.get((view.getTag()));
            if (data != null) {
                ((AppCompatButton) view).setText("" + data.value);
                buttons[data.position].setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                buttons[data.associationPosition].setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));

                found.add(data.position);
                if (type == 1 && found.size() >= 16) {
                    startResultActivity(1);
                } else if (type == 0 && found.size() >= 36) {
                    startResultActivity(1);
                }
            }
            searchingPosition = -1;
            errorPosition = -1;
            currentPosition = -1;
        } else {
            found.remove(found.size() - 1);
            currentPosition = (Integer) view.getTag();
            startTimerForLookup();
        }
    }

    private void startResultActivity(int i) {
        cmTimer.stop();

        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("time", cmTimer.getText());
        intent.putExtra("success", i);
        intent.putExtra("count", found.size());

        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (101 == requestCode) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                //save data
                type = data.getIntExtra("type", 0);

                rangeStart = data.getIntExtra("rangeStart", 0);
                rangeEnd = data.getIntExtra("rangeEnd", 100);
                timer = data.getIntExtra("timer", 1);
                initialDelay = data.getIntExtra("initialDelay", 10000);

                initialize();
            }
        }
    }

    private void notifyStarted() {
        btnStart.setEnabled(!isStarted);
        btnSettings.setEnabled(!isStarted);
        btnEnd.setEnabled(isStarted);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == btnStart.getId()) {
            //start 10 sec timer
            searchingPosition = -1;
            found.clear();
            positionMemory.clear();
            positionMap.clear();

            setUpUI();
            startTimerForInitialVisibility();

        } else if (v.getId() == btnEnd.getId()) {
            // stop game and assign random
            startResultActivity(0);
        } else if (v.getId() == btnSettings.getId()) {
            Intent intent = new Intent(this, SettingsActivity.class);
            intent.putExtra("type", type);
            intent.putExtra("timer", timer);
            intent.putExtra("rangeStart", rangeStart);
            intent.putExtra("rangeEnd", rangeEnd);
            intent.putExtra("initialDelay", initialDelay);

            startActivityForResult(intent, 101);
        } else if (v.getId() == btnExit.getId()) {
            // stop game and assign random
            finish();
        } else {
            if (!isStarted || isShowingError) return;
            //check for success
            checkForAssociatedItem(v);
        }
    }
}
