package com.example.amptown;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class database extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "AMPTOWN.db";

    public database(Context context)
    {
        super(context, DATABASE_NAME, null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE save (id INTEGER, saveTime NUMERIC, day INTEGER, hour INTEGER, minute INTEGER, wood INTEGER, stone INTEGER , gold INTEGER, woodStorage INTEGER, stoneStorage INTEGER, woodStorageMax INTEGER, " +
                "stoneStorageMax INTEGER, dayTimeLeft INTEGER, woodcutterCount INTEGER, axeLVL INTEGER, " +
                "stonecutterCount INTEGER, pickaxeLVL INTEGER, marketplaceLVL INTEGER, woodBuy INTEGER, woodSell INTEGER, stoneBuy INTEGER, stoneSell INTEGER, barracksLVL INTEGER, soldiersCount INTEGER," +
                " swordLVL INTEGER, soldiersDPS INTEGER, dungMax INTEGER, banditSpawned INTEGER, banditNext INTEGER, banditWood INTEGER, banditStone INTEGER, banditGold INTEGER, woodHammerClick INTEGER, stoneHammerClick INTEGER," +
                "woodcutterStorageLVL INTEGER, woodcutterSpeedLVL INTEGER, stonecutterStorageLVL INTEGER, stonecutterSpeedLVL INTEGER, woodGenRate INTEGER, stoneGenRate INTEGER, woodClickGen INTEGER, stoneClickGen INTEGER," +
                "woodcutterLVL INTEGER, stonecutterLVL INTEGER, woodTransportLeftStart INTEGER, stoneTransportLeftStart INTEGER )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS save");
        onCreate(db);
    }

    public void newSave(Integer ID)
    {
        SQLiteDatabase db = this.getWritableDatabase();
       // db.execSQL("DROP TABLE save");
       // onCreate(db);
        db.delete("save","id=?",new String[] {String.valueOf(ID)});

        ContentValues contentValues = new ContentValues();
        contentValues.put("id", ID);
        contentValues.put("saveTime",getDateTime());
        contentValues.put("day", 0);
        contentValues.put("hour", 0);
        contentValues.put("minute", 0);
        contentValues.put("wood", 0);
        contentValues.put("stone", 0);
        contentValues.put("gold", 0);
        contentValues.put("woodStorage", 0);
        contentValues.put("stoneStorage", 0);
        contentValues.put("woodStorageMax", 200);
        contentValues.put("stoneStorageMax", 200);
        contentValues.put("dayTimeLeft", 300000);
        contentValues.put("woodcutterCount", 0);
        contentValues.put("axeLVL", 1);
        contentValues.put("stonecutterCount", 0);
        contentValues.put("pickaxeLVL", 1);
        contentValues.put("marketplaceLVL", 0);
        contentValues.put("woodBuy", 100);
        contentValues.put("woodSell", 25);
        contentValues.put("stoneBuy", 120);
        contentValues.put("stoneSell", 30);
        contentValues.put("barracksLVL", 0);
        contentValues.put("soldiersCount", 0);
        contentValues.put("swordLVL", 1);
        contentValues.put("soldiersDPS", 0);
        contentValues.put("dungMax", 1);
        contentValues.put("banditSpawned", 0);
        contentValues.put("banditNext", 3);
        contentValues.put("banditWood", 0);
        contentValues.put("banditStone", 0);
        contentValues.put("banditGold", 0);
        contentValues.put("woodHammerClick", 3);
        contentValues.put("stoneHammerClick", 3);
        contentValues.put("woodcutterStorageLVL", 1);
        contentValues.put("woodcutterSpeedLVL", 1);
        contentValues.put("stonecutterStorageLVL", 1);
        contentValues.put("stonecutterSpeedLVL", 1);
        contentValues.put("woodGenRate", 0);
        contentValues.put("stoneGenRate", 0);
        contentValues.put("woodClickGen", 1);
        contentValues.put("stoneClickGen", 1);
        contentValues.put("woodcutterLVL", 0);
        contentValues.put("stonecutterLVL", 0);
        contentValues.put("woodTransportLeftStart", 20000);
        contentValues.put("stoneTransportLeftStart", 20000);

        db.insert("save", null, contentValues);

        db.execSQL("DROP TABLE IF EXISTS price"+ID);
        db.execSQL("CREATE TABLE price"+ ID +" (name TEXT, wood TEXT, stone TEXT, gold TEXT)");

        initPrice(ID);
    }

    private void initPrice(Integer ID)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", "woodCutter");
        contentValues.put("wood", 100);
        contentValues.put("stone",100);
        contentValues.put("gold", 0);
        db.insert("price"+ID, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("name", "woodClick");
        contentValues.put("wood", 150);
        contentValues.put("stone",100);
        contentValues.put("gold", 0);
        db.insert("price"+ID, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("name", "woodGen");
        contentValues.put("wood", 110);
        contentValues.put("stone",120);
        contentValues.put("gold", 0);
        db.insert("price"+ID, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("name", "woodStorage");
        contentValues.put("wood", 1000);
        contentValues.put("stone",500);
        contentValues.put("gold", 100);
        db.insert("price"+ID, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("name", "woodSpeed");
        contentValues.put("wood", 2000);
        contentValues.put("stone",1200);
        contentValues.put("gold", 250);
        db.insert("price"+ID, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("name", "stoneCutter");
        contentValues.put("wood", 100);
        contentValues.put("stone",100);
        contentValues.put("gold", 0);
        db.insert("price"+ID, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("name", "stoneClick");
        contentValues.put("wood", 140);
        contentValues.put("stone",150);
        contentValues.put("gold", 0);
        db.insert("price"+ID, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("name", "stoneGen");
        contentValues.put("wood", 100);
        contentValues.put("stone",130);
        contentValues.put("gold", 0);
        db.insert("price"+ID, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("name", "stoneStorage");
        contentValues.put("wood", 500);
        contentValues.put("stone",1100);
        contentValues.put("gold", 90);
        db.insert("price"+ID, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("name", "stoneSpeed");
        contentValues.put("wood", 1500);
        contentValues.put("stone",2200);
        contentValues.put("gold", 200);
        db.insert("price"+ID, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("name", "marketplace");
        contentValues.put("wood", 400);
        contentValues.put("stone",450);
        contentValues.put("gold", 0);
        db.insert("price"+ID, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("name", "barracksBuild");
        contentValues.put("wood", 2500);
        contentValues.put("stone",2500);
        contentValues.put("gold", 250);
        db.insert("price"+ID, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put("name", "barracksSoldier");
        contentValues.put("wood", 1000);
        contentValues.put("stone",1500);
        contentValues.put("gold", 100);
        db.insert("price"+ID, null, contentValues);contentValues = new ContentValues();
        contentValues.put("name", "barracksSword");
        contentValues.put("wood", 2500);
        contentValues.put("stone",2800);
        contentValues.put("gold", 500);
        db.insert("price"+ID, null, contentValues);
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd-MM-yyyy HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public void update(Integer ID, String item, long value)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(item, value);
        contentValues.put("saveTime",getDateTime());
        db.update("save",contentValues,"id=?",new String[] {String.valueOf(ID)});
    }

    public void updatePrice(String  name,Integer ID, Integer wood, Integer stone, Integer gold)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("wood", wood);
        contentValues.put("stone",stone);
        contentValues.put("gold", gold);
        db.update("price"+ID,contentValues,"name=?",new String[]{name});
    }

    public void updateStoraged(Integer ID, Integer woodStorage, Integer stoneStorage)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("woodStorage", woodStorage);
        contentValues.put("stoneStorage",stoneStorage);
        db.update("save" ,contentValues,"id=?",new String[] {String.valueOf(ID)});
    }

    public void updateResources(Integer ID, Integer wood, Integer stone, Integer gold)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("wood", wood);
        contentValues.put("stone",stone);
        contentValues.put("gold", gold);
        db.update("save" ,contentValues,"id=?",new String[] {String.valueOf(ID)});
    }

    public Cursor getData(int ID){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select * from save where id=" + ID + "", null);
        return res;
    }

    public Cursor getPrices(int ID){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select * from price" + ID, null);
        return res;
    }
}
