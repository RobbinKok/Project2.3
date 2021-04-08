import java.io.Reader;
import java.util.Arrays;
import java.util.Scanner;

public class AardappelKroket {
    private Scanner reader = new Scanner(System.in);
    private Reversie reversie;

    public AardappelKroket() {
        reversie = new Reversie(null);
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
        if (reversie.computerPlays()) {
            String[] compMove = reversie.chooseMove();
            System.out.println("Computer Move = " + Arrays.toString(compMove));
            return compMove;
        } else {
            String line = reader.nextLine();
            return line.split(",");
        }
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
