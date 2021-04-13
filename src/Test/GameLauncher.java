package Test;

import AI.AIv2;
import AI.AIBest;
import TicTacToe.TicTacToeGameV2;

public class GameLauncher {
    private TicTacToeGameV2 game;
    private AIv2 ai;


    public GameLauncher() {
        game = new TicTacToeGameV2();
//        ai = new AIv2(game);


        while (!game.gameOver()) {
            int move = move();
            game.place(game.getBoard(),move / 3, move % 3, 0);

            System.out.println(game);
        }
        System.out.println("AI.Game over " + game.winner() + " wins");
    }


    private int move() {
        if (game.boardIsEmpty(game.getBoard())) {
            return 0;
        }
        AIBest aiBest = ai.chooseMove(game.computerPlays() ? game.COMPUTER : game.PLAYER);

        return aiBest.row * 3 + aiBest.column;
    }

}
