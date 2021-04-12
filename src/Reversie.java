//package java;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;


import javafx.application.Platform;
import AI.Game;
import javafx.scene.paint.Color;

public class Reversie extends Game {
    public static final int BLACK = 0;
    public static final int WHITE = 1;
    private static final int EMPTY = 2;

    public static final int BLACK_WIN = 0;
    public static final int DRAW = 1;
    public static final int UNCLEAR = 2;
    public static final int WHITE_WIN = 3;

    private int[][] board = new int[8][8];
    public int side = BLACK;
    private int blackScore;
    private int whiteScore;

    OthelloGameController gui = null;

    public Reversie() {
        resetBoard();
    }

    public Reversie(OthelloGameController gui) {
        this.gui = gui;
        resetBoard();
        findAllScores();
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
            for (int j = 0; j < board[i].length; j++) {
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

    private void resetBoard() {
        for (int i = 0; i < board.length; i++) {
            Arrays.fill(board[i], EMPTY);
        }
        board[3][4] = BLACK;
        board[3][3] = WHITE;
        board[4][3] = BLACK;
        board[4][4] = WHITE;
    }

    public void playMove(int x, int y) {
        board[x][y] = side;

        findAllScores();

//        Platform.runLater(() -> {
        // gui
        if (gui != null) {
            if (side == BLACK)
                gui.changeNodeColor(x, y, Color.BLACK);
            else
                gui.changeNodeColor(x, y, Color.WHITE);

            gui.updateCurrentPlayer(side);

            // sets the score in the gui
            gui.setScore(blackScore, whiteScore);

            // adds the move to the movelist in the gui
            gui.addMove(side, x + 1, y + 1);
        }

        if (side == BLACK)
            this.side = WHITE;
        else
            this.side = BLACK;
//        });
    }

    public void move(String[] coords) {
        int x = Integer.parseInt(coords[0]);
        int y = Integer.parseInt(coords[1]);
//        System.out.println("Is this a corner?: " +  isCorner(x,y));
        if (moveOK(x, y)) {
            flip(side, x, y, 4);
            playMove(x, y);
            if (gameOver()) {
                winner();
            }
        }
    }

    public boolean flip(int player, int x, int y, int direction) {
        int dir = 0;
        for (int k = y - 1; k <= y + 1; k++) {
            for (int s = x - 1; s <= x + 1; s++) {
                if (k < 0 || k >= board.length || s < 0 || s >= board.length) {
                    dir++;
                    continue;
                }
                if (board[s][k] != player && board[s][k] != EMPTY && direction == 4) {
                    if (flip(player, s, k, dir)) {
                        if (gui != null)
                            if (side == BLACK) gui.changeNodeColor(s, k, Color.BLACK);
                            else gui.changeNodeColor(s, k, Color.WHITE);


                        board[s][k] = player;
                    }
                }
                if (board[s][k] != player && board[s][k] != EMPTY && direction == dir && dir != 4) {
                    if (flip(player, s, k, dir)) {
                        board[s][k] = player;
                        if (gui != null)
                            if (side == BLACK) gui.changeNodeColor(s, k, Color.BLACK);
                            else gui.changeNodeColor(s, k, Color.WHITE);

                        return true;
                    }
                }
                if (direction != 4 && dir == direction) {
                    if (board[s][k] == player) {
                        return true;
                    }
                }
                dir++;
            }
        }
        return false;
    }

    public int[] canFlip(int x, int y, int player, int direction) {
        return canFlip(x, y, player, direction, board);
    }

    /**
     * return -1,-1 direction heeft geen mogelijke zet.
     */
    public int[] canFlip(int x, int y, int player, int direction, int[][] board) {
        int dir = 0;
        for (int k = y - 1; k <= y + 1; k++) {
            for (int s = x - 1; s <= x + 1; s++) {
                if (direction == dir) {
                    if (k < 0 || k >= board.length || s < 0 || s >= board.length || board[s][k] == player) {
                        int[] output = {-1, -1};
                        return output;
                    }
                    if (board[s][k] != player && board[s][k] != EMPTY) {
                        return canFlip(s, k, player, direction, board);
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


    public ArrayList<int[]> checkBorders(int x, int y, int player) {
        return checkBorders(x, y, player, board);
    }

    /**
     * |0|1|2|
     * |3|4|5|
     * |6|7|8|
     **/
    public ArrayList<int[]> checkBorders(int x, int y, int player, int[][] board) {
        ArrayList<int[]> output = new ArrayList<>();
        int dir = 0;
        for (int k = y - 1; k <= y + 1; k++) {
            for (int s = x - 1; s <= x + 1; s++) {
                if (k < 0 || k >= board.length || s < 0 || s >= board.length) {
                    dir++;
                    continue;
                }
                if (board[s][k] != player && board[s][k] != EMPTY && dir != 4) {
                    // return flipColor(k, s, player, x, y);
                    int[] tmp = canFlip(s, k, player, dir);
                    if (tmp[0] != -1) {
                        output.add(tmp);
                    }
                }
                // if(dir==5) {
                //     System.out.println("side= "+side);
                //     System.out.println("dir 5: "+s+", "+k);
                //     System.out.println("player: "+ board[x][y]);
                // }
                dir++;
            }
        }
        return output;
    }

    public ArrayList<int[]> possibleMoves(int player) {
        ArrayList<int[]> output = new ArrayList<>();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[j][i] == player) {
                    ArrayList<int[]> tmp = checkBorders(j, i, player);
                    output.addAll(tmp);
                }
            }
        }
//        if (output.size() == 0) {
//            if (side == BLACK) this.side = WHITE;
//            else this.side = BLACK;
//        }
        // output.forEach(e -> {System.out.println("mogelijke zet: "+ e[0]+","+e[1]);});
        return output;
    }

    public boolean moveOK(int x, int y) {
        System.out.println("Checking for move: " + x + " : " + y);
        ArrayList<int[]> moves = possibleMoves(side);

        if (x < 0 || y < 0 || x >= board.length || y >= board.length) {
            System.out.println("Move out of bounds.");
            return false;
        }
        for (int[] debug : moves)
            System.out.println("Possible move: " + debug[0] + " : " + debug[1]);

        for (int[] move : moves) {
            if (move[0] == x && move[1] == y) return true;
        }
        System.out.println("x:" + x + " y:" + y);
        System.out.println("Non-legal move.");
        return false;
    }

    public void winner() {
        findAllScores();
        if (blackScore > whiteScore) {
            System.out.println("Black Wins!");
        } else if (blackScore < whiteScore) {
            System.out.println("White Wins!");
        } else {
            System.out.println("It's a draw");
        }
    }

    public boolean gameOver() {
        boolean noEmpty = true;
        boolean noMoves = false;
        ArrayList<int[]> movesB = possibleMoves(BLACK);
        ArrayList<int[]> movesW = possibleMoves(WHITE);
        if (movesB.size() == 0 && movesW.size() == 0) noMoves = true;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[j][i] == EMPTY) noEmpty = false;
            }
        }
        if (noEmpty) {
            System.out.println("Game Over");
            return true;
        }
        if (noMoves) {
            System.out.println("Game Over");
            System.out.println("No moves possible");
            return true;
        }
        return false;
    }


    public static boolean isRegion2(int c, int r) {
        return ((r == 1 || r == 6) && (c == 2 || c == 3 || c == 4 || c == 5)) || ((c == 1 || c == 6) && (r == 2 || r == 3 || r == 4 || r == 5));
    }

    public static boolean isRegion3(int c, int r) {
        return ((r == 0 || r == 7) && (c == 2 || c == 3 || c == 4 || c == 5)) || ((c == 0 || c == 7) && (r == 2 || r == 3 || r == 4 || r == 5));
    }

    public static boolean isRegion4(int c, int r) {
        return !isRegion5(r, c) && (r == 0 || r == 1 || r == 6 || r == 7) && (c == 0 || c == 1 || c == 6 || c == 7);
    }

    public static boolean isRegion5(int c, int r) {
        return (r == 0 || r == 7) && (c == 0 || c == 7);
    }


    /**
     * TODO: REPLACE FUNCTION WITH isRegion5
     *
     * @param r
     * @param c
     * @return
     */
    public boolean isCorner(int r, int c) {
        if (r == 0 && c == 0 || r == 7 && c == 7 || r == 0 && c == 7 || r == 7 && c == 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isAroundCorner(int r, int c) {
        if (r == 0 && c == 1 || r == 1 && c == 0 || r == 1 && c == 1) {
            return true;
        } else if (r == 6 && c == 0 || r == 7 && c == 1 || r == 6 && c == 1) {
            return true;
        } else if (r == 0 && c == 6 || r == 1 && c == 6 || r == 1 && c == 7) {
            return true;
        } else if (r == 6 && c == 7 || r == 7 && c == 6 || r == 6 && c == 6) {
            return true;
        } else {
            return false;
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

    public boolean computerPlays() {
        return this.side == WHITE;
    }

    @Override
    public ArrayList<int[]> getPossibleMoves(int side) {
        return possibleMoves(side);
    }

    @Override
    public ArrayList<int[]> getPossibleMoves(int side, int[][] _board) {
        ArrayList<int[]> output = new ArrayList<>();
        for (int i = 0; i < _board.length; i++) {
            for (int j = 0; j < _board.length; j++) {
                if (_board[j][i] == side) {
                    ArrayList<int[]> tmp = checkBorders(j, i, side, _board);
                    output.addAll(tmp);
                }
            }
        }
        return output;

    }

    @Override
    public void place(int x, int y, int side) {
        board[y][x] = side;
    }

    public void switchSide() {
        if (this.side == BLACK) {
            this.side = WHITE;
        } else if (this.side == WHITE) {
            this.side = BLACK;
        }
    }

    /**
     * Board with regions
     * Region 1 = 1, point = 3
     * Region 2 = 2, point = 2
     * Region 3 = 3, point = 5
     * Region 4 = 4, point = -5
     * Region 5 = 5, point = 10
     * [5][4][3][3][3][3][4][5]
     * [4][4][2][2][2][2][4][4]
     * [3][2][1][1][1][1][2][3]
     * [3][2][1][1][1][1][2][3]
     * [3][2][1][1][1][1][2][3]
     * [3][2][1][1][1][1][2][3]
     * [4][4][2][2][2][2][4][4]
     * [5][4][3][3][3][3][4][5]
     *
     * @param depth
     * @param current_x
     * @param current_y
     * @return
     */
    @Override
    public int check(int depth, int current_x, int current_y) {
        int score = 0;
        if (isRegion5(current_y, current_x)) {
            score = 10;
        } else if (depth == 5) {
            if (isRegion5(current_y, current_x)) {
                score = 10;
            } else if (isRegion4(current_y, current_x)) {
                score = -5;
            } else if (isRegion3(current_y, current_x)) {
                score = 5;
            } else if (isRegion2(current_y, current_x)) {
                score = 2;
            } else {
                score = 3;
            }
        } else {
            return 0;
        }

        return side == COMPUTER ? score : -score;
    }

    @Override
    public boolean boardIsFull() {
        return false;
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

    @Override
    public int[][] getBoard() {
        return board;
    }

    @Override
    public void setBoard(int[][] board) {
        this.board = board;
    }
}
