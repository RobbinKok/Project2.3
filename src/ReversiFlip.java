import javafx.scene.paint.Color;
import AI.Game;
import AI.GameV2;

public class ReversiFlip {
    private static final int EMPTY = 2;

    private int[][] board;

    private OthelloGameController gui;

    public ReversiFlip(int[][] board) {
        this.board = board;
    }

    public ReversiFlip(int[][] board, OthelloGameController gui) {
        this.board = board;
        this.gui = gui;
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
                            if (player == ReversieV2.WHITE) gui.changeNodeColor(s, k, Color.BLACK);
                            else gui.changeNodeColor(s, k, Color.WHITE);
                        board[s][k] = player;
                    }
                }
                if (board[s][k] != player && board[s][k] != EMPTY && direction == dir && dir != 4) {
                    if (flip(player, s, k, dir)) {
                        board[s][k] = player;
                        if (gui != null)
                            if (player == ReversieV2.WHITE) gui.changeNodeColor(s, k, Color.BLACK); // TODO: KIJKEN OF SIDE ECHT WEL PLAYER MOCHT WORDEN
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

    /**
     * return -1,-1 direction heeft geen mogelijke zet.
     */
    public int[] canFlip(int x, int y, int player, int direction) {
        int dir = 0;
        for (int k = y - 1; k <= y + 1; k++) {
            for (int s = x - 1; s <= x + 1; s++) {
                if (direction == dir) {
                    if (k < 0 || k >= board.length || s < 0 || s >= board.length || board[s][k] == player) {
                        int[] output = {-1, -1};
                        return output;
                    }
                    if (board[s][k] != player && board[s][k] != EMPTY) {
                        return canFlip(s, k, player, direction/*, board*/);
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

    public int[][] getBoard() {
        return board;
    }
}