package AI;

import java.util.ArrayList;
import java.util.concurrent.ForkJoinPool;

public class AIv3 {

    public Game game;

    public AIv3(Game game) {
        this.game = game;
    }

    public AIBest chooseMove(int side) {
        int opp = side == game.PLAYER ? game.COMPUTER : game.PLAYER;
        int bRow = -1;
        int bColumn = -1;
        int value = Integer.MIN_VALUE;
        int bestDepth = Integer.MAX_VALUE;

//        Long result = forkJoinPool.invoke(new Sum(numbers,0,numbers.length));
        ArrayList<MiniMax> miniMaxArray = new ArrayList<>();
        ArrayList<Thread> threads = new ArrayList<>();

        int[][] board = game.getBoard();
        for (int[] move : game.getPossibleMoves(board, side)) {
            int row = move[0];
            int column = move[1];

            int[][] current = game.getBoard();

            board = game.place(board, column, row, side);

            MiniMax miniMax = new MiniMax(board, opp, side, 2, row, column);
            Thread thread = new Thread(miniMax);
            thread.start();
            miniMaxArray.add(miniMax);
            threads.add(thread);

//            MiniMaxNode miniMaxNode = new MiniMaxNode(0, game, board, opp, side, 2, row, column, Integer.MIN_VALUE, Integer.MAX_VALUE);

//            if (result.points > value) {
//                bRow = row;
//                bColumn = column;
//                value = result.points;
//                bestDepth = result.depth;
//            }

            board = current;
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
            int nThreads = Runtime.getRuntime().availableProcessors();

            ForkJoinPool forkJoinPool = new ForkJoinPool(nThreads);

            value = forkJoinPool.invoke(new MiniMaxNode(0, game, board, opp, side, 2, row, column, Integer.MIN_VALUE, Integer.MAX_VALUE));
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
