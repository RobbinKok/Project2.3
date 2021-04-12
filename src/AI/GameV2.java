package AI;

import java.util.ArrayList;

public abstract class GameV2 {
    public int PLAYER;
    public int COMPUTER;

    public abstract ArrayList<int[]> getPossibleMoves(int[][] board, int side);

    public abstract int[][] place(int[][] board, int row, int column, int side);

    public abstract int checkScore(int[][] board, int row, int column, int depth);

    public abstract boolean gameOver(int[][] board);

    public abstract int[][] getBoard();
}
