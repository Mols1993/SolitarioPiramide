package com.mols1993.solitariopiramide;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created by cypher on 6/20/17.
 */

//P = Pica, C = Corazón, D = Diamante, T = Trebol
public class Card extends android.support.v7.widget.AppCompatButton{
    public double number = 0;
    public char type = 'A';
    MainActivity main;
    public Card(Context context, int width, double num, char t) {
        super(context);
        setWidth(width);
        setMaxWidth(width);
        setMinimumWidth(width);
        number = num;
        type = t;
        main = (MainActivity) context;
        setText(String.valueOf(number) + String.valueOf(type));
        clickable(true);
    }

    public void clickable(boolean state){
        final Card c = this;

        if(state){
            setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    Card card = c;
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        main.move(card);
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
    }
}
