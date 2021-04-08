import AI.AI;
import AI.AIBest;
import AI.Game;

import java.io.Reader;
import java.util.Arrays;
import java.util.Scanner;

public class AardappelKroket {
    private Scanner reader = new Scanner(System.in);
    private Reversie reversie;
    private AI ai;

    public AardappelKroket() {
        reversie = new Reversie();
        ai = new AI(reversie);
        while (true) {
            reversie.findAllScores();
            System.out.println("Black: " + reversie.getBlackScore());
            System.out.println("White: " + reversie.getWhiteScore());
            System.out.println(reversie.toString());
            System.out.println("Enter: X,Y");
            reversie.move(move());
        }
    }

    private String[] move() {
//        if (reversie.computerPlays()) {
////            String[] compMove = reversie.chooseMove();
////            System.out.println("Computer Move = " + Arrays.toString(compMove));
//            AIBest aiBest = ai.chooseMove(Game.COMPUTER);
//            return new String[]{ String.valueOf(aiBest.row), String.valueOf(aiBest.column) };
//        } else {
//            String line = reader.nextLine();
//            return line.split(",");
//        }
        AIBest aiBest = ai.chooseMove(reversie.computerPlays() ? Game.COMPUTER : Game.PLAYER);
        System.out.println("x " + aiBest.row + " y " + aiBest.column);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return new String[]{String.valueOf(aiBest.row), String.valueOf(aiBest.column)};
    }

    public static void main(String[] args) {
        new AardappelKroket();
//        while (true) {
//
//            System.out.println(reversie.toString());
//            System.out.println("Enter: X,Y");
////            String line = reader.nextLine();
////            coords = line.split(",");
////            reversie.move(coords);
//        }
    }

}
