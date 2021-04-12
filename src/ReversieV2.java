import AI.Game;
import AI.GameV2;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Arrays;

public class ReversieV2 extends GameV2 {
    public static final int BLACK = 0;
    public static final int WHITE = 1;
    private static final int EMPTY = 2;

    private int[][] playingBoard = new int[8][8];

    public int side;

    public OthelloGameController gui = null;

    private int blackScore;
    private int whiteScore;

    public ReversieV2(int playerColor, int computerColor) {
        this.PLAYER = playerColor;
        this.COMPUTER = computerColor;

        this.side = this.PLAYER == BLACK ? this.PLAYER : this.COMPUTER;

        resetBoard();
        findAllScores();
    }

    public ReversieV2(int playerColor, int computerColor, OthelloGameController gui) {
        this(playerColor, computerColor);
        this.gui = gui;
    }


    public int getBlackScore() {
        return blackScore;
    }

    public int getWhiteScore() {
        return whiteScore;
    }

    private void resetBoard() {
        for (int i = 0; i < playingBoard.length; i++) {
            Arrays.fill(playingBoard[i], EMPTY);
        }
        playingBoard[3][4] = BLACK;
        playingBoard[3][3] = WHITE;
        playingBoard[4][3] = BLACK;
        playingBoard[4][4] = WHITE;
    }


    public void findAllScores() {
        blackScore = 0;
        whiteScore = 0;
        for (int i = 0; i < playingBoard.length; i++) {
            for (int j = 0; j < playingBoard[i].length; j++) {
                if (playingBoard[i][j] == WHITE) {
                    whiteScore++;
                } else if (playingBoard[i][j] == BLACK) {
                    blackScore++;
                }
            }
        }
    }

    /**
     * UI en bord updaten
     *
     * @param column
     * @param row
     * @param side
     */
    public void playMove(int column, int row, int side) {
        ReversiFlip flip = new ReversiFlip(playingBoard, gui);
        flip.flip(side, column, row, 4);
        playingBoard = flip.getBoard();

        playingBoard[row][column] = side;

        findAllScores();

        if (this.gui != null) {
            if (side == BLACK)
                gui.changeNodeColor(column, row, Color.BLACK);
            else
                gui.changeNodeColor(column, row, Color.WHITE);

            gui.updateCurrentPlayer(side);

            // sets the score in the gui
            gui.setScore(blackScore, whiteScore);

            // adds the move to the movelist in the gui
            gui.addMove(side, column + 1, row + 1);
        }

        swapSides(side);
    }


    public ArrayList<int[]> checkBorders(int column, int row, int side, int[][] board) {
        ArrayList<int[]> output = new ArrayList<>();
        ReversiFlip flip = new ReversiFlip(board);
        int dir = 0;
        for (int k = column - 1; k <= column + 1; k++) {
            for (int s = row - 1; s <= row + 1; s++) {
                if (k < 0 || k >= board.length || s < 0 || s >= board.length) {
                    dir++;
                    continue;
                }
                if (board[s][k] != side && board[s][k] != EMPTY && dir != 4) {
                    // return flipColor(k, s, player, x, y);
                    int[] tmp = flip.canFlip(s, k, side, dir);
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

    public void swapSides(int side) {
        this.side = side == this.PLAYER ? this.COMPUTER : this.PLAYER;
    }

    /**
     *         Arrays.stream(matrix).map(int[]::clone).toArray(int[][]::new)
     * @return
     */
    @Override
    public int[][] getBoard() {
        int[][] board = new int[8][8];

        for(int i = 0; i < board.length; i++) {
            for(int j = 0; j < board[i].length; j++) {
                board[i][j] = playingBoard[i][j];
            }
        }

        return this.playingBoard;
    }


    @Override
    public ArrayList<int[]> getPossibleMoves(int[][] board, int side) {
        ArrayList<int[]> output = new ArrayList<>();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[j][i] == side) {
                    ArrayList<int[]> tmp = checkBorders(j, i, side, board);
                    output.addAll(tmp);
                }
            }
        }

        return output;
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

    /**
     * For AI
     *
     * @param board
     * @param row
     * @param column
     * @param side
     * @return
     */
    @Override
    public int[][] place(int[][] board, int row, int column, int side) {
        ReversiFlip flip = new ReversiFlip(board, gui);
        flip.flip(side, column, row, 4);
        playingBoard = flip.getBoard();
        board[row][column] = side;
        return board;
    }

    /**
     * @return
     */
    @Override
    public boolean gameOver(int[][] board) {
        boolean noEmpty = true;
        boolean noMoves = false;
        ArrayList<int[]> movesPlayer = this.getPossibleMoves(board, this.PLAYER);
        ArrayList<int[]> movesComputer = this.getPossibleMoves(board, WHITE);
        if (movesPlayer.size() == 0 && movesComputer.size() == 0) noMoves = true;
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

    public boolean computerPlays() {
        return this.side == COMPUTER;
    }

    // AI logic

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
     * @param row
     * @param column
     * @param depth
     * @return
     */
    @Override
    public int checkScore(int[][] board, int row, int column, int depth) {
        int score = 0;
        if (isRegion5(column, row)) {
            score = 10;
        } else if (depth == 5) {
            if (isRegion5(column, row)) {
                score = 10;
            } else if (isRegion4(column, row)) {
                score = -5;
            } else if (isRegion3(column, row)) {
                score = 5;
            } else if (isRegion2(column, row)) {
                score = 2;
            } else {
                score = 3;
            }
        } else {
            return 0;
        }

        return side == COMPUTER ? score : -score;
    }


    /**
     * @return
     */
    @Override
    public String toString() {
        String output = "";
        for (int i = 0; i < playingBoard.length; i++) {
            for (int j = 0; j < playingBoard[i].length; j++) {
                if (playingBoard[j][i] != EMPTY) {
                    output += playingBoard[j][i] + " ";
                } else {
                    output += "- ";
                }
            }
            output += "\n";
        }
        return output;
    }


}
