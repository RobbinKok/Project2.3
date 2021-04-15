import AI.Game;
import javafx.application.Platform;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Arrays;

public class Reversie extends Game {
    public static final int BLACK = 0;
    public static final int WHITE = 1;
    private static final int EMPTY = 2;

    private int[][] playingBoard = new int[8][8];

    public int side;

    private OthelloGameController gui;

    private int blackScore;
    private int whiteScore;

    public boolean isPlaying = true;

    public Reversie(int playerColor, int computerColor) {
        this.PLAYER = playerColor;
        this.COMPUTER = computerColor;

        this.side = this.PLAYER == BLACK ? this.PLAYER : this.COMPUTER;

        resetBoard();
        findAllScores();
    }

    public Reversie(int playerColor, int computerColor, OthelloGameController othelloGameController) {
        this(playerColor, computerColor);
        gui = othelloGameController;
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
        if (column != -1 && row != -1) {

            ReversiFlip flip = new ReversiFlip(playingBoard, gui);
            flip.flip(side, column, row, 4);
            playingBoard = flip.getBoard();

            playingBoard[column][row] = side;

            findAllScores();

            if (gui != null) {
                Platform.runLater(() -> {
                    if (side == BLACK)
                        gui.changeNodeColor(column, row, Color.BLACK);
                    else
                        gui.changeNodeColor(column, row, Color.WHITE);

                    gui.updateCurrentPlayer(side);

                    // sets the score in the gui
                    gui.setScore(blackScore, whiteScore);

                    // adds the move to the movelist in the gui
                    gui.addMove(side, column + 1, row + 1);
                });
            }

            swapSides(side);

        } else {
            swapSides(side);

            if (this.getPossibleMoves(playingBoard, this.side).size() < 1) {
                isPlaying = false;
            }

        }

    }


    /**
     * k = ROW
     * s = COLUMN
     *
     * @param column
     * @param row
     * @param side
     * @param board
     * @return
     */
    public ArrayList<int[]> checkBorders(int column, int row, int side, int[][] board) {
        ArrayList<int[]> output = new ArrayList<>();
        ReversiFlip flip = new ReversiFlip(board);
        int dir = 0;
        for (int k = row - 1; k <= row + 1; k++) {
            for (int s = column - 1; s <= column + 1; s++) {
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
     * Arrays.stream(matrix).map(int[]::clone).toArray(int[][]::new)
     *
     * @return the current state of the playingboard.Hai
     */
    @Override
    public int[][] getBoard() {
        return Arrays.stream(this.playingBoard).map(int[]::clone).toArray(int[][]::new);
    }

    @Override
    public ArrayList<int[]> getPossibleMoves(int[][] board, int side) {
        ArrayList<int[]> output = new ArrayList<>();
        for (int row = 0; row < board.length; row++) {
            for (int column = 0; column < board[row].length; column++) {
                if (board[column][row] == side) {
                    ArrayList<int[]> tmp = checkBorders(column, row, side, board);
                    output.addAll(tmp);
                }
            }
        }

        return output;
    }

    public String winner() {
        findAllScores();
        if (blackScore > whiteScore) {
            return "Black";
        } else if (blackScore < whiteScore) {
            return "White";
        } else {
            return "Nobody";
        }
    }

    public boolean isLegal(int x, int y, int side) {
        ArrayList<int[]> moves = getPossibleMoves(getBoard(), side);
        for (int[] move : moves) {
            if (move[0]==x && move[1]==y) return true;
        }
        return false;
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
        ReversiFlip flip = new ReversiFlip(board);
        flip.flip(side, column, row, 4);
        board = flip.getBoard();
        board[column][row] = side;
        return board;
    }

    /**
     * @return
     */
    @Override
    public boolean gameOver() {
        boolean noEmpty = true;
        boolean noMoves = false;
        ArrayList<int[]> movesPlayer = this.getPossibleMoves(playingBoard, this.PLAYER);
        ArrayList<int[]> movesComputer = this.getPossibleMoves(playingBoard, WHITE);
        if (movesPlayer.size() == 0 && movesComputer.size() == 0) noMoves = true;
        for (int i = 0; i < playingBoard.length; i++) {
            for (int j = 0; j < playingBoard.length; j++) {
                if (playingBoard[j][i] == EMPTY) noEmpty = false;
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
        //return !isPlaying;
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

    public int region1Counter = 0;
    public int region2Counter = 0;
    public int region3Counter = 0;
    public int region4Counter = 0;
    public int region5Counter = 0;

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
     * @param row
     * @param column
     * @param depth
     * @return
     */
    @Override
    public int checkScore(int score, int[][] board, int row, int column, int depth) {
        if (isRegion2(column, row)) {
            score += 1;
            region2Counter++;
        } else if (isRegion4(column, row)) {
            score += -5;
            region4Counter++;
        } else if (isRegion3(column, row)) {
            score += 5;
            region3Counter++;
        } else if (isRegion5(column, row)) {
            score += 10;
            region5Counter++;
        } else {
            score += 1;
            region1Counter++;
        }

        return side == PLAYER ? score : -score;
    }


    /**
     * @return
     */
    @Override
    public String toString() {
        String output = "";
        for (int row = 0; row < playingBoard.length; row++) {
            for (int column = 0; column < playingBoard[row].length; column++) {
                if (playingBoard[column][row] != EMPTY) {
                    output += playingBoard[column][row] + " ";
                } else {
                    output += "- ";
                }
            }
            output += "\n";
        }
        return output;
    }


}
