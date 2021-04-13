import AI.AI;
import AI.AIBest;
import AI.AIv2;
import TicTacToe.TicTacToeGameV2;

import java.util.Scanner;

public class Tui {

    private Scanner reader = new Scanner(System.in);
    private TicTacToeGameV2 t;
    private AI ai;

    public Tui() {
        do {
            System.out.println("*** new AI.Game ***\n");
            t = new TicTacToeGame();
//            ai = new AIv2(t);
            if (t.computerPlays())
                System.out.println("I start:\n");
            else
                System.out.println("You start:\n");

            while(!t.gameOver()) {
                int move = move();
                t.playMove(move / 3, move % 3, t.side);

                System.out.println(t);
            }
            System.out.println("AI.Game over " + t.winner() + " wins");
        } while (nextGame());
    }

    public static void main(String[] args) {
        new Tui();
    }

    private int move() {
        if (t.computerPlays()){
            if (t.boardIsEmpty()) {
                return 0;
            } else {
                AIBest aiBest = ai.chooseMove(t.COMPUTER);
                return aiBest.row * 3 + aiBest.column;
            }
        } else {
            int humanMove;
            do
            {
                System.out.print("Human move    = ");
                // enter integer for the position on the tictactoe board
                // 012
                // 345
                // 678
                humanMove = reader.nextInt();
            }
            while (!t.moveOk(humanMove));
            return humanMove;
        }
    }

    private boolean nextGame()
    {
        Character yn;
        do
        {
            System.out.print("next AI.Game? enter Y/N: ");
            yn=(reader.next()).charAt(0);
            System.out.println(""+yn);
        }
        while  (!(yn=='Y' || yn=='y' || yn=='N' || yn=='n'));
        return yn=='Y'|| yn=='y';
    }
}
