package ntu.mdp.pathfinding.Algo;

import ntu.mdp.pathfinding.Obstacle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShortestPathAlgo {
    private Arena arena;
    private static int[] quadrantReverse = new int[]{0, 3, 4, 1, 2};
    private static int[] quadrantSameYSide = new int[]{0, 4, 3, 2, 1};
    private static int[] quadrantSameXSide = new int[]{0, 2, 1, 4, 3};

    private int R;

    private Obstacle[] obstacles;
    private int carR, carC;

    private ShortestPathBF shortestPathBF;

    public ShortestPathAlgo(int m, int n, Obstacle[] obstacles, int r, int c) {
        this.obstacles = obstacles;
        this.carR = r;
        this.carC = c;
        this.arena = new Arena(m, n, obstacles);
        this.shortestPathBF = new ShortestPathBF(obstacles, r, c);
    }

    public List<CarMove> findShortestValidPath() {
        shortestPathBF.findPath();

        Map<Integer, Obstacle> idxMapping = shortestPathBF.getIdxMapping();
        while (shortestPathBF.hasNextPath()) {
            int[] path = shortestPathBF.getNextPath();
            boolean pathValid = true;
            ArrayList<CarMove> moveSteps = new ArrayList<>();
            Obstacle car = idxMapping.get(path[0]);
            int carR = car.getR(), carC = car.getC(), theta = 90;
            for (int i = 1; i < path.length-1; i++) {
                Obstacle ob = idxMapping.get(path[i]);
                TrajectoryCalculation trajectoryCalculation = new TrajectoryCalculation(ob, carR, carC, theta);
                TrajectoryResult trajectoryResult= trajectoryCalculation.trajectoryResult();
                if (!validatePath(trajectoryResult, carR, carC, ob.getTargetedR(), ob.getTargetedC())) { pathValid = false; break; }
                moveSteps.add(trajectoryResult.getCarMove());
                carR = ob.getTargetedR(); carC = ob.getTargetedC();
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
        return checkCircle(startR, startC, r1, c1, circle1R, circle1C) && checkLine(r1, c1, r2, c2) &&
                checkCircle(r2, c2, endR, endC, circle2R, circle2C);
    }

    private boolean checkLine(int r1, int c1, int r2, int c2) {
        double a = (double) (r2 - r1) / (c2 - c1), b = r1 - c1 * a;
        for (int i = c1; i <= c2; i++) {
            double y = a * i + b;
            int j = (int) Math.ceil(y);
            if (!arena.checkWithCorrespondingBlock(i, j)) return false;
        }
        return true;
    }

    private boolean checkCircle(int r1, int c1, int r2, int c2, int circleR, int circleC) {
        int recTanR1 = 0, recTanC1 = 0, recTanR2 = 0, recTanC2 = 0;
        int quadrant1 = getQuadrantOfPoints(r1, c1, circleR, circleC);
        int quadrant2 = getQuadrantOfPoints(r2, c2, circleR, circleC);

        if (quadrant1 == quadrant2) {
            recTanR1 = Math.min(r1, r2); recTanR2 = Math.max(r1, r2);
            recTanC1 = Math.min(c1, c2); recTanC2 = Math.max(c1, c2);
        } else if (checkReverseQuadrant(quadrant1, quadrant2)) {
            recTanR1 = circleR - R; recTanR2 = circleR + R;
            recTanC1 = circleC - R; recTanC2 = circleC + R;
        } else if (checkQuadrantSameXSide(quadrant1, quadrant2)) {
            recTanR1 = quadrant1 <= 2 ? circleR - R : Math.min(r1, r2);
            recTanR2 = quadrant1 <= 2 ? Math.max(r1, r2) : circleR + R;
            recTanC1 = Math.min(c1, c2); recTanC2 = Math.max(c1, c2);
        } else if(checkQuadrantSameYSide(quadrant1, quadrant2)) {
            recTanR1 = Math.min(r1, r2); recTanR2 = Math.max(r1, r2);
            recTanC1 = (quadrant1 == 2 || quadrant1 == 3) ? circleC - R : Math.min(c1, c2);
            recTanC2 = (quadrant1 == 2 || quadrant1 == 3) ? Math.max(c1, c2) : circleC + R;
        }

        return checkPathWithArena(recTanR1, recTanC1, recTanR2, recTanC2);
    }

    private boolean checkPathWithArena(int r1, int c1, int r2, int c2) {
        for (int i  = r1; i <= r2; i++) {
            if (!arena.checkWithCorrespondingBlock(i, c1)) return false;
            if (!arena.checkWithCorrespondingBlock(i, c2)) return false;
        }

        for (int j = c1; j <= c2; j++) {
            if (!arena.checkWithCorrespondingBlock(r1, j)) return false;
            if (!arena.checkWithCorrespondingBlock(r2, j)) return false;
        }
        return true;
    }

    private int getQuadrantOfPoints(int r, int c, int circleR, int circleC) {
        if (c >= circleC) {
            return r <= circleR ? 1 : 4;
        } else {
            return r <= circleR ? 2 : 3;
        }
    }

    private boolean checkReverseQuadrant(int quadrant1, int quadrant2) {
        return quadrantReverse[quadrant1] == quadrant2;
    }

    private boolean checkQuadrantSameXSide(int quadrant1, int quadrant2) {
        return quadrantSameXSide[quadrant1] == quadrant2;
    }

    private boolean checkQuadrantSameYSide(int quadrant1, int quadrant2) {
        return quadrantSameYSide[quadrant1] == quadrant2;
    }
}
