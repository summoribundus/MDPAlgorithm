package ntu.mdp.pathfinding.Algo;

import ntu.mdp.pathfinding.Obstacle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShortestPathAlgo {
    private Arena arena;
    private Obstacle[] obstacles;
    private int carR, carC;
    private ShortestPathBF shortestPathBF;
    private List<int[]> lastPathGrids;

    private List<CarMove> moveSteps;

    private int R, m, n;

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
            lastPathGrids = new ArrayList<>();
            moveSteps = new ArrayList<>();
            int[] path = shortestPathBF.getNextPath();
            boolean pathValid = true;
            Obstacle car = idxMapping.get(path[0]);
            int carR = car.getR(), carC = car.getC(), theta = 90, reverseEnd = carR;
            for (int i = 1; i < path.length-1; i++) {
                Obstacle ob = idxMapping.get(path[i]);
                TrajectoryCalculation trajectoryCalculation = new TrajectoryCalculation(ob, carR, carC, theta);
                TrajectoryResult trajectoryResult= trajectoryCalculation.trajectoryResult();
                if (!validatePath(trajectoryResult, carR, carC, ob.getTargetedR(), ob.getTargetedC())) { pathValid = false; break; }
                moveSteps.add(trajectoryResult.getCarMove());

                carR = ob.getTargetedR(); carC = ob.getTargetedC(); theta = trajectoryResult.getCarMove().getTurnTheta2();
                reverseEnd = Math.min(carR + R, m);
                moveSteps.add(new CarMove(-1, -1, -(reverseEnd - carR)));
                addReversePoints(carR, reverseEnd, carC);
                carC = reverseEnd;
            }
            if (pathValid) return moveSteps;
        }
        return null;
    }

    private boolean validatePath(TrajectoryResult trajectoryResult, int startR, int startC, int endR, int endC) {
        int r1 = trajectoryResult.getPt1()[0], c1 = trajectoryResult.getPt1()[1];
        int r2 = trajectoryResult.getPt2()[0], c2 = trajectoryResult.getPt2()[1];
        int circle1R = trajectoryResult.getCircle1()[0], circle1C = trajectoryResult.getCircle1()[1];
        int circle2R = trajectoryResult.getCircle2()[0], circle2C = trajectoryResult.getCircle2()[1];
        boolean isClockwise1 = trajectoryResult.isClockwiseTurn1(), isClockwise2 = trajectoryResult.isClockwiseTurn2();

        List<int[]> points = TrajectoryToArenaGrid.findGridCirclePath(startR, startC, r1, c1, circle1R, circle1C, isClockwise1);
        if (!validatePoint(points)) return false;
        lastPathGrids.addAll(points);

        points = TrajectoryToArenaGrid.findGirdLinePath(r1, c1, r2, c2);
        if (!validatePoint(points)) return false;
        lastPathGrids.addAll(points);

        points = TrajectoryToArenaGrid.findGridCirclePath(r2, c2, endR, endC, circle2R, circle2C, isClockwise2);
        if(!validatePoint(points)) return false;
        lastPathGrids.addAll(points);

        return true;
    }

    private boolean validatePoint(List<int[]> points) {
        for (int[] p : points) {
            if (arena.checkWithCorrespondingBlock(p[0], p[1])) return false;
        }
        return true;
    }

    private void addReversePoints(int start, int end, int c) {
        for (int i = start + 1; i <= end; i++) lastPathGrids.add(new int[]{i, c});
    }

    public List<int[]> getLastPathGrids() {
        return lastPathGrids;
    }

    public List<CarMove> getMoveSteps() {
        return moveSteps;
    }
}
