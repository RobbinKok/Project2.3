package Test;

import AI.AI;
import AI.Game;
import AI.AIBest;
import TicTacToe.TicTacToeGame;

public class GameLauncher {
    private TicTacToeGame game;
    private AI ai;


    public GameLauncher() {
        game = new TicTacToeGame();
        ai = new AI(game);


        while (!game.gameOver()) {
            game.place(move());

            System.out.println(game);
        }
        System.out.println("AI.Game over " + game.winner() + " wins");
    }


    private int move() {
        if (game.boardIsEmpty()) {
            return 0;
        }
        AIBest aiBest = ai.chooseMove(game.computerPlays() ? game.COMPUTER : game.PLAYER);

        return aiBest.row * 3 + aiBest.column;
    }

}
