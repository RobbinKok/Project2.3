package AI;

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

        for(int[] move : game.getPossibleMoves(side)) {
            int x = move[0];
            int y = move[1];

            game.place(x, y, side);
            MinMaxResult moveVal = minimax(opp, side, 1);
            game.place(x, y, Game.EMPTY);

            if (moveVal.points > value || bestDepth < moveVal.depth) {
                bX = x;
                bY = y;
                value = moveVal.points;
                bestDepth = moveVal.depth;
            }
        }

        return new AIBest(value, bX, bY, bestDepth);
    }

    private MinMaxResult minimax(int side, int opp, int depth) {
        int check = game.check();

        if (check != 0) {
            return new MinMaxResult(check, depth);
        }

        if (game.boardIsFull()) {
            return new MinMaxResult(0, depth);
        }

        int max = Integer.MIN_VALUE;
        int min = Integer.MAX_VALUE;

        for (int[] move : game.getPossibleMoves(side)) {
            int x = move[0];
            int y = move[1];

            game.place(x, y, side);

            MinMaxResult result = minimax(opp, side, depth + 1);
            if (side == Game.COMPUTER) { // todo: replace with boolean
                max = Math.max(max, result.points);
            } else if (side == Game.PLAYER) {
                min = Math.min(min, result.points);
            }

            game.place(x, y, Game.EMPTY);
        }

        return new MinMaxResult(side == Game.COMPUTER ? max : min, depth);
    }
}
