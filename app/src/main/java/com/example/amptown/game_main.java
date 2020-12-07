package com.example.amptown;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class game_main extends AppCompatActivity {

    private long dayTime = 600000;
    private long dayTimeLeft = dayTime;
    private CountDownTimer dayTimer;

    private int minute = 0;
    private int hour = 0;
    private int day = 0;

    private TextView minuteView;
    private TextView hourView;
    private TextView dayView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_main);

        BottomNavigationView bottomNavigation =  findViewById(R.id.gameBotomMemu);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        NavigationUI.setupWithNavController(bottomNavigation,navHostFragment.getNavController());

        minuteView = findViewById(R.id.minute);
        hourView = findViewById(R.id.hour);
        dayView = findViewById(R.id.day);

        dayTimer = new CountDownTimer(dayTimeLeft, 4400) {
            @Override
            public void onTick(long millisUntilFinished) {
                dayTimeLeft = millisUntilFinished;
                minute+=10;
                if(minute>=60)
                {
                    minute=0;
                    hour++;
                }
                minuteView.setText("Minute: "+ minute);
                if(hour>=24)
                {
                    hour = 0;
                    day++;
                }
                hourView.setText("Hour: "+ hour);
                dayView.setText("Day: "+ day);
                Log.d("test", "m "+minute+" h " + hour + " d " + day);
            }

            @Override
            public void onFinish() {

                dayTimeLeft = dayTime;
                dayTimer.start();
            }
        }.start();
    }
}