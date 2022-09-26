package ntu.mdp.pathfinding.Algo.AStar;

import ntu.mdp.pathfinding.Algo.CarMove;
import ntu.mdp.pathfinding.Point;

import java.util.ArrayList;
import java.util.List;

public class ShortestPathAStarResult implements Comparable<ShortestPathAStarResult> {
    private List<CarMove> carMoves;
    private List<Point> pointPath;

    private int cost;

    public ShortestPathAStarResult(List<Point> pointPath, int cost){
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

    public void computeCompressedCarMove() {
        carMoves = new ArrayList<>();

        Integer lastDir = null;
        for (Point point : pointPath) {
            if (lastDir == null) {

            }
        }
    }

    @Override
    public int compareTo(ShortestPathAStarResult o) {
        return Integer.compare(cost, o.cost);
    }
}
