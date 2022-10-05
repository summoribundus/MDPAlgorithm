package ntu.mdp.pathfinding.Algo.Trajectory.TrajectoryCalculation;

import ntu.mdp.pathfinding.Algo.Trajectory.TrajectoryToArenaGrid;
import ntu.mdp.pathfinding.Instruction.CurveInstruction;
import ntu.mdp.pathfinding.Instruction.Instruction;
import ntu.mdp.pathfinding.Instruction.LineInstruction;
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

    private int totalLength;

    private List<List<Point>> path = new ArrayList<>();
    private List<Instruction> instructionList;


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

        // filling the instruction list.
        this.instructionList = new ArrayList<>();
        CurveInstruction c1ins = new CurveInstruction(c1.getTheta(), c1.isClockwiseTurn());
        c1ins.setGridPath(recoverCurvePts(c1));
        instructionList.add(c1ins);

        CurveInstruction c2ins = new CurveInstruction(c2.getTheta(), c2.isClockwiseTurn());
        c2ins.setGridPath(recoverCurvePts(c2));
        instructionList.add(c2ins);

        CurveInstruction c3ins = new CurveInstruction(c3.getTheta(), c3.isClockwiseTurn());
        c3ins.setGridPath(recoverCurvePts(c3));
        instructionList.add(c3ins);
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

        // filling the instruction list.
        this.instructionList = new ArrayList<>();
        CurveInstruction c1ins = new CurveInstruction(c1.getTheta(), c1.isClockwiseTurn());
        c1ins.setGridPath(recoverCurvePts(c1));
        instructionList.add(c1ins);

        LineInstruction l2ins = new LineInstruction(l2.getLength());
        l2ins.setGridPath(recoverStraightLinePts(l2));
        instructionList.add(l2ins);

        CurveInstruction c3ins = new CurveInstruction(c3.getTheta(), c3.isClockwiseTurn());
        c3ins.setGridPath(recoverCurvePts(c3));
        instructionList.add(c3ins);
    }

    private List<Point> recoverCurvePts(TrajectoryCurve c){
        int[] cStartPt = c.getStartPt();
        int[] cEndPt = c.getEndPt();
        int[] cCenterPt = c.getCenter();
        return TrajectoryToArenaGrid.findGridCirclePath(cStartPt[1], cStartPt[0], cEndPt[1], cEndPt[0], cCenterPt[1], cCenterPt[0], c.isClockwiseTurn(), null);
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

    public int[] getCircleInter() {
        if (!isAllCurve)
            return null;
        return intermediateCurve.getCenter();
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

    public List<Instruction> getInstructionList(){
        return this.instructionList;
    }
}
