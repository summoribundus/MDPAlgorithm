package ntu.mdp.pathfinding.Algo.Trajectory;

import ntu.mdp.pathfinding.Algo.Arena;
import ntu.mdp.pathfinding.Algo.ShortestPathBF;
import ntu.mdp.pathfinding.Algo.Trajectory.TrajectoryCalculation.TrajectoryCalculation;
import ntu.mdp.pathfinding.Algo.Trajectory.TrajectoryCalculation.TrajectoryResult;
import ntu.mdp.pathfinding.Obstacle;
import ntu.mdp.pathfinding.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShortestPathTrajectory {
    public final static int[][] dReverses = {{0, -1}, {-1, 0}, {0, 1}, {1, 0}};

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

}
