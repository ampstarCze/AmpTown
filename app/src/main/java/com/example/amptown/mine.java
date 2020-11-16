package com.example.amptown;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

public class mine extends Fragment implements View.OnClickListener  {

    boolean builded = false;
    int hammerClicked = 0;
    ImageView pickAxeButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);

        pickAxeButton = view.findViewById(R.id.pickaxe);
        pickAxeButton.setOnClickListener(this);
        if (!builded) {
            pickAxeButton.setImageResource(R.drawable.hammer);
        }

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.pickaxe) {
            if (!builded) {
                if (hammerClicked < 2) {
                    hammerClicked++;
                } else {
                    hammerClicked = 0;
                    pickAxeButton.setImageResource(R.drawable.pick_axe);
                    builded = true;
                }
            }
        }
    }
}