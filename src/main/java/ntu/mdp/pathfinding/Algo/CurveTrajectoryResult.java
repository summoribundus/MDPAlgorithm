package ntu.mdp.pathfinding.Algo;

public class CurveTrajectoryResult extends TrajectoryResult{

    private int[] circle3;

    private int theta3;

    private boolean isClockwiseTurn3;

    public CurveTrajectoryResult(int[] pt1, int[] pt2, int[] circle1, int[] circle2, int[] circle3,
                                 int theta1, int theta2, int theta3,
                                 int length, int dir1, int dir2, int dir3) {
        super(pt1, pt2, circle1, circle2, theta1, theta2, length, dir1, dir2);
        this.circle3 = circle3;
        this.theta3 = theta3;
        isClockwiseTurn3 = (dir3 == 1)? true: false;
    }


    public int[] getCircle3() {
        return circle3;
    }

    public int getTheta3() {return theta3;}

    public boolean isClockwiseTurn3() {
        return isClockwiseTurn3;
    }

}
