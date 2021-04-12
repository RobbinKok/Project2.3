package AI;

import java.util.ArrayList;

public class AIv2 {
    private GameV2 game;

    public AIv2(GameV2 game) {
        this.game = game;
    }

    public synchronized AIBest chooseMove(int side) {
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

            int current = board[column][row];
//            board[column][row] = side;
            game.place(board, column, row, side);

            // runnable
            MiniMax miniMax = new MiniMax(board, opp, side, 2, row, column);
            Thread thread = new Thread(miniMax);
            thread.start();
            miniMaxArray.add(miniMax);
            threads.add(thread);

            game.place(board, column, row, current);
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
            value = miniMax(this.opp, this.side, this.depth, this.row, this.column, this.board);
        }

        private MinMaxResult miniMax(int side, int opp, int depth, int current_x, int current_y, int[][] board) {
            int check = game.checkScore(board, depth, current_x, current_y);

            if (check != 0) {
                return new MinMaxResult(check, depth);
            }

            ArrayList<int[]> moves = game.getPossibleMoves(board, side/*, board*/);

            int max = Integer.MIN_VALUE;
            int min = Integer.MAX_VALUE;

            for (int[] move : moves) {
                int move_row = move[0];
                int move_column = move[1];

                int current = board[move_column][move_row];
                board[move_column][move_row] = side;

                MinMaxResult result = miniMax(opp, side, depth + 1, move_row, move_column, board);
                if (side == game.COMPUTER) { // todo: replace with boolean
                    max = Math.max(max, result.points);
                } else if (side == game.PLAYER) {
                    min = Math.min(min, result.points);
                }

                board[move_column][move_row] = current;
            }

            return new MinMaxResult(side == game.COMPUTER ? max : min, depth);
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
}
