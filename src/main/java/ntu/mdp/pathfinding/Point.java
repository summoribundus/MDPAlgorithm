package ntu.mdp.pathfinding;

public class Point {
    private int x, y;
    private boolean matchingPoint;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
        matchingPoint = false;
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

