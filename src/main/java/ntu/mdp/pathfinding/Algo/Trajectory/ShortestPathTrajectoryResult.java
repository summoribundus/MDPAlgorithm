package ntu.mdp.pathfinding.Algo.Trajectory;

import ntu.mdp.pathfinding.Algo.CarMove;
import ntu.mdp.pathfinding.Point;

import java.util.List;

public class ShortestPathTrajectoryResult implements Comparable<ShortestPathTrajectoryResult> {
    private final List<Point> pathGrids;
    private final int pathCost;
    private final List<CarMove> carMoves;

    public ShortestPathTrajectoryResult(int cost, List<Point> pathGrids, List<CarMove> carMoves) {
        this.carMoves = carMoves;
        this.pathGrids = pathGrids;
        this.pathCost = cost;
    }

    public List<Point> getPathGrids() {
        return pathGrids;
    }

    public List<CarMove> getCarMoves() {
        return carMoves;
    }

    public int getPathCost() {
        return pathCost;
    }

    @Override
    public int compareTo(ShortestPathTrajectoryResult o) {
        return Integer.compare(pathCost, o.pathCost);
    }
}
