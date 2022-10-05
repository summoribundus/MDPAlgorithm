package ntu.mdp.pathfinding;

public class Point {
    private int r, c;
    private boolean matchingPoint;

    private int matchingObstacleID;

    private CarMoveFlag moveFlag; // 0: turning left 90, 1: turning right 90; 2: reversing; 3: forwarding

    private CarFacingDir facingDir;

    public Point(int r, int c) {
        this.r = r;
        this.c = c;
        matchingPoint = false;
    }

    public Point(int r, int c, CarMoveFlag moveFlag){
        this.r = r;
        this.c = c;
        this.moveFlag = moveFlag; // 0: turning left 90, 1: turning right 90; 2: reversing; 3: forwarding
    }

    public Point(int r, int c, CarMoveFlag moveFlag, CarFacingDir carFacingDir) {
        this.r = r;
        this.c = c;
        this.moveFlag = moveFlag;
        this.facingDir = carFacingDir;
    }

    public Point(int r, int c, boolean matchingPoint, int matchingObstacleID, CarMoveFlag moveFlag) {
        this.r = r;
        this.c = c;
        this.moveFlag = moveFlag;
        this.matchingObstacleID = matchingObstacleID;
        this.matchingPoint = matchingPoint;
    }
    public int getR() {
        return r;
    }

    public int getC() {
        return c;
    }

    public boolean isMatchingPoint() {
        return matchingPoint;
    }

    public CarMoveFlag getMoveFlag() {
        return moveFlag;
    }

    public CarFacingDir getFacingDir() {
        return facingDir;
    }

    public void setFacingDir(CarFacingDir facingDir) {
        this.facingDir = facingDir;
    }

    public int getMatchingObstacleID(){
        return this.matchingObstacleID;
    }

    @Override
    public String toString() {
        return r + "-" + c + "-" + moveFlag;
    } // colon separator cannot work
}

