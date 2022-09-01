package ntu.mdp.pathfinding.Algo;

public class Curve extends Trajectory {
    private int[] startPt, endPt;

    private int[] center;

    private int theta; // in degrees

    private boolean isClockwiseTurn;

    private int arcLen;

    public Curve(int[] start, int[] end, int[] center, int theta, boolean isclkwise){
        super(start, end);
        this.theta = theta;
        this.center = center;
        this.isClockwiseTurn = isclkwise;
        this.arcLen = AlgoConstant.R * theta;
    }

    public int[] getStartPt(){return this.startPt;}

    public int[] getEndPt(){return this.endPt;}

    public int getTheta(){return  this.theta;}

    public int[] getCenter() {return this.center;}

    public int getArcLen() {return arcLen;}

    public boolean isClockwiseTurn() {return this.isClockwiseTurn;}


}
