package ntu.mdp.pathfinding.Algo;

import ntu.mdp.pathfinding.Obstacle;

import java.util.Map;
import java.util.concurrent.PriorityBlockingQueue;

class PathDistanceResult implements Comparable<PathDistanceResult> {
    private int[] path;
    private int cost;
    public PathDistanceResult(int[] path, int cost) {
        this.path = path;
        this.cost = cost;
    }

    @Override
    public int compareTo(PathDistanceResult o) {
        return Integer.compare(this.cost, o.cost);
    }

    public int getCost() {
        return cost;
    }

    public int[] getPath() {
        return path;
    }
}

public class PathDistance implements Runnable {
    private int[] paths;
    private Map<Integer, Obstacle> map;
    private int n;
    private PriorityBlockingQueue<PathDistanceResult> pq;
    public PathDistance(int[] paths, Map<Integer, Obstacle> map, PriorityBlockingQueue<PathDistanceResult> pq) {
        this.n = paths.length;
        this.map = map;
        this.paths = paths;
        this.pq = pq;
    }

    @Override
    public void run() {
        int cost = 0;
        for (int i = 0; i < n-1; i++) {
            Obstacle ob1 = map.get(paths[i]), ob2 = map.get(paths[i+1]);
            cost += NodeCost.distanceOfObstacles(ob1.getTargetedR(), ob1.getTargetedC(), ob2.getTargetedR(), ob2.getTargetedC());
        }
        this.pq.add(new PathDistanceResult(paths, cost));
    }
}