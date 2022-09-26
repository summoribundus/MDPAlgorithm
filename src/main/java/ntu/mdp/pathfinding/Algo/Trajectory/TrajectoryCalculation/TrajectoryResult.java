package ntu.mdp.pathfinding.Algo.Trajectory.TrajectoryCalculation;

import ntu.mdp.pathfinding.Algo.CarMove;
import ntu.mdp.pathfinding.Algo.Trajectory.TrajectoryToArenaGrid;
import ntu.mdp.pathfinding.Point;

import java.util.ArrayList;
import java.util.List;

public class TrajectoryResult {

    private TrajectoryCurve startCurve;
    private TrajectoryLine straightLine;
    private TrajectoryCurve intermediateCurve;
    private TrajectoryCurve endCurve;

    private int[] pt1, pt2;

    private boolean isAllCurve;

    private CarMove carMove;

    private int totalLength;

    private List<List<Point>> path = new ArrayList<>();


    // curve, curve, curve
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
                c3.getTheta(), c3.isClockwiseTurn());
        this.path = constructCCCPath(c1, c2, c3);
        carMove.setPath(path);
    }

    public List<List<Point>> constructCCCPath(TrajectoryCurve c1, TrajectoryCurve c2, TrajectoryCurve c3){
        List<List<Point>> ans = new ArrayList<>();

        ans.add(recoverCurvePts(c1));
        ans.add(recoverCurvePts(c2));
        ans.add(recoverCurvePts(c3));

        return ans;
    }


    // curve, straight line, curve
    public TrajectoryResult(TrajectoryCurve c1, TrajectoryLine l2, TrajectoryCurve c3){
        this.startCurve = c1;
        this.straightLine = l2;
        this.intermediateCurve = null;
        this.endCurve = c3;
        this.isAllCurve = false;

        this.pt1 = l2.getStartPt();
        this.pt2 = l2.getEndPt();

        this.totalLength = c1.getArcLen() + l2.getLength() + c3.getArcLen();

        this.carMove = new CarMove(c1.getTheta(), c1.isClockwiseTurn(),
                c3.getTheta(), c3.isClockwiseTurn(), l2.getLength());

        this.path = constructCSCPath(c1, l2, c3);
        carMove.setPath(path);
    }

    private List<List<Point>> constructCSCPath(TrajectoryCurve c1, TrajectoryLine l2, TrajectoryCurve c3){

        List<List<Point>> ans = new ArrayList<>();

        ans.add(recoverCurvePts(c1));
        ans.add(recoverStraightLinePts(l2));
        ans.add(recoverCurvePts(c3));

        return ans;
    }


    private List<Point> recoverCurvePts(TrajectoryCurve c){

        int[] cStartPt = c.getStartPt();
        int[] cEndPt = c.getEndPt();
        int[] cCenterPt = c.getCenter();
        return TrajectoryToArenaGrid.findGridCirclePath(cStartPt[1], cStartPt[0], cEndPt[1], cEndPt[0], cCenterPt[1], cCenterPt[0], c.isClockwiseTurn());// c, r to r, c

    }

    private List<Point> recoverStraightLinePts(TrajectoryLine l){

        int[] lStartPt = l.getStartPt();
        int[] lEndPt = l.getEndPt();

        return TrajectoryToArenaGrid.findGirdLinePath(lStartPt[1], lStartPt[0], lEndPt[1], lEndPt[0]);
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

    public List<List<Point>> getPath(){
        return this.path;
    }
}
