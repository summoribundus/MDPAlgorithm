package ntu.mdp.pathfinding.Algo.AStar;

import ntu.mdp.pathfinding.Algo.CarMove;

public class ShortestPathAStarResult {
    private CarMove carMove;

    public ShortestPathAStarResult(int[] pt1, int[] pt2) {
        carMove = new CarMove(0, false, 0, false, lengthCompute(pt1, pt2));
    }

    public ShortestPathAStarResult(boolean isClockWiseTurn) {
        carMove = new CarMove(90, isClockWiseTurn, 0, false, 0);
    }
    private int lengthCompute(int[] pt1, int[] pt2) {
        if (pt1[0] == pt2[0])  return Math.abs(pt1[1] - pt2[1]);
        else return Math.abs(pt1[0] - pt2[0]);
    }

    public CarMove getCarMove() {
        return carMove;
    }
}
