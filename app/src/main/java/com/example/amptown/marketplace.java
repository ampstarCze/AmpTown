package com.example.amptown;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class marketplace extends Fragment implements View.OnClickListener {

    Button marketplaceBuild;
    Button btnWoodBuy;
    Button btnWoodSell;
    Button btnStoneBuy;
    Button btnStoneSell;

    TextView marketBuildPrice;
    static TextView marketWoodPriceB;
    static TextView marketWoodPriceS;
    static TextView marketStonePriceB;
    static TextView marketStonePriceS;
    TextView marketWoodText;
    TextView marketStoneText;

    int marketplaceLVL;

    static int woodBuy;
    static int woodSell;
    static int stoneBuy;
    static int stoneSell;
    static int woodBuyDef = 100;
    static int woodSellDef = 25;
    static int stoneBuyDef = 120;
    static int stoneSellDef = 30;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_marketplace, container, false);

        marketplaceBuild = view.findViewById(R.id.marketplaceBuild);
        btnWoodBuy = view.findViewById(R.id.woodBuy);
        btnWoodSell = view.findViewById(R.id.woodSell);
        btnStoneBuy = view.findViewById(R.id.stoneBuy);
        btnStoneSell = view.findViewById(R.id.stoneSell);
        marketBuildPrice = view.findViewById(R.id.marketBuildPrice);
        marketWoodPriceB = view.findViewById(R.id.marketWoodPriceB);
        marketWoodPriceS = view.findViewById(R.id.marketWoodPriceS);
        marketStonePriceB = view.findViewById(R.id.marketStonePriceB);
        marketStonePriceS = view.findViewById(R.id.marketStonePriceS);
        marketWoodText = view.findViewById(R.id.marketWoodText);
        marketStoneText = view.findViewById(R.id.marketStoneText);

        Cursor cursor = game_main.db.getData(game_main.ID);
        cursor.moveToFirst();

        woodBuy = cursor.getInt(cursor.getColumnIndex("woodBuy"));
        woodSell = cursor.getInt(cursor.getColumnIndex("woodSell"));
        stoneBuy = cursor.getInt(cursor.getColumnIndex("stoneBuy"));
        stoneSell = cursor.getInt(cursor.getColumnIndex("stoneSell"));
        marketplaceLVL = cursor.getInt(cursor.getColumnIndex("marketplaceLVL"));

        if (!cursor.isClosed()) {
            cursor.close();
        }

        if (marketplaceLVL > 0) {
            btnWoodBuy.setVisibility(View.VISIBLE);
            btnWoodSell.setVisibility(View.VISIBLE);
            btnStoneBuy.setVisibility(View.VISIBLE);
            btnStoneSell.setVisibility(View.VISIBLE);
            marketBuildPrice.setVisibility(View.GONE);
            marketplaceBuild.setVisibility(View.GONE);
            marketWoodPriceB.setVisibility(View.VISIBLE);
            marketWoodPriceS.setVisibility(View.VISIBLE);
            marketStonePriceB.setVisibility(View.VISIBLE);
            marketStonePriceS.setVisibility(View.VISIBLE);
            marketWoodText.setVisibility(View.VISIBLE);
            marketStoneText.setVisibility(View.VISIBLE);
        }

        marketWoodPriceS.setText("100/" + woodBuy + "g");
        marketWoodPriceB.setText(woodSell + "g/100");
        marketStonePriceS.setText("100/" + stoneBuy + "g");
        marketStonePriceB.setText(stoneSell + "g/100");

        btnWoodBuy.setOnClickListener(this);
        btnWoodSell.setOnClickListener(this);
        btnStoneSell.setOnClickListener(this);
        btnStoneBuy.setOnClickListener(this);
        marketplaceBuild.setOnClickListener(this);

        game_main.currentFragment = getFragmentManager().findFragmentById(R.id.fragment);

        return view;
    }

    public static void restartPrice() {
        woodBuy = woodBuyDef;
        woodSell = woodSellDef;
        stoneBuy = stoneBuyDef;
        stoneSell = stoneSellDef;

        if (game_main.currentFragment instanceof marketplace) {
            marketWoodPriceS.setText("100/" + woodBuy + "g");
            marketWoodPriceB.setText(woodSell + "g/100");
            marketStonePriceS.setText("100/" + stoneBuy + "g");
            marketStonePriceB.setText(stoneSell + "g/100");
        }

        game_main.db.update(game_main.ID, "woodBuy",woodBuy);
        game_main.db.update(game_main.ID, "woodSell",woodSell);
        game_main.db.update(game_main.ID, "stoneBuy",stoneBuy);
        game_main.db.update(game_main.ID, "stoneSell",stoneSell);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.marketplaceBuild) {
            if(marketplaceLVL <1)
            {
                if(game_main.wood >= 400 && game_main.stone>=450)
                {
                    game_main.wood -=400;
                    game_main.stone -=450;
                    marketplaceLVL = 1;
                    game_main.db.updateResources(game_main.ID, game_main.wood, game_main.stone, game_main.gold);
                    game_main.db.update(game_main.ID,"marketplaceLVL",1 );
                    btnWoodBuy.setVisibility(View.VISIBLE);
                    btnWoodSell.setVisibility(View.VISIBLE);
                    btnStoneBuy.setVisibility(View.VISIBLE);
                    btnStoneSell.setVisibility(View.VISIBLE);
                    marketBuildPrice.setVisibility(View.GONE);
                    marketplaceBuild.setVisibility(View.GONE);
                    marketWoodPriceB.setVisibility(View.VISIBLE);
                    marketWoodPriceS.setVisibility(View.VISIBLE);
                    marketStonePriceB.setVisibility(View.VISIBLE);
                    marketStonePriceS.setVisibility(View.VISIBLE);
                    marketWoodText.setVisibility(View.VISIBLE);
                    marketStoneText.setVisibility(View.VISIBLE);

                    game_main.updateText();
                }
            }
        }
        if (v.getId() == R.id.woodBuy) {
            if(game_main.gold >= woodBuy)
            {
                game_main.gold -= woodBuy;
                game_main.wood += 100;
                woodBuy += (0.2*woodBuy);
                game_main.db.updateResources(game_main.ID, game_main.wood, game_main.stone, game_main.gold);
                game_main.db.update(game_main.ID, "woodBuy",woodBuy);
                marketWoodPriceS.setText("100/" + woodBuy + "g");
                game_main.updateText();
            }
        }
        if (v.getId() == R.id.woodSell) {
            if(game_main.wood >= 100)
            {
                game_main.gold += woodSell;
                game_main.wood -= 100;
                woodSell -= (0.2*woodSell);
                if(woodSell<0)
                {
                    woodSell=0;
                }
                game_main.db.updateResources(game_main.ID, game_main.wood, game_main.stone, game_main.gold);
                game_main.db.update(game_main.ID, "woodSell",woodSell);
                marketWoodPriceB.setText(woodSell + "g/100");
                game_main.updateText();
            }
        }
        if (v.getId() == R.id.stoneBuy) {
            if(game_main.gold >= stoneBuy)
            {
                game_main.gold -= stoneBuy;
                game_main.stone += 100;
                stoneBuy += (0.3*stoneBuy);
                game_main.db.updateResources(game_main.ID, game_main.wood, game_main.stone, game_main.gold);
                game_main.db.update(game_main.ID, "stoneBuy",stoneBuy);
                marketStonePriceS.setText("100/" + stoneBuy + "g");
                game_main.updateText();
            }
        }
        if (v.getId() == R.id.stoneSell) {
            if(game_main.stone >= 100)
            {
                game_main.gold += stoneSell;
                game_main.stone -= 100;
                stoneSell -= (0.3*stoneSell);
                if(stoneSell<0)
                {
                    stoneSell=0;
                }
                game_main.db.updateResources(game_main.ID, game_main.wood, game_main.stone, game_main.gold);
                game_main.db.update(game_main.ID, "stoneSell",stoneSell);
                marketStonePriceB.setText(stoneSell + "g/100");
                game_main.updateText();
            }
        }
    }
}