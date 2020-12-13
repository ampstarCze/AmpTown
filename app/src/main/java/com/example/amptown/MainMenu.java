package com.example.amptown;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainMenu extends AppCompatActivity {

    HomeWatcher homeWatcher;
    private boolean mIsBound = false;
    public MusicPlayer mServ;
    private database db;
    private ListView saveList;
    boolean canLoad[] = new boolean[] {false, false, false};

    private SharedPreferences preferences;
    private SharedPreferences.Editor preferencesEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmenu);
        db = new database(this);

        preferences = getSharedPreferences("AmpTown", Context.MODE_PRIVATE);
        preferencesEditor = preferences.edit();
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
        saveList = findViewById(R.id.saveList);
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

    @Override
    public void onBackPressed() {
       if(saveList.getVisibility() == View.VISIBLE){
           saveList.setVisibility(View.GONE);
       }
       else
       {
           finish();
       }

    }

    public void MainMenuExit(View view) {
        finishAffinity();
    }

    public void mainMenuSettings(View view) {
        Intent settings = new Intent(getApplicationContext(), Settings.class);
        startActivity(settings);
        finish();
    }

    public void mainMenuLoadGame(View view) {
        String saveTime;
        List<String> gamesNames = new ArrayList<>();
        Cursor cursor = db.getData(1);
        if(cursor.getCount()>0){
            cursor.moveToFirst();
             saveTime = cursor.getString(cursor.getColumnIndexOrThrow("saveTime"));
             canLoad[0] = true;
        }
        else
        {
            saveTime = "";
        }
        if(!cursor.isClosed())
        {
            cursor.close();
        }
        gamesNames.add("Game slot 1 \n"+saveTime);

        cursor = db.getData(2);
        if(cursor.getCount()>0){
            cursor.moveToFirst();
            saveTime = cursor.getString(cursor.getColumnIndexOrThrow("saveTime"));
            canLoad[1] = true;
        }
        else
        {
            saveTime = "";
        }
        if(!cursor.isClosed())
        {
            cursor.close();
        }
        gamesNames.add("Game slot 2 \n"+saveTime);

        cursor = db.getData(3);
        if(cursor.getCount()>0){
            cursor.moveToFirst();
            saveTime = cursor.getString(cursor.getColumnIndexOrThrow("saveTime"));
            canLoad[2] = true;
        }
        else
        {
            saveTime = "";
        }
        if(!cursor.isClosed())
        {
            cursor.close();
        }
        gamesNames.add("Game slot 3 \n"+saveTime);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,R.layout.save_item, gamesNames );
        saveList.setAdapter(arrayAdapter);
        saveList.setVisibility(View.VISIBLE);
        saveList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(canLoad[i]) {
                    Intent loadGame = new Intent(getApplicationContext(), game_main.class);
                    preferencesEditor.putInt("slotID",i+1);
                    preferencesEditor.putBoolean("loadGame",true);
                    preferencesEditor.commit();
                    startActivity(loadGame);
                    finish();
                }
            }
        });
    }

    public void mainMenuNewGame(View view) {
        List<String> gamesNames = new ArrayList<>();
        gamesNames.add("Game slot 1");
        gamesNames.add("Game slot 2");
        gamesNames.add("Game slot 3");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,R.layout.save_item, gamesNames );
        saveList.setAdapter(arrayAdapter);
        saveList.setVisibility(View.VISIBLE);
        saveList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent newGame = new Intent(getApplicationContext(), game_main.class);
                preferencesEditor.putInt("slotID",i+1);
                preferencesEditor.putBoolean("loadGame",false);
                preferencesEditor.commit();
                startActivity(newGame);
                finish();
            }
        });

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