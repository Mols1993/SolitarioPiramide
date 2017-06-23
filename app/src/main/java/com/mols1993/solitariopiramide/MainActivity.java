package com.mols1993.solitariopiramide;

import android.content.Context;
import android.graphics.Point;
import android.support.annotation.IntegerRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public Card[] selected = new Card[2];
    Card[][] board = new Card[7][7];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point s = new Point();
        display.getSize(s);
        int width = s.x;
        int height = s.y;
        Log.i("Width", String.valueOf(width));


        List<String> cardList = new ArrayList<>();
        List<Card> deck = new ArrayList<>();

        try {
            Collections.addAll(cardList, getAssets().list("cards"));
            cardList.remove(cardList.size() - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        cardList.remove(0);
        cardList.remove(0);

        Collections.shuffle(cardList);

        for(int i = 0; i < cardList.size(); i++){
            String[] name = cardList.get(i).split("\\.");
            String type = name[0].substring(0,1);
            String value = name[0].substring(1);
            Log.i("Card", type + " " + value);
            Card c = new Card(this, width/7, Integer.parseInt(value), type.charAt(0));
            deck.add(c);
        }

        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.mainLayout);

        int counter = 0;

        for(int i = 0; i < 7; i++){
            LinearLayout ll = new LinearLayout(this);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.weight = 1;
            params.gravity = Gravity.CENTER_HORIZONTAL;
            if(i > 0) {
                params.setMargins(0, (int) -(((width / 7) * 1.5)), 0, 0);
            }
            ll.setLayoutParams(params);

            for(int j = 0; j < i + 1; j++){
                board[i][j] = deck.get(counter);
                ll.addView(board[i][j]);
                counter++;
            }
            mainLayout.addView(ll);
        }
        checkClickables();
    }

    public void move(Card c){
        if(selected[0] == null){
            selected[0] = c;
            Log.i("Move", String.valueOf(c.number));
            if(selected[0].number == 13){
                selected[0].delete();
                selected[0] = null;
                checkClickables();
            }
        }
        else{
            selected[1] = c;
            if(selected[0].number + selected[1].number == 13 && selected[0] != selected[1]){
                selected[0].delete();
                selected[1].delete();
            }
            selected[0] = null;
            selected[1] = null;
            checkClickables();
        }
    }

    public void checkClickables(){
        for(int i = 0; i < 7; i++){
            for(int j = 0; j < 7; j++){
                if(board[i][j] != null){
                    if (i == 6){
                        if(board[i][j].getVisibility() == View.VISIBLE){
                            board[i][j].clickable(true);
                            Log.i("Clickable", i + " " + j);
                        }
                    }
                    else if((board[i + 1][j].getVisibility() == View.INVISIBLE || board[i + 1][j + 1].getVisibility() == View.INVISIBLE) && board[i][j].number != 13){
                        if(board[i][j].getVisibility() == View.VISIBLE){
                            board[i][j].clickable(true);
                            Log.i("Clickable", i + " " + j);
                        }
                    }
                    else if(board[i + 1][j].getVisibility() == View.INVISIBLE && board[i + 1][j + 1].getVisibility() == View.INVISIBLE && board[i][j].number == 13){
                        if(board[i][j].getVisibility() == View.VISIBLE){
                            board[i][j].clickable(true);
                            Log.i("Clickable", i + " " + j);
                        }
                    }
                    else{
                        board[i][j].clickable(false);
                    }
                }
            }
        }
    }
}
