package ntu.mdp.pathfinding.Algo.Trajectory.TrajectoryCalculation;

import ntu.mdp.pathfinding.Algo.AlgoConstant;

public class TrajectoryCurve extends Trajectory {

    private int[] center;

    private int theta; // in degrees

    private boolean isClockwiseTurn;

    private int arcLen;

    public TrajectoryCurve(int[] start, int[] end, int[] center, int theta, boolean isclkwise){
        super(start, end);
        this.theta = theta;
        this.center = center;
        this.isClockwiseTurn = isclkwise;
        this.arcLen = Math.round((float)(AlgoConstant.R * Math.toRadians(theta)));
    }

    public int[] getStartPt(){return this.startPt;}

    public int[] getEndPt(){return this.endPt;}

    public int getTheta(){return  this.theta;}

    public int[] getCenter() {return this.center;}

    public int getArcLen() {return arcLen;}

    public boolean isClockwiseTurn() {return this.isClockwiseTurn;}
}
