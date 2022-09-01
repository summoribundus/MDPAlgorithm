package ntu.mdp.pathfinding.Algo;

import ntu.mdp.pathfinding.Obstacle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

class ShortestPathTrajectoryResult implements Comparable<ShortestPathTrajectoryResult> {
    private final List<int[]> pathGrids;
    private final int pathCost, batch, idx;
    private final List<CarMove> carMoves;

    public ShortestPathTrajectoryResult(int batch, int idx, List<int[]> pathGrids, List<CarMove> carMoves) {
        this.carMoves = carMoves;
        this.pathGrids = pathGrids;
        this.pathCost = pathGrids.size();
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

public class ShortestPathTrajectoryTask implements Callable<ShortestPathTrajectoryResult> {
    private final int batch, idx;
    private final int[] path;
    private final Map<Integer, Obstacle> idxMap;
    private final Arena arena;

    public ShortestPathTrajectoryTask(int batch, int idx, int[] path, Map<Integer, Obstacle> idxMap, Arena arena) {
        this.batch = batch;
        this.idx = idx;
        this.path = path;
        this.idxMap = idxMap;
        this.arena = arena;
    }

    @Override
    public ShortestPathTrajectoryResult call() throws Exception {
        List<int[]> pathGrids = new ArrayList<>();
        List<CarMove> carMoves = new ArrayList<>();
        Obstacle car = idxMap.get(path[0]);
        int carR = car.getR(), carC = car.getC(), theta = 270;
        boolean pathValid = true;
        for (int i = 1; i < path.length; i++) {
            Obstacle ob = idxMap.get(path[i]);
            TrajectoryCalculation trajectoryCalculation = new TrajectoryCalculation(ob.getTargetedC(),
                    ob.getTargetedR(), ob.getDir(), carC, carR, theta);
            TrajectoryResult trajectoryResult = trajectoryCalculation.trajectoryResult();
            if (trajectoryResult == null || !ShortestPathTrajectory.validatePath(trajectoryResult, carR, carC,
                    ob.getTargetedR(), ob.getTargetedC(), pathGrids, arena)) { pathValid = false; break; }
            carMoves.add(trajectoryResult.getCarMove());

            carR = ob.getTargetedR(); carC = ob.getTargetedC(); theta = ob.getTargetedDegree();
            if (i == path.length-1) continue;

            Obstacle nextOb = idxMap.get(path[i+1]);
            int reversedR = carR, reversedC = carC;
            boolean found = false;
            List<int[]> reversePath = new ArrayList<>();
            while (!arena.checkWithCorrespondingBlock(reversedR, reversedC)) {
                trajectoryCalculation = new TrajectoryCalculation(nextOb.getTargetedC(), nextOb.getTargetedR(),
                        nextOb.getDir(), reversedC, reversedR, theta);
                trajectoryResult = trajectoryCalculation.trajectoryResult();

                if (trajectoryResult != null && ShortestPathTrajectory.validatePath(trajectoryResult,
                        reversedR, reversedC, nextOb.getTargetedR(), nextOb.getTargetedC(), null, arena)) {
                    found = true;
                    break;
                }
                reversedR += ShortestPathTrajectory.dReverses[ob.getDir()][0]; reversedC += ShortestPathTrajectory.dReverses[ob.getDir()][1];
                reversePath.add(new int[]{reversedR, reversedC});
            }

            if (!found) { pathValid = false; break;}
            pathGrids.addAll(reversePath);
            carR = reversedR; carC = reversedC;
        }
        if (!pathValid) return null;
        return new ShortestPathTrajectoryResult(batch, idx, pathGrids, carMoves);
    }
}
