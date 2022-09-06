package ntu.mdp.pathfinding.Algo.Trajectory;

import ntu.mdp.pathfinding.Algo.CarMove;

public class TrajectoryResult {

    private TrajectoryCurve startCurve;
    private TrajectoryLine straightLine;
    private TrajectoryCurve intermediateCurve;
    private TrajectoryCurve endCurve;

    private int[] pt1, pt2;

    private boolean isAllCurve;

    private CarMove carMove;

    private int totalLength;

    public TrajectoryResult(TrajectoryCurve c1, TrajectoryCurve c2, TrajectoryCurve c3) {
        this.startCurve = c1;
        this.intermediateCurve = c2;
        this.straightLine = null;
        this.endCurve = c3;
        this.isAllCurve = true;

        this.pt1 = c2.getStartPt();
        this.pt2 = c2.getEndPt();

        this.totalLength = c1.getArcLen() + c2.getArcLen() + c3.getArcLen();

        this.carMove = new CarMove(c1.getTheta(), c1.isClockwiseTurn(), c2.getTheta(), c2.isClockwiseTurn(),
                c3.getTheta(), c3.isClockwiseTurn(), totalLength);
    }


    public TrajectoryResult(TrajectoryCurve c1, TrajectoryLine l2, TrajectoryCurve c3){
        this.startCurve = c1;
        this.straightLine = l2;
        this.intermediateCurve = null;
        this.endCurve = c3;
        this.isAllCurve = false;

        this.pt1 = l2.getStartPt();
        this.pt2 = l2.getEndPt();

        this.totalLength = c1.getArcLen() + l2.getLength() + c3.getArcLen();
//        System.out.println("c1 arclen: " + c1.getArcLen());
//        System.out.println("l2 length: " + l2.getLength());
//        System.out.println("c3 arclen: " + c3.getArcLen());

        this.carMove = new CarMove(c1.getTheta(), c1.isClockwiseTurn(),
                c3.getTheta(), c3.isClockwiseTurn(), totalLength);
    }

    public int[] getPt1() {
        return this.pt1;
    }

    public int[] getPt2() {
        return this.pt2;
    }

    public int[] getCircle1() {
       return startCurve.getCenter();
    }

    public int[] getCircle2() {
        return endCurve.getCenter();
    }

    public int getStartTheta() { return startCurve.getTheta();}

    public int getIntermediateTheta() {
        if (!isAllCurve)
            return -1;
        return intermediateCurve.getTheta();
    }

    public int getEndTheta() { return endCurve.getTheta();}

    public int[] getCircleInter() {
        if (!isAllCurve)
            return null;
        return intermediateCurve.getCenter();
    }

    public CarMove getCarMove() {
        return carMove;
    }

    public boolean isAllCurve() {
        return isAllCurve;
    }

    public boolean isClockwiseTurnStart() {
        return startCurve.isClockwiseTurn();
    }

    public boolean isClockwiseTurnIntermediate() {
        if (!isAllCurve)
            return false;
        return intermediateCurve.isClockwiseTurn();
    }

    public boolean isClockwiseTurnEnd() {return endCurve.isClockwiseTurn();}

    public int getTotalLength() {return totalLength;}
}
