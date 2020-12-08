package com.example.amptown;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class game_main extends AppCompatActivity {

    private SharedPreferences preferences;
    private SharedPreferences.Editor preferencesEditor;

    private long dayTime = 600000;
    private long dayTimeLeft = dayTime;
    private CountDownTimer dayTimer;
    private database db;

    public MusicPlayer mServ;
    private boolean mIsBound = false;
    HomeWatcher homeWatcher;

    private int minute = 0;
    private int hour = 0;
    private int day = 0;

    private TextView minuteView;
    private TextView hourView;
    private TextView dayView;

    public int ID;
    private boolean loadGame;

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

        preferences = getSharedPreferences("AmpTown", Context.MODE_PRIVATE);
        preferencesEditor = preferences.edit();

         ID = preferences.getInt("slotID",1);
         loadGame = preferences.getBoolean("loadGame",false);
         db = new database(this);

        doBindService();
        Intent music = new Intent();
        music.setClass(this, MusicPlayer.class);
        startService(music);

        homeWatcher = new HomeWatcher(this);
        homeWatcher.setOnHomePressedListener(new HomeWatcher.OnHomePressedListener() {
            @Override
            public void onHomePressed() {
                if (mServ != null) {
                    mServ.pauseMusic();
                }
            }
            @Override
            public void onHomeLongPressed() {
                if (mServ != null) {
                    mServ.pauseMusic();
                }
            }
        });
        homeWatcher.startWatch();

        if(!loadGame)
        {
            db.newSave(ID);
        }
        else
        {
            loadGame();
        }

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
                db.update(ID,"minute",minute);
                db.update(ID,"dayTimeLeft", dayTimeLeft);
                minuteView.setText("Minute: "+ minute);
                if(hour>=24)
                {
                    hour = 0;
                    day++;
                }
                db.update(ID,"hour",hour);
                db.update(ID,"day",day);
                hourView.setText("Hour: "+ hour);
                dayView.setText("Day: "+ day);
             //ToDo Remove
                Log.d("test", "m "+minute+" h " + hour + " d " + day);
            }

            @Override
            public void onFinish() {

                dayTimeLeft = dayTime;
                dayTimer.start();
            }
        }.start();
    }

    private void loadGame() {
        Cursor cursor = db.getData(ID);
        cursor.moveToFirst();

        day = cursor.getInt(cursor.getColumnIndex("day"));
        hour = cursor.getInt(cursor.getColumnIndex("hour"));
        minute = cursor.getInt(cursor.getColumnIndex("minute"));
        dayTimeLeft = cursor.getInt(cursor.getColumnIndex("dayTimeLeft"));

        minuteView.setText("Minute: "+ minute);
        hourView.setText("Hour: "+ hour);
        dayView.setText("Day: "+ day);

        if(!cursor.isClosed())
        {
            cursor.close();
        }
    }

    @Override
    public void onBackPressed() {
        Intent mainMenu = new Intent(getApplicationContext(), MainMenu.class);
        startActivity(mainMenu);
        dayTimer.cancel();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mServ != null) {
            mServ.resumeMusic();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        PowerManager pm = (PowerManager)
                getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = false;
        if (pm != null) {
            isScreenOn = pm.isInteractive();
        }

        if (!isScreenOn) {
            if (mServ != null) {
                mServ.pauseMusic();
            }
        }
    }

    private ServiceConnection Scon =new ServiceConnection(){

        public void onServiceConnected(ComponentName name, IBinder
                binder) {
            mServ = ((MusicPlayer.ServiceBinder)binder).getService();
        }

        public void onServiceDisconnected(ComponentName name) {
            mServ = null;
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        doUnbindService();
        Intent music = new Intent();
        music.setClass(this,MusicPlayer.class);
        homeWatcher.stopWatch();
        stopService(music);
    }

    void doBindService(){
        bindService(new Intent(this,MusicPlayer.class),
                Scon, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    void doUnbindService()
    {
        if(mIsBound)
        {
            unbindService(Scon);
            mIsBound = false;
        }
    }

}