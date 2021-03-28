package main.java;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Reversie {
    private static final int BLACK = 0;
    private static final int WHITE = 1;
    private static final int EMPTY = 2;

    public  static final int BLACK_WIN    = 0;
    public  static final int DRAW         = 1;
    public  static final int UNCLEAR      = 2;
    public  static final int WHITE_WIN    = 3;

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

    public void playMove(int x, int y){
        // TODO: Fill board based on player color (better implementation)
        Random random = new Random();
        int color = random.nextInt(2);
        board[x][y] = color;
    }

    public boolean legalMove(int x, int y){
        // TODO: Check if the move is legal
        return true;
    }

    public boolean moveOK(int x, int y){
        if (x < 0 || y < 0 || x >board.length || y > board.length){
            return false;
        }
        return board[x][y] == EMPTY;
    }

    public static void main(String[] args) {
        Reversie reversie = new Reversie();
        Scanner reader = new Scanner(System.in);
        String[] coords;
        while (true) {
            System.out.println(reversie.toString());
            int x;
            int y;

                System.out.println("Enter X and Y");
                String line = reader.nextLine();
                coords = line.split(",");
                x = Integer.parseInt(coords[0]);
                y = Integer.parseInt(coords[1]);
                reversie.playMove(x,y);



        }
    }
}
