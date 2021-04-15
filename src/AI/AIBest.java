package AI;

public class AIBest {
    public int row;
    public int column;
    int val;
    public int depth;

    public AIBest(int v) {
        this(v, 0, 0, 0);
    }

    public AIBest(int v, int r, int c, int d) {
        val = v;
        row = r;
        column = c;
        depth = d;
    }


    @Override
    public String toString() {
        return "Best{" +
                "row=" + row +
                ", column=" + column +
                ", val=" + val +
                ", depth=" + depth +
                '}';
    }
}
