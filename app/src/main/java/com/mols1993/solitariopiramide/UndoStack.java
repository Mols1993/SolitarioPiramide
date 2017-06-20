package com.mols1993.solitariopiramide;

import android.content.Context;
import android.widget.Button;

import java.util.Stack;

/**
 * Created by Camilo Vega on 20-06-2017.
 */

public class UndoStack {
    Stack<Movement> stack;

    public UndoStack(){
        stack = new Stack<Movement>();
    }

    public void draw(){
        stack.push(new Movement());
    }

    public void deleteCards(int n1, int n2, char t1, char t2, int[] p1, int[] p2){
        stack.push(new Movement(n1,n2,t1,t2,p1,p2));
    }

    public void undo(){
        if(!stack.empty()){
            Movement m = stack.pop();

        }
    }



}
