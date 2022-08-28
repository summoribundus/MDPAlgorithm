package ntu.mdp.pathfinding.Algo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TrajectoryToArenaGrid {
    private static int[] quadrantReverse = {0, 3, 4, 1, 2};
    private static int[] quadrantSameYSide = {0, 4, 3, 2, 1};
    private static int[] quadrantSameXSide = {0, 2, 1, 4, 3};
    private static int[][][] dEdgeLooping = {{{-1, 0}, {0, 1}, {1, 0}, {0, -1}}, {{1, 0}, {0, -1}, {-1, 0}, {0, 1}}};

    private static int R = 5;

    public static List<int[]> findReversePath(int r1, int c1, int r2, int c2, int[] dReverse) {
        List<int[]> pointsPassed = new ArrayList<>();
        if (dReverse[0] == 0) {
            for (int i = c1; i != c2; i += dReverse[1]) {
                pointsPassed.add(new int[]{r1, i});
            }
        } else {
            for (int i = r1; i != r2; i += dReverse[0]) {
                pointsPassed.add(new int[]{i, c1});
            }
        }
        pointsPassed.add(new int[]{r2, c2});
        return pointsPassed;
    }

    public static List<int[]> findGirdLinePath(int r1, int c1, int r2, int c2) {
        List<int[]> pointsPassed = new ArrayList<>();
        double a = (double) (r2 - r1) / (c2 - c1), b = r1 - c1 * a;
        int step = c1 < c2 ? 1 : -1;
        for (int i = c1; i != c2; i += step) {
            double y = a * i + b;
            int j = (int) Math.ceil(y);
            pointsPassed.add(new int[]{j, i});
        }
        pointsPassed.add(new int[]{r2, c2});
        return pointsPassed;
    }

    public static List<int[]> findGridCirclePath(int r1, int c1, int r2, int c2, int circleR, int circleC, boolean isClockWise) {
        int recTanR1 = 0, recTanC1 = 0, recTanR2 = 0, recTanC2 = 0;
        int quadrant1 = getQuadrantOfPoints(r1, c1, circleR, circleC);
        int quadrant2 = getQuadrantOfPoints(r2, c2, circleR, circleC);;

        if (quadrant1 == quadrant2) {
            recTanR1 = Math.min(r1, r2); recTanR2 = Math.max(r1, r2);
            recTanC1 = Math.min(c1, c2); recTanC2 = Math.max(c1, c2);
        } else if (checkReverseQuadrant(quadrant1, quadrant2)) {
            boolean CClockwiseTurn = (quadrant1 == 2 || quadrant1 == 4) ? ((quadrant2 == 2) != isClockWise) : ((quadrant1 == 1) == isClockWise);
            boolean RClockwiseTurn = (quadrant1 == 2 || quadrant1 == 4) ? ((quadrant2 == 2) != isClockWise) : ((quadrant2 == 1) == isClockWise);
            recTanR1 = RClockwiseTurn ? circleR - R : Math.min(r1, r2);
            recTanR2 = RClockwiseTurn ? Math.max(r1, r2) : circleR + R;
            recTanC1 = CClockwiseTurn ? Math.min(c1, c2) : circleC - R;
            recTanC2 = CClockwiseTurn ? circleC + R : Math.max(c1, c2);
        } else if (checkQuadrantSameXSide(quadrant1, quadrant2)) {
            recTanR1 = quadrant1 <= 2 ? circleR - R : Math.min(r1, r2);
            recTanR2 = quadrant1 <= 2 ? Math.max(r1, r2) : circleR + R;
            recTanC1 = Math.min(c1, c2); recTanC2 = Math.max(c1, c2);
        } else if(checkQuadrantSameYSide(quadrant1, quadrant2)) {
            recTanR1 = Math.min(r1, r2); recTanR2 = Math.max(r1, r2);
            recTanC1 = (quadrant1 == 2 || quadrant1 == 3) ? circleC - R : Math.min(c1, c2);
            recTanC2 = (quadrant1 == 2 || quadrant1 == 3) ? Math.max(c1, c2) : circleC + R;
        }

        int[][] endingPoint = {{recTanR1, recTanC2, recTanR2, recTanC1}, {recTanR2, recTanC1, recTanR1, recTanC2}};

        int edge1 = getEdgeOfPoints(recTanR1, recTanR2, recTanC1, recTanC2, r1, c1);
        int edge2 = getEdgeOfPoints(recTanR1, recTanR2, recTanC1, recTanC2, r2, c2);
        int turn = isClockWise ? 0 : 1;
        endingPoint[turn][edge2] = (edge2 == 0 || edge2 == 2) ? r2 : c2;
        return circleRecTanEdgeLooping(endingPoint[turn], r1, c1, r2, c2, edge1, edge2, turn);
    }

    private static List<int[]> circleRecTanEdgeLooping(int[] endP, int r1, int c1, int r2, int c2, int edge1, int edge2, int turn) {
        List<int[]> pointsPassed = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            int walkingEdge = turn == 0 ? (edge1 + i) % 4 : (edge1 - i + 4) % 4;
            int[] currentDir = dEdgeLooping[turn][walkingEdge];
            if (currentDir[0] == 0) {
                while (c1 != endP[walkingEdge]) {
                    pointsPassed.add(new int[]{r1, c1});
                    c1 += currentDir[1];
                }
            } else {
                while (r1 != endP[walkingEdge]) {
                    pointsPassed.add(new int[]{r1, c1});
                    r1 += currentDir[0];
                }
            }
            if (walkingEdge == edge2) break;
        }
        pointsPassed.add(new int[]{r2, c2});
        return pointsPassed;
    }

    private static int getEdgeOfPoints(int recTanR1, int recTanR2, int recTanC1, int recTanC2, int r, int c) {
        if (c == recTanC1) return 0;
        else if (c == recTanC2) return 2;
        else {
            return recTanR1 == r ? 1 : 3;
        }
    }

    private static int getQuadrantOfPoints(int r, int c, int circleR, int circleC) {
        if (c >= circleC) {
            return r <= circleR ? 1 : 4;
        } else {
            return r <= circleR ? 2 : 3;
        }
    }

    private static boolean checkReverseQuadrant(int quadrant1, int quadrant2) {
        return quadrantReverse[quadrant1] == quadrant2;
    }

    private static boolean checkQuadrantSameXSide(int quadrant1, int quadrant2) {
        return quadrantSameXSide[quadrant1] == quadrant2;
    }

    private static boolean checkQuadrantSameYSide(int quadrant1, int quadrant2) {
        return quadrantSameYSide[quadrant1] == quadrant2;
    }

    public static void main(String[] args){
//        List<int[]> points = findGridCirclePath(11, 0, 15, 8, 11, 5, false);
//        List<int[]> points = findGirdLinePath();
        List<int[]> points = findReversePath(15, 8, 15, 3, new int[]{0, -1});
        for (int[] p : points) {
            System.out.println(Arrays.toString(p));
        }
    }
}
