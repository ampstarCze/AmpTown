package com.example.amptown;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;

public class game_battle extends AppCompatActivity  {

    fightView fightView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game_battle);

        fightView = findViewById(R.id.fight_view);

        fightView.setHP(2000);

    }
}