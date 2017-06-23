package com.mols1993.solitariopiramide;

import android.content.Context;
import android.view.View;


import java.util.List;
import java.util.Stack;

/**
 * Created by Camilo Vega on 20-06-2017.
 */

public class UndoStack {
    Stack<Movement> moveStack;
    Stack<Card> deckPileStack;
    Deck deck;
    Card[][] board;
    Context context;
    int width;

    public UndoStack(Card[][] b, Stack<Card> p, Deck d, int w,Context c){
        moveStack = new Stack<Movement>();
        board = b;
        deckPileStack = p;
        deck = d;
        width = w;
        context = c;
    }

    public void draw(){
        moveStack.push(new Movement());
    }

    public void deleteCards(int n1, int n2, char t1, char t2, int[] p1, int[] p2){
        moveStack.push(new Movement(n1,n2,t1,t2,p1,p2));
    }

    public void undo(){
        if(!moveStack.empty()){
            Movement m = moveStack.pop();
            if(m.draw){
                //cuando se esté implementado el mazo y la pila.
                deck.currentCard--;
                deckPileStack.pop();
            }
            else{
                int[] laPilaPos = {-1,-1};
                if (m.pos1[0]!=-1 && m.pos1[1]!=-1) {
                    board[m.pos1[0]][m.pos1[1]].setVisibility(View.VISIBLE);
                    board[m.pos1[0]][m.pos1[1]].clickable(true);
                    if(m.num1!=13){
                        if(m.pos2[0]!=-1 && m.pos2[1]!=-1){
                            board[m.pos2[0]][m.pos2[1]].setVisibility(View.VISIBLE);
                            board[m.pos2[0]][m.pos2[1]].clickable(true);
                        }
                        else{
                            deckPileStack.push(new Card(context,width,m.num2,m.type2));
                        }
                    }
                }
                else{
                    //cuando esté implementada la Pila hay que hacer que el elemento se agre
                    deckPileStack.push(new Card(context,width,m.num1,m.type1));
                    if(m.num1!=13){
                        board[m.pos2[0]][m.pos2[1]].setVisibility(View.VISIBLE);
                        board[m.pos2[0]][m.pos2[1]].clickable(true);
                    }
                }
            }

        }
    }



}
