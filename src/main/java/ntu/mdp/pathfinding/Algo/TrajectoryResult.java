package ntu.mdp.pathfinding.Algo;

public class TrajectoryResult {
    private int[] pt1, pt2, circle1, circle2;

    private boolean isClockwiseTurn1, isClockwiseTurn2;
    private CarMove carMove;

    public TrajectoryResult(int[] pt1, int[] pt2, int[] circle1, int[] circle2, int theta1, int theta2, int length, boolean isClockwiseTurn1, boolean isClockwiseTurn2) {
        this.pt1 = pt1;
        this.pt2 = pt2;
        this.circle1 = circle1;
        this.circle2 = circle2;
        this.carMove = new CarMove(theta1, theta2, length);
        this.isClockwiseTurn1 = isClockwiseTurn1;
        this.isClockwiseTurn2 = isClockwiseTurn2;
    }

    public int[] getPt1() {
        return pt1;
    }

    public int[] getPt2() {
        return pt2;
    }

    public int[] getCircle1() {
        return circle1;
    }

    public int[] getCircle2() {
        return circle2;
    }

    public CarMove getCarMove() {
        return carMove;
    }

    public boolean isClockwiseTurn1() {
        return isClockwiseTurn1;
    }

    public boolean isClockwiseTurn2() {
        return isClockwiseTurn2;
    }
}
