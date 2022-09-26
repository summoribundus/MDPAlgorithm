package ntu.mdp.pathfinding.Algo.Trajectory;

import ntu.mdp.pathfinding.Instruction.Instruction;
import ntu.mdp.pathfinding.Point;

import java.util.List;

public class ShortestPathTrajectoryResult implements Comparable<ShortestPathTrajectoryResult> {
    private final List<Point> pathGrids;
    private final int pathCost;
    private final List<Instruction> instructions;

    public ShortestPathTrajectoryResult(int cost, List<Point> pathGrids, List<Instruction> instructions) {
        this.instructions = instructions;
        this.pathGrids = pathGrids;
        this.pathCost = cost;
    }

    public List<Point> getPathGrids() {
        return pathGrids;
    }

    public List<Instruction> getInstructions() {
        return instructions;
    }

    public int getPathCost() {
        return pathCost;
    }

    @Override
    public int compareTo(ShortestPathTrajectoryResult o) {
        return Integer.compare(pathCost, o.pathCost);
    }
}
