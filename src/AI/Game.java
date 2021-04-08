package AI;

import java.util.ArrayList;

public abstract class Game {

    public static final int PLAYER = 0;
    public static final int COMPUTER = 1;
    public static final int EMPTY = 2;

    public static final int PLAYER_WIN = 0;
    public static final int DRAW = 1;
    public static final int UNCLEAR = 2;
    public static final int COMPUTER_WIN = 3;



    public abstract ArrayList<int[]> getPossibleMoves(int side);

    public abstract void place(int x, int y, int side);

    public abstract int check(int depth);

    public abstract boolean boardIsFull();

    public abstract int[][] getBoard();

    public abstract void setBoard(int[][] board);
}
