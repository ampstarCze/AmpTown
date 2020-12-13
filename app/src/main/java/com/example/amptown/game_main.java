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
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Random;

public class game_main extends AppCompatActivity {

    private SharedPreferences preferences;
    private SharedPreferences.Editor preferencesEditor;

    public static Fragment currentFragment;

    private long dayTime = 300000;
    private long dayTimeLeft = dayTime;
    private CountDownTimer dayTimer;
    public static database db;

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

    public static int wood = 0;
    public static int gold = 0;
    public static int stone = 0;
    public static int woodStorage = 0;
    public static int woodStorageMax = 200;
    public static int stoneStorage = 0;
    public static int stoneStorageMax = 200;
    public static int woodClickGen = 1;
    public static int stoneClickGen = 1;

    public static int woodGenRate = 0;
    public static int stoneGenRate = 0;

    static boolean stoneBuilded = false;
    static boolean woodBuilded = false;
    public static int woodHammerClick = 3;
    public static int stoneHammerClick = 3;

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
    public static int banditWood = 0;
    public static int banditStone = 0;
    public static int banditGold = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_main);

        BottomNavigationView bottomNavigation = findViewById(R.id.gameBotomMemu);
        bottomNavigation.setOnNavigationItemSelectedListener(navListener);
        bottomNavigation.setSelectedItemId(R.id.town);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new town()).commit();

        minuteView = findViewById(R.id.minute);
        hourView = findViewById(R.id.hour);
        dayView = findViewById(R.id.day);

        frameTextL = findViewById(R.id.frameTextL);
        frameTextM = findViewById(R.id.frameTextM);
        frameTextR = findViewById(R.id.frameTextR);

        preferences = getSharedPreferences("AmpTown", Context.MODE_PRIVATE);
        preferencesEditor = preferences.edit();

        ID = preferences.getInt("slotID", 1);
        loadGame = preferences.getBoolean("loadGame", false);
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

        if (!loadGame) {
            db.newSave(ID);
        } else {
            loadGame();
        }

        dayTimer = new CountDownTimer(dayTimeLeft, 2083) {
            @Override
            public void onTick(long millisUntilFinished) {
                dayTimeLeft = millisUntilFinished;
                minute += 10;

                generatResources();
                updateText();

                if (minute >= 60) {
                    minute = 0;
                    hour++;
                }
                db.update(ID, "minute", minute);
                db.update(ID, "dayTimeLeft", dayTimeLeft);
                minuteView.setText("Minute: " + minute);
                if (hour >= 24) {
                    hour = 0;
                    day++;
                }
                db.update(ID, "hour", hour);
                db.update(ID, "day", day);
                hourView.setText("Hour: " + hour);
                dayView.setText("Day: " + day);
            }

            @Override
            public void onFinish() {
                if (day >= banditNext) {
                    banditSpawned = 1;
                    db.update(ID, "banditSpawned", banditSpawned);
                }
                banditRaid();
                damageGen();
                marketplace.restartPrice();
                dayTimeLeft = dayTime;
                dayTimer.start();
            }
        }.start();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
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

        wood = cursor.getInt(cursor.getColumnIndex("wood"));
        stone = cursor.getInt(cursor.getColumnIndex("stone"));
        gold = cursor.getInt(cursor.getColumnIndex("gold"));
        woodStorage = cursor.getInt(cursor.getColumnIndex("woodStorage"));
        stoneStorage = cursor.getInt(cursor.getColumnIndex("stoneStorage"));
        woodStorageMax = cursor.getInt(cursor.getColumnIndex("woodStorageMax"));
        stoneStorageMax = cursor.getInt(cursor.getColumnIndex("stoneStorageMax"));
        dayTimeLeft = cursor.getInt(cursor.getColumnIndex("dayTimeLeft"));
        dungMaxLvl = cursor.getInt(cursor.getColumnIndex("dungMax"));
        banditSpawned = cursor.getInt(cursor.getColumnIndex("banditSpawned"));
        banditNext = cursor.getInt(cursor.getColumnIndex("banditNext"));
        banditWood = cursor.getInt(cursor.getColumnIndex("banditWood"));
        banditStone = cursor.getInt(cursor.getColumnIndex("banditStone"));
        banditGold = cursor.getInt(cursor.getColumnIndex("banditGold"));
        woodHammerClick = cursor.getInt(cursor.getColumnIndex("woodHammerClick"));
        stoneHammerClick = cursor.getInt(cursor.getColumnIndex("stoneHammerClick"));
        woodGenRate = cursor.getInt(cursor.getColumnIndex("woodGenRate"));
        stoneGenRate = cursor.getInt(cursor.getColumnIndex("stoneGenRate"));
        woodClickGen = cursor.getInt(cursor.getColumnIndex("woodClickGen"));
        stoneClickGen = cursor.getInt(cursor.getColumnIndex("stoneClickGen"));
        woodTransportLeftStart = cursor.getInt(cursor.getColumnIndex("woodTransportLeftStart"));
        stoneTransportLeftStart = cursor.getInt(cursor.getColumnIndex("stoneTransportLeftStart"));

        minuteView.setText("Minute: " + minute);
        hourView.setText("Hour: " + hour);
        dayView.setText("Day: " + day);

        if (!cursor.isClosed()) {
            cursor.close();
        }
    }

    @Override
    public void onBackPressed() {

        if (currentFragment instanceof map) {
            if (map.dungList.getVisibility() == View.VISIBLE) {
                map.dungList.setVisibility(View.GONE);
                return;
            }
        }
        if (currentFragment instanceof woodcutter || currentFragment instanceof stonecutter || currentFragment instanceof barracks || currentFragment instanceof marketplace) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new town()).addToBackStack(null).commit();
                return;
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

    private ServiceConnection Scon = new ServiceConnection() {

        public void onServiceConnected(ComponentName name, IBinder
                binder) {
            mServ = ((MusicPlayer.ServiceBinder) binder).getService();
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
        music.setClass(this, MusicPlayer.class);
        homeWatcher.stopWatch();
        stopService(music);
    }

    void doBindService() {
        bindService(new Intent(this, MusicPlayer.class),
                Scon, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    void doUnbindService() {
        if (mIsBound) {
            unbindService(Scon);
            mIsBound = false;
        }
    }

    static void updateText() {
        if (currentFragment instanceof forest) {
            frameTextL.setText("Storaged wood: " + woodStorage + " / " + woodStorageMax);
        }

        if (currentFragment instanceof mine) {
            frameTextL.setText("Storaged stone: " + stoneStorage + " / " + stoneStorageMax);
        }
        if (currentFragment instanceof town || currentFragment instanceof woodcutter || currentFragment instanceof stonecutter || currentFragment instanceof barracks || currentFragment instanceof marketplace) {
            frameTextL.setText("Wood: " + wood);
            frameTextM.setText("Stone: " + stone);
            frameTextR.setText("Gold: " + gold);
        }
    }

    void generatResources() {
        if(woodBuilded) {
            woodStorage += woodGenRate;
            if (woodStorage > woodStorageMax) {
                woodStorage = woodStorageMax;
            }
        }
        if(stoneBuilded) {
            stoneStorage += stoneGenRate;
            if (stoneStorage > stoneStorageMax) {
                stoneStorage = stoneStorageMax;
            }
        }
        db.updateStoraged(ID, woodStorage, stoneStorage);
    }

    public static void genClick() {
        if (currentFragment instanceof forest) {
            woodStorage += woodClickGen;
            if (woodStorage > woodStorageMax) {
                woodStorage = woodStorageMax;
            }
            db.update(ID, "woodStorage", woodStorage);
        }

        if (currentFragment instanceof mine) {
            stoneStorage += stoneClickGen;
            if (stoneStorage > stoneStorageMax) {
                stoneStorage = stoneStorageMax;
            }
            db.update(ID, "stoneStorage", stoneStorage);
        }
        updateText();
    }

    public static void initWoodTransport() {
        if (!woodTransportProgress) {
            woodTransportProgress = true;
            woodTransportLeft = woodTransportLeftStart;
            forest.forestTransportText.setText("" + woodTransportLeft / 1000);
            forest.forestTransportText.setVisibility(View.VISIBLE);
            woodTransportTimer = new CountDownTimer(woodTransportLeft, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    woodTransportLeft = millisUntilFinished;
                    forest.forestTransportText.setText("" + woodTransportLeft / 1000);
                }

                @Override
                public void onFinish() {
                    woodTransportLeft = woodTransportLeftStart;
                    forest.forestTransportText.setVisibility(View.GONE);
                    if (isRaided()) {
                        wood += woodStorage;
                        db.update(ID, "wood", wood);
                    }
                    woodStorage = 0;
                    woodTransportProgress = false;
                    if (currentFragment instanceof forest) {
                        frameTextL.setText("Storaged wood: " + woodStorage + " / " + woodStorageMax);
                    }
                    if (currentFragment instanceof town || currentFragment instanceof woodcutter || currentFragment instanceof stonecutter || currentFragment instanceof barracks || currentFragment instanceof marketplace) {
                        frameTextL.setText("Wood: " + wood);
                        frameTextM.setText("Stone: " + stone);
                        frameTextR.setText("Gold: " + gold);
                    }
                    db.update(ID, "woodStorage", woodStorage);
                }
            }.start();
        }
    }

    public static void initStoneTransport() {
        if (!stoneTransportPrograss) {
            stoneTransportLeft = stoneTransportLeftStart;
            stoneTransportPrograss = true;
            mine.mineTransportText.setText("" + stoneTransportLeft / 1000);
            mine.mineTransportText.setVisibility(View.VISIBLE);
            stoneTransportTimer = new CountDownTimer(stoneTransportLeft, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    stoneTransportLeft = millisUntilFinished;
                    mine.mineTransportText.setText("" + stoneTransportLeft / 1000);
                }

                @Override
                public void onFinish() {
                    stoneTransportLeft = stoneTransportLeftStart;
                    mine.mineTransportText.setVisibility(View.GONE);
                    if (isRaided()) {
                        stone += stoneStorage;
                        db.update(ID, "stone", stone);
                    }
                    stoneStorage = 0;
                    if (currentFragment instanceof mine) {
                        frameTextL.setText("Storaged stone: " + stoneStorage + " / " + stoneStorageMax);
                    }
                    stoneTransportPrograss = false;
                    if (currentFragment instanceof town || currentFragment instanceof woodcutter || currentFragment instanceof stonecutter || currentFragment instanceof barracks || currentFragment instanceof marketplace) {
                        frameTextL.setText("Wood: " + wood);
                        frameTextM.setText("Stone: " + stone);
                        frameTextR.setText("Gold: " + gold);
                    }
                    db.update(ID, "stoneStorage", stoneStorage);
                }
            }.start();
        }
    }

    private static boolean isRaided() {
        final int random = new Random().nextInt((10 - 1) + 1) + 1;
        if (random == 1) {
            return false;
        } else {
            return true;
        }
    }

    private void banditRaid() {
        if (banditSpawned == 1) {

            int looted;
            looted = (int) (wood * 0.25);
            wood -= looted;
            banditWood += looted;
            if (wood < 0) {
                wood = 0;
            }

            looted = (int) (stone * 0.25);
            stone -= looted;
            banditStone += looted;
            if (stone < 0) {
                stone = 0;
            }

            looted = (int) (gold * 0.25);
            gold -= looted;
            banditGold += looted;
            if (gold < 0) {
                gold = 0;
            }

            if (currentFragment instanceof town || currentFragment instanceof woodcutter || currentFragment instanceof stonecutter || currentFragment instanceof barracks || currentFragment instanceof marketplace) {
                frameTextL.setText("Wood: " + wood);
                frameTextM.setText("Stone: " + stone);
                frameTextR.setText("Gold: " + gold);
            }

            db.updateResources(ID, wood, stone, gold);
            db.update(ID,"banditWood" ,banditWood);
            db.update(ID,"banditStone" ,banditStone);
            db.update(ID,"banditGold" ,banditGold);


        }
    }

    private void damageGen()
    {
        int random = new Random().nextInt((10 - 1) + 1) + 1;
        if (random == 1) {
            stoneHammerClick = 3;
            stoneBuilded = false ;
        }
        random = new Random().nextInt((10 - 1) + 1) + 1;
        if (random == 1) {
            woodHammerClick = 3;
            woodBuilded = false ;
        }
    }
}