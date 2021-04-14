package AI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

public class AIv2 {
    private Game game;

    private static ConcurrentHashMap<int[][], TableEntry> transpositionTable;
    private static final int MAX_TRANSPOSITION_ENTRIES = 1000;

    public AIv2(Game game) {
        this.game = game;
        this.transpositionTable = new ConcurrentHashMap<>();
    }

    public AIBest chooseMove(int side) {
        int opp = side == game.PLAYER ? game.COMPUTER : game.PLAYER;
        int bRow = -1;
        int bColumn = -1;
        int value = Integer.MIN_VALUE;
        int bestDepth = Integer.MAX_VALUE;


        ArrayList<MiniMax> miniMaxArray = new ArrayList<>();
        ArrayList<Thread> threads = new ArrayList<>();

        int[][] board = game.getBoard();
        for (int[] move : game.getPossibleMoves(board, side)) {
            int row = move[0];
            int column = move[1];

            int[][] current = game.getBoard();
//            board[column][row] = side;
            board = game.place(board, column, row, side);

            // runnable
            MiniMax miniMax = new MiniMax(board, opp, side, 2, row, column);
            Thread thread = new Thread(miniMax);
            thread.start();
            miniMaxArray.add(miniMax);
            threads.add(thread);

//            game.place(board, column, row, current);
            board = current;
//            board[column][row] = current;
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for (MiniMax miniMax : miniMaxArray) {
            MinMaxResult result = miniMax.getValue();

            // hier nog shit doen
            if (result.points > value) {
                bRow = miniMax.getRow();
                bColumn = miniMax.getColumn();
                value = result.points;
                bestDepth = result.depth;
            }
        }

        return new AIBest(value, bRow, bColumn, bestDepth);
    }

    class MiniMax implements Runnable {
        // return value
        private volatile MinMaxResult value;

        // other
        private int[][] board;
        private int side;
        private int opp;
        private int depth;
        private int row;
        private int column;

        public MiniMax(int[][] board, int side, int opp, int depth, int row, int column) {
            this.board = board;
            this.side = side;
            this.opp = opp;
            this.depth = depth;
            this.row = row;
            this.column = column;
        }

        @Override
        public void run() {
            value = miniMax(0, this.opp, this.side, this.depth, this.row, this.column, this.board, Integer.MIN_VALUE, Integer.MAX_VALUE);
        }

        private MinMaxResult miniMax(int score, int side, int opp, int depth, int current_x, int current_y, int[][] board, int alpha, int beta) {
            int check = game.checkScore(score, board, current_x, current_y, depth);

            ArrayList<int[]> moves = game.getPossibleMoves(board, side);
//            moves = orderMoves(board, moves, depth);

            if (moves.size() == 0 || depth == 7) {
                return new MinMaxResult(check, depth);
            }

            int max = Integer.MIN_VALUE;
            int min = Integer.MAX_VALUE;

            for (int[] move : moves) {
                int move_row = move[0];
                int move_column = move[1];

                int[][] current = board;
//                board[move_column][move_row] = side;
                board = game.place(board, column, row, side);

                MinMaxResult result = miniMax(check, opp, side, depth + 1, move_row, move_column, board, alpha, beta);

                if (side == game.COMPUTER) { // todo: replace with boolean
                    max = Math.max(max, result.points);
                    alpha = Math.max(alpha, result.points);
                    if (beta <=  alpha)
                        break;
                } else if (side == game.PLAYER) {
                    min = Math.min(min, result.points);
                    beta = Math.min(beta, result.points);
                    if (beta <= alpha)
                        break;
                }

                board = current;
//                board[move_column][move_row] = current;
//                game.place(board, column, row, current);
            }

            return new MinMaxResult(side == game.COMPUTER ? max : min, depth);
        }

        private ArrayList<int[]> orderMoves(int[][] board, ArrayList<int[]> moves, int depth) {
            int[] scores = new int[moves.size()];

            for (int i = 0; i < moves.size(); i++) {
                int[] coord = moves.get(i);
                scores[i] = game.checkScore(0, board, coord[0], coord[1], depth);
            }

            for (int i = 0; i < moves.size(); i++) {
                int current = scores[i];
                int[] currentMove = moves.get(i);
                int previous = i - 1;

                while (previous >= 0 && scores[previous] > current) {
                    scores[previous + 1] = scores[previous];
                    moves.set(previous + 1, moves.get(previous));
                    previous -= 1;
                }
                scores[previous + 1] = current;
                moves.set(previous + 1, currentMove);
            }

            return moves;
        }

        public MinMaxResult getValue() {
            return value;
        }

        public int getRow() {
            return row;
        }

        public int getColumn() {
            return column;
        }
    }

    public class TableEntry {
        private MinMaxResult value;
        private int depth;
        private Flag flag;

        public TableEntry(MinMaxResult value, int depth, Flag flag) {
            this.value = value;
            this.depth = depth;
            this.flag = flag;
        }
    }

    public enum Flag {
        UPPER_BOUND,
        EXACT,
        LOWER_BOUND
    }
}
