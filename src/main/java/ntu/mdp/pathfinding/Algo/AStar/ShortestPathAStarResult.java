package ntu.mdp.pathfinding.Algo.AStar;

import ntu.mdp.pathfinding.Algo.CarMove;
import ntu.mdp.pathfinding.Point;

import java.util.List;

public class ShortestPathAStarResult implements Comparable<ShortestPathAStarResult> {
    private List<CarMove> carMoves;
    private List<Point> pointPath;

    private int cost;

    public ShortestPathAStarResult(List<CarMove> carMoves, List<Point> pointPath, int cost){
        this.carMoves = carMoves;
        this.pointPath = pointPath;
        this.cost = cost;
    }

    public List<CarMove> getCarMoves() {
        return carMoves;
    }

    public List<Point> getPointPath() {
        return pointPath;
    }

    public int getCost() {
        return cost;
    }

    @Override
    public int compareTo(ShortestPathAStarResult o) {
        return Integer.compare(cost, o.cost);
    }
}
