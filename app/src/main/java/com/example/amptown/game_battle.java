package com.example.amptown;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class game_battle extends AppCompatActivity implements View.OnClickListener {

    fightView fightView;
    public static ConstraintLayout battleFinalCont;
    public static TextView battleResult;
    public static Button confirmBattle;

    public static int DungLVL;
    public static boolean banditCamp = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        DungLVL = intent.getIntExtra("dungLVL",1);

        if(DungLVL == 0)
        {
            banditCamp = true;
        }

        setContentView(R.layout.activity_game_battle);

        fightView = findViewById(R.id.fight_view);
        battleFinalCont = findViewById(R.id.battleFinalCont);
        battleResult = findViewById(R.id.battleResult);
        confirmBattle = findViewById(R.id.confirmBattle);

        confirmBattle.setOnClickListener(this);

        fightView.initText();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        fightView.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        fightView.resume();
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        fightView.pause();
        finish();
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.confirmBattle) {
            finish();
        }
    }
}