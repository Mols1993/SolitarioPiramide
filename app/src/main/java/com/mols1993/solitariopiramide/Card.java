package com.mols1993.solitariopiramide;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

import java.io.IOException;

/**
 * Created by cypher on 6/20/17.
 */

//P = Pica, C = Corazón, D = Diamante, T = Trebol
public class Card extends android.support.v7.widget.AppCompatImageButton{
    public int number = 0;
    public char type = 'A';
    MainActivity main;
    String fileName = "";
    public Card(Context context, int width, int num, char t) {
        super(context);
        setMaxWidth(width);
        setMinimumWidth(width);
        setMinimumHeight((int) (width * 1.333));
        setMaxHeight((int) (width * 1.333));
        number = num;
        type = t;
        main = (MainActivity) context;
        fileName = "cards/" + type + String.valueOf(num) + ".png";
        Log.i("FileName", fileName);
        try {
            Drawable d = Drawable.createFromStream(context.getAssets().open(fileName), null);
            setBackground(d);
        } catch (IOException e) {
            e.printStackTrace();
        }
        clickable(true);
    }

    public void clickable(boolean state){
        final Card c = this;
        c.setAlpha(1f);

        if(state){
            setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    Card card = c;
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        main.move(card);
                        c.setAlpha(0.8f);
                    }
                    return false;
                }
            });
        }
        else {
            setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return false;
                }
            });
        }
    }

    public void delete(){
        setVisibility(View.INVISIBLE);
        clickable(false);
        main.updateTop();
    }

    public void changeCard(Context context, Card card){
        number = card.number;
        type = card.type;
        fileName = card.fileName;
        try {
            Drawable d = Drawable.createFromStream(context.getAssets().open(fileName), null);
            setBackground(d);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
