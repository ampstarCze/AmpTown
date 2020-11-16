package com.example.amptown;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class forest extends Fragment implements View.OnClickListener {

    boolean builded = false;
    int hammerClicked = 0;
    ImageButton axeButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forest, container, false);

        axeButton = view.findViewById(R.id.axe);
        axeButton.setOnClickListener(this);
        if (!builded) {
            axeButton.setImageResource(R.drawable.hammer);
        }
        return view;
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.axe) {
            if (!builded) {
                if (hammerClicked < 2) {
                    hammerClicked++;
                } else {
                    hammerClicked = 0;
                    axeButton.setImageResource(R.drawable.axe);
                    builded = true;
                }
            }
        }
    }
}