package main.java;

import java.util.ArrayList;
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
                if (board[j][i] != EMPTY) {
                    output += board[j][i] + " ";
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
        board[3][4] = WHITE;
        board[3][3] = BLACK;
        board[4][3] = WHITE;
        board[4][4] = BLACK;
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
            flip(side, x, y, 4);
            playMove(x, y);
        }
    }

    public boolean flip(int player, int x, int y, int direction) {
        int dir = 0;
        for (int k=y-1; k<=y+1; k++) {
            for (int s=x-1; s<=x+1; s++) {
                if ( k<0 || k>=board.length || s<0 || s>=board.length) {
                    System.out.println("poop");
                    continue;
                }
                if (board[s][k]!=player && board[s][k]!=EMPTY && direction==4) {
                    if (flip(player, s, k, dir)) {
                        board[s][k]=player;
                    } 
                }
                if (direction!=4 && dir==direction) {
                    if (board[s][k]==player) {
                        return true;
                    }
                }
                dir++;
            }
        }
        return false;
    }

    /**
     * return -1,-1 direction heeft geen mogelijke zet.
     */
    public int[] canFlip(int x, int y, int player, int direction) {
        int dir = 0;
        for (int k=y-1; k<=y+1; k++) {
            for (int s=x-1; s<=x+1; s++) {
                if (direction==dir) {
                    if ( k<0 || k>=board.length || s<0 || s>=board.length || board[s][k]==player) {
                        int[] output = {-1, -1};
                        return output;
                    }
                    if (board[s][k]!=player && board[s][k]!=EMPTY) {
                        return canFlip(s, k, player, direction);
                    }
                    // System.out.println("x= "+ s+ " y= "+ k);
                    // System.out.println(dir+" (dir) &"+ direction+" (direction)");
                    int[] output1 = {s, k};
                    return output1;
                }
                dir++;
            }
        }        
        int[] tmp = {-1, -1};
        return tmp;
    } 


    /** 
     * 
     * |0|1|2|
     * |3|4|5|
     * |6|7|8|
     * 
     * **/
    public ArrayList<int[]> checkBorders(int x, int y, int player) {
        ArrayList<int[]> output = new ArrayList<>();
        int dir = 0;
            for (int k=y-1; k<=y+1; k++) {
                for (int s=x-1; s<=x+1; s++) {
                    if (board[s][k]!=player && board[s][k]!=EMPTY && dir!=4) {
                        // return flipColor(k, s, player, x, y);
                        int[] tmp = canFlip(s, k, player, dir);          
                        if(tmp[0]!=-1) {
                            output.add(tmp);
                        }              
                    }
                    if(dir ==5) {
                        
                        System.out.println("side= "+side);
                        System.out.println("dir 5: "+s+", "+k);
                        System.out.println("player: "+ board[x][y]);
                    }
                    dir++;
                }
            }
        return output;
    }

    public ArrayList<int[]> possibleMoves(int player) {
        ArrayList<int[]> output = new ArrayList<>(); 
        for (int i=0; i<board.length; i++) {
            for (int j=0; j<board.length; j++) {
                if (board[j][i] == player) {
                    ArrayList<int[]> tmp = checkBorders(j, i, player);
                    output.addAll(tmp);
                }
            }
        }
        output.forEach(e -> {System.out.println("mogelijke zet: "+ e[0]+","+e[1]);});
        return output;
    }

    public boolean legalMove(int x, int y) {
        // TODO: Check if the move is legal
        return true;
    }

    public boolean moveOK(int x, int y) {
        ArrayList<int[]> moves = possibleMoves(side);
        if (x < 0 || y < 0 || x >= board.length || y >= board.length) {
            System.out.println("Move out of bounds.");
            return false;
        }
        for (int[] move : moves) {
            if (move[0]==x && move[1]==y) return true;
        }
        System.out.println("Non-legal move.");
        return false;
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
