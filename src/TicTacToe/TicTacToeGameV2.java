package TicTacToe;

import AI.Game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class TicTacToeGameV2 extends Game {

    private static final int EMPTY = 2;
    public static final int PLAYER_WIN = 0;
    public static final int DRAW = 1;
    public static final int UNCLEAR = 2;
    public static final int COMPUTER_WIN = 3;


    private int[][] playingBoard = new int[3][3];
    private Random random = new Random();
    public int side = random.nextInt(2);
    private char computerChar, humanChar;
    private int position = UNCLEAR;

    private boolean isPlaying = true;

    public TicTacToeGameV2() {
        clearBoard();
        initSide();
    }

    private void initSide() {
        this.COMPUTER = random.nextInt(2);
        this.PLAYER = this.COMPUTER != 0 ? 0 : 1;
        if (this.side == COMPUTER) {
            computerChar = 'X';
            humanChar = 'O';
        } else {
            computerChar = 'O';
            humanChar = 'X';
        }
    }

    // Simple supporting routines
    private void clearBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                playingBoard[i][j] = EMPTY;
            }
        }
    }


    @Override
    public ArrayList<int[]> getPossibleMoves(int[][] board, int side) {
        ArrayList<int[]> output = new ArrayList<>();
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[r].length; c++) {
                if (board[r][c] == EMPTY) {
                    output.add(new int[]{r, c});
                }
            }
        }
        return output;
    }

    public boolean boardIsFull(int[][] board) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean boardIsEmpty(int[][] board) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] != EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isAWin(int side, int[][] board) {

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

    @Override
    public int[][] place(int[][] board, int row, int column, int side) {
        board[column][row] = side;
        return board;
    }

    public void playMove(int row, int column, int side) {
        playingBoard[row][column] = side;
        if (side == COMPUTER) this.side = PLAYER;
        else this.side = COMPUTER;
    }

    @Override
    public int checkScore(int score, int[][] board, int row, int column, int depth) {
        if (isAWin(this.COMPUTER, board)) {
            return 10;
        } else if (isAWin(this.PLAYER, board)) {
            return -10;
        }
        return 0;
    }

    private int positionValue() {
        if (isAWin(this.COMPUTER, playingBoard)) {
            return COMPUTER_WIN;
        } else if (isAWin(this.PLAYER, playingBoard)) {
            return PLAYER;
        } else if (boardIsFull(playingBoard)) {
            return DRAW;
        } else {
            return UNCLEAR;
        }
    }

    @Override
    public boolean gameOver() {
        this.position = positionValue();
        return this.position != UNCLEAR;
    }

    @Override
    public int[][] getBoard() {
        return Arrays.stream(this.playingBoard).map(int[]::clone).toArray(int[][]::new);
    }

    public String winner() {
        if (this.position == COMPUTER_WIN) return "computer";
        else if (this.position == PLAYER_WIN) return "human";
        else return "nobody";
    }

    public boolean computerPlays() {
        return side == COMPUTER;
    }

    public boolean moveOk(int move) {
        return (move >= 0 && move <= 8 && playingBoard[move / 3][move % 3] == EMPTY);
    }

    public String toString() {
        String output = "";
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (playingBoard[i][j] != EMPTY) {
                    output = playingBoard[i][j] == COMPUTER ? output + computerChar + " " : output + humanChar + " ";
                } else {
                    output += "_ ";
                }
            }
            output += '\n';
        }
        return output;
    }
}
