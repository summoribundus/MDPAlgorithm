package ntu.mdp.pathfinding.Algo.Trajectory;

import ntu.mdp.pathfinding.Algo.CarMove;

import java.util.List;

public class ShortestPathTrajectoryResult implements Comparable<ShortestPathTrajectoryResult> {
    private final List<int[]> pathGrids;
    private final int pathCost, batch, idx;
    private final List<CarMove> carMoves;

    public ShortestPathTrajectoryResult(int batch, int idx, int cost, List<int[]> pathGrids, List<CarMove> carMoves) {
        this.carMoves = carMoves;
        this.pathGrids = pathGrids;
        this.pathCost = cost;
        this.batch = batch;
        this.idx = idx;
    }

    public List<int[]> getPathGrids() {
        return pathGrids;
    }

    public List<CarMove> getCarMoves() {
        return carMoves;
    }

    public int getBatch() {
        return batch;
    }

    public int getIdx() {
        return idx;
    }

    public int getPathCost() {
        return pathCost;
    }

    @Override
    public int compareTo(ShortestPathTrajectoryResult o) {
        return Integer.compare(pathCost, o.pathCost);
    }
}
