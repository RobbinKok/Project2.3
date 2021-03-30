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
    private int side=BLACK; 
    private int blackScore;
    private int whiteScore;


    public Reversie() {
        resetBoard();
    }

    public int getBlackScore() {
        return blackScore;
    }

    public int getWhiteScore() {
        return whiteScore;
    }

    @Override
    public String toString() {
        String output = "";
        for (int i = 0; i < board.length; i++) {
            for(int j = 0; j < board[i].length; j++) {
                if (board[i][j] != EMPTY) {
                    output += board[i][j] + " ";
                } else {
                    output += "- ";
                }
            }
            output += "\n";
        }
        return output;
    }

    private void resetBoard(){
        for (int i = 0; i < board.length; i++) {
            Arrays.fill(board[i], EMPTY);
        }
        board[3][3] = WHITE;
        board[3][4] = BLACK;
        board[4][4] = WHITE;
        board[4][3] = BLACK;
    }
 
    public void playMove(int x, int y) {
        // TODO: Fill board based on player color (better implementation)
        board[x][y] = side;
        if (side==BLACK) this.side=WHITE; else this.side=BLACK;
    }

    public void move(String[] coords) {
        int x = Integer.parseInt(coords[0]);
        int y = Integer.parseInt(coords[1]);
        if (moveOK(x, y)) {
            playMove(x, y);
        }
    }

    public void flipColor(int k, int s, int player, int x, int y) {
        for (int c=k-1; c<=k+1; c++) {
            for (int u=s-1; u<=s+1; u++) {
                if (board[k][s]!=player && board[k][s]!=EMPTY) {
                                          
                }
            }
        }
    }

    public int checkBorders(int x, int y, int player) {
            for (int k=x-1; k<=x+1; k++) {
                for (int s=y-1; s<=y+1; s++) {
                    if (board[k][s]!=player && board[k][s]!=EMPTY) {
                        flipColor(k, s, player, x, y);                        
                    }
                }
            }
        return null;
    }

    public int[][] possibleMoves(int player) {
        for (int i=0; i<board.length; i++) {
            for (int j=0; j<board.length; j++) {
                if (board[i][j] == player) {
                    checkBorders(i, j, player);
                }
            }
        }
        return null;
    }

    public boolean legalMove(int x, int y) {
        // TODO: Check if the move is legal
        return true;
    }

    public boolean moveOK(int x, int y) {
        if (x < 0 || y < 0 || x >= board.length || y >= board.length) {
            System.out.println("Move out of bounds.");
            return false;
        }
        return board[x][y] == EMPTY;
    }
    
    public boolean blackTurn() {
        return null;
    }

    public boolean gameOver() {
        return null;
    }

    public static void main(String[] args) {
        Reversie reversie = new Reversie();
        Scanner reader = new Scanner(System.in);
        String[] coords;
        while (true) {
            System.out.println(reversie.toString());
                System.out.println("Enter: X,Y");
                String line = reader.nextLine();
                coords = line.split(",");
                reversie.move(coords);
        }
    }
}
