package com.example.amptown;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class town extends Fragment implements View.OnClickListener {

    Button townWoodcutter;
    Button townStonemason;
    Button townMarketplace;
    Button townBarracks;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_town, container, false);

        game_main.currentFragment = getFragmentManager().findFragmentById(R.id.fragment);

        townWoodcutter = view.findViewById(R.id.townWoodcutter);
        townStonemason = view.findViewById(R.id.townStonemason);
        townMarketplace = view.findViewById(R.id.townMarketplace);
        townBarracks = view.findViewById(R.id.townBarracks);
        townWoodcutter.setOnClickListener(this);
        townStonemason.setOnClickListener(this);
        townMarketplace.setOnClickListener(this);
        townBarracks.setOnClickListener(this);

        game_main.frameTextL.setText("Wood: " + game_main.wood);
        game_main.frameTextM.setText("Stone: " + game_main.stone);
        game_main.frameTextR.setText("Gold: " + game_main.gold);

        game_main.frameTextL.setVisibility(View.VISIBLE);
        game_main.frameTextM.setVisibility(View.VISIBLE);
        game_main.frameTextR.setVisibility(View.VISIBLE);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.townWoodcutter) {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new woodcutter()).addToBackStack(null).commit();
        }
        if (v.getId() == R.id.townStonemason) {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new stonecutter()).addToBackStack(null).commit();
        }
        if (v.getId() == R.id.townMarketplace) {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new marketplace()).addToBackStack(null).commit();
        }
        if (v.getId() == R.id.townBarracks) {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new barracks()).addToBackStack(null).commit();
        }
    }
}