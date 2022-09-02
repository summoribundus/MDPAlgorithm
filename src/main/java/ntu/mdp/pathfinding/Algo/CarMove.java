package ntu.mdp.pathfinding.Algo;

public class CarMove {
    private int turnTheta1, turnTheta2, turnTheta3;

    private boolean isClockwise1, isClockwise2, isClockwise3;
    private int moveLength;

    public CarMove(int turnTheta1, boolean isClockwise1,
                   int turnTheta2, boolean isClockwise2,
                   int moveLength) {
        this.turnTheta1 = turnTheta1;
        this.isClockwise1 = isClockwise1;
        this.turnTheta2 = turnTheta2;
        this.isClockwise2 = isClockwise2;
        this.moveLength = moveLength;
    }

    public CarMove(int turnTheta1, boolean isClockwise1,
                   int turnTheta2, boolean isClockwise2,
                   int turnTheta3, boolean isClockwise3,
                   int moveLength){
        this.turnTheta1 = turnTheta1;
        this.isClockwise1 = isClockwise1;
        this.turnTheta2 = turnTheta2;
        this.isClockwise2 = isClockwise2;
        this.turnTheta3 = turnTheta3;
        this.isClockwise3 = isClockwise3;
        this.moveLength = moveLength;
    }

    public int getTurnTheta1() {
        return turnTheta1;
    }

    public int getMoveLength() {
        return moveLength;
    }

    public int getTurnTheta2() {
        return turnTheta2;
    }

    @Override
    public String toString() {
        return "CarMove{" +
                "turnTheta1=" + turnTheta1 +
                ", turnTheta2=" + turnTheta2 +
                ", moveLength=" + moveLength +
                '}';
    }
}
