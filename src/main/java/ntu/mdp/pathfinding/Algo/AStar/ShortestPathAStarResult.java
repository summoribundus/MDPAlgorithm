package ntu.mdp.pathfinding.Algo.AStar;

import ntu.mdp.pathfinding.*;
import ntu.mdp.pathfinding.Algo.Trajectory.TrajectoryToArenaGrid;
import ntu.mdp.pathfinding.Instruction.CurveInstruction;
import ntu.mdp.pathfinding.Instruction.Instruction;
import ntu.mdp.pathfinding.Instruction.LineInstruction;
import ntu.mdp.pathfinding.Instruction.TakePictureInstruction;

import java.util.ArrayList;
import java.util.List;

public class ShortestPathAStarResult implements Comparable<ShortestPathAStarResult> {
    private List<Instruction> instructions;
    private List<Point> pointPath;

    private int cost;

    public ShortestPathAStarResult(List<Point> pointPath, int cost){
        this.pointPath = pointPath;
        this.cost = cost;
    }

    public List<Instruction> getInstructions() {
        return instructions;
    }

    public List<Point> getPointPath() {
        return pointPath;
    }

    public int getCost() {
        return cost;
    }

    public void computeCompressedCarMove() {
        instructions = new ArrayList<>();
        List<Point> pathSegment = new ArrayList<>();

        int lastDir = -1;
        Point st = null;
        int i = 0, len = pointPath.size();
        while (i < len) {
            Point point = pointPath.get(i++);
            if (point.isMatchingPoint())
                instructions.add(new TakePictureInstruction(point.getMatchingObstacleID()));

            if (point.getMoveFlag() == lastDir) {
                pathSegment.add(point);
                continue;
            }

            if (st != null) {
                int rLen = point.getX() - st.getX(), cLen = point.getY() - st.getY();
                LineInstruction li = new LineInstruction(rLen == 0 ? cLen : rLen);
                li.setGridPath(pathSegment);
                instructions.add(li);
                st = null;
            }

            if (point.getMoveFlag() == 0 || point.getMoveFlag() == 1) {
                Point endPoint = pathSegment.get(i++);
                int rLen = endPoint.getX() - point.getX(), cLen = endPoint.getY() - point.getY();
                if (point.getMoveFlag() == 0) {
                    rLen *= -1; cLen *= -1;
                }
                int circleR = cLen > 0 ? Math.min(point.getX(), endPoint.getX()) : Math.max(point.getX(), endPoint.getX());
                int circleC = rLen > 0 ? Math.max(point.getY(), endPoint.getY()) : Math.min(point.getY(), endPoint.getY());
                List<Point> segment = TrajectoryToArenaGrid.findGridCirclePath(point.getX(), point.getY(), endPoint.getX(), endPoint.getY(), circleR, circleC, point.getMoveFlag() == 1);

                CurveInstruction ci = new CurveInstruction(90, point.getMoveFlag() == 1);
                ci.setGridPath(segment);
                instructions.add(ci);
            } else {
                st = point;
                pathSegment = new ArrayList<>();
            }

            lastDir = point.getMoveFlag();
        }
    }

    @Override
    public int compareTo(ShortestPathAStarResult o) {
        return Integer.compare(cost, o.cost);
    }
}
