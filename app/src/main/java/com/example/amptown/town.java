package com.example.amptown;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class town extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_town, container, false);

        game_main.currentFragment = getFragmentManager().findFragmentById(R.id.fragment);

        game_main.frameTextL.setText("Wood: " + game_main.wood);
        game_main.frameTextM.setText("Stone: " + game_main.stone);
        game_main.frameTextR.setText("Gold: " + game_main.gold);

        game_main.frameTextL.setVisibility(View.VISIBLE);
        game_main.frameTextM.setVisibility(View.VISIBLE);
        game_main.frameTextR.setVisibility(View.VISIBLE);

        return view;
    }
}