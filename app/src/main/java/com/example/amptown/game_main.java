package com.example.amptown;

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
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Random;

public class game_main extends FragmentActivity {

    private SharedPreferences preferences;
    private SharedPreferences.Editor preferencesEditor;

   public static Fragment currentFragment;

    private long dayTime = 600000;
    private long dayTimeLeft = dayTime;
    private CountDownTimer dayTimer;
    private static database db;

    public MusicPlayer mServ;
    private boolean mIsBound = false;
    HomeWatcher homeWatcher;

    private int minute = 0;
    private int hour = 0;
    public static int day = 0;

    private TextView minuteView;
    private TextView hourView;
    private TextView dayView;

    public static TextView frameTextL;
    public static TextView frameTextM;
    public static TextView frameTextR;

    public static int ID;
    private boolean loadGame;

    public static int woodStorege =0;
    public static int woodStoregeMax = 200;
    public static int wood =0;
    public static int gold =0;
    public static int stoneStorage =0;
    public static int stoneStoregeMax = 200;
    public static int stone =0;
    public static int woodClickGen = 1;
    public static int stoneCLickGen = 1;

    private int woodGenRate = 0;
    private int stoneGenRate = 0;

    static boolean stoneBuilded = false;
    static boolean woodBuilded = false;

    static long woodTransportLeftStart = 20000;
    static long stoneTransportLeftStart = 20000;
    static long woodTransportLeft = woodTransportLeftStart;
    static long stoneTransportLeft = stoneTransportLeftStart;
    static private CountDownTimer woodTransportTimer;
    static private CountDownTimer stoneTransportTimer;
    static boolean woodTransportProgress = false;
    static boolean stoneTransportPrograss = false;

    static int dungMaxLvl = 1;
    public static int banditSpawned = 0;
    public static int banditNext = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_main);

        BottomNavigationView bottomNavigation =  findViewById(R.id.gameBotomMemu);
        bottomNavigation.setOnNavigationItemSelectedListener(navListener);
        bottomNavigation.setSelectedItemId(R.id.town);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment,new town()).commit();

        minuteView = findViewById(R.id.minute);
        hourView = findViewById(R.id.hour);
        dayView = findViewById(R.id.day);

        frameTextL = findViewById(R.id.frameTextL);
        frameTextM = findViewById(R.id.frameTextM);
        frameTextR = findViewById(R.id.frameTextR);

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

                generatResources();
                updateText();

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
            }

            @Override
            public void onFinish() {
                if(day >= banditNext)
                {
                    banditSpawned = 1;
                    db.update(ID,"banditSpawned",banditSpawned);
                    //TODO raid
                }
                dayTimeLeft = dayTime;
                dayTimer.start();
            }
        }.start();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId())
            {
                case R.id.map:
                    selectedFragment = new map();
                    break;
                case R.id.forest:
                    selectedFragment = new forest();
                    break;
                case R.id.town:
                    selectedFragment = new town();
                    break;
                case R.id.mine:
                    selectedFragment = new mine();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, selectedFragment).commit();
            return true;
        }
    };

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

        if (currentFragment instanceof map) {
            if(map.dungList.getVisibility() == View.VISIBLE) {
                map.dungList.setVisibility(View.GONE);
                return;
            }
        }

        Intent mainMenu = new Intent(getApplicationContext(), MainMenu.class);
        startActivity(mainMenu);
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

    static void updateText()
    {
        if (currentFragment instanceof forest) {
            frameTextL.setText("Storaged wood: " + woodStorege + " / "+ woodStoregeMax);
        }

        if (currentFragment instanceof mine) {
            frameTextL.setText("Storaged stone: " + stoneStorage + " / "+ stoneStoregeMax);
        }
    }

    void generatResources()
    {
        woodStorege += woodGenRate;
        if(woodStorege > woodStoregeMax)
        {
            woodStorege = woodStoregeMax;
        }

        stoneStorage += stoneGenRate;
        if(stoneStorage > stoneStoregeMax)
        {
            stoneStorage = stoneStoregeMax;
        }
        db.updateStoraged(ID,woodStorege,stoneStorage);
    }

    public static void genClick()
    {
        if (currentFragment instanceof forest) {
            woodStorege += woodClickGen;
            if(woodStorege > woodStoregeMax)
            {
                woodStorege = woodStoregeMax;
            }
        }

        if (currentFragment instanceof mine) {
            stoneStorage += stoneCLickGen;
            if(woodStorege > woodStoregeMax)
            {
                woodStorege = woodStoregeMax;
            }
        }
        updateText();
    }

    public static void initWoodTransport()
    {
        if(!woodTransportProgress)
        {
            woodTransportProgress = true;
            forest.forestTransportText.setVisibility(View.VISIBLE);
            woodTransportTimer = new CountDownTimer( woodTransportLeft, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    woodTransportLeft = millisUntilFinished;
                    forest.forestTransportText.setText(""+woodTransportLeft/1000);
                }

                @Override
                public void onFinish() {
                    woodTransportLeft = woodTransportLeftStart;
                    forest.forestTransportText.setVisibility(View.GONE);
                    if(isRaided()) {
                        wood += woodStorege;
                        db.update(ID, "wood", wood);
                    }
                    woodStorege = 0;
                    woodTransportProgress = false;
                    if (currentFragment instanceof forest) {
                        frameTextL.setText("Storaged wood: " + woodStorege + " / " + woodStoregeMax);
                    }
                    if (currentFragment instanceof town) {
                        frameTextL.setText("Wood: " + wood);
                        frameTextM.setText("Stone: " + stone);
                        frameTextR.setText("Gold: " + gold);
                    }
                    db.update(ID, "woodStorage", woodStorege);
                }
            }.start();
        }
    }

    public static void initStoneTransport()
    {
        if(!stoneTransportPrograss)
        {
            stoneTransportPrograss = true;
            mine.mineTransportText.setVisibility(View.VISIBLE);
            stoneTransportTimer = new CountDownTimer( stoneTransportLeft, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    stoneTransportLeft = millisUntilFinished;
                    mine.mineTransportText.setText(""+stoneTransportLeft/1000);
                }

                @Override
                public void onFinish() {
                    stoneTransportLeft = stoneTransportLeftStart;
                    mine.mineTransportText.setVisibility(View.GONE);
                    if(isRaided()) {
                        stone += stoneStorage;
                        db.update(ID, "stone", stone);
                    }
                    stoneStorage = 0;
                    if (currentFragment instanceof mine) {
                        frameTextL.setText("Storaged stone: " + stoneStorage + " / " + stoneStoregeMax);
                    }
                    stoneTransportPrograss = false;
                    if (currentFragment instanceof town) {
                        frameTextL.setText("Wood: " + wood);
                        frameTextM.setText("Stone: " + stone);
                        frameTextR.setText("Gold: " + gold);
                    }
                    db.update(ID, "stoneStorage", stoneStorage);
                }
            }.start();
        }
    }

    private static boolean isRaided()
    {
        final int random = new Random().nextInt((10 - 1) + 1) + 1;
        if(random == 1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

}