package com.example.se2_exploding_kittens.cards;

import com.example.se2_exploding_kittens.R;

public class NopeCard extends Cards{
    public NopeCard() {


    }

    private static int imageResource = R.drawable.nopecard;
    @Override
    public int getImageResource() {
        return imageResource;
    }

    @Override
    public void cardAction(Player p1, Player p2) {

    }
}
