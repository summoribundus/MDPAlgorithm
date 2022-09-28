package ntu.mdp.pathfinding.Algo.Trajectory;

import ntu.mdp.pathfinding.*;
import ntu.mdp.pathfinding.Algo.Arena;
import ntu.mdp.pathfinding.Algo.Trajectory.TrajectoryCalculation.TrajectoryCalculation;
import ntu.mdp.pathfinding.Algo.Trajectory.TrajectoryCalculation.TrajectoryResult;
import ntu.mdp.pathfinding.Instruction.Instruction;
import ntu.mdp.pathfinding.Instruction.LineInstruction;
import ntu.mdp.pathfinding.Instruction.TakePictureInstruction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class ShortestPathTrajectoryTask implements Callable<ShortestPathTrajectoryResult> {
    private final int[] path;
    private final Map<Integer, Obstacle> idxMap;
    private final Arena arena;

    public ShortestPathTrajectoryTask(int[] path, Map<Integer, Obstacle> idxMap, Arena arena) {
        this.path = path;
        this.idxMap = idxMap;
        this.arena = arena;
    }

    @Override
    public ShortestPathTrajectoryResult call() throws Exception {
        List<Point> pathGrids = new ArrayList<>();
        List<Instruction> instructions = new ArrayList<>();
        Obstacle car = idxMap.get(path[0]);
        int carR = car.getR(), carC = car.getC(), theta = 270;
        boolean pathValid = true;
        int cost = 0;
        for (int i = 1; i < path.length; i++) {
            Obstacle ob = idxMap.get(path[i]);
            TrajectoryCalculation trajectoryCalculation = new TrajectoryCalculation(ob.getTargetedC(),
                    ob.getTargetedR(), ob.getDir(), carC, carR, theta);
            TrajectoryResult trajectoryResult = trajectoryCalculation.trajectoryResult();
            if (trajectoryResult == null || !ShortestPathTrajectory.validatePath(trajectoryResult, carR, carC,
                    ob.getTargetedR(), ob.getTargetedC(), pathGrids, arena)) { pathValid = false; break; }
            instructions.addAll(trajectoryResult.getInstructionList());
            cost += trajectoryResult.getTotalLength();

            carR = ob.getTargetedR(); carC = ob.getTargetedC(); theta = ob.getTargetedDegree();
            pathGrids.add(new Point(carR, carC,true, ob.getObstacleID()));
            instructions.add(new TakePictureInstruction(ob.getObstacleID()));
            if (i == path.length-1) continue;

            Obstacle nextOb = idxMap.get(path[i+1]);
            int reversedR = carR, reversedC = carC;
            boolean found = false;
            List<Point> reversePath = new ArrayList<>();
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
                reversePath.add(new Point(reversedR, reversedC));
            }

            if (!found) { pathValid = false; break;}
            int length = reversedR == carR ? Math.abs(reversedC - carC) * 5 : Math.abs(reversedR - carR) * 5;
            instructions.add(new LineInstruction(-length));
            pathGrids.addAll(reversePath);
            carR = reversedR; carC = reversedC;
        }
        if (!pathValid) return null;
        return new ShortestPathTrajectoryResult(cost, pathGrids, instructions);
    }
}
