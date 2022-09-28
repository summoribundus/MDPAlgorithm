package ntu.mdp.pathfinding;

public class Point {
    private int x, y;
    private boolean matchingPoint;

    private int matchingObstacleID;

    private int moveFlag; // 0: turning, 1: straight; 2: reversing

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
        matchingPoint = false;
    }

    public Point(int x, int y, int moveFlag){
        this.x = x;
        this.y = y;
        this.moveFlag = moveFlag; // 0: turning left 90, 1: turning right 90; 2: reversing; 3: forwarding
    }

    public Point(int x, int y, boolean matchingPoint, int matchingObstacleID) { //,
        this.x = x;
        this.y = y;
        this.matchingObstacleID = matchingObstacleID;
        this.matchingPoint = matchingPoint;
    }
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
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
        return x + "-" + y;
    } // colon separator cannot work
}

