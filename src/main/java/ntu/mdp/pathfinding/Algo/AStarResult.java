package ntu.mdp.pathfinding.Algo;

import ntu.mdp.pathfinding.Car;

import java.util.List;

public class AStarResult {
    private List<CarMove> carMoves;
    private List<int[]> pointPath;

    public AStarResult(List<CarMove> carMoves, List<int[]> pointPath){
        this.carMoves = carMoves;
        this.pointPath = pointPath;
    }
}
