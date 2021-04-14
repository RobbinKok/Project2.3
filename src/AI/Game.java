package AI;

import java.util.ArrayList;

public abstract class Game {
    public int PLAYER;
    public int COMPUTER;

    public abstract ArrayList<int[]> getPossibleMoves(int[][] board, int side);

    public abstract int[][] place(int[][] board, int row, int column, int side);

    public abstract int checkScore(int score, int[][] board, int row, int column, int depth);

    public abstract boolean gameOver();

    public abstract int[][] getBoard();
}
