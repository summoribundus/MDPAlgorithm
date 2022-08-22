package com.example.pathfinding;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class Car {
    private LinkedBlockingQueue<Point> queue;

    public Car(int x, int y) {
        queue = new LinkedBlockingQueue<>();

        Point point = new Point(x, y);
        queue.add(point);
    }

    public void goTo(int x, int y) {
        Point point = new Point(x, y);
        queue.add(point);
    }

    public LinkedBlockingQueue<Point> getQueue() {
        return queue;
    }
}
