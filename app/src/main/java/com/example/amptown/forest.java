package com.example.amptown;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class forest extends Fragment implements View.OnClickListener {

    int hammerClicked = 0;
    ImageButton axeButton;
    ImageView vagonButton;

    public static TextView forestTransportText;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forest, container, false);

        game_main.frameTextL.setText("Storaged wood: " + game_main.woodStorege + " / " + game_main.woodStoregeMax);
        game_main.frameTextL.setVisibility(View.VISIBLE);
        game_main.frameTextM.setVisibility(View.GONE);
        game_main.frameTextR.setVisibility(View.GONE);

        forestTransportText = view.findViewById(R.id.forest_wagon_text);
        forestTransportText.setText(""+game_main.woodTransportLeft/1000);

        if (game_main.woodTransportProgress) {
            forestTransportText.setVisibility(View.VISIBLE);
        }

        game_main.currentFragment = getFragmentManager().findFragmentById(R.id.fragment);

        axeButton = view.findViewById(R.id.axe);
        vagonButton = view.findViewById(R.id.forest_wagon);
        axeButton.setOnClickListener(this);
        if (!game_main.woodBuilded) {
            axeButton.setImageResource(R.drawable.hammer);
        }
        vagonButton.setOnClickListener(this);

        return view;
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.axe) {
            if (!game_main.woodBuilded) {
                if (hammerClicked < 2) {
                    hammerClicked++;
                } else {
                    hammerClicked = 0;
                    axeButton.setImageResource(R.drawable.axe);
                    game_main.woodBuilded = true;
                }
            } else {
                game_main.genClick();
            }
        }
        if(v.getId() == R.id.forest_wagon)
        {
            game_main.initWoodTransport();
        }
    }
}