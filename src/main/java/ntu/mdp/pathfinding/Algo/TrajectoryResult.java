package ntu.mdp.pathfinding.Algo;

public class TrajectoryResult {

    private Curve startCurve;
    private Line straightLine;
    private Curve intermediateCurve;
    private Curve endCurve;

    private int[] pt1, pt2;

    private boolean isAllCurve;

    private CarMove carMove;

    private int totalLength;

    public TrajectoryResult(Curve c1, Curve c2, Curve c3) {
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


    public TrajectoryResult(Curve c1, Line l2, Curve c3){
        this.startCurve = c1;
        this.straightLine = l2;
        this.intermediateCurve = null;
        this.endCurve = c3;
        this.isAllCurve = false;

        this.pt1 = l2.getStartPt();
        this.pt2 = l2.getEndPt();

        this.totalLength = c1.getArcLen() + l2.getLength() + c3.getArcLen();

        this.carMove = new CarMove(c1.getTheta(), c1.isClockwiseTurn(),
                c3.getTheta(), c3.isClockwiseTurn(), totalLength);
    }

    public int[] getPt1() {
        return this.pt1;
    }

    public int[] getPt2() {
        return pt2;
    }

    public int[] getCircle1() {
       return startCurve.getCenter();
    }

    public int[] getCircle2() {
        return endCurve.getCenter();
    }

    public int[] getCircleInter() {
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
