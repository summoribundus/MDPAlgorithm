package ntu.mdp.pathfinding.Algo;

import ntu.mdp.pathfinding.Car;
import ntu.mdp.pathfinding.Obstacle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShortestPathAlgo {
    private final Arena arena;
    private final ShortestPathBF shortestPathBF;
    private final static int[][] dReverses = {{0, -1}, {-1, 0}, {0, 1}, {1, 0}};

    private List<CarMove> carMoves;
    private List<int[]> pathGrids;

    public ShortestPathAlgo(int m, int n, Obstacle[] obstacles, int r, int c) {
        this.arena = new Arena(m, n, obstacles);
        this.shortestPathBF = new ShortestPathBF(obstacles, r, c);
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
                pathGrids.add(new int[]{r, startC});
            }
            for (int c = startC; c != ob.getTargetedC(); c += cStep) {
                pathGrids.add(new int[]{ob.getTargetedR(), c});
            }
            pathGrids.add(new int[]{ob.getTargetedR(), ob.getTargetedC()});
            startR = ob.getTargetedR(); startC = ob.getTargetedC();
        }
        return new ArrayList<>();
    }

    public List<CarMove> findShortestValidPath() {
        shortestPathBF.findPath();

        Map<Integer, Obstacle> idxMapping = shortestPathBF.getIdxMapping();
        int cnt = 0;
        shortestPathBF.getPathSize();
        while (shortestPathBF.hasNextPath()) {
            System.out.println("Checking " + cnt +" path.");
            cnt++;
            carMoves = new ArrayList<>();
            pathGrids = new ArrayList<>();
            int[] path = shortestPathBF.getNextPath();
            boolean pathValid = true;
            Obstacle car = idxMapping.get(path[0]);
            int carR = car.getR(), carC = car.getC(), theta = 270;
            for (int i = 1; i < path.length; i++) {
                System.out.println("carR: " + carR + " carC: " + carC + " theta: " + theta);
                Obstacle ob = idxMapping.get(path[i]);
                TrajectoryCalculation trajectoryCalculation = new TrajectoryCalculation(ob.getTargetedC(), ob.getTargetedR(), ob.getDir(), carC, carR, theta);
                TrajectoryResult trajectoryResult = trajectoryCalculation.trajectoryResult();
                if (trajectoryResult == null) { pathValid = false; break; }
                if (!validatePath(trajectoryResult, carR, carC, ob.getTargetedR(), ob.getTargetedC(), true)) { pathValid = false; break; }
                carR = ob.getTargetedR(); carC = ob.getTargetedC(); theta = ob.getTargetedDegree();
                if (i != path.length-1) {
                    Obstacle nextOb = idxMapping.get(path[i+1]);
                    int reversedR = carR, reversedC = carC;
                    boolean found = false;
                    List<int[]> reversePath = new ArrayList<>();
                    while (arena.inRange(reversedR, reversedC)) {
                        trajectoryCalculation = new TrajectoryCalculation(nextOb.getTargetedC(), nextOb.getTargetedR(), nextOb.getDir(), reversedC, reversedR, theta);
                        trajectoryResult = trajectoryCalculation.trajectoryResult();
                        if (trajectoryResult != null && validatePath(trajectoryResult, reversedR, reversedC, nextOb.getTargetedR(), nextOb.getTargetedC(), false)) {found = true; break;}
                        reversedR += dReverses[ob.getDir()][0]; reversedC += dReverses[ob.getDir()][1];
                        reversePath.add(new int[]{reversedR, reversedC});
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

    private boolean validatePath(TrajectoryResult trajectoryResult, int startR, int startC, int endR, int endC, boolean pathRecord) {
        int r1 = trajectoryResult.getPt1()[1], c1 = trajectoryResult.getPt1()[0];
        int r2 = trajectoryResult.getPt2()[1], c2 = trajectoryResult.getPt2()[0];
        int circle1R = trajectoryResult.getCircle1()[1], circle1C = trajectoryResult.getCircle1()[0];
        int circle2R = trajectoryResult.getCircle2()[1], circle2C = trajectoryResult.getCircle2()[0];
        boolean isClockwise1 = trajectoryResult.isClockwiseTurn1(), isClockwise2 = trajectoryResult.isClockwiseTurn2();

        List<int[]> points1 = TrajectoryToArenaGrid.findGridCirclePath(startR, startC, r1, c1, circle1R, circle1C, isClockwise1);
        if (points1 == null || !validatePoint(points1)) return false;

        List<int[]> points2 = TrajectoryToArenaGrid.findGirdLinePath(r1, c1, r2, c2);
        if (points2 == null || !validatePoint(points2)) return false;

        List<int[]> points3 = TrajectoryToArenaGrid.findGridCirclePath(r2, c2, endR, endC, circle2R, circle2C, isClockwise2);
        if(points3 == null || !validatePoint(points3)) return false;

        if (pathRecord) {
            pathGrids.addAll(points1);
            pathGrids.addAll(points2);
            pathGrids.addAll(points3);
        }
        return true;
    }

    private boolean validateReversePath(int r1, int c1, int r2, int c2, int[] dReverse) {
        List<int[]> points = TrajectoryToArenaGrid.findReversePath(r1, c1, r2, c2, dReverse);
        if (!validatePoint(points)) return false;
        pathGrids.addAll(points);
        return true;
    }

    private boolean validatePoint(List<int[]> points) {
        for (int[] p : points) {
            if (arena.checkWithCorrespondingBlock(p[0], p[1])) return false;
        }
        return true;
    }

    public List<CarMove> getCarMoves() {
        return carMoves;
    }

    public List<int[]> getPathGrids() {
        return pathGrids;
    }
}
