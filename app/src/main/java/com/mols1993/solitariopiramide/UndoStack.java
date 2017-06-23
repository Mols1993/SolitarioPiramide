package com.mols1993.solitariopiramide;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;



import java.util.List;
import java.util.Stack;

/**
 * Created by Camilo Vega on 20-06-2017.
 */

public class UndoStack {
    Stack<Movement> moveStack;
    Stack<Card> laPila;
    List<Card> deck;
    Card[][] board;
    Context context;
    int width;

    public UndoStack(Card[][] b, Stack<Card> p, List<Card> d, int w){
        moveStack = new Stack<Movement>();
        board = b;
        laPila = p;
        deck = d;
        width = w;
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
                deck.add(laPila.pop());
            }
            else{
                int[] laPilaPos = {-1,-1};
                if (m.pos1 != laPilaPos) {
                    board[m.pos1[0]][m.pos1[1]].setVisibility(View.VISIBLE);
                    if(m.num1!=13){
                        if(m.pos2 != laPilaPos){
                            board[m.pos2[0]][m.pos2[1]].setVisibility(View.VISIBLE);
                        }
                        else{
                            laPila.push(new Card(context,width,m.num2,m.type2));
                        }
                    }
                }
                else{
                    //cuando esté implementada la Pila hay que hacer que el elemento se agre
                    laPila.push(new Card(context,width,m.num1,m.type1));
                    if(m.num1!=13){
                        board[m.pos2[0]][m.pos2[1]].setVisibility(View.VISIBLE);
                    }
                }
            }

        }
    }



}
