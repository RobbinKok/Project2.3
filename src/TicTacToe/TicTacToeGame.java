package TicTacToe;

import AI.Game;

import java.util.ArrayList;
import java.util.Random;

public class TicTacToeGame extends Game {

    private int[][] board = new int[3][3];
    private Random random = new Random();
    private int side = random.nextInt(2);
    private int position = UNCLEAR;
    private char computerChar, humanChar;


    public TicTacToeGame() {
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

    @Override
    public ArrayList<int[]> getPossibleMoves(int side) {
        ArrayList<int[]> output = new ArrayList<>();
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[r].length; c++) {
                if (board[r][c] == EMPTY) {
                    output.add(new int[] {r, c});
                }
            }
        }
        return output;
    }


    // Place functions
    public void place(int move) {
        place(move / 3, move % 3, this.side);
        if (side == COMPUTER) this.side = PLAYER;
        else this.side = COMPUTER;
    }

    @Override
    public void place(int x, int y, int side) {
        board[x][y] = side;
    }


    // Simple supporting routines
    private void clearBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = EMPTY;
            }
        }
    }

    @Override
    public boolean boardIsFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean boardIsEmpty() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] != EMPTY) {
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


    // AI.AI logics
    @Override
    public int check() {
        if (isAWin(Game.COMPUTER)) {
            return 10;
        } else if (isAWin(Game.PLAYER)) {
            return -10;
        }
        return 0;
    }

    // Other
    public boolean computerPlays() {
        return side == COMPUTER;
    }

    public boolean gameOver() {
        this.position = positionValue();
        return this.position != UNCLEAR;
    }

    private int positionValue() {
        if (isAWin(COMPUTER)) {
            return COMPUTER_WIN;
        } else if (isAWin(PLAYER)) {
            return PLAYER;
        } else if (boardIsFull()) {
            return DRAW;
        } else {
            return UNCLEAR;
        }
    }

    public String winner() {
        if (this.position == COMPUTER_WIN) return "computer";
        else if (this.position == PLAYER_WIN) return "human";
        else return "nobody";
    }

    public boolean moveOk(int move) {
        return (move >= 0 && move <= 8 && board[move / 3][move % 3] == EMPTY);
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
}
