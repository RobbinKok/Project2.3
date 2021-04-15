package AI;

import java.util.ArrayList;
import java.util.concurrent.ForkJoinPool;

public class AIv3 extends AbstractAI{

    public Game game;

    public AIv3(Game game) {
        this.game = game;
    }

    @Override
    public AIBest chooseMove(int side) {
        long startTime = System.currentTimeMillis();
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

            MiniMax miniMax = new MiniMax(board, side, opp, 2, row, column, move.length, startTime);
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
            }

            if (result.depth > bestDepth) {
                bestDepth = result.depth;
            }

            miniMax.shutdown();
        }

        System.out.println("Depth2 = " + bestDepth);
        return new AIBest(value, bRow, bColumn, bestDepth);
    }


    class MiniMax implements Runnable {
        // return value
        private volatile MinMaxResult value;

        private ForkJoinPool forkJoinPool;

        private int[][] board;
        private int side;
        private int opp;
        private int depth;
        private int row;
        private int column;
        private int divider;
        private long start;

        public MiniMax(int[][] board, int side, int opp, int depth, int row, int column, int divider, long start) {
            this.board = board;
            this.side = side;
            this.opp = opp;
            this.depth = depth;
            this.row = row;
            this.column = column;
            this.divider = divider;
            this.start = start;
        }

        @Override
        public void run() {
            int nThreads = Runtime.getRuntime().availableProcessors();

            forkJoinPool = new ForkJoinPool(nThreads / this.divider);

            value = forkJoinPool.invoke(new MiniMaxNode(0, game, board, opp, side, 2, row, column, Integer.MIN_VALUE, Integer.MAX_VALUE, start));
            System.out.println(value.depth);
            forkJoinPool.shutdown();
        }

        public void shutdown() {
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
