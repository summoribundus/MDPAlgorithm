package ntu.mdp.pathfinding.Algo;

public class CarMove {
    private int turnTheta1, turnTheta2;
    private int moveLength;

    public CarMove(int turnTheta1, int turnTheta2, int moveLength) {
        this.turnTheta1 = turnTheta1;
        this.turnTheta2 = turnTheta2;
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
}
