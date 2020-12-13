package com.example.amptown;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class stonecutter extends Fragment implements View.OnClickListener {

    Button btnStonemasonryBuild;
    Button btnStonemasonryStonemason;
    Button btnStonemasonryPickAxe;
    Button btnStonemasonryStorage;
    Button btnStonemasonrySpeed;

    TextView stonemasonryBuildPrice;
    TextView stonemasonryStonemasonText;
    TextView stonemasonryStonemasonPrice;
    TextView stonemasonryPickaxeText;
    TextView stonemasonryPickaxePrice;
    TextView stonemasonryStorageText;
    TextView stonemasonryStoragePrice;
    TextView stonemasonrySpeedText;
    TextView stonemasonrySpeedPrice;

    int stonecutterLVL;
    int stonecutterCount;
    int pickaxeLVL;
    int stonecutterStorageLVL;
    int stonecutterSpeedLVL;

    int stonemasonryPickaxeWood;
    int stonemasonryPickaxeStone;
    int stonemasonryStonemasonWood;
    int stonemasonryStonemasonStone;
    int stonemasonryStorageWood;
    int stonemasonryStorageStone;
    int stonemasonryStorageGold;
    int stonemasonrySpeedWood;
    int stonemasonrySpeedStone;
    int stonemasonrySpeedGold;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stonecutter, container, false);

        game_main.currentFragment = getFragmentManager().findFragmentById(R.id.fragment);

        btnStonemasonryBuild = view.findViewById(R.id.btnStonemasonryBuild);
        btnStonemasonryStonemason = view.findViewById(R.id.btnStonemasonryStonemason);
        btnStonemasonryPickAxe = view.findViewById(R.id.btnStonemasonryPickAxe);
        btnStonemasonryStorage = view.findViewById(R.id.btnStonemasonryStorage);
        btnStonemasonrySpeed = view.findViewById(R.id.btnStonemasonrySpeed);
        stonemasonryBuildPrice = view.findViewById(R.id.stonemasonryBuildPrice);
        stonemasonryStonemasonText = view.findViewById(R.id.stonemasonryStonemasonText);
        stonemasonryStonemasonPrice = view.findViewById(R.id.stonemasonryStonemasonPrice);
        stonemasonryPickaxeText = view.findViewById(R.id.stonemasonryPickaxeText);
        stonemasonryPickaxePrice = view.findViewById(R.id.stonemasonryPickaxePrice);
        stonemasonryStorageText = view.findViewById(R.id.stonemasonryStorageText);
        stonemasonryStoragePrice = view.findViewById(R.id.stonemasonryStoragePrice);
        stonemasonrySpeedText = view.findViewById(R.id.stonemasonrySpeedText);
        stonemasonrySpeedPrice = view.findViewById(R.id.stonemasonrySpeedPrice);

        Cursor cursor = game_main.db.getData(game_main.ID);
        cursor.moveToFirst();
        stonecutterLVL = cursor.getInt(cursor.getColumnIndex("stonecutterLVL"));
        stonecutterCount = cursor.getInt(cursor.getColumnIndex("stonecutterCount"));
        pickaxeLVL = cursor.getInt(cursor.getColumnIndex("pickaxeLVL"));
        stonecutterStorageLVL = cursor.getInt(cursor.getColumnIndex("stonecutterStorageLVL"));
        stonecutterSpeedLVL = cursor.getInt(cursor.getColumnIndex("stonecutterSpeedLVL"));

        if (!cursor.isClosed()) {
            cursor.close();
        }

        cursor = game_main.db.getPrices(game_main.ID);
        cursor.moveToPosition(6);
        stonemasonryPickaxeWood = cursor.getInt(cursor.getColumnIndex("wood"));
        stonemasonryPickaxeStone = cursor.getInt(cursor.getColumnIndex("stone"));
        cursor.moveToNext();
        stonemasonryStonemasonWood = cursor.getInt(cursor.getColumnIndex("wood"));
        stonemasonryStonemasonStone = cursor.getInt(cursor.getColumnIndex("stone"));
        cursor.moveToNext();
        stonemasonryStorageWood = cursor.getInt(cursor.getColumnIndex("wood"));
        stonemasonryStorageStone = cursor.getInt(cursor.getColumnIndex("stone"));
        stonemasonryStorageGold = cursor.getInt(cursor.getColumnIndex("gold"));
        cursor.moveToNext();
        stonemasonrySpeedWood = cursor.getInt(cursor.getColumnIndex("wood"));
        stonemasonrySpeedStone = cursor.getInt(cursor.getColumnIndex("stone"));
        stonemasonrySpeedGold = cursor.getInt(cursor.getColumnIndex("gold"));

        if (!cursor.isClosed()) {
            cursor.close();
        }

        if (stonecutterLVL > 0) {
            btnStonemasonryBuild.setVisibility(View.GONE);
            btnStonemasonryStonemason.setVisibility(View.VISIBLE);
            btnStonemasonryPickAxe.setVisibility(View.VISIBLE);
            btnStonemasonryStorage.setVisibility(View.VISIBLE);
            btnStonemasonrySpeed.setVisibility(View.VISIBLE);
            stonemasonryBuildPrice.setVisibility(View.GONE);
            stonemasonryStonemasonText.setVisibility(View.VISIBLE);
            stonemasonryStonemasonPrice.setVisibility(View.VISIBLE);
            stonemasonryPickaxeText.setVisibility(View.VISIBLE);
            stonemasonryPickaxePrice.setVisibility(View.VISIBLE);
            stonemasonryStorageText.setVisibility(View.VISIBLE);
            stonemasonryStoragePrice.setVisibility(View.VISIBLE);
            stonemasonrySpeedText.setVisibility(View.VISIBLE);
            stonemasonrySpeedPrice.setVisibility(View.VISIBLE);
        }

        stonemasonryStonemasonText.setText("Stonemasons count: " + stonecutterCount);
        stonemasonryStonemasonPrice.setText("Wood: " + stonemasonryStonemasonWood + "\nStone: " + stonemasonryStonemasonStone);

        stonemasonryPickaxeText.setText("Pickaxe LVL: " + pickaxeLVL);
        stonemasonryPickaxePrice.setText("Wood: " + stonemasonryPickaxeWood + "\nStone: " + stonemasonryPickaxeStone);

        stonemasonryStorageText.setText("Storage LVL: " + stonecutterStorageLVL);
        stonemasonryStoragePrice.setText("Wood: " + stonemasonryStorageWood + "\nStone: " + stonemasonryStorageStone + "\nGold: " + stonemasonryStorageGold);

        if (stonecutterSpeedLVL <= 4) {
            stonemasonrySpeedText.setText("Transport speed LVL: " + stonecutterSpeedLVL);
            stonemasonrySpeedPrice.setText("Wood: " + stonemasonrySpeedWood + "\nStone: " + stonemasonrySpeedStone + "\nGold: " + stonemasonrySpeedGold);
        } else {
            stonemasonrySpeedText.setText("Transport speed LVL: " + stonecutterSpeedLVL);
            stonemasonrySpeedPrice.setText("MAX LVL");
            btnStonemasonrySpeed.setVisibility(View.GONE);
        }

        btnStonemasonryPickAxe.setOnClickListener(this);
        btnStonemasonryBuild.setOnClickListener(this);
        btnStonemasonryStonemason.setOnClickListener(this);
        btnStonemasonrySpeed.setOnClickListener(this);
        btnStonemasonryStorage.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnStonemasonryBuild) {
            if (stonecutterLVL < 1) {
                if (game_main.wood >= 100 && game_main.stone >= 100) {

                    game_main.wood -= 100;
                    game_main.stone -= 100;
                    stonecutterLVL = 1;
                    game_main.db.updateResources(game_main.ID, game_main.wood, game_main.stone, game_main.gold);
                    game_main.db.update(game_main.ID, "stonecutterLVL", 1);

                    btnStonemasonryBuild.setVisibility(View.GONE);
                    btnStonemasonryStonemason.setVisibility(View.VISIBLE);
                    btnStonemasonryPickAxe.setVisibility(View.VISIBLE);
                    btnStonemasonryStorage.setVisibility(View.VISIBLE);
                    btnStonemasonrySpeed.setVisibility(View.VISIBLE);
                    stonemasonryBuildPrice.setVisibility(View.GONE);
                    stonemasonryStonemasonText.setVisibility(View.VISIBLE);
                    stonemasonryStonemasonPrice.setVisibility(View.VISIBLE);
                    stonemasonryPickaxeText.setVisibility(View.VISIBLE);
                    stonemasonryPickaxePrice.setVisibility(View.VISIBLE);
                    stonemasonryStorageText.setVisibility(View.VISIBLE);
                    stonemasonryStoragePrice.setVisibility(View.VISIBLE);
                    stonemasonrySpeedText.setVisibility(View.VISIBLE);
                    stonemasonrySpeedPrice.setVisibility(View.VISIBLE);

                    game_main.updateText();
                }
            }
        }
        if (v.getId() == R.id.btnStonemasonryStonemason) {
            if (game_main.wood >= stonemasonryStonemasonWood && game_main.stone >= stonemasonryStonemasonStone) {
                game_main.wood -= stonemasonryStonemasonWood;
                game_main.stone -= stonemasonryStonemasonStone;
                stonecutterCount++;
                if (game_main.stoneGenRate == 0) {
                    game_main.stoneGenRate = 1;
                } else {
                    game_main.stoneGenRate *= 2;
                }
                stonemasonryStonemasonWood += (0.8 * stonemasonryStonemasonWood);
                stonemasonryStonemasonStone += (0.8 * stonemasonryStonemasonStone);
                game_main.db.updateResources(game_main.ID, game_main.wood, game_main.stone, game_main.gold);
                game_main.db.updatePrice("stoneGen", game_main.ID, stonemasonryStonemasonWood, stonemasonryStonemasonStone, 0);
                game_main.db.update(game_main.ID, "stonecutterCount", stonecutterCount);
                game_main.db.update(game_main.ID, "stoneGenRate", game_main.stoneGenRate);
                stonemasonryStonemasonText.setText("Stonemasons count: " + stonecutterCount);
                stonemasonryStonemasonPrice.setText("Wood: " + stonemasonryStonemasonWood + "\nStone: " + stonemasonryStonemasonStone);
                game_main.updateText();
            }
        }
        if (v.getId() == R.id.btnStonemasonryPickAxe) {
            if (game_main.wood >= stonemasonryPickaxeWood && game_main.stone >= stonemasonryPickaxeStone) {
                game_main.wood -= stonemasonryPickaxeWood;
                game_main.stone -= stonemasonryPickaxeStone;
                pickaxeLVL++;
                game_main.stoneClickGen *= 2;
                stonemasonryPickaxeWood += (0.8 * stonemasonryPickaxeWood);
                stonemasonryPickaxeStone += (0.8 * stonemasonryPickaxeStone);
                game_main.db.updateResources(game_main.ID, game_main.wood, game_main.stone, game_main.gold);
                game_main.db.updatePrice("stoneClick", game_main.ID, stonemasonryPickaxeWood, stonemasonryPickaxeStone, 0);
                game_main.db.update(game_main.ID, "pickaxeLVL", pickaxeLVL);
                game_main.db.update(game_main.ID, "stoneClickGen", game_main.stoneClickGen);
                stonemasonryPickaxeText.setText("Pickaxe LVL: " + pickaxeLVL);
                stonemasonryPickaxePrice.setText("Wood: " + stonemasonryPickaxeWood + "\nStone: " + stonemasonryPickaxeStone);
                game_main.updateText();
            }
        }
        if (v.getId() == R.id.btnStonemasonryStorage) {
            if (game_main.wood >= stonemasonryStorageWood && game_main.stone >= stonemasonryStorageStone && game_main.gold >= stonemasonryStorageGold) {
                game_main.wood -= stonemasonryStorageWood;
                game_main.stone -= stonemasonryStorageStone;
                game_main.gold -= stonemasonryStorageGold;
                stonecutterStorageLVL++;
                game_main.stoneStorageMax *= 2;
                stonemasonryStorageWood += (0.9 * stonemasonryStorageWood);
                stonemasonryStorageStone += (0.95 * stonemasonryStorageStone);
                stonemasonryStorageGold += (0.7 * stonemasonryStorageGold);
                game_main.db.updateResources(game_main.ID, game_main.wood, game_main.stone, game_main.gold);
                game_main.db.updatePrice("stoneStorage", game_main.ID, stonemasonryStorageWood, stonemasonryStorageStone, stonemasonryStorageGold);
                game_main.db.update(game_main.ID, "stonecutterStorageLVL", stonecutterStorageLVL);
                game_main.db.update(game_main.ID, "stoneStorageMax", game_main.stoneStorageMax);
                stonemasonryStorageText.setText("Storage LVL: " + stonecutterStorageLVL);
                stonemasonryStoragePrice.setText("Wood: " + stonemasonryStorageWood + "\nStone: " + stonemasonryStorageStone + "\nGold: " + stonemasonryStorageGold);
                game_main.updateText();
            }
        }
        if (v.getId() == R.id.btnStonemasonrySpeed) {
            if (stonecutterSpeedLVL <= 4) {
                if (game_main.wood >= stonemasonrySpeedWood && game_main.stone >= stonemasonrySpeedStone && game_main.gold >= stonemasonrySpeedGold) {
                    game_main.wood -= stonemasonrySpeedWood;
                    game_main.stone -= stonemasonrySpeedStone;
                    game_main.gold -= stonemasonrySpeedGold;
                    stonecutterSpeedLVL++;
                    game_main.stoneTransportLeftStart -= 5000;
                    stonemasonrySpeedWood += (0.8 * stonemasonrySpeedWood);
                    stonemasonrySpeedStone += (0.9 * stonemasonrySpeedStone);
                    stonemasonrySpeedGold += (0.75 * stonemasonrySpeedGold);
                    game_main.db.updateResources(game_main.ID, game_main.wood, game_main.stone, game_main.gold);
                    game_main.db.updatePrice("stoneSpeed", game_main.ID, stonemasonrySpeedWood, stonemasonrySpeedStone, stonemasonrySpeedGold);
                    game_main.db.update(game_main.ID, "stonecutterSpeedLVL", stonecutterSpeedLVL);
                    game_main.db.update(game_main.ID, "stoneTransportLeftStart", game_main.stoneTransportLeftStart);
                    stonemasonrySpeedText.setText("Transport speed LVL: " + stonecutterSpeedLVL);
                    stonemasonrySpeedPrice.setText("Wood: " + stonemasonrySpeedWood + "\nStone: " + stonemasonrySpeedStone + "\nGold: " + stonemasonrySpeedGold);
                    game_main.updateText();
                }
            } else {
                stonemasonrySpeedText.setText("Transport speed LVL: " + stonecutterSpeedLVL);
                stonemasonrySpeedPrice.setText("MAX LVL");
                btnStonemasonrySpeed.setVisibility(View.GONE);
            }
        }
    }
}