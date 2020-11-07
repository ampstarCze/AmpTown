package com.example.amptown;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import java.io.Serializable;

public class MusicPlayer extends Service implements MediaPlayer.OnErrorListener, Serializable {

    private final IBinder mBinder = new ServiceBinder();
    private SharedPreferences myPref;
    MediaPlayer mPlayer;

    public MusicPlayer() {
    }

    public class ServiceBinder extends Binder {
        MusicPlayer getService() {
            return MusicPlayer.this;
        }
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        myPref = getSharedPreferences("AmpTown", Context.MODE_PRIVATE);
        mPlayer = MediaPlayer.create(this,R.raw.backgroundmusic);
        mPlayer.setOnErrorListener(this);
        float volume =  myPref.getInt("musicVolume",50) / 100f;
        if (mPlayer != null) {
            mPlayer.setLooping(true);
            mPlayer.setVolume(volume, volume);
        }

        mPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {

            public boolean onError(MediaPlayer mp, int what, int
                    extra) {

                onError(mPlayer, what, extra);
                return true;
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mPlayer != null) {
            boolean enabled = myPref.getBoolean("enableMusic",true);
            if(enabled) {
                mPlayer.start();
            }
        }
        return START_NOT_STICKY;
    }

    public void changeVolume(int volume)
    {
        float vol = volume / 100f;
        mPlayer.setVolume(vol,vol);
    }

    public void pauseMusic() {
        if (mPlayer != null) {
            if (mPlayer.isPlaying()) {
                mPlayer.pause();
            }
        }
    }

    public void resumeMusic() {
        if (mPlayer != null) {
            if (!mPlayer.isPlaying()) {
                mPlayer.start();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPlayer != null) {
            try {
                mPlayer.stop();
                mPlayer.release();
            } finally {
                mPlayer = null;
            }
        }
    }

    public boolean onError(MediaPlayer mp, int what, int extra) {

        Toast.makeText(this, "Music player failed", Toast.LENGTH_SHORT).show();
        if (mPlayer != null) {
            try {
                mPlayer.stop();
                mPlayer.release();
            } finally {
                mPlayer = null;
            }
        }
        return false;
    }

}
