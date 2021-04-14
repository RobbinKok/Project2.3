import AI.AIv3;
import AI.AIBest;

public class BamiSchijf {

    private Reversie reversie;
    private AIv3 ai;
    private boolean playing = true;

    public static void main(String[] args) {
        new BamiSchijf();
    }

    public BamiSchijf() {
        long startTime = System.currentTimeMillis();
        reversie = new Reversie(Reversie.WHITE, Reversie.BLACK);
        ai = new AIv3(reversie);
//        reversie.COMPUTER = Reversie.WHITE;
//        reversie.PLAYER = Reversie.BLACK;
//        reversie.side = Reversie.BLACK;
        while (!reversie.gameOver()) {
            reversie.findAllScores();
            System.out.println("Black: " + reversie.getBlackScore());
            System.out.println("White: " + reversie.getWhiteScore());
            System.out.println(reversie.toString());
//            System.out.println("Enter: X,Y");
//            long startMoveTime = System.currentTimeMillis();
            move();
//            System.out.println(System.currentTimeMillis() - startMoveTime);
        }
        System.out.println("DONE!");
        long estimatedTime = System.currentTimeMillis() - startTime;
        System.out.println(estimatedTime);
        System.out.println("Region 1 = " + reversie.region1Counter);
        System.out.println("Region 2 = " + reversie.region2Counter);
        System.out.println("Region 3 = " + reversie.region3Counter);
        System.out.println("Region 4 = " + reversie.region4Counter);
        System.out.println("Region 5 = " + reversie.region5Counter);
    }


    private void move() {
        int side = reversie.computerPlays() ? reversie.COMPUTER : reversie.PLAYER;
        AIBest aiBest = ai.chooseMove(side);
//        System.out.println("x " + aiBest.row + " y " + aiBest.column);

//        if (aiBest.row == -1 && aiBest.column == -1) {
//            playing = false;
//        }

//        try {
//            Thread.sleep(500);
//        } catch (InterruptedException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }

        reversie.playMove(aiBest.row, aiBest.column, side);

//        return new String[]{String.valueOf(aiBest.row), String.valueOf(aiBest.column)};
    }
}
