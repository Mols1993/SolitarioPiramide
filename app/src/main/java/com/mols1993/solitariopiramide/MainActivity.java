package com.mols1993.solitariopiramide;

import android.content.Context;
import android.graphics.Point;
import android.os.Debug;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.text.Layout;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    public Card[] selected = new Card[2];
    Card[][] board = new Card[7][7];
    Stack<Card> deckPileStack = new Stack<>();
    Card deckPileTop;
    UndoStack undo;
    TextView txt;
    Deck d;
    AlertDialog alert;
    Chronometer chronoTrigger;

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

        chronoTrigger = (Chronometer) findViewById(R.id.chronoTrigger);
        chronoTrigger.start();

        Switch nintendoSwitch = (Switch) findViewById(R.id.switchCardBG);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(R.layout.popup);
        alert = builder.create();

        List<String> cardList = new ArrayList<>();
        List<Card> deck = new ArrayList<>();
        List<Card> remainingDeck;


        try {
            Collections.addAll(cardList, getAssets().list("cards"));
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

        remainingDeck = deck.subList(counter, deck.size());

        d = new Deck(this, "cards/BA.png", width/7, remainingDeck);

        nintendoSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    d.setBG("cards/BR.png");
                }
                else{
                    d.setBG("cards/BA.png");
                }
            }
        });

        undo = new UndoStack(board, deckPileStack, d, width/7,this);

        LinearLayout ll = new LinearLayout(this);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.weight = 0;
        params.gravity = Gravity.LEFT;
        ll.setLayoutParams(params);
        ll.addView(d);

        deckPileTop = new Card(this, width/7, 1, 'C');
        deckPileTop.setVisibility(View.INVISIBLE);
        ll.addView(deckPileTop);

        Button btn = new Button(this);
        btn.setText("Deshacer");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                undo.undo();
                updateTop();
                updateTxt();
                checkClickables();
            }
        });
        ll.addView(btn);

        txt = new TextView(this);
        ll.addView(txt);

        mainLayout.addView(ll);
    }

    public void drawCard(Card currentCard){
        Log.i("FN", currentCard.fileName);
        deckPileTop.setVisibility(View.VISIBLE);
        deckPileStack.push(currentCard);
        undo.draw();
        updateTxt();
        updateTop();
    }

    public void updateTxt(){
        txt.setText(String.valueOf(undo.moveStack.size()));
    }

    public void updateTop(){
        Log.i("Top", "algo");
        if(!deckPileStack.empty()) {
            deckPileTop.setVisibility(View.VISIBLE);
            deckPileTop.changeCard(this, deckPileStack.peek());
            deckPileTop.clickable(true);
        }
        else {
            deckPileTop.setVisibility(View.INVISIBLE);
        }
    }

    public void move(Card c){
        if(selected[0] == null){
            selected[0] = c;
            Log.i("Move", String.valueOf(c.number));
            if(selected[0].number == 13){
                selected[0].delete();
                int[] p1 = getPos(selected[0]);
                undo.deleteCards(selected[0].number, 0, selected[0].type, '0', p1, p1);
                if(p1[0] == -1){
                    deckPileStack.pop();
                    updateTop();
                }
                selected[0] = null;
                checkClickables();
            }
        }
        else{
            selected[1] = c;
            if(selected[0].number + selected[1].number == 13 && selected[0] != selected[1]){
                if(!deckPileStack.empty()) {
                    if (selected[0].fileName == deckPileStack.peek().fileName || selected[1].fileName == deckPileStack.peek().fileName) {
                        deckPileStack.pop();
                    }
                }
                int[] p1, p2;
                p1 = getPos(selected[0]);
                p2 = getPos(selected[1]);
                undo.deleteCards(selected[0].number, selected[1].number, selected[0].type, selected[1].type, p1, p2);
                selected[0].delete();
                selected[1].delete();
            }
            selected[0] = null;
            selected[1] = null;
            checkClickables();
        }
        updateTxt();
    }

    public int[] getPos(Card card){
        int[] p1 = {-1, -1};
        for(int i = 0; i < 7; i++){
            for(int j = 0; j < i + 1; j++){
                if(board[i][j].fileName == card.fileName){
                    p1[0] = i;
                    p1[1] = j;
                }
            }
        }
        return p1;
    }

    public boolean checkWin(){
        for(int i = 0; i < 7; i++){
            for(int j = 0; j < i + 1; j++){
                if(board[i][j] != null){
                    if(board[i][j].getVisibility() == View.VISIBLE){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void checkClickables(){
        if(checkWin()){
            txt.setText("Pirámide Completada!");
            chronoTrigger.stop();
        }
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

    public void instrucciones(View v){
        alert.show();
    }
}
