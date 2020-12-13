package com.example.amptown;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class mine extends Fragment implements View.OnClickListener  {

    ImageView pickAxeButton;
    ImageView vagonButton;

    public static TextView mineTransportText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);

        game_main.currentFragment = getFragmentManager().findFragmentById(R.id.fragment);

        game_main.frameTextL.setText("Storaged stone: " + game_main.stoneStorage + " / "+ game_main.stoneStoregeMax);
        game_main.frameTextL.setVisibility(View.VISIBLE);
        game_main.frameTextM.setVisibility(View.GONE);
        game_main.frameTextR.setVisibility(View.GONE);

        mineTransportText = view.findViewById(R.id.mine_wagon_text);
        mineTransportText.setText(""+game_main.stoneTransportLeft/1000);

        if (game_main.stoneTransportPrograss) {
            mineTransportText.setVisibility(View.VISIBLE);
        }

        pickAxeButton = view.findViewById(R.id.pickaxe);
        vagonButton = view.findViewById(R.id.mine_wagon);
        pickAxeButton.setOnClickListener(this);
        if (!game_main.stoneBuilded) {
            pickAxeButton.setImageResource(R.drawable.hammer);
        }
        vagonButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.pickaxe) {
            if (!game_main.stoneBuilded) {
                if (game_main.stoneHammerClick > 1) {
                    game_main.stoneHammerClick--;

                } else {
                    pickAxeButton.setImageResource(R.drawable.pick_axe);
                    game_main.stoneBuilded = true;
                }
                game_main.db.update(game_main.ID,"woodHammerClick", game_main.woodHammerClick);
            }
            else
            {
                game_main.genClick();
            }
        }
        if(v.getId() == R.id.mine_wagon)
        {
            game_main.initStoneTransport();
        }
    }
}