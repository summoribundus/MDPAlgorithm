package ntu.mdp.pathfinding.Algo;

import ntu.mdp.pathfinding.Car;

import java.util.List;

public class AStarResult implements Comparable<AStarResult> {
    private List<CarMove> carMoves;
    private List<int[]> pointPath;

    private int cost;

    public AStarResult(List<CarMove> carMoves, List<int[]> pointPath, int cost){
        this.carMoves = carMoves;
        this.pointPath = pointPath;
        this.cost = cost;
    }

    public List<CarMove> getCarMoves() {
        return carMoves;
    }

    public List<int[]> getPointPath() {
        return pointPath;
    }

    public int getCost() {
        return cost;
    }

    @Override
    public int compareTo(AStarResult o) {
        return Integer.compare(cost, o.cost);
    }
}
