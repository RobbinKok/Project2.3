//package java;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

import javafx.scene.paint.Color;

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

    OrthelloGameController gui;

    public Reversie(OrthelloGameController gui)
    {
        this.gui = gui;
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
        gui.changeNodeColor(3, 4, Color.WHITE);
        board[3][3] = BLACK;
        gui.changeNodeColor(3, 3, Color.BLACK);
        board[4][3] = WHITE;
        gui.changeNodeColor(4, 3, Color.WHITE);
        board[4][4] = BLACK;
        gui.changeNodeColor(4, 4, Color.BLACK);
    }

    public void playMove(int x, int y) {
        // TODO: Fill board based on player color (better implementation)
        board[x][y] = side;
        // gui
        if(side == BLACK) gui.changeNodeColor(x, y, Color.BLACK); else gui.changeNodeColor(x, y, Color.WHITE);

        if (side==BLACK) this.side=WHITE; else this.side=BLACK;
        gui.updateCurrentPlayer(side);
        findAllScores();
        // sets the score in the gui
        gui.setScore(blackScore, whiteScore);
        // adds the move to the movelist in the gui
        gui.addMove(side, x+1, y+1);
    }

    public void move(String[] coords) {
        int x = Integer.parseInt(coords[0]);
        int y = Integer.parseInt(coords[1]);
        System.out.println("Is this a corner?: " +  isCorner(x,y));
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
                    dir++;
                    continue;
                }
                if (board[s][k]!=player && board[s][k]!=EMPTY && direction==4) {
                    if (flip(player, s, k, dir))
                    {
                        if(side == BLACK) gui.changeNodeColor(s, k, Color.BLACK); else gui.changeNodeColor(s, k, Color.WHITE);

                        board[s][k]=player;
                    }
                }
                if (board[s][k]!=player && board[s][k]!=EMPTY && direction==dir && dir!=4) {
                    if (flip(player, s, k, dir)) {
                        board[s][k]=player;
                        if(side == BLACK) gui.changeNodeColor(s, k, Color.BLACK); else gui.changeNodeColor(s, k, Color.WHITE);

                        return true;
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
                    if (k<0 || k>=board.length || s<0 || s>=board.length || board[s][k]==player) {
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
                    if (k<0 || k>=board.length || s<0 || s>=board.length) {
                        dir++;
                        continue;
                    }
                    if (board[s][k]!=player && board[s][k]!=EMPTY && dir!=4) {
                        // return flipColor(k, s, player, x, y);
                        int[] tmp = canFlip(s, k, player, dir);
                        if(tmp[0]!=-1) {
                            output.add(tmp);
                        }
                    }
                    if(dir==5) {

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
        return false;
    }

    public boolean gameOver() {
        return false;
    }

    public boolean isCorner(int r, int c){
        if (r == 0 && c == 0 || r == 7 && c ==7 || r == 0 && c == 7 || r == 7 && c == 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isAroundCorner(int r, int c){
        if (r == 0 && c ==1 || r == 1 && c == 0 || r == 1 && c == 1 ){
            return true;
        } else if (r == 6 && c == 0 || r == 7 && c == 1 || r == 6 && c == 1){
            return true;
        } else if (r == 0 && c == 6 || r ==1 && c == 6 || r ==1 && c == 7){
            return true;
        } else if (r == 6 && c == 7 || r == 7 && c ==6 || r == 6 && c == 6){
            return true;
        } else {
            return  false;
        }
    }

    void findAllScores() {
        blackScore = 0;
        whiteScore = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == WHITE) {
                    whiteScore++;
                } else if (board[i][j] == BLACK) {
                    blackScore++;
                }
            }
        }
    }

    public String[] chooseMove() {
        Best move = this.chooseMove(WHITE);
        return new String[]{String.valueOf(move.row), String.valueOf(move.column)};
    }

    private Best chooseMove(int _side) {
        int opponent = EMPTY;
        int simpleEval;
        int bestRow = -1;
        int bestColumn = -1;
        int value = Integer.MIN_VALUE;
        ArrayList<int[]> possibleMoves = possibleMoves(_side);

        if (_side == BLACK) {
            opponent = WHITE;
        }
        if (_side == WHITE) {
            opponent = BLACK;
        }


        //TODO: iedere possible move doorlopen en daar een minimax op uitoefenen
        for (int[] move : possibleMoves) {
            int x = move[0];
            int y = move[1];
            board[x][y] = _side;
            int[] moveVal = minmax(opponent, _side, 1, x, y);

            board[x][y] = EMPTY;

            if (moveVal[0] > value) {
                bestRow = y;
                bestColumn = x;
                value = moveVal[0];
            }
        }

        return new Best(value, bestColumn, bestRow);
    }

    private int[] minmax(int _side, int opp, int depth, int current_x, int current_y) {
        if (isCorner(current_x, current_y)) {
            return new int[]{_side == WHITE ? 1000 : -1000, depth};
        } else if (isAroundCorner(current_x, current_y)) {
            return new int[]{_side == WHITE ? -1000 : 1000, depth};
        } else if (depth == 8 /* || game over*/) {
            findAllScores();
            if (_side == WHITE) {
                return new int[]{whiteScore, depth};
            } else if (_side == BLACK) {
                return new int[]{-blackScore, depth};
            }
        }


        int min = 1000;
        int max = -1000;

        ArrayList<int[]> possibleMoves = possibleMoves(_side);

        for (int[] move : possibleMoves) {
            int x = move[0];
            int y = move[1];

            board[x][y] = _side;

            int[] result = minmax(opp, _side, depth + 1, x, y);
            if (_side == WHITE) {
                max = Math.max(max, result[0]);
            } else if (_side == BLACK) {
                min = Math.min(min, result[0]);
            }

            board[x][y] = EMPTY;
        }

        return _side == WHITE ? new int[]{max, depth} : new int[]{min, depth};
    }

    public boolean computerPlays() {
        return this.side == WHITE;
    }

    private class Best {
        int row;
        int column;
        int val;

        public Best(int v) {
            this(v, 0, 0);

        }

        public Best(int v, int r, int c) {
            val = v;
            row = r;
            column = c;
        }
    }

}
