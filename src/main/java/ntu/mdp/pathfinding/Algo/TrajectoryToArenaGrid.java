package ntu.mdp.pathfinding.Algo;

import java.util.List;

public class TrajectoryToArenaGrid {
    private static int[] quadrantReverse = new int[]{0, 3, 4, 1, 2};
    private static int[] quadrantSameYSide = new int[]{0, 4, 3, 2, 1};
    private static int[] quadrantSameXSide = new int[]{0, 2, 1, 4, 3};

    private int R;

    private List<int[]> fridGridPathFromTrajectory(TrajectoryResult result) {

    }

    private List<int[]> findGridCirclePath(int r1, int c1, int r2, int c2, int circleR, int circleC, boolean isClockWise) {
        int recTanR1 = 0, recTanC1 = 0, recTanR2 = 0, recTanC2 = 0;
        int quadrant1 = getQuadrantOfPoints(r1, c1, circleR, circleC);
        int quadrant2 = getQuadrantOfPoints(r2, c2, circleR, circleC);

        if (quadrant1 == quadrant2) {
            recTanR1 = Math.min(r1, r2); recTanR2 = Math.max(r1, r2);
            recTanC1 = Math.min(c1, c2); recTanC2 = Math.max(c1, c2);
        } else if (checkReverseQuadrant(quadrant1, quadrant2)) {
            if (quadrant1 == 1) {
                if (isClockWise) {
                    recTanR1 = circleR + R; rec
                }
            }
        } else if (checkQuadrantSameXSide(quadrant1, quadrant2)) {
            recTanR1 = quadrant1 <= 2 ? circleR - R : Math.min(r1, r2);
            recTanR2 = quadrant1 <= 2 ? Math.max(r1, r2) : circleR + R;
            recTanC1 = Math.min(c1, c2); recTanC2 = Math.max(c1, c2);
        } else if(checkQuadrantSameYSide(quadrant1, quadrant2)) {
            recTanR1 = Math.min(r1, r2); recTanR2 = Math.max(r1, r2);
            recTanC1 = (quadrant1 == 2 || quadrant1 == 3) ? circleC - R : Math.min(c1, c2);
            recTanC2 = (quadrant1 == 2 || quadrant1 == 3) ? Math.max(c1, c2) : circleC + R;
        }
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
