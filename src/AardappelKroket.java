import AI.AIBest;
import AI.AIv2;

import java.io.Reader;
import java.util.Arrays;
import java.util.Scanner;

public class AardappelKroket {
    private Scanner reader = new Scanner(System.in);
    private ReversieV2 reversie;
    private AIv2 ai;
    private boolean playing = true;

    public AardappelKroket() {
        long startTime = System.currentTimeMillis();
        reversie = new ReversieV2(0, 1);
        ai = new AIv2(reversie);
        reversie.COMPUTER = Reversie.WHITE;
        reversie.PLAYER = Reversie.BLACK;
        reversie.side = Reversie.BLACK;
        while (playing) {
            reversie.findAllScores();
            System.out.println("Black: " + reversie.getBlackScore());
            System.out.println("White: " + reversie.getWhiteScore());
            System.out.println(reversie.toString());
            System.out.println("Enter: X,Y");
//            reversie.playMove(move());
        }
        System.out.println("DONE!");
        long estimatedTime = System.currentTimeMillis() - startTime;
        System.out.println(estimatedTime);
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
        AIBest aiBest = ai.chooseMove(reversie.computerPlays() ? reversie.COMPUTER : reversie.PLAYER);
        System.out.println("x " + aiBest.row + " y " + aiBest.column);
        
        if (aiBest.row == -1 && aiBest.column == -1) {
            playing = false;
        }
        
        try {
            Thread.sleep(500);
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
