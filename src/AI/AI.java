package AI;

import java.util.ArrayList;

public class AI {

    private Game game;

    public AI(Game game) {
        this.game = game;
    }

    public AIBest chooseMove(int side) {
        int opp = side == game.PLAYER ? game.COMPUTER : game.PLAYER;
        int bX = -1;
        int bY = -1;
        int value = Integer.MIN_VALUE;
        int bestDepth = Integer.MAX_VALUE;


        int[][] board = this.game.getBoard();
        for (int[] move : game.getPossibleMoves(board, side)) {
            int x = move[0];
            int y = move[1];

            int[][] oldBoard = game.getBoard();

            board = game.place(board, y, x, side);
            MinMaxResult moveVal = minimax(board, opp, side, 2, x, y);
            board = oldBoard;

            if (moveVal.points > value/* || bestDepth < moveVal.depth*/) {
                bX = x;
                bY = y;
                value = moveVal.points;
                bestDepth = moveVal.depth;
            }
        }

        return new AIBest(value, bX, bY, bestDepth);
    }

    private MinMaxResult minimax(int[][] board, int side, int opp, int depth, int current_x, int current_y) {
        int check = game.checkScore(0, board, depth, current_x, current_y);

        if (check != 0) {
            return new MinMaxResult(check, depth);
        }
        ArrayList<int[]> moves = game.getPossibleMoves(board, side);

//        if (moves.isEmpty()) {
//            return new MinMaxResult(0, depth);
//        }

//        if (game.boardIsFull()) {
//            return new MinMaxResult(0, depth);
//        }

        int max = Integer.MIN_VALUE;
        int min = Integer.MAX_VALUE;




        for (int[] move : moves) {
            int x = move[0];
            int y = move[1];


            int[][] current = board;
//            System.out.println("x = " + x + ", y = " + y + "current = " + side);
            board = game.place(board, y, x, side);

            MinMaxResult result = minimax(board, opp, side, depth + 1, x, y);
            if (side == game.COMPUTER) { // todo: replace with boolean
                max = Math.max(max, result.points);
            } else if (side == game.PLAYER) {
                min = Math.min(min, result.points);
            }

//            board = current;
        }

        return new MinMaxResult(side == game.COMPUTER ? max : min, depth);
    }

    private void printBoard(int[][] board) {
        String output = "";
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[j][i] != 2) {
                    output += board[j][i] + " ";
                } else {
                    output += "- ";
                }
            }
            output += "\n";
        }
        System.out.println(output);
    }
}
