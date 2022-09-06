package ntu.mdp.pathfinding.Algo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Edge {
    private int stR, stC;
    private int edR, edC;
    private int dr, dc;

    public Edge(int stR, int stC, int edR, int edC) {
        this.stR = stR;
        this.stC = stC;
        this.edR = edR;
        this.edC = edC;
        this.dr = Integer.compare(edR, stR);
        this.dc = Integer.compare(edC, stC);
    }

    public int getStR() {
        return stR;
    }

    public int getStC() {
        return stC;
    }

    public int getEdR() {
        return edR;
    }

    public int getEdC() {
        return edC;
    }

    public int getDr() {
        return dr;
    }

    public int getDc() {
        return dc;
    }

    @Override
    public String toString() {
        return "Edge{" +
                "stR=" + stR +
                ", stC=" + stC +
                ", edR=" + edR +
                ", edC=" + edC +
                ", dr=" + dr +
                ", dc=" + dc +
                '}';
    }
}

public class TrajectoryToArenaGrid {
    private static final int[] quadrantReverse = {0, 3, 4, 1, 2};
    private static final int[] quadrantSameYSide = {0, 4, 3, 2, 1};
    private static final int[] quadrantSameXSide = {0, 2, 1, 4, 3};
    private static final int[][][] dEdgeLooping = {{{-1, 0}, {0, 1}, {1, 0}, {0, -1}}, {{1, 0}, {0, -1}, {-1, 0}, {0, 1}}};

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
        int i;
        for (i = c1; i >= 0 && i != c2 && i < AlgoConstant.GridN; i += step) {
            double y = a * i + b;
            int j = (int) Math.ceil(y);
            pointsPassed.add(new int[]{j, i});
        }
        if (i != c2) {
            return null;
        }
        pointsPassed.add(new int[]{r2, c2});
        return pointsPassed;
    }

    public static List<int[]> findGridCirclePath(int r1, int c1, int r2, int c2, int circleR, int circleC, boolean isClockWise) {
        int recTanR1 = 0, recTanC1 = 0, recTanR2 = 0, recTanC2 = 0;
        int quadrant1 = getQuadrantOfPoints(r1, c1, circleR, circleC);
        int quadrant2 = getQuadrantOfPoints(r2, c2, circleR, circleC);
        List<Edge> edges = new ArrayList<>();

        if (quadrant1 == quadrant2) {
//            boolean shorter = (quadrant1 == 1 || quadrant1 == 4) ? r1 > r2 ? isClockWise : !isClockWise : r1 < r2 ? isClockWise : !isClockWise;
//            if (shorter) {
//                int xAxis = (quadrant1 == 1 || quadrant1 == 2) ? Math.max(r1, r2) : Math.min(r1, r2);
//                int yAxis = (quadrant1 == 1 || quadrant1 == 2) ? Math.max(c1, c2) : Math.min(c1, c2);
//                edges.add(new Edge(r1, c1, xAxis, yAxis));
//                edges.add(new Edge(xAxis, yAxis, r2, c2));
//            } else {
//                int yAxis1 = (quadrant1 == 1 || quadrant1 == 2) ? r1 > r2 ? circleR - AlgoConstant.R : r1 : r1 < r2 ? circleR + AlgoConstant.R : r1;
//                int xAxis1 = (quadrant1 == 1 || quadrant1 == 2) ? r1 > r2 ? c1 : (quadrant1 == 1) ? circleC + AlgoConstant.R : circleC - AlgoConstant.R : r1 < r2 ? c1 : (quadrant1 == 3) ? circleC - AlgoConstant.R : circleC + AlgoConstant.R;
//                int yAxis2 = (quadrant1 == 1 || quadrant1 == 2) ? r1 > r2 ? circleR - AlgoConstant.R : circleR + AlgoConstant.R : r1 < r2 ? circleR + AlgoConstant.R : circleR - AlgoConstant.R;
//                int xAxis2 = (quadrant1 == 1 || quadrant1 == 3) ? r1 > r2 ? circleC - AlgoConstant.R : circleC + AlgoConstant.R : r1 < r2 ? circleC - AlgoConstant.R : circleC + AlgoConstant.R;
//                int yAxis3 = (quadrant1 == 1 || quadrant1 == 2) ? circleR + AlgoConstant.R : circleR - AlgoConstant.R;
//                int xAxis3 = (quadrant1 == 1 || quadrant1 == 4) ? circleC - AlgoConstant.R : circleC + AlgoConstant.R;
//                edges.add(new Edge(r1, c1, yAxis1, xAxis1));
//                edges.add(new Edge(yAxis1, xAxis1, yAxis2, xAxis2));
//                edges.add(new Edge(yAxis2, xAxis2, yAxis3,  xAxis3));
//                edges.add(new Edge(yAxis3, xAxis3, circleR + AlgoConstant.R, circleC + AlgoConstant.R));
//                edges.add(new Edge(circleR + AlgoConstant.R, circleC + AlgoConstant.R, r2, circleC + AlgoConstant.R));
//                edges.add(new Edge(r2, circleC + AlgoConstant.R, r2, c2));
//            }
            if (quadrant1 == 1) {
                if (r1 < r2) {
                    if (isClockWise) {
                        edges.add(new Edge(r1, c1, r1, c2));
                        edges.add(new Edge(r1, c2, r2, c2));
                    } else {
                        edges.add(new Edge(r1, c1, circleR - AlgoConstant.R, c1));
                        edges.add(new Edge(circleR - AlgoConstant.R, c1, circleR - AlgoConstant.R, circleC - AlgoConstant.R));
                        edges.add(new Edge(circleR - AlgoConstant.R, circleC - AlgoConstant.R, circleR + AlgoConstant.R, circleC - AlgoConstant.R));
                        edges.add(new Edge(circleR + AlgoConstant.R, circleC - AlgoConstant.R, circleR + AlgoConstant.R, circleC + AlgoConstant.R));
                        edges.add(new Edge(circleR + AlgoConstant.R, circleC + AlgoConstant.R, r2, circleC + AlgoConstant.R));
                        edges.add(new Edge(r2, circleC + AlgoConstant.R, r2, c2));
                    }
                } else {
                    if (!isClockWise) {
                        edges.add(new Edge(r1, c1, r2, c1));
                        edges.add(new Edge(r2, c1, r2, c2));
                    } else {
                        edges.add(new Edge(r1, c1, r1, circleC + AlgoConstant.R));
                        edges.add(new Edge(r1, circleC + AlgoConstant.R, circleR + AlgoConstant.R, circleC + AlgoConstant.R));
                        edges.add(new Edge(circleR + AlgoConstant.R, circleC + AlgoConstant.R, circleR + AlgoConstant.R, circleC - AlgoConstant.R));
                        edges.add(new Edge(circleR + AlgoConstant.R, circleC - AlgoConstant.R, circleR - AlgoConstant.R, circleC - AlgoConstant.R));
                        edges.add(new Edge(circleR - AlgoConstant.R, circleC - AlgoConstant.R, circleR - AlgoConstant.R, c2));
                        edges.add(new Edge(circleR - AlgoConstant.R, c2, r2, c2));
                    }
                }
            } else if (quadrant1 == 2) {
                if (r1 > r2) {
                    if (isClockWise) {
                        edges.add(new Edge(r1, c1, r2, c1));
                        edges.add(new Edge(r2, c1, r2, c2));
                    } else {
                        edges.add(new Edge(r1, r2, r1, circleC - AlgoConstant.R));
                        edges.add(new Edge(r1, circleC - AlgoConstant.R, circleR + AlgoConstant.R, circleC - AlgoConstant.R));
                        edges.add(new Edge(circleR + AlgoConstant.R, circleC - AlgoConstant.R, circleR + AlgoConstant.R, circleC + AlgoConstant.R));
                        edges.add(new Edge(circleR + AlgoConstant.R, circleC + AlgoConstant.R, circleR - AlgoConstant.R, circleC + AlgoConstant.R));
                        edges.add(new Edge(circleR - AlgoConstant.R, circleC + AlgoConstant.R, circleR - AlgoConstant.R, c2));
                        edges.add(new Edge(circleR - AlgoConstant.R, c2, r2, c2));
                    }
                } else {
                    if (!isClockWise) {
                        edges.add(new Edge(r1, c1, r1, c2));
                        edges.add(new Edge(r1, c2, r2, c2));
                    } else {
                        edges.add(new Edge(r1, c1, circleR - AlgoConstant.R, c1));
                        edges.add(new Edge(circleR - AlgoConstant.R, c1, circleR - AlgoConstant.R, circleC + AlgoConstant.R));
                        edges.add(new Edge(circleR - AlgoConstant.R, circleC + AlgoConstant.R, circleR + AlgoConstant.R, circleC + AlgoConstant.R));
                        edges.add(new Edge(circleR + AlgoConstant.R, circleC + AlgoConstant.R, circleR + AlgoConstant.R, circleC - AlgoConstant.R));
                        edges.add(new Edge(circleR + AlgoConstant.R, circleC - AlgoConstant.R, r2, circleC - AlgoConstant.R));
                        edges.add(new Edge(r2, circleC - AlgoConstant.R, r2, c2));
                    }
                }
            } else if (quadrant1 == 3) {
                if (r1 > r2) {
                    if (isClockWise) {
                        edges.add(new Edge(r1, c1, r1, c2));
                        edges.add(new Edge(r1, c2, r2, c2));
                    } else {
                        edges.add(new Edge(r1, c1, circleR + AlgoConstant.R, c1));
                        edges.add(new Edge(circleR + AlgoConstant.R, c1, circleR + AlgoConstant.R, circleC + AlgoConstant.R));
                        edges.add(new Edge(circleR + AlgoConstant.R, circleC + AlgoConstant.R, circleR - AlgoConstant.R, circleC + AlgoConstant.R));
                        edges.add(new Edge(circleR - AlgoConstant.R, circleC + AlgoConstant.R, circleR - AlgoConstant.R, circleC - AlgoConstant.R));
                        edges.add(new Edge(circleR - AlgoConstant.R, circleC - AlgoConstant.R, r2, circleC - AlgoConstant.R));
                        edges.add(new Edge(r2, circleC - AlgoConstant.R, r2, c2));
                    }
                } else {
                    if (!isClockWise) {
                        edges.add(new Edge(r1, c1, r2, c1));
                        edges.add(new Edge(r2, c1, r2, c2));
                    } else {
                        edges.add(new Edge(r1, c1, r1, circleC - AlgoConstant.R));
                        edges.add(new Edge(r1, circleC - AlgoConstant.R, circleR - AlgoConstant.R, circleC - AlgoConstant.R));
                        edges.add(new Edge(circleR - AlgoConstant.R, circleC - AlgoConstant.R, circleR - AlgoConstant.R, circleC + AlgoConstant.R));
                        edges.add(new Edge(circleR - AlgoConstant.R, circleC + AlgoConstant.R, circleR + AlgoConstant.R, circleC + AlgoConstant.R));
                        edges.add(new Edge(circleR + AlgoConstant.R, circleC + AlgoConstant.R, circleR + AlgoConstant.R, c2));
                        edges.add(new Edge(circleR + AlgoConstant.R, c2, r2, c2));
                    }
                }
            } else {
                if (r1 < r2) {
                    if (isClockWise) {
                        edges.add(new Edge(r1, c1, r2, c1));
                        edges.add(new Edge(r2, c1, r2, c2));
                    } else {
                        edges.add(new Edge(r1, c1, r1, circleC + AlgoConstant.R));
                        edges.add(new Edge( r1, circleC + AlgoConstant.R, circleR - AlgoConstant.R, circleC + AlgoConstant.R));
                        edges.add(new Edge(circleR - AlgoConstant.R, circleC + AlgoConstant.R, circleR - AlgoConstant.R, circleC - AlgoConstant.R));
                        edges.add(new Edge(circleR - AlgoConstant.R, circleC - AlgoConstant.R, circleR + AlgoConstant.R, circleC - AlgoConstant.R));
                        edges.add(new Edge(circleR + AlgoConstant.R, circleC - AlgoConstant.R, circleR + AlgoConstant.R, c2));
                        edges.add(new Edge(circleR + AlgoConstant.R, c2, r2, c2));
                    }
                } else {
                    if (!isClockWise) {
                        edges.add(new Edge(r1, c1, r1, c2));
                        edges.add(new Edge(r1, c2, r2, c2));
                    } else {
                        edges.add(new Edge(r1, c1, circleR + AlgoConstant.R, c1));
                        edges.add(new Edge(circleR + AlgoConstant.R, c1, circleR + AlgoConstant.R, circleC - AlgoConstant.R));
                        edges.add(new Edge(circleR + AlgoConstant.R, circleC - AlgoConstant.R, circleR - AlgoConstant.R, circleC - AlgoConstant.R));
                        edges.add(new Edge(circleR - AlgoConstant.R, circleC - AlgoConstant.R, circleR - AlgoConstant.R, circleC + AlgoConstant.R));
                        edges.add(new Edge(circleR - AlgoConstant.R, circleC + AlgoConstant.R, r2, circleC + AlgoConstant.R));
                        edges.add(new Edge(r2, circleC + AlgoConstant.R, r2, c2));
                    }
                }
            }
        } else if (checkReverseQuadrant(quadrant1, quadrant2)) {
//            boolean p1 = (quadrant1 == 1 || quadrant1 == 2) ? isClockWise : !isClockWise;
//            if (p1) {
//                int xAxis = (quadrant1 == 1 || quadrant1 == 4) ? circleC + AlgoConstant.R : circleC - AlgoConstant.R;
//                int yAxis = (quadrant1 == 1 || quadrant1 == 2) ? circleR + AlgoConstant.R : circleR - AlgoConstant.R;
//                edges.add(new Edge(r1, c1, r1, xAxis));
//                edges.add(new Edge(r1, xAxis, yAxis, xAxis));
//                edges.add(new Edge(yAxis, xAxis, yAxis, c2));
//                edges.add(new Edge(yAxis, c2, r2, c2));
//            } else {
//                int yAxis = (quadrant1 == 1 || quadrant1 == 2) ? circleR - AlgoConstant.R : circleR + AlgoConstant.R;
//                int xAxis = (quadrant1 == 1 || quadrant1 == 4) ? circleC - AlgoConstant.R : circleC + AlgoConstant.R;
//                edges.add(new Edge(r1, c1, yAxis, c1));
//                edges.add(new Edge(yAxis, c1, yAxis, xAxis));
//                edges.add(new Edge(yAxis, xAxis, r2, xAxis));
//                edges.add(new Edge(r2, xAxis, r2, c2));
//            }
            if (quadrant1 == 1) {
                if (isClockWise) {
                    edges.add(new Edge(r1, c1, r1, circleC + AlgoConstant.R));
                    edges.add(new Edge(r1, circleC + AlgoConstant.R, circleR + AlgoConstant.R, circleC + AlgoConstant.R));
                    edges.add(new Edge(circleR + AlgoConstant.R, circleC + AlgoConstant.R, circleR + AlgoConstant.R, c2));
                    edges.add(new Edge(circleR + AlgoConstant.R, c2, r2, c2));
                } else {
                    edges.add(new Edge(r1, c1, circleR - AlgoConstant.R, c1));
                    edges.add(new Edge(circleR - AlgoConstant.R, c1, circleR - AlgoConstant.R, circleC - AlgoConstant.R));
                    edges.add(new Edge(circleR - AlgoConstant.R, circleC - AlgoConstant.R, r2, circleC - AlgoConstant.R));
                    edges.add(new Edge(r2, circleC - AlgoConstant.R, r2, c2));
                }
            } else if (quadrant1 == 2) {
                if (!isClockWise) {
                    edges.add(new Edge(r1, c1, r1, circleC - AlgoConstant.R));
                    edges.add(new Edge(r1, circleC - AlgoConstant.R, circleR + AlgoConstant.R, circleC - AlgoConstant.R));
                    edges.add(new Edge(circleR + AlgoConstant.R, circleC - AlgoConstant.R, circleR + AlgoConstant.R, c2));
                    edges.add(new Edge(circleR + AlgoConstant.R, c2, r2, c2));
                } else {
                    edges.add(new Edge(r1, c1, circleR - AlgoConstant.R, c1));
                    edges.add(new Edge(circleR - AlgoConstant.R, c1, circleR - AlgoConstant.R, circleC + AlgoConstant.R));
                    edges.add(new Edge(circleR - AlgoConstant.R, circleC + AlgoConstant.R, r2, circleC + AlgoConstant.R));
                    edges.add(new Edge(r2, circleC + AlgoConstant.R, r2, c2));
                }
            } else if (quadrant1 == 3) {
                if (isClockWise) {
                    edges.add(new Edge(r1, c1, r1, circleC - AlgoConstant.R));
                    edges.add(new Edge(r1, circleC - AlgoConstant.R, circleR - AlgoConstant.R, circleC - AlgoConstant.R));
                    edges.add(new Edge(circleR - AlgoConstant.R, circleC - AlgoConstant.R, circleR - AlgoConstant.R, c2));
                    edges.add(new Edge(circleR - AlgoConstant.R, c2, r2, c2));
                } else {
                    edges.add(new Edge(r1, c1, circleR + AlgoConstant.R, c1));
                    edges.add(new Edge(circleR + AlgoConstant.R, c1, circleR + AlgoConstant.R, circleC + AlgoConstant.R));
                    edges.add(new Edge(circleR + AlgoConstant.R, circleC + AlgoConstant.R, r2, circleC + AlgoConstant.R));
                    edges.add(new Edge(r2, circleC + AlgoConstant.R, r2, c2));
                }
            } else {
                if (!isClockWise) {
                    edges.add(new Edge(r1, c1, r1, circleC + AlgoConstant.R));
                    edges.add(new Edge(r1, circleC + AlgoConstant.R, circleR - AlgoConstant.R, circleC + AlgoConstant.R));
                    edges.add(new Edge(circleR - AlgoConstant.R, circleC + AlgoConstant.R, circleR - AlgoConstant.R, c2));
                    edges.add(new Edge(circleR - AlgoConstant.R, c2, r2, c2));
                } else {
                    edges.add(new Edge(r1, c1, circleR + AlgoConstant.R, c1));
                    edges.add(new Edge(circleR + AlgoConstant.R, c1, circleR + AlgoConstant.R, circleC - AlgoConstant.R));
                    edges.add(new Edge(circleR + AlgoConstant.R, circleC - AlgoConstant.R, r2, circleC - AlgoConstant.R));
                    edges.add(new Edge(r2, circleC - AlgoConstant.R, r2, c2));
                }
            }
        } else if (checkQuadrantSameXSide(quadrant1, quadrant2)) {
            boolean shorter = (quadrant1 == 1 || quadrant1 == 3) ? !isClockWise : isClockWise;
            if (shorter) {
                int yAxis = (quadrant1 == 1 || quadrant1 == 2) ? circleR - AlgoConstant.R : circleR + AlgoConstant.R;
                edges.add(new Edge(r1, c1, yAxis, c1));
                edges.add(new Edge(yAxis , c1, yAxis, c2));
                edges.add(new Edge(yAxis, c2, r2, c2));
            } else {
                int xAxis1 = (quadrant1 == 1 || quadrant1 == 4) ? circleC + AlgoConstant.R : circleC - AlgoConstant.R;
                int yAxis = (quadrant1 == 1 || quadrant1 == 2) ? circleR + AlgoConstant.R : circleR - AlgoConstant.R;
                int xAxis2 = (quadrant1 == 1 || quadrant1 == 4) ? circleC - AlgoConstant.R : circleC + AlgoConstant.R;
                edges.add(new Edge(r1, c1, r1, xAxis1));
                edges.add(new Edge(r1, xAxis1, yAxis, xAxis1));
                edges.add(new Edge(yAxis, xAxis1, yAxis, xAxis2));
                edges.add(new Edge(yAxis, xAxis2, r2, xAxis2));
                edges.add(new Edge(r2, xAxis2, r2, c2));
            }

//            if (quadrant1 == 1) {
//                if (!isClockWise) {
////                    edges.add(new Edge(r1, c1, circleR - AlgoConstant.R, c1));
////                    edges.add(new Edge(circleR - AlgoConstant.R, c1, circleR - AlgoConstant.R, c2));
////                    edges.add(new Edge(circleR - AlgoConstant.R, c2, r2, c2));
//                } else {
////                    edges.add(new Edge(r1, c1, r1, circleC + AlgoConstant.R));
////                    edges.add(new Edge(r1, circleC + AlgoConstant.R, circleR + AlgoConstant.R, circleC + AlgoConstant.R));
////                    edges.add(new Edge(circleR + AlgoConstant.R, circleC + AlgoConstant.R, circleR + AlgoConstant.R, circleC - AlgoConstant.R));
//                    edges.add(new Edge(circleR + AlgoConstant.R, circleC - AlgoConstant.R, r2, circleC - AlgoConstant.R));
//                    edges.add(new Edge(r2, circleC - AlgoConstant.R, r2, c2));
//                }
//            } else if (quadrant1 == 2) {
//                if (isClockWise) {
////                    edges.add(new Edge(r1, c1, circleR - AlgoConstant.R, c1));
////                    edges.add(new Edge(circleR - AlgoConstant.R, c1, circleR - AlgoConstant.R, c2));
////                    edges.add(new Edge(circleR - AlgoConstant.R, c2, r2, c2));
//                } else {
////                    edges.add(new Edge(r1, c1, r1, circleC - AlgoConstant.R));
////                    edges.add(new Edge(r1, circleC - AlgoConstant.R, circleR + AlgoConstant.R, circleC - AlgoConstant.R));
////                    edges.add(new Edge(circleR + AlgoConstant.R, circleC - AlgoConstant.R, circleR + AlgoConstant.R, circleC + AlgoConstant.R));
//                    edges.add(new Edge(circleR + AlgoConstant.R, circleC + AlgoConstant.R, r2, circleC + AlgoConstant.R));
//                    edges.add(new Edge(r2, circleC + AlgoConstant.R, r2, c2));
//                }
//            } else if (quadrant1 == 3) {
//                if (!isClockWise) {
////                    edges.add(new Edge(r1, c1, circleR + AlgoConstant.R, c1));
////                    edges.add(new Edge(circleR + AlgoConstant.R, c1, circleR + AlgoConstant.R, c2));
////                    edges.add(new Edge(circleR + AlgoConstant.R, c2, r2, c2));
//                } else {
////                    edges.add(new Edge(r1, c1, r1, circleC - AlgoConstant.R));
////                    edges.add(new Edge(r1, circleC - AlgoConstant.R, circleR - AlgoConstant.R, circleC - AlgoConstant.R));
////                    edges.add(new Edge(circleR - AlgoConstant.R, circleC - AlgoConstant.R, circleR - AlgoConstant.R, circleC + AlgoConstant.R));
//                    edges.add(new Edge(circleR - AlgoConstant.R, circleC + AlgoConstant.R, r2, circleC + AlgoConstant.R));
//                    edges.add(new Edge(r2, circleC + AlgoConstant.R, r2, c2));
//                }
//            } else {
//                if (isClockWise) {
////                    edges.add(new Edge(r1, c1, circleR + AlgoConstant.R, c1));
////                    edges.add(new Edge(circleR + AlgoConstant.R, c1, circleR + AlgoConstant.R, c2));
////                    edges.add(new Edge(circleR + AlgoConstant.R, c2, r2, c2));
//                } else {
////                    edges.add(new Edge(r1, c1, r1, circleC + AlgoConstant.R));
////                    edges.add(new Edge(r1, circleC + AlgoConstant.R, circleR - AlgoConstant.R, circleC + AlgoConstant.R));
////                    edges.add(new Edge(circleR - AlgoConstant.R, circleC + AlgoConstant.R, circleR - AlgoConstant.R, circleC - AlgoConstant.R));
//                    edges.add(new Edge(circleR - AlgoConstant.R, circleC - AlgoConstant.R, r2, circleC - AlgoConstant.R));
//                    edges.add(new Edge(r2, circleC - AlgoConstant.R, r2, c2));
//                }
//            }
        } else if(checkQuadrantSameYSide(quadrant1, quadrant2)) {
            boolean shorter = (quadrant1 == 1 || quadrant1 == 3) ? isClockWise : !isClockWise;
            if (shorter) {
                int xAxis = (quadrant1 == 1 || quadrant1 == 4) ? circleC + AlgoConstant.R : circleC - AlgoConstant.R;
                edges.add(new Edge(r1, c1, r1, xAxis));
                edges.add(new Edge(r1, xAxis, r2, xAxis));
                edges.add(new Edge(r2, xAxis, r2, c2));
            } else {
                int yAxis1 = (quadrant1 == 1 || quadrant1 == 2) ? circleR - AlgoConstant.R : circleR + AlgoConstant.R;
                int xAxis = (quadrant1 == 1 || quadrant1 == 4) ? circleC - AlgoConstant.R : circleC + AlgoConstant.R;
                int yAxis2 = (quadrant1 == 1 || quadrant1 == 2) ? circleR + AlgoConstant.R : circleR - AlgoConstant.R;
                edges.add(new Edge(r1, c1, yAxis1, c1));
                edges.add(new Edge(yAxis1, c1, yAxis1, xAxis));
                edges.add(new Edge(yAxis1, xAxis, yAxis2, xAxis));
                edges.add(new Edge(yAxis2, xAxis, yAxis2, c2));
                edges.add(new Edge(yAxis2, c2, r2, c2));
            }
//            if (quadrant1 == 1) {
//                if (isClockWise) {
//                    edges.add(new Edge(r1, c1, r1, circleC + AlgoConstant.R));
//                    edges.add(new Edge(r1, circleC + AlgoConstant.R, r2, circleC + AlgoConstant.R));
//                    edges.add(new Edge(r2, circleC + AlgoConstant.R, r2, c2));
//                } else {
//                    edges.add(new Edge(r1, c1, circleR - AlgoConstant.R, c1));
//                    edges.add(new Edge(circleR - AlgoConstant.R, c1, circleR - AlgoConstant.R, circleC - AlgoConstant.R));
//                    edges.add(new Edge(circleR - AlgoConstant.R, circleC - AlgoConstant.R, circleR + AlgoConstant.R, circleC - AlgoConstant.R));
//                    edges.add(new Edge(circleR + AlgoConstant.R, circleC - AlgoConstant.R, circleR + AlgoConstant.R, c2));
//                    edges.add(new Edge(circleR + AlgoConstant.R, c2, r2, c2));
//                }
//            } else if (quadrant1 == 2) {
//                if (!isClockWise) {
//                    edges.add(new Edge(r1, c1, r1, circleC - AlgoConstant.R));
//                    edges.add(new Edge(r1, circleC - AlgoConstant.R, r2, circleC - AlgoConstant.R));
//                    edges.add(new Edge(r2, circleC - AlgoConstant.R, r2, c2));
//                } else {
//                    edges.add(new Edge(r1, c1, circleR - AlgoConstant.R, c1));
//                    edges.add(new Edge(circleR - AlgoConstant.R, c1, circleR - AlgoConstant.R, circleC + AlgoConstant.R));
//                    edges.add(new Edge(circleR - AlgoConstant.R, circleC + AlgoConstant.R, circleR + AlgoConstant.R, circleC + AlgoConstant.R));
//                    edges.add(new Edge(circleR + AlgoConstant.R, circleC + AlgoConstant.R, circleR + AlgoConstant.R, c2));
//                    edges.add(new Edge(circleR + AlgoConstant.R, c2, r2, c2));
//                }
//            } else if (quadrant1 == 3) {
//                if (isClockWise) {
//                    edges.add(new Edge(r1, c1, r1, circleC - AlgoConstant.R));
//                    edges.add(new Edge(r1, circleC - AlgoConstant.R, r2, circleC - AlgoConstant.R));
//                    edges.add(new Edge(r2, circleC - AlgoConstant.R, r2, c2));
//                } else {
//                    edges.add(new Edge(r1, c1, circleR + AlgoConstant.R, c1));
//                    edges.add(new Edge(circleR + AlgoConstant.R, c1, circleR + AlgoConstant.R, circleC + AlgoConstant.R));
//                    edges.add(new Edge(circleR + AlgoConstant.R, circleC + AlgoConstant.R, circleR - AlgoConstant.R, circleC + AlgoConstant.R));
//                    edges.add(new Edge(circleR - AlgoConstant.R, circleC + AlgoConstant.R, circleR - AlgoConstant.R, c2));
//                    edges.add(new Edge(circleR - AlgoConstant.R, c2, r2, c2));
//                }
//            } else {
//                if (!isClockWise) {
//                    edges.add(new Edge(r1, c1, r1, circleC + AlgoConstant.R));
//                    edges.add(new Edge(r1, circleC + AlgoConstant.R, r2, circleC + AlgoConstant.R));
//                    edges.add(new Edge(r2, circleC + AlgoConstant.R, r2, c2));
//                } else {
//                    edges.add(new Edge(r1, c1, circleR + AlgoConstant.R, c1));
//                    edges.add(new Edge(circleR + AlgoConstant.R, c1, circleR + AlgoConstant.R, circleC - AlgoConstant.R));
//                    edges.add(new Edge(circleR + AlgoConstant.R, circleC - AlgoConstant.R, circleR - AlgoConstant.R, circleC - AlgoConstant.R));
//                    edges.add(new Edge(circleR - AlgoConstant.R, circleC - AlgoConstant.R, circleR - AlgoConstant.R, c2));
//                    edges.add(new Edge(circleR - AlgoConstant.R, c2, r2, c2));
//                }
//            }
        }
        return circleRecTanEdgeLooping(edges);
    }

    private static List<int[]> circleRecTanEdgeLooping(List<Edge> edges) {
        List<int[]> pointsPassed = new ArrayList<>();
        for (Edge edge : edges) {
            int r = edge.getStR(), c = edge.getStC();
            if (edge.getDr() == 0) {
                while (0 <= c && c < AlgoConstant.GridN && c != edge.getEdC()) {
                    pointsPassed.add(new int[]{r, c});
                    c += edge.getDc();
                }
                if (c != edge.getEdC()) return null;
            } else {
                while (0 <= r && r < AlgoConstant.GridM && r != edge.getEdR()) {
                    pointsPassed.add(new int[]{r, c});
                    r += edge.getDr();
                }
                if (r != edge.getEdR()) return null;
            }
        }
        Edge last = edges.get(edges.size()-1);
        pointsPassed.add(new int[]{last.getEdR(), last.getEdC()});
        return pointsPassed;
    }

