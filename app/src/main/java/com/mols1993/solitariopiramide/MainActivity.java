package com.mols1993.solitariopiramide;

import android.content.Context;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

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

        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.mainLayout);

        for(int i = 0; i < 7; i++){
            LinearLayout ll = new LinearLayout(this);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.weight = 1;
            params.gravity = Gravity.CENTER_HORIZONTAL;
            ll.setLayoutParams(params);

            for(int j = 0; j < i + 1; j++){
                Card c = new Card(this, width/7, 6.5, 'A');
                board[i][j] = c;
                Log.i("Card", "In " + i + " " + j);
                ll.addView(c);
            }
            mainLayout.addView(ll);
        }
        checkClickables();
    }

    public void move(Card c){
        if(selected[0] == null){
            selected[0] = c;
            Log.i("Move", String.valueOf(c.number));
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
                        if(board[i][j].getVisibility() == View.VISIBLE) {
                            board[i][j].clickable(true);
                            Log.i("Clickable", i + " " + j);
                        }
                    }
                    else if(board[i + 1][j].getVisibility() == View.INVISIBLE || board[i + 1][j + 1].getVisibility() == View.INVISIBLE){
                        if(board[i][j].getVisibility() == View.VISIBLE) {
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
