package ntu.mdp.pathfinding;

public class Point {
    private int x, y;
    private boolean matchingPoint;

    private int moveFlag; // 0: turning, 1: straight; 2: reversing

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
        matchingPoint = false;
    }

    public Point(int x, int y, int moveFlag){
        this.x = x;
        this.y = y;
        this.moveFlag = moveFlag; // 0: turning, 1: straight; 2: reversing
    }

    public Point(int x, int y, boolean matchingPoint) {
        this.x = x;
        this.y = y;
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
}

