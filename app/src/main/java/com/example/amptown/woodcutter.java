package com.example.amptown;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class woodcutter extends Fragment implements View.OnClickListener {

    Button btnSawmillBuild;
    Button btnSawmillLumberjack;
    Button btnSawmillAxe;
    Button btnSawmillStorage;
    Button btnSawmillSpeed;

    TextView sawmillBuildPrice;
    TextView sawmillLumberjackText;
    TextView sawmillLumberjackPrice;
    TextView sawmillAxeText;
    TextView sawmillAxePrice;
    TextView sawmillStorageText;
    TextView sawmillStoragePrice;
    TextView sawmillSpeedText;
    TextView sawmillSpeedPrice;

    int woodcutterLVL;
    int woodcutterCount;
    int woodcutterAxeLVL;
    int woodcutterStorageLVL;
    int woodcutterSpeedLVL;

    int woodcutterAxeWood;
    int woodcutterAxeStone;
    int woodcutterLumberjackWood;
    int woodcutterLumberjackStone;
    int woodcutterStorageWood;
    int woodcutterStorageStone;
    int woodcutterStorageGold;
    int woodcutterSpeedWood;
    int woodcutterSpeedStone;
    int woodcutterSpeedGold;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_woodcutter, container, false);

        game_main.currentFragment = getFragmentManager().findFragmentById(R.id.fragment);

        btnSawmillBuild = view.findViewById(R.id.sawmillBuild);
        btnSawmillLumberjack = view.findViewById(R.id.btnSawmillLumberjack);
        btnSawmillAxe = view.findViewById(R.id.btnSawmillAxe);
        btnSawmillStorage = view.findViewById(R.id.btnSawmillStorage);
        btnSawmillSpeed = view.findViewById(R.id.btnSawmillSpeed);
        sawmillBuildPrice = view.findViewById(R.id.sawmillBuildPrice);
        sawmillLumberjackText = view.findViewById(R.id.sawmillLumberjackText);
        sawmillLumberjackPrice = view.findViewById(R.id.sawmillLumberjackPrice);
        sawmillAxeText = view.findViewById(R.id.sawmillAxeText);
        sawmillAxePrice = view.findViewById(R.id.sawmillAxePrice);
        sawmillStorageText = view.findViewById(R.id.sawmillStorageText);
        sawmillStoragePrice = view.findViewById(R.id.sawmillStoragePrice);
        sawmillSpeedText = view.findViewById(R.id.sawmillSpeedText);
        sawmillSpeedPrice = view.findViewById(R.id.sawmillSpeedPrice);

        Cursor cursor = game_main.db.getData(game_main.ID);
        cursor.moveToFirst();
        woodcutterLVL = cursor.getInt(cursor.getColumnIndex("woodcutterLVL"));
        woodcutterCount = cursor.getInt(cursor.getColumnIndex("woodcutterCount"));
        woodcutterAxeLVL = cursor.getInt(cursor.getColumnIndex("axeLVL"));
        woodcutterStorageLVL = cursor.getInt(cursor.getColumnIndex("woodcutterStorageLVL"));
        woodcutterSpeedLVL = cursor.getInt(cursor.getColumnIndex("woodcutterSpeedLVL"));

        if (!cursor.isClosed()) {
            cursor.close();
        }

        cursor = game_main.db.getPrices(game_main.ID);
        cursor.moveToPosition(1);
        woodcutterAxeWood = cursor.getInt(cursor.getColumnIndex("wood"));
        woodcutterAxeStone = cursor.getInt(cursor.getColumnIndex("stone"));
        cursor.moveToNext();
        woodcutterLumberjackWood = cursor.getInt(cursor.getColumnIndex("wood"));
        woodcutterLumberjackStone = cursor.getInt(cursor.getColumnIndex("stone"));
        cursor.moveToNext();
        woodcutterStorageWood = cursor.getInt(cursor.getColumnIndex("wood"));
        woodcutterStorageStone = cursor.getInt(cursor.getColumnIndex("stone"));
        woodcutterStorageGold = cursor.getInt(cursor.getColumnIndex("gold"));
        cursor.moveToNext();
        woodcutterSpeedWood = cursor.getInt(cursor.getColumnIndex("wood"));
        woodcutterSpeedStone = cursor.getInt(cursor.getColumnIndex("stone"));
        woodcutterSpeedGold = cursor.getInt(cursor.getColumnIndex("gold"));

        if (!cursor.isClosed()) {
            cursor.close();
        }

        if (woodcutterLVL > 0) {
            btnSawmillBuild.setVisibility(View.GONE);
            btnSawmillLumberjack.setVisibility(View.VISIBLE);
            btnSawmillAxe.setVisibility(View.VISIBLE);
            btnSawmillStorage.setVisibility(View.VISIBLE);
            btnSawmillSpeed.setVisibility(View.VISIBLE);
            sawmillBuildPrice.setVisibility(View.GONE);
            sawmillLumberjackText.setVisibility(View.VISIBLE);
            sawmillLumberjackPrice.setVisibility(View.VISIBLE);
            sawmillAxeText.setVisibility(View.VISIBLE);
            sawmillAxePrice.setVisibility(View.VISIBLE);
            sawmillStorageText.setVisibility(View.VISIBLE);
            sawmillStoragePrice.setVisibility(View.VISIBLE);
            sawmillSpeedText.setVisibility(View.VISIBLE);
            sawmillSpeedPrice.setVisibility(View.VISIBLE);
        }

        sawmillLumberjackText.setText("Lumberjacks count: " + woodcutterCount);
        sawmillLumberjackPrice.setText("Wood: " + woodcutterLumberjackWood + "\nStone: " + woodcutterLumberjackStone);

        sawmillAxeText.setText("Axe LVL: " + woodcutterAxeLVL);
        sawmillAxePrice.setText("Wood: " + woodcutterAxeWood + "\nStone: " + woodcutterAxeStone);

        sawmillStorageText.setText("Storage LVL: " + woodcutterStorageLVL);
        sawmillStoragePrice.setText("Wood: " + woodcutterStorageWood + "\nStone: " + woodcutterStorageStone + "\nGold: " + woodcutterStorageGold);

        if (woodcutterSpeedLVL <= 4) {
            sawmillSpeedText.setText("Transport speed LVL: " + woodcutterSpeedLVL);
            sawmillSpeedPrice.setText("Wood: " + woodcutterSpeedWood + "\nStone: " + woodcutterSpeedStone + "\nGold: " + woodcutterSpeedGold);
        } else {
            sawmillSpeedText.setText("Transport speed LVL: " + woodcutterSpeedLVL);
            sawmillSpeedPrice.setText("MAX LVL");
            btnSawmillSpeed.setVisibility(View.GONE);
        }

        btnSawmillAxe.setOnClickListener(this);
        btnSawmillBuild.setOnClickListener(this);
        btnSawmillLumberjack.setOnClickListener(this);
        btnSawmillSpeed.setOnClickListener(this);
        btnSawmillStorage.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.sawmillBuild) {
            if (woodcutterLVL < 1) {
                if (game_main.wood >= 100 && game_main.stone >= 100) {

                    game_main.wood -= 100;
                    game_main.stone -= 100;
                    woodcutterLVL = 1;
                    game_main.db.updateResources(game_main.ID, game_main.wood, game_main.stone, game_main.gold);
                    game_main.db.update(game_main.ID, "woodcutterLVL", 1);

                    btnSawmillBuild.setVisibility(View.GONE);
                    btnSawmillLumberjack.setVisibility(View.VISIBLE);
                    btnSawmillAxe.setVisibility(View.VISIBLE);
                    btnSawmillStorage.setVisibility(View.VISIBLE);
                    btnSawmillSpeed.setVisibility(View.VISIBLE);
                    sawmillBuildPrice.setVisibility(View.GONE);
                    sawmillLumberjackText.setVisibility(View.VISIBLE);
                    sawmillLumberjackPrice.setVisibility(View.VISIBLE);
                    sawmillAxeText.setVisibility(View.VISIBLE);
                    sawmillAxePrice.setVisibility(View.VISIBLE);
                    sawmillStorageText.setVisibility(View.VISIBLE);
                    sawmillStoragePrice.setVisibility(View.VISIBLE);
                    sawmillSpeedText.setVisibility(View.VISIBLE);
                    sawmillSpeedPrice.setVisibility(View.VISIBLE);

                    game_main.updateText();
                }
            }
        }
        if (v.getId() == R.id.btnSawmillLumberjack) {
            if (game_main.wood >= woodcutterLumberjackWood && game_main.stone >= woodcutterLumberjackStone) {
                game_main.wood -= woodcutterLumberjackWood;
                game_main.stone -= woodcutterLumberjackStone;
                woodcutterCount++;
                if (game_main.woodGenRate == 0) {
                    game_main.woodGenRate = 1;
                } else {
                    game_main.woodGenRate *= 2;
                }
                woodcutterLumberjackWood += (0.8 * woodcutterLumberjackWood);
                woodcutterLumberjackStone += (0.8 * woodcutterLumberjackStone);
                game_main.db.updateResources(game_main.ID, game_main.wood, game_main.stone, game_main.gold);
                game_main.db.updatePrice("woodGen", game_main.ID, woodcutterLumberjackWood, woodcutterLumberjackStone, 0);
                game_main.db.update(game_main.ID, "woodcutterCount", woodcutterCount);
                game_main.db.update(game_main.ID, "woodGenRate", game_main.woodGenRate);
                sawmillLumberjackText.setText("Lumberjacks count: " + woodcutterCount);
                sawmillLumberjackPrice.setText("Wood: " + woodcutterLumberjackWood + "\nStone: " + woodcutterLumberjackStone);
                game_main.updateText();
            }
        }
        if (v.getId() == R.id.btnSawmillAxe) {
            if (game_main.wood >= woodcutterAxeWood && game_main.stone >= woodcutterAxeStone) {
                game_main.wood -= woodcutterAxeWood;
                game_main.stone -= woodcutterAxeStone;
                woodcutterAxeLVL++;
                game_main.woodClickGen *= 2;
                woodcutterAxeWood += (0.8 * woodcutterAxeWood);
                woodcutterAxeStone += (0.8 * woodcutterAxeStone);
                game_main.db.updateResources(game_main.ID, game_main.wood, game_main.stone, game_main.gold);
                game_main.db.updatePrice("woodClick", game_main.ID, woodcutterAxeWood, woodcutterAxeStone, 0);
                game_main.db.update(game_main.ID, "axeLVL", woodcutterAxeLVL);
                game_main.db.update(game_main.ID, "woodClickGen", game_main.woodClickGen);
                sawmillAxeText.setText("Axe LVL: " + woodcutterAxeLVL);
                sawmillAxePrice.setText("Wood: " + woodcutterAxeWood + "\nStone: " + woodcutterAxeStone);
                game_main.updateText();
            }
        }
        if (v.getId() == R.id.btnSawmillStorage) {
            if (game_main.wood >= woodcutterStorageWood && game_main.stone >= woodcutterStorageStone && game_main.gold >= woodcutterStorageGold) {
                game_main.wood -= woodcutterStorageWood;
                game_main.stone -= woodcutterStorageStone;
                game_main.gold -= woodcutterStorageGold;
                woodcutterStorageLVL++;
                game_main.woodStorageMax *= 2;
                woodcutterStorageWood += (0.9 * woodcutterStorageWood);
                woodcutterStorageStone += (0.95 * woodcutterStorageStone);
                woodcutterStorageGold += (0.7 * woodcutterStorageGold);
                game_main.db.updateResources(game_main.ID, game_main.wood, game_main.stone, game_main.gold);
                game_main.db.updatePrice("woodStorage", game_main.ID, woodcutterStorageWood, woodcutterStorageStone, woodcutterStorageGold);
                game_main.db.update(game_main.ID, "woodcutterStorageLVL", woodcutterStorageLVL);
                game_main.db.update(game_main.ID, "woodStorageMax", game_main.woodStorageMax);
                sawmillStorageText.setText("Storage LVL: " + woodcutterStorageLVL);
                sawmillStoragePrice.setText("Wood: " + woodcutterStorageWood + "\nStone: " + woodcutterStorageStone + "\nGold: " + woodcutterStorageGold);
                game_main.updateText();
            }
        }
        if (v.getId() == R.id.btnSawmillSpeed) {
            if (woodcutterSpeedLVL <= 4) {
                if (game_main.wood >= woodcutterSpeedWood && game_main.stone >= woodcutterSpeedStone && game_main.gold >= woodcutterSpeedGold) {
                    game_main.wood -= woodcutterSpeedWood;
                    game_main.stone -= woodcutterSpeedStone;
                    game_main.gold -= woodcutterSpeedGold;
                    woodcutterSpeedLVL++;
                    game_main.woodTransportLeftStart -= 5000;
                    woodcutterSpeedWood += (0.8 * woodcutterSpeedWood);
                    woodcutterSpeedStone += (0.9 * woodcutterSpeedStone);
                    woodcutterSpeedGold += (0.75 * woodcutterSpeedGold);
                    game_main.db.updateResources(game_main.ID, game_main.wood, game_main.stone, game_main.gold);
                    game_main.db.updatePrice("woodSpeed", game_main.ID, woodcutterSpeedWood, woodcutterSpeedStone, woodcutterSpeedGold);
                    game_main.db.update(game_main.ID, "woodcutterSpeedLVL", woodcutterSpeedLVL);
                    game_main.db.update(game_main.ID, "woodTransportLeftStart", game_main.woodTransportLeftStart);
                    sawmillSpeedText.setText("Transport speed LVL: " + woodcutterSpeedLVL);
                    sawmillSpeedPrice.setText("Wood: " + woodcutterSpeedWood + "\nStone: " + woodcutterSpeedStone + "\nGold: " + woodcutterSpeedGold);
                    game_main.updateText();
                }
            } else {
                sawmillSpeedText.setText("Transport speed LVL: " + woodcutterSpeedLVL);
                sawmillSpeedPrice.setText("MAX LVL");
                btnSawmillSpeed.setVisibility(View.GONE);
            }
        }
    }
}