package com.mols1993.solitariopiramide;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

import java.io.IOException;
import java.util.List;

/**
 * Created by cypher on 6/23/17.
 */

public class Deck extends android.support.v7.widget.AppCompatImageButton{

    MainActivity main;
    int currentCard = 0;
    public Deck(final Context context, String fileName, int width, final List<Card> deckList) {
        super(context);
        main = (MainActivity) context;
        setMaxWidth(width);
        setMinimumWidth(width);
        setMinimumHeight((int) (width * 1.333));
        setMaxHeight((int) (width * 1.333));
        try {
            Drawable d = Drawable.createFromStream(context.getAssets().open(fileName), null);
            setBackground(d);
        } catch (IOException e) {
            e.printStackTrace();
        }

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    main.drawCard(deckList.get(currentCard));
                    currentCard++;
                    if(currentCard == deckList.size()){
                        currentCard = 0;
                    }
                }
                return false;
            }
        });
    }
}
