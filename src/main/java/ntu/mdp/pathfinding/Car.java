package ntu.mdp.pathfinding;

import java.util.LinkedList;

public class Car {
    private int r, c;
    private LinkedList<Point> queue;

    private LinkedList<Point> queueCopy;

    public Car(int x, int y) {
        r = x; c = y;
        queue = new LinkedList<>();
    }

    public void goTo(int x, int y) {
        Point point = new Point(x, y);
        queue.add(point);
    }

    public void refreshQueue() {
        queueCopy = new LinkedList<>(queue);
    }

    public LinkedList<Point> getQueue() {
        return queueCopy;
    }

    public int getR() {
        return r;
    }

    public int getC() {
        return c;
    }
}
