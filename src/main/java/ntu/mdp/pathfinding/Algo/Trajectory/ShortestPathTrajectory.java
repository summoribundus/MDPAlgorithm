package ntu.mdp.pathfinding.Algo.Trajectory;

import ntu.mdp.pathfinding.Algo.Arena;
import ntu.mdp.pathfinding.Algo.CarMove;
import ntu.mdp.pathfinding.Algo.ShortestPathBF;
import ntu.mdp.pathfinding.Algo.Trajectory.TrajectoryCalculation.TrajectoryCalculation;
import ntu.mdp.pathfinding.Algo.Trajectory.TrajectoryCalculation.TrajectoryResult;
import ntu.mdp.pathfinding.Obstacle;
import ntu.mdp.pathfinding.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShortestPathTrajectory {
    private final Arena arena;
    private final ShortestPathBF shortestPathBF;
    public final static int[][] dReverses = {{0, -1}, {-1, 0}, {0, 1}, {1, 0}};

    private List<CarMove> carMoves;
    private List<Point> pathGrids;

    public ShortestPathTrajectory(Arena arena, ShortestPathBF shortestPathBF) {
        this.arena = arena;
        this.shortestPathBF = shortestPathBF;
    }

    public List<CarMove> fakeShortestValidPathForTesting() {
        shortestPathBF.findPath();
        Map<Integer, Obstacle> idxMapping = shortestPathBF.getIdxMapping();
        int[] path = shortestPathBF.getNextPath();
        pathGrids = new ArrayList<>();
        int startR = idxMapping.get(path[0]).getR(), startC = idxMapping.get(path[0]).getC(), rStep, cStep;
        for (int i = 1; i < path.length; i++) {
            Obstacle ob = idxMapping.get(path[i]);
            rStep = ob.getTargetedR() > startR ? 1 : -1;
            cStep = ob.getTargetedC() > startC ? 1 : -1;
            for (int r = startR; r != ob.getTargetedR(); r += rStep) {
                pathGrids.add(new Point(r, startC));
            }
            for (int c = startC; c != ob.getTargetedC(); c += cStep) {
                pathGrids.add(new Point(ob.getTargetedR(), c));
            }
            pathGrids.add(new Point(ob.getTargetedR(), ob.getTargetedC()));
            startR = ob.getTargetedR(); startC = ob.getTargetedC();
        }
        return new ArrayList<>();
    }

    public List<CarMove> findShortestValidPath() {
        Map<Integer, Obstacle> idxMapping = shortestPathBF.getIdxMapping();
        while (shortestPathBF.hasNextPath()) {
            carMoves = new ArrayList<>();
            pathGrids = new ArrayList<>();
            int[] path = shortestPathBF.getNextPath();
            boolean pathValid = true;
            Obstacle car = idxMapping.get(path[0]);
            int carR = car.getR(), carC = car.getC(), theta = 270;
            for (int i = 1; i < path.length; i++) {
                Obstacle ob = idxMapping.get(path[i]);
                TrajectoryCalculation trajectoryCalculation = new TrajectoryCalculation(ob.getTargetedC(), ob.getTargetedR(), ob.getDir(), carC, carR, theta);
                TrajectoryResult trajectoryResult = trajectoryCalculation.trajectoryResult();
                if (trajectoryResult == null) { pathValid = false; break; }
                if (!validatePath(trajectoryResult, carR, carC, ob.getTargetedR(), ob.getTargetedC(), pathGrids, arena)) { pathValid = false; break; }
                carR = ob.getTargetedR(); carC = ob.getTargetedC(); theta = ob.getTargetedDegree();
                if (i != path.length-1) {
                    Obstacle nextOb = idxMapping.get(path[i+1]);
                    int reversedR = carR, reversedC = carC;
                    boolean found = false;
                    List<Point> reversePath = new ArrayList<>();
                    while (!arena.checkWithCorrespondingBlock(reversedR, reversedC)) {
                        trajectoryCalculation = new TrajectoryCalculation(nextOb.getTargetedC(), nextOb.getTargetedR(), nextOb.getDir(), reversedC, reversedR, theta);
                        trajectoryResult = trajectoryCalculation.trajectoryResult();
                        if (trajectoryResult != null && validatePath(trajectoryResult, reversedR, reversedC, nextOb.getTargetedR(), nextOb.getTargetedC(), null, arena)) {found = true; break;}
                        reversedR += dReverses[ob.getDir()][0]; reversedC += dReverses[ob.getDir()][1];
                        reversePath.add(new Point(reversedR, reversedC));
                    }
                    if (!found) { pathValid = false; break;}
                    pathGrids.addAll(reversePath);
                    carR = reversedR; carC = reversedC;
                }
            }
            if (pathValid) return carMoves;

        }
        return null;
    }

    public static boolean validatePath(TrajectoryResult trajectoryResult, int startR, int startC, int endR, int endC,
                                       List<Point> gridCollector, Arena arena) {

        int r1 = trajectoryResult.getPt1()[1], c1 = trajectoryResult.getPt1()[0];
        int r2 = trajectoryResult.getPt2()[1], c2 = trajectoryResult.getPt2()[0];
        int circle1R = trajectoryResult.getCircle1()[1], circle1C = trajectoryResult.getCircle1()[0];
        int circle2R = trajectoryResult.getCircle2()[1], circle2C = trajectoryResult.getCircle2()[0];

        List<Point> points1 = TrajectoryToArenaGrid.findGridCirclePath(startR, startC, r1, c1, circle1R, circle1C, trajectoryResult.isClockwiseTurnStart());
        if (points1 == null || !arena.validatePoint(points1)) return false;

        List<Point> points2;
        if (!trajectoryResult.isAllCurve()) {
            points2 = TrajectoryToArenaGrid.findGirdLinePath(r1, c1, r2, c2);
        } else {
            int circleInterR = trajectoryResult.getCircleInter()[1], circleInterC = trajectoryResult.getCircleInter()[0];
            points2 = TrajectoryToArenaGrid.findGridCirclePath(r1, c1, r2, c2, circleInterR, circleInterC, trajectoryResult.isClockwiseTurnIntermediate());
        }
        if (points2 == null || !arena.validatePoint(points2)) return false;

        List<Point> points3 = TrajectoryToArenaGrid.findGridCirclePath(r2, c2, endR, endC, circle2R, circle2C, trajectoryResult.isClockwiseTurnEnd());
        if(points3 == null || !arena.validatePoint(points3)) return false;

        if (gridCollector != null) {
            gridCollector.addAll(points1);
            gridCollector.addAll(points2);
            gridCollector.addAll(points3);
        }
        return true;
    }

    public List<CarMove> getCarMoves() {
        return carMoves;
    }

    public List<Point> getPathGrids() {
        return pathGrids;
    }
}
