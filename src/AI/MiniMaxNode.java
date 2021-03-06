package AI;

import java.util.ArrayList;
import java.util.concurrent.RecursiveTask;

public class MiniMaxNode extends RecursiveTask<MinMaxResult> {
    private int[][] board;
    private int side;
    private int opp;
    private int depth;
    private int row;
    private int column;
    private Game game;
    private int alpha;
    private int beta;
    private int score;
    private long startTime;

    private boolean running = true;

    public MiniMaxNode(int score, Game game, int[][] board, int side, int opp, int depth, int row, int column, int alpha, int beta, long startTime) {
        this.score = score;
        this.game = game;
        this.board = board;
        this.side = side;
        this.opp = opp;
        this.depth = depth;
        this.row = row;
        this.column = column;
        this.alpha = alpha;
        this.beta = beta;
        this.startTime = startTime;
    }

    @Override
    protected MinMaxResult compute() {

        int check = game.checkScore(score, board, row, column, depth);

        ArrayList<int[]> moves = game.getPossibleMoves(board, side);
        orderMoves(board, moves, depth);

        long currentTime = System.currentTimeMillis();
        long over = currentTime - startTime;
        if (moves.size() == 0 || depth == 5 || over > 8000) {
            return new MinMaxResult(check, depth);
        }

        int max = Integer.MIN_VALUE;
        int min = Integer.MAX_VALUE;

        depth += 1;

        for (int[] move : moves) {
            int move_row = move[0];
            int move_column = move[1];

            int[][] current = game.getBoard();

            board = game.place(board, column, row, side);

            MiniMaxNode miniMaxNode = new MiniMaxNode(check, game, board, opp, side, depth, move_row, move_column, alpha, beta, startTime);
            MinMaxResult result = miniMaxNode.compute();

            if (side == game.PLAYER) {
                max = Math.max(max, result.points);
                alpha = Math.max(alpha, result.points);
                if (beta <=  alpha)
                    break;
            } else if (side == game.COMPUTER) {
                min = Math.min(min, result.points);
                beta = Math.min(beta, result.points);
                if (beta <= alpha)
                    break;
            }

            board = current;
        }

        return new MinMaxResult(side == game.PLAYER ? max : min, depth);
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

//    @Override
//    public void run() {
//        while (running) {
//
//            int check = game.checkScore(score, board, row, column, depth);
//
//            ArrayList<int[]> moves = game.getPossibleMoves(board, side);
//
//            if (moves.size() == 0 || depth == 7) {
//                result = new MinMaxResult(check, depth);
//                running = false;
//            }
//
//
//            int max = Integer.MIN_VALUE;
//            int min = Integer.MAX_VALUE;
//
//            for (int[] move : moves) {
//                int move_row = move[0];
//                int move_column = move[1];
//
//                int[][] current = board;
//
//                board = game.place(board, column, row, side);
//
//                MiniMaxNode miniMaxNode = new MiniMaxNode(check, game, board, opp, side, depth + 1, move_row, move_column, alpha, beta); // todo: implement alpha beta
//                Thread thread = new Thread(miniMaxNode);
//                thread.start();
//                try {
//                    thread.join();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//                while (result == null) {
//
//                }
////                MinMaxResult minMaxResult = miniMaxNode.getResult();
//                if (side == game.COMPUTER) {
//                    max = Math.max(max, result.points);
////                    alpha = Math.max(alpha, minMaxResult.points);
////                    if (beta <= alpha)
////                        break;
//                } else if (side == game.PLAYER) {
//                    min = Math.min(min, result.points);
////                    beta = Math.min(beta, minMaxResult.points);
////                    if (beta <= alpha)
////                        break;
//                }
//
//                board = current;
//            }
//
//            result = new MinMaxResult(side == game.COMPUTER ? max : min, depth);
//            running = false;
//        }
//    }
//
//    public MinMaxResult getResult() {
//        return result;
//    }
//
//    public int getRow() {
//        return row;
//    }
//
//    public int getColumn() {
//        return column;
//    }
}
