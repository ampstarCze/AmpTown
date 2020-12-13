package com.example.amptown;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class barracks extends Fragment implements View.OnClickListener {

    Button btnBarracksSolder;
    Button btnBarracksSword;
    Button barracksBuild;

    TextView barracksBuildPrice;
    TextView barracksSoldierText;
    TextView barracksSwordText;
    TextView barracksSoldierPrice;
    TextView barracksSwordPrice;

    int soldierWood;
    int soldierStone;
    int soldierGold;
    int swordWood;
    int swordStone;
    int swordGold;

    int barracksLVL;
    int swordLVL;
    int soldiersCount;
    int soldiersDPS;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_barracks, container, false);

        game_main.currentFragment = getFragmentManager().findFragmentById(R.id.fragment);

        btnBarracksSolder = view.findViewById(R.id.btnBarracksSoldier);
        btnBarracksSword = view.findViewById(R.id.btnBarracksSword);
        barracksBuild = view.findViewById(R.id.barracksBuild);
        barracksBuildPrice = view.findViewById(R.id.barracksBuildPrice);
        barracksSoldierText = view.findViewById(R.id.barracksSoldierText);
        barracksSwordText = view.findViewById(R.id.barracksSwordText);
        barracksSoldierPrice = view.findViewById(R.id.barracksSoldierPrice);
        barracksSwordPrice = view.findViewById(R.id.barracksSwordPrice);

        Cursor cursor = game_main.db.getData(game_main.ID);
        cursor.moveToFirst();

        swordLVL = cursor.getInt(cursor.getColumnIndex("swordLVL"));
        soldiersCount = cursor.getInt(cursor.getColumnIndex("soldiersCount"));
        soldiersDPS = cursor.getInt(cursor.getColumnIndex("soldiersDPS"));
        barracksLVL = cursor.getInt(cursor.getColumnIndex("barracksLVL"));

        if (!cursor.isClosed()) {
            cursor.close();
        }

        cursor = game_main.db.getPrices(game_main.ID);
        cursor.moveToPosition(12);

        soldierWood = cursor.getInt(cursor.getColumnIndex("wood"));
        soldierStone = cursor.getInt(cursor.getColumnIndex("stone"));
        soldierGold = cursor.getInt(cursor.getColumnIndex("gold"));

        cursor.moveToNext();

        swordWood = cursor.getInt(cursor.getColumnIndex("wood"));
        swordStone = cursor.getInt(cursor.getColumnIndex("stone"));
        swordGold = cursor.getInt(cursor.getColumnIndex("gold"));

        if (!cursor.isClosed()) {
            cursor.close();
        }

        if (barracksLVL > 0) {
            btnBarracksSolder.setVisibility(View.VISIBLE);
            btnBarracksSword.setVisibility(View.VISIBLE);
            barracksBuild.setVisibility(View.GONE);
            barracksBuildPrice.setVisibility(View.GONE);
            barracksSoldierText.setVisibility(View.VISIBLE);
            barracksSwordText.setVisibility(View.VISIBLE);
            barracksSoldierPrice.setVisibility(View.VISIBLE);
            barracksSwordPrice.setVisibility(View.VISIBLE);
        }

        barracksSoldierText.setText("Soldiers count: " + soldiersCount);
        barracksSoldierPrice.setText("Wood: " + soldierWood + "\nStone: " + soldierStone + "\nGold: " + soldierGold);

        barracksSwordText.setText("Sword LVL: " + swordLVL);
        barracksSwordPrice.setText("Wood: " + swordWood + "\nStone: " + swordStone + "\nGold: " + swordGold);

        btnBarracksSolder.setOnClickListener(this);
        btnBarracksSword.setOnClickListener(this);
        barracksBuild.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.barracksBuild) {
            if (barracksLVL < 1) {
                if(game_main.wood >= 2500 && game_main.stone>=2500 && game_main.gold >=250) {

                    game_main.wood -=2500;
                    game_main.stone -=2500;
                    game_main.gold -=250;
                    barracksLVL = 1;
                    game_main.db.updateResources(game_main.ID, game_main.wood, game_main.stone, game_main.gold);
                    game_main.db.update(game_main.ID,"barracksLVL",1 );

                    btnBarracksSolder.setVisibility(View.VISIBLE);
                    btnBarracksSword.setVisibility(View.VISIBLE);
                    barracksBuild.setVisibility(View.GONE);
                    barracksBuildPrice.setVisibility(View.GONE);
                    barracksSoldierText.setVisibility(View.VISIBLE);
                    barracksSwordText.setVisibility(View.VISIBLE);
                    barracksSoldierPrice.setVisibility(View.VISIBLE);
                    barracksSwordPrice.setVisibility(View.VISIBLE);

                    game_main.updateText();
                }
            }
        }
        if (v.getId() == R.id.btnBarracksSoldier) {
            if(game_main.wood >= soldierWood && game_main.stone>=soldierStone && game_main.gold >=soldierGold)
            {
                game_main.wood -=soldierWood;
                game_main.stone -=soldierStone;
                game_main.gold -=soldierGold;
                soldiersCount++;
                soldiersDPS += (soldiersCount * 2);
                soldierWood += (0.3 * soldierWood);
                soldierStone += (0.4 * soldierStone);
                soldierGold += (0.2 * soldierGold);
                game_main.db.updateResources(game_main.ID, game_main.wood, game_main.stone, game_main.gold);
                game_main.db.updatePrice("barracksSoldier",game_main.ID,soldierWood,soldierStone,soldierGold);
                game_main.db.update(game_main.ID,"soldiersCount",soldiersCount );
                game_main.db.update(game_main.ID,"soldiersDPS",soldiersDPS );
                barracksSoldierText.setText("Soldiers count: " + soldiersCount);
                barracksSoldierPrice.setText("Wood: " + soldierWood + "\nStone: " + soldierStone + "\nGold: " + soldierGold);
                game_main.updateText();
            }
        }
        if (v.getId() == R.id.btnBarracksSword) {
            if(game_main.wood >= swordWood && game_main.stone>=swordStone && game_main.gold >=swordGold)
            {
                game_main.wood -=swordWood;
                game_main.stone -=swordStone;
                game_main.gold -=swordGold;
                swordLVL++;
                swordWood += (0.9 * swordWood);
                swordStone +=  swordStone;
                swordGold += (0.5 * swordGold);
                game_main.db.updateResources(game_main.ID, game_main.wood, game_main.stone, game_main.gold);
                game_main.db.updatePrice("barracksSword",game_main.ID,swordWood,swordStone,swordGold);
                game_main.db.update(game_main.ID,"swordLVL",swordLVL );
                barracksSwordText.setText("Sword LVL: " + swordLVL);
                barracksSwordPrice.setText("Wood: " + swordWood + "\nStone: " + swordStone + "\nGold: " + swordGold);
                game_main.updateText();
            }
        }
    }
}