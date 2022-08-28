package ntu.mdp.pathfinding.Algo;

import ntu.mdp.pathfinding.Car;
import ntu.mdp.pathfinding.Obstacle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShortestPathAlgo {
    private Arena arena;
    private Obstacle[] obstacles;
    private int carR, carC;
    private ShortestPathBF shortestPathBF;
    private int[][] dReverses = {{0, -1}, {-1, 0}, {0, 1}, {1, 0}};
    private int R = 25, m, n;

    private List<CarMove> carMoves;
    private List<int[]> pathGrids;

    public ShortestPathAlgo(int m, int n, Obstacle[] obstacles, int r, int c) {
        this.obstacles = obstacles;
        this.m = m;
        this.n = n;
        this.carR = r;
        this.carC = c;
        this.arena = new Arena(m, n, obstacles);
        this.shortestPathBF = new ShortestPathBF(obstacles, r, c);
    }

    public List<CarMove> findShortestValidPath() {
        shortestPathBF.findPath();

        Map<Integer, Obstacle> idxMapping = shortestPathBF.getIdxMapping();
        while (shortestPathBF.hasNextPath()) {
            carMoves = new ArrayList<>();
            pathGrids = new ArrayList<>();
            int[] path = shortestPathBF.getNextPath();
            boolean pathValid = true;
            Obstacle car = idxMapping.get(path[0]);
            int carR = car.getR(), carC = car.getC(), theta = 90;
            for (int i = 1; i < path.length; i++) {
                Obstacle ob = idxMapping.get(path[i]);
                TrajectoryCalculation trajectoryCalculation = new TrajectoryCalculation(ob.getTargetedC(), ob.getTargetedR(), ob.getDir(), carR, carR, theta);
                TrajectoryResult trajectoryResult= trajectoryCalculation.trajectoryResult();
                if (!validatePath(trajectoryResult, carR, carC, ob.getTargetedR(), ob.getTargetedC())) { pathValid = false; break; }
                carR = ob.getTargetedR(); carC = ob.getTargetedC(); theta = trajectoryResult.getCarMove().getTurnTheta2();
                if (i != path.length-1) {
                    int reversedR = carR + dReverses[ob.getDir()][0] * (R-1), reverseC = carC + dReverses[ob.getDir()][1] * (R-1);
                    if (!validateReversePath(carR, carC, reversedR, reverseC, dReverses[ob.getDir()])) { pathValid = false; break; }
                    carR = reversedR; carC = reverseC;
                }
            }
            if (pathValid) return carMoves;
        }
        return null;
    }

    private boolean validatePath(TrajectoryResult trajectoryResult, int startR, int startC, int endR, int endC) {
        int r1 = trajectoryResult.getPt1()[0], c1 = trajectoryResult.getPt1()[1];
        int r2 = trajectoryResult.getPt2()[0], c2 = trajectoryResult.getPt2()[1];
        int circle1R = trajectoryResult.getCircle1()[0], circle1C = trajectoryResult.getCircle1()[1];
        int circle2R = trajectoryResult.getCircle2()[0], circle2C = trajectoryResult.getCircle2()[1];
        boolean isClockwise1 = trajectoryResult.isClockwiseTurn1(), isClockwise2 = trajectoryResult.isClockwiseTurn2();

        List<int[]> points1 = TrajectoryToArenaGrid.findGridCirclePath(startR, startC, r1, c1, circle1R, circle1C, isClockwise1);
        if (!validatePoint(points1)) return false;

        List<int[]> points2 = TrajectoryToArenaGrid.findGirdLinePath(r1, c1, r2, c2);
        if (!validatePoint(points2)) return false;

        List<int[]> points3 = TrajectoryToArenaGrid.findGridCirclePath(r2, c2, endR, endC, circle2R, circle2C, isClockwise2);
        if(!validatePoint(points3)) return false;

        pathGrids.addAll(points1);
        pathGrids.addAll(points2);
        pathGrids.addAll(points3);
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
