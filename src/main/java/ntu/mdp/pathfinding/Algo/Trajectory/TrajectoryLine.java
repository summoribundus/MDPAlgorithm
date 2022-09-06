package ntu.mdp.pathfinding.Algo.Trajectory;

public class TrajectoryLine extends Trajectory {

    private int length;

    public TrajectoryLine(int[] startPt, int[] endPt){
        super(startPt, endPt);
        this.length = Math.round((float)(calculateEuclideanDistance(startPt[0], startPt[1], endPt[0], endPt[1])));
    }

    public int[] getStartPt() {return this.startPt;}

    public int[] getEndPt() {return this.endPt;}

    public int getLength() {return this.length;}

    private double calculateEuclideanDistance(double c1, double r1, double c2, double r2){
        double deltaC = Math.abs(c1-c2);
        double deltaR = Math.abs(r1-r2);
        return Math.sqrt(Math.pow(deltaC, 2) + Math.pow(deltaR, 2));
    }
}
