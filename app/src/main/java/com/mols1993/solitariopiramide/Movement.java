package com.mols1993.solitariopiramide;

/**
 * Created by Camilo Vega on 20-06-2017.
 */

public class Movement {
    public boolean draw;
    public int num1,num2;
    public int[] pos1, pos2;
    public char type1,type2;

    public Movement(int n1,int n2, char t1, char t2, int[] p1 , int[] p2){
        num1=n1;
        num2=n2;
        pos1 = p1;
        pos2 = p2;
        type1 = t1;
        type2 = t2;
        draw = false;
    }

    public Movement(){
        draw = true;
    }
}
