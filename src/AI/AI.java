package AI;

import java.util.ArrayList;

public class AI {

    private Game game;

    public AI(Game game) {
        this.game = game;
    }

    public AIBest chooseMove(int side) {
        int opp = side == Game.PLAYER ? Game.COMPUTER : Game.PLAYER;
        int bX = -1;
        int bY = -1;
        int value = Integer.MIN_VALUE;
        int bestDepth = Integer.MAX_VALUE;

        for (int[] move : game.getPossibleMoves(side)) {
            int x = move[0];
            int y = move[1];

            int current = game.getBoard()[y][x];

            game.place(y, x, side);
            MinMaxResult moveVal = minimax(opp, side, 1, x, y);
            game.place(y, x, current);

            if (moveVal.points > value/* || bestDepth < moveVal.depth*/) {
                bX = x;
                bY = y;
                value = moveVal.points;
                bestDepth = moveVal.depth;
            }
        }

        return new AIBest(value, bX, bY, bestDepth);
    }

    private MinMaxResult minimax(int side, int opp, int depth, int current_x, int current_y) {
        int check = game.check(depth, current_x, current_y);

        if (check != 0) {
            return new MinMaxResult(check, depth);
        }

        ArrayList<int[]> moves = game.getPossibleMoves(side);

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


            int[][] board = game.getBoard();
            int current = board[y][x];
            game.place(x, y, side);

            MinMaxResult result = minimax(opp, side, depth + 1, x, y);
            if (side == Game.COMPUTER) { // todo: replace with boolean
                max = Math.max(max, result.points);
            } else if (side == Game.PLAYER) {
                min = Math.min(min, result.points);
            }

            game.place(x, y, current);
        }

        return new MinMaxResult(side == Game.COMPUTER ? max : min, depth);
    }
}
