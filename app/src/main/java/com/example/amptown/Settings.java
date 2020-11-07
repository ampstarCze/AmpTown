package com.example.amptown;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

public class Settings extends AppCompatActivity {

    private SharedPreferences preferences;
    private SharedPreferences.Editor preferencesEditor;
    public MusicPlayer mServ;
    private boolean mIsBound = false;
    HomeWatcher homeWatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        preferences = getSharedPreferences("AmpTown", Context.MODE_PRIVATE);
        preferencesEditor = preferences.edit();
        final SeekBar volumeSeek = findViewById(R.id.volumeScroll);
        final TextView volumeText= findViewById(R.id.volumeText);
        final Switch enableMusic = findViewById(R.id.musicEnable);

        int volume = preferences.getInt("musicVolume",50);
        boolean enabled = preferences.getBoolean("enableMusic",true);

        volumeSeek.setProgress(volume);
        enableMusic.setChecked(enabled);

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
        enableMusic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    mServ.resumeMusic();
                    preferencesEditor.putBoolean("enableMusic",true);
                }
                else
                {
                    mServ.pauseMusic();
                    preferencesEditor.putBoolean("enableMusic",false);
                }
                preferencesEditor.apply();
            }
        });
        volumeText.setText(""+volumeSeek.getProgress()+"%");
        volumeSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                volumeText.setText(""+progress+"%");
                preferencesEditor.putInt("musicVolume",progress);
                preferencesEditor.commit();
                mServ.changeVolume(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent mainMenu = new Intent(getApplicationContext(), MainMenu.class);
        startActivity(mainMenu);
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