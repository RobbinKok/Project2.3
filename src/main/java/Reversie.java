package main.java;

import java.util.Arrays;

public class Reversie {
    private static final int BLACK = 0;
    private static final int WHITE = 1;
    private static final int EMPTY = 2;


    private int[][] board = new int[8][8];

    public Reversie() {
        resetBoard();
    }

    private void resetBoard(){
        for (int i = 0; i < board.length; i++){
            Arrays.fill(board[i], EMPTY);
        }
        board[3][3] = WHITE;
        board[3][4] = BLACK;
        board[4][4] = WHITE;
        board[4][3] = BLACK;
    }

    @Override
    public String toString() {
        String output = "";
        for (int i = 0; i < board.length; i++){
            for(int j = 0; j < board[i].length; j++){
                if (board[i][j] != EMPTY){
                    output += board[i][j] + " ";
                } else {
                    output += "- ";
                }
            }
            output += "\n";
        }
        return output;
    }

    public static void main(String[] args) {
        Reversie reversie = new Reversie();
        System.out.println(reversie.toString());
    }
}
