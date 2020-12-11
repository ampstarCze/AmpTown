package com.example.amptown;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class map extends Fragment implements View.OnClickListener {

    ImageView door;
    public static ListView dungList;
    public static ImageView camp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map, container, false);

        game_main.currentFragment = getFragmentManager().findFragmentById(R.id.fragment);

        game_main.frameTextL.setVisibility(View.GONE);
        game_main.frameTextM.setVisibility(View.GONE);
        game_main.frameTextR.setVisibility(View.GONE);

        dungList = view.findViewById(R.id.dungList);
        camp = view.findViewById(R.id.image_camp);
        camp.setOnClickListener(this);

        door = view.findViewById(R.id.image_door);
        door.setOnClickListener(this);

        if (game_main.banditSpawned == 0) {
            camp.setVisibility(View.GONE);
        } else {
            camp.setVisibility(View.VISIBLE);
        }
        return view;
    }

    public void showLevels() {
        List<String> dungs = new ArrayList<>();

        for (int i = 1; i <= game_main.dungMaxLvl; i++) {
            dungs.add("Floor: " + i);
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, dungs);
        dungList.setAdapter(arrayAdapter);
        dungList.setVisibility(View.VISIBLE);
        dungList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                dungList.setVisibility(View.GONE);
                Intent dungBattle = new Intent(getActivity().getApplicationContext(), game_battle.class);
                dungBattle.putExtra("dungLVL", i + 1);
                startActivity(dungBattle);

            }
        });

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.image_door) {
            showLevels();
        }
        if (v.getId() == R.id.image_camp) {
            Intent dungBattle = new Intent(getActivity().getApplicationContext(), game_battle.class);
            dungBattle.putExtra("dungLVL", 0);
            startActivity(dungBattle);
            ;
        }
    }

}