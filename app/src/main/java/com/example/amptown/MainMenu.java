package com.example.amptown;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.view.View;

public class MainMenu extends AppCompatActivity {

    HomeWatcher homeWatcher;
    private boolean mIsBound = false;
    private SharedPreferences preferences;
    public MusicPlayer mServ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmenu);

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        doUnbindService();
        Intent music = new Intent();
        music.setClass(this,MusicPlayer.class);
        homeWatcher.stopWatch();
        stopService(music);
    }


    public void MainMenuExit(View view) {
        finish();
        System.exit(0);
    }

    public void mainMenuSettings(View view) {
        Intent settings = new Intent(getApplicationContext(), Settings.class);
        startActivity(settings);
        finish();
    }

    public void mainMenuLoadGame(View view) {
        Intent loadGame = new Intent(getApplicationContext(), game_battle.class);
        startActivity(loadGame);
        finish();
    }

    public void mainMenuNewGame(View view) {
        Intent newGame = new Intent(getApplicationContext(), game_main.class);
        startActivity(newGame);
        finish();
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