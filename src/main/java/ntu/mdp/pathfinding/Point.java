package ntu.mdp.pathfinding;

public class Point {
    private int r, c;
    private boolean matchingPoint;

    private int matchingObstacleID;

    private int moveFlag; // 0: turning left 90, 1: turning right 90; 2: reversing; 3: forwarding

    public Point(int r, int c) {
        this.r = r;
        this.c = c;
        matchingPoint = false;
    }

    public Point(int r, int c, int moveFlag){
        this.r = r;
        this.c = c;
        this.moveFlag = moveFlag; // 0: turning left 90, 1: turning right 90; 2: reversing; 3: forwarding
    }

    public Point(int r, int c, boolean matchingPoint, int matchingObstacleID) { //,
        this.r = r;
        this.c = c;
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

    public int getMoveFlag() {
        return moveFlag;
    }

    public int getMatchingObstacleID(){
        return this.matchingObstacleID;
    }

    @Override
    public String toString() {
        return r + "-" + c;
    } // colon separator cannot work
}

