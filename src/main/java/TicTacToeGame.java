package main.java;

import java.util.Random;

class TicTacToe {
    private static final int HUMAN = 0;
    private static final int COMPUTER = 1;
    public static final int EMPTY = 2;

    public static final int HUMAN_WIN = 0;
    public static final int DRAW = 1;
    public static final int UNCLEAR = 2;
    public static final int COMPUTER_WIN = 3;

    private int[][] board = new int[3][3];
    private Random random = new Random();
    private int side = random.nextInt(2);
    private int position = UNCLEAR;
    private char computerChar, humanChar;

    // Constructor
    public TicTacToe() {
        clearBoard();
        initSide();
    }

    private void initSide() {
        if (this.side == COMPUTER) {
            computerChar = 'X';
            humanChar = 'O';
        } else {
            computerChar = 'O';
            humanChar = 'X';
        }
    }

    public void setComputerPlays() {
        this.side = COMPUTER;
        initSide();
    }

    public void setHumanPlays() {
        this.side = HUMAN;
        initSide();
    }

    public boolean computerPlays() {
        return side == COMPUTER;
    }

    public int chooseMove() {
        Best best = chooseMove(COMPUTER);
        return best.row * 3 + best.column;
    }

    // Find optimal move
    private Best chooseMove(int side) {
        int opp = EMPTY;              // The other side
//        Best reply;           // Opponent's best reply
        int simpleEval;       // Result of an immediate evaluation
        int bestRow = 0;
        int bestColumn = 0;
        int value = -1;

        if (side == HUMAN) {
            opp = COMPUTER;
        }
        if (side == COMPUTER) {
            opp = HUMAN;
        }

        // Hoekje pakken wordt gezien als beste set in TicTacToe
        if (boardIsEmpty()){
            return new Best(side, 0,0);
        }

        if ((simpleEval = positionValue()) != UNCLEAR)
            return new Best(simpleEval);


        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[y].length; x++) {
                if (board[y][x] == EMPTY) {
                    place(y, x, opp);
                    int moveVal = minmax(side, opp);

                    place(y, x, EMPTY);

                    if (moveVal == 10){
                        return new Best(side,y,x);
                    } else if (moveVal == -10){
                        return new Best(side,y,x);
                    }

                    else if (moveVal > value) {
                        bestRow = y;
                        bestColumn = x;
                        value = moveVal;
                    }

                }
            }
        }

        return new Best(value, bestRow, bestColumn);
    }

    public int minmax(int side, int opp) {
        if (isAWin(COMPUTER)) {
            return 10;
        } else if (isAWin(HUMAN)) {
            return -10;
        }

        if (boardIsFull()) {
            return 0;
        }


        int min = 1000;
        int max = -1000;
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[y].length; x++) {
                if (board[y][x] == EMPTY) {
                    place(y, x, side);

                    if (side == COMPUTER) {
                        max = Math.max(max, minmax(opp, side));
                    } else if (side == HUMAN) {
                        min = Math.min(min, minmax(opp, side));
                    }

                    place(y, x, EMPTY);
                }
            }
        }
        return side == HUMAN ? max : min;
    }


    //check if move ok
    public boolean moveOk(int move) {
        return (move >= 0 && move <= 8 && board[move / 3][move % 3] == EMPTY);
    }

    // play move
    public void playMove(int move) {
        board[move / 3][move % 3] = this.side;
        if (side == COMPUTER) this.side = HUMAN;
        else this.side = COMPUTER;
    }


    // Simple supporting routines
    private void clearBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = EMPTY;
            }
        }
    }


    private boolean boardIsFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean boardIsEmpty(){
        for (int i = 0; i < board.length; i++){
            for (int j = 0; j < board[i].length; j++){
                if (board[i][j] != EMPTY){
                    return false;
                }
            }
        }
        return true;
    }

    // Returns whether 'side' has won in this position
    private boolean isAWin(int side) {

        /** Checks all the rows */
        for (int i = 0; i < 3; i++) {
            if ((board[i][0] == side) && (board[i][1] == side) && (board[i][2] == side)) {
                return true;
            }
        }

        /** Check all columns */
        for (int j = 0; j < 3; j++)
            if ((board[0][j] == side)
                    && (board[1][j] == side)
                    && (board[2][j] == side)) {
                return true;
            }

        /** Check major diagonal */
        if ((board[0][0] == side)
                && (board[1][1] == side)
                && (board[2][2] == side)) {
            return true;
        }

        /** Check subdiagonal */
        if ((board[0][2] == side)
                && (board[1][1] == side)
                && (board[2][0] == side)) {
            return true;
        }

        return false;

    }

    // Play a move, possibly clearing a square
    private void place(int row, int column, int piece) {
        board[row][column] = piece;
    }

    private boolean squareIsEmpty(int row, int column) {
        return board[row][column] == EMPTY;
    }

    // Compute static value of current position (win, draw, etc.)
    private int positionValue() {
        if (isAWin(COMPUTER)) {
            return COMPUTER_WIN;
        } else if (isAWin(HUMAN)) {
            return HUMAN_WIN;
        } else if (boardIsFull()) {
            return DRAW;
        } else {
            return UNCLEAR;
        }
    }


    public String toString() {
        String output = "";
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] != EMPTY) {
                    output = board[i][j] == COMPUTER ? output + computerChar + " " : output + humanChar + " ";
                } else {
                    output += "_ ";
                }
            }
            output += '\n';
        }
        return output;
    }

    public boolean gameOver() {
        this.position = positionValue();
        return this.position != UNCLEAR;
    }

    public String winner() {
        if (this.position == COMPUTER_WIN) return "computer";
        else if (this.position == HUMAN_WIN) return "human";
        else return "nobody";
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

