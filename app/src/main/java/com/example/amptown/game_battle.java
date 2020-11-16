package com.example.amptown;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
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
        Intent mainMenu = new Intent(getApplicationContext(), MainMenu.class);
        startActivity(mainMenu);
        finish();
        super.onBackPressed();
    }
}