//    private static List<int[]> circleRecTanEdgeLooping(int[] endP, int r1, int c1, int r2, int c2, int edge1, int edge2, int turn) {
//        List<int[]> pointsPassed = new ArrayList<>();
//        for (int i = 0; i < 4; i++) {
//            int walkingEdge = turn == 0 ? (edge1 + i) % 4 : (edge1 - i + 4) % 4;
//            int[] currentDir = dEdgeLooping[turn][walkingEdge];
//            if (currentDir[0] == 0) {
//                while (0 <= c1 && c1 < AlgoConstant.GridN && c1 != endP[walkingEdge]) {
//                    pointsPassed.add(new int[]{r1, c1});
//                    c1 += currentDir[1];
//                }
//                if (c1 != endP[walkingEdge]) return null;
//            } else {
//                while (0 <= r1 && r1 < AlgoConstant.GridM && r1 != endP[walkingEdge]) {
//                    pointsPassed.add(new int[]{r1, c1});
//                    r1 += currentDir[0];
//                }
//                if (r1 != endP[walkingEdge]) return null;
//            }
//            if (walkingEdge == edge2) break;
//        }
//        pointsPassed.add(new int[]{r2, c2});
//        return pointsPassed;
//    }

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
        List<int[]> points = findGridCirclePath(5, 10, 3, 2, 7, 5, true);
//        List<int[]> points = findGirdLinePath();
//        List<int[]> points = findReversePath(15, 8, 15, 3, new int[]{0, -1});
        for (int[] p : points) {
            System.out.println(Arrays.toString(p));
        }
    }
}